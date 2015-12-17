/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.utils;

import com.m4rc310.ui.gui.componentUtils.ComponentAdapter;
import com.m4rc310.ui.gui.componentUtils.Dialog;
import com.m4rc310.ui.gui.componentUtils.impl.GuiUtilsImpl;
import java.awt.Component;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import net.sf.trugger.scan.ClassScan;

/**
 *
 * @author Marcelo
 */
public class GuiManager {

    private final Map<Dialog, Class> mapsDialogs;

    private StringListener stringListener;

    private GuiManager() {
        this.mapsDialogs = new HashMap<>();
    }

    public static GuiManager getInstance() {
        return GuiManagerHolder.INSTANCE;
    }

    private static class GuiManagerHolder {

        private static final GuiManager INSTANCE = new GuiManager();
    }

    public void prepareManager(String pack) {
        Set<Class> clasz = ClassScan.findAll().recursively().annotatedWith(Dialog.class).in(pack);
        clasz.stream().forEach((cc) -> {
            Annotation annotation = cc.getAnnotation(Dialog.class);
            if (annotation instanceof Dialog) {
                Dialog dialog = (Dialog) annotation;
                mapsDialogs.put(dialog, cc);
            }
        });
    }
    
    public List<Class> listForms(String pathForms){
        List<Class> ret = new ArrayList<>();
        
        Set<Class> clasz = ClassScan.findAll().recursively().annotatedWith(Dialog.class).in(pathForms);
        clasz.stream().forEach((cc) -> {
            Annotation annotation = cc.getAnnotation(Dialog.class);
            if (annotation instanceof Dialog) {
                ret.add(cc);
            }
        });
        
        return ret;
    }
    
    public Field[] getFields(Class clasz){
        try {
            return clasz.getFields();
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }
    
    
    public List<JComponent> getComponentsForObject(Object o){
        ArrayList<JComponent> ret = new ArrayList<>();
        
        for (Class class1 : ClassScan.findAll().recursively().
                assignableTo(ComponentAdapter.class).in("com.m4rc310.ui.gui.componentUtils.adapters")) {
            ComponentAdapter ca;
            try {
                ca = (ComponentAdapter) class1.newInstance();
                Class classComponent = ca.getClassForComponent();
                
                System.out.println(ca.getClassForValue().equals(o.getClass()));
                
                
//                System.out.prfintln(classComponent);
                
//                ret.add(component);
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(GuiUtilsImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        
        return ret;
    }

    public void makeJDialog(Object o){
        Class c = o.getClass();
        try {
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
//                System.out.println(field.getType());
                
                
//                System.out.println(field);
            }
            
        } catch (Exception e) {
        }
    }
    
    public void showDialogInModal(String alias, Object... args) {
        show(null, true, alias, args);
    }

    public void showDialog(Object parent, boolean modal, String alias, Object... args) {
        show(parent, modal, alias, args);
    }

    public void showDialog(String alias, Object... args) {
        show(null, false, alias, args);
    }

    private void show(Object parent, boolean modal, String alias, Object... args) {
        mapsDialogs.entrySet().stream().forEach((Map.Entry<Dialog, Class> entrySet) -> {
            Dialog key = entrySet.getKey();
            Class type = entrySet.getValue();

            //String salias = String.format("ref.%s", key.alias());
            if (key.alias().equals(alias)) {
                String title = getString(key.title());
                try {
                    Method method = type.getMethod("setTitle", String.class);
                    method.setAccessible(true);

                    Class[] classArgs = new Class[args.length];
                    for (int i = 0; i < args.length; i++) {
                        classArgs[i] = args[i].getClass();
                    }
                    Constructor constructor = type.getConstructor(classArgs);
                    constructor.setAccessible(true);

//                    Object newObject = type.newInstance();
                    Object newObject = constructor.newInstance(args);
                    method.invoke(newObject, title);

                    method = type.getMethod("setModal", boolean.class);
                    method.invoke(newObject, modal);
                    method = type.getMethod("setLocationRelativeTo", Component.class);
                    method.invoke(newObject, parent);

                    method = type.getMethod("setVisible", boolean.class);
                    method.invoke(newObject, true);

//                    if(parent!=null)
                } catch (NoSuchMethodException | SecurityException |
                        InstantiationException | IllegalAccessException |
                        IllegalArgumentException | InvocationTargetException e) {
                        e.printStackTrace(System.err);
                }
            }
        });

    }

    public void setStringListener(StringListener stringListener) {
        this.stringListener = stringListener;
    }

    public String getString(String text, Object... args) {
        if (stringListener != null) {
            return stringListener.getString(text, args);
        }

        return text;
    }

    public interface StringListener {

        String getString(String text, Object... args);
    }

    public static void main(String[] args) {
        new GuiManager().test();
    }
    
    private void test(){
        GuiManager gm = GuiManager.getInstance();
        
        gm.getComponentsForObject(new ForTest());
        
    }
    
    
    private class ForTest{
        private String value;
    }
    
    
}
