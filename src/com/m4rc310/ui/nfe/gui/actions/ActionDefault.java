/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.nfe.gui.actions;

import br.edu.utfpr.cm.tsi.utils.B;
import br.edu.utfpr.cm.tsi.utils.LogServer;
import com.m4rc310.ui.nfe.gui.actions.Action.Listener;
import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import sun.misc.MessageUtils;

//import org.json.simple.parser.JSONParser;
/**
 *
 * @author marcelo
 */
public class ActionDefault implements Action, Listener, DialogMethods {

    private final Collection<Listener> listeners;
    private Dialog dialog;

    public ActionDefault() {
        listeners = new ArrayList<>();
    }

    @Override
    public void addGuiListener(Listener ls) {
        synchronized (this) {
            Iterator<Listener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                Listener listener = iterator.next();
                if (listener.getClass().equals(ls.getClass())) {
//                    listeners.remove(listener);
                    iterator.remove();
                }
            }
            listeners.add(ls);
        }
    }

    @Override
    public void removeGuiListener(Listener ls) {
        synchronized (this) {
            log("removeing listener: {0}", ls);
            listeners.remove(ls);
        }
    }

    @Override
    public void addAllListenerScanner(Object container) {

        try {
            if (container instanceof Listener) {
                addGuiListener((Listener) container);
            }

            Class cc = container.getClass();

            Collection<Field> fields = new ArrayList<>();
            while (!cc.equals(Object.class)) {
                fields.addAll(Arrays.asList(cc.getDeclaredFields()));
                fields.addAll(Arrays.asList(cc.getFields()));
                cc = cc.getSuperclass();
            }

            for (Field declaredField : fields) {
                declaredField.setAccessible(true);
                Object obj = declaredField.get(container);
                if (obj instanceof Listener) {
                    addGuiListener((Listener) obj);
                }
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace(System.err);
        }
    }

    public void set(Object target, String fieldName, Object value) {
        Class ctype = target.getClass();
        try {
            Field field = ctype.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public <T> T get(Object target, String fieldName, Class<T> type) {
        Class ctype = target.getClass();
        try {
            Field field = ctype.getDeclaredField(fieldName);
            field.setAccessible(true);

            return (T) field.get(target);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace(System.err);
            return null;
        }
    }

    protected void fireInThread(String metodo, Object... values) {
        synchronized (this) {
            new Thread() {
                @Override
                public void run() {
                    fire(metodo, values);
                }
            }.start();
        }
    }

    private int countMethods(Object target, String method) {
        Collection<Method> methods = new ArrayList<>();
        Class ctarget = target.getClass();
        methods.addAll(Arrays.asList(ctarget.getMethods()));
        methods.addAll(Arrays.asList(ctarget.getDeclaredMethods()));

        int num = 0;

        num = methods.stream().filter((m) -> (m.getName().equals(method))).map((_item) -> 1).reduce(num, Integer::sum);

        return num;
    }

    private Method getMethod(Object target, String methodName, Class... types) {
        Collection<Method> methods = new ArrayList<>();
        Class ctarget = target.getClass();
        methods.addAll(Arrays.asList(ctarget.getDeclaredMethods()));
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                method.setAccessible(true);
                return method;
            }
        }
        methods.clear();
        methods.addAll(Arrays.asList(ctarget.getMethods()));
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                method.setAccessible(true);
                return method;
            }
        }
        throw new UnsupportedOperationException("Method not found!");
    }

    private Object invokeMethod(Object target, Method method, Object... args) {
        try {
            method.setAccessible(true);
            return method.invoke(target, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            log(e.getMessage());
            return null;
        }
    }

    private boolean containMethod(Listener listener, String sMethod) {
        Collection<Method> methods = new ArrayList<>();
        Class listenerClass = listener.getClass();
        methods.addAll(Arrays.asList(listenerClass.getDeclaredMethods()));
        methods.addAll(Arrays.asList(listenerClass.getMethods()));
        return methods.stream().anyMatch((method) -> (method.getName().equals(sMethod)));
    }

    private Method getMethod(Listener listener, String sMethod) {
        Collection<Method> methods = new ArrayList<>();
        Class listenerClass = listener.getClass();
        methods.addAll(Arrays.asList(listenerClass.getDeclaredMethods()));
        methods.addAll(Arrays.asList(listenerClass.getMethods()));
        for (Method method : methods) {
            if (method.getName().equals(sMethod)) {
                return method;
            }
        }
        throw new UnsupportedOperationException("Method not found");
    }

    protected Object fireReturn(String metodo, Object... values) {
        List ret = new ArrayList();
        Collection<Method> methods = new ArrayList<>();

        Map<Method, Listener> methodsToInvoke = new HashMap<>();

        for (Listener listener : listeners) {
            if (containMethod(listener, metodo)) {
                Method method = getMethod(listener, metodo);
                method.setAccessible(true);
                try {
                    Object value = method.invoke(listener, values);
                    ret.add(value);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                }
            }
        }

        if (ret.size() == 1) {
            return ret.get(0);
        }

        return ret;
    }

    private void fire_(String metodo, Object... values) {
        listeners.remove(this);
        fire(metodo, values);
    }

    private void fireInTread_(String metodo, Object... values) {
        listeners.remove(this);
        fireInThread(metodo, values);
    }

    protected void fire(String metodo, Object... values) {

//        System.out.printf("Fire to Method: %s\n", metodo);
        synchronized (listeners) {
            Iterator<Listener> itr = listeners.iterator();
            while (itr.hasNext()) {
                Listener listener = itr.next();
                if (containMethod(listener, metodo)) {
                    Method method = getMethod(listener, metodo);
                    method.setAccessible(true);
                    try {

                        StringBuilder sb = new StringBuilder();
                        for (Object arg : values) {
                            sb.append(arg).append(",");
                        }

                        LogServer.getInstance().debug(listener, "tentando invocar o metodo: {0}({1})...", metodo, sb.toString());
                        method.invoke(listener, values);
                        LogServer.getInstance().debug(listener, "metodo invocado!", metodo);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        LogServer.getInstance().error(e);
                    }
                }
            }
        }
    }

    protected Object[] getObjects(Object... args) {
        return args;
    }

    protected void validate(boolean condition, String message, Object... args) throws Exception {
        message = MessageFormat.format(message, args);
        if (condition) {
            throw new Exception(message);
        }
    }

    public static void sCloneObject(Object source, Object target) {
        new ActionDefault().cloneObjectLocal(source, target);
    }

    protected void cloneObjectLocal(Object source, Object target) {
        Class csource = source.getClass();
        Class ctarget = target.getClass();
        Collection<Field> targetFields = new ArrayList<>();

        targetFields.addAll(Arrays.asList(ctarget.getDeclaredFields()));
        targetFields.addAll(Arrays.asList(ctarget.getFields()));

        while (ctarget != Object.class) {
            ctarget = ctarget.getSuperclass();
            targetFields.addAll(Arrays.asList(ctarget.getDeclaredFields()));
            targetFields.addAll(Arrays.asList(ctarget.getFields()));
        }

        for (Field targetField : targetFields) {
            targetField.setAccessible(true);
            try {
                Field field = csource.getDeclaredField(targetField.getName());
                field.setAccessible(true);
                Object object = field.get(source);
                targetField.set(target, object);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                LogServer.getInstance().error(e);
            }
        }
    }

    protected void log(String text, Object... args) {
        try {
//            text = MessageFormat.format(text, args);
        } catch (Exception e) {
        }
//        System.out.println(text);
    }

    protected String getString(String text, Object... args) {
        return B.getString(text, args);
    }

    @Override
    public Object changeAnnotationValue(Annotation annotation, String key, Object newValue) {
        return fireReturn("changeAnnotationValue", annotation, key, newValue);
    }

    @Override
    public void changeLabel(String ref, String label) {
        fire_("changeLabel", ref, label);
    }

    @Override
    public void changeText(String fieldName, String text) {
        fire_("changeText", fieldName, text);
    }

    @Override
    public void clear() {
        fire_("clear");
    }

    @Override
    public void clearComponents(String ref) {
        fire_("clearComponents", ref);
    }

    @Override
    public void clearComponentsIn(Object... targets) {
        fire_("clearComponentsIn", Arrays.copyOf(targets, targets.length));
    }

    @Override
    public void cloneObject(Object source, Object target) {
        cloneObjectLocal(source, target);
    }

    @Override
    public void dispose() {
        try {
            dialog.dispose();
        } catch (Exception e) {
            LogServer.getInstance().error(e);
        }
    }

    @Override
    public void editable(boolean editable, String... fieldNames) {
        fire_("editable", editable, Arrays.copyOf(fieldNames, fieldNames.length));
    }

    @Override
    public void editableAll(boolean editable) {
        fire_("editableAll", editable);
    }

    @Override
    public void enable(boolean enable, String... fieldNames) {
        fire_("enable", enable, Arrays.copyOf(fieldNames, fieldNames.length));
    }

    @Override
    public void enableAll(boolean enable) {
        fire_("enableAll", enable);
    }

    @Override
    public void enableGroup(boolean enable, String... groups) {
        fire_("enableGroup", enable, Arrays.copyOf(groups, groups.length));
    }

    @Override
    public void editableGroup(boolean enable, String... groups) {
        fire_("editableGroup", enable, Arrays.copyOf(groups, groups.length));
    }

    @Override
    public void enabledTab(String name, int index, boolean enable) {
        fire_("enabledTab", name, index, enable);
    }

    @Override
    public void grabFocus(String ref) {
        fireInTread_("grabFocus", ref.toLowerCase());
//        fire("grabFocus", ref.toLowerCase());
    }

    @Override
    public void selectedTab(String name, int index) {
        fire_("selectedTab", name, index);
    }

    @Override
    public void setButtonDefault(String ref) {
        fire_("setButtonDefault", ref);
    }

    @Override
    public void showPassword(String ref, boolean show) {
        fire_("showPassword", ref, show);
    }

    @Override
    public void update(Object... targets) {
    }

    @Override
    public void updateField(String... fieldName) {
//        fire_("updateField", Arrays.copyOf(fieldName, fieldName.length));
    }

    @Override
    public void visible(boolean editable, String... fieldNames) {
        fire_("visible", editable, Arrays.copyOf(fieldNames, fieldNames.length));
    }

    @Override
    public void setActionListener(String ref, ActionListener actionListener) {
        fire_("setActionListener", ref.toLowerCase(), actionListener);
    }

//    @Override
    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }
}
