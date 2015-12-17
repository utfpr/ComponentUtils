/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.componentUtils;

import com.m4rc310.ui.gui.componentUtils.impl.GuiUtilsImpl;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.trugger.scan.ClassScan;

/**
 *
 * @author mls
 */
public class ComponentBuilder {
    
    private final List<ComponentAdapter> adapters;
    private final Collection<ComponentAdapter.NComponentListener> componentListeners;
    private String packageAdapters;
    private boolean fireListener = true;
    
    public ComponentBuilder() {
        this("com.m4rc310.ui.gui.componentUtils.adapters");
    }
    
    public ComponentBuilder(String packageAdapters) {
        componentListeners = new ArrayList<>();
        this.packageAdapters = packageAdapters;
        this.adapters = new ArrayList<>();
        this.init();
    }
    
    private void init() {
        for (Class class1 : ClassScan.findAll().recursively().
                assignableTo(ComponentAdapter.class).in(packageAdapters)) {
            ComponentAdapter ca;
            try {
                ca = (ComponentAdapter) class1.newInstance();
                registerComponentAdapter(ca);
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(GuiUtilsImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void put(Object target, String key, Object value) {
        String[] keys = key.split("\\.");
        if (keys.length > 1) {
            try {
                Class type = target.getClass();
                
                int lenght = keys.length;
                
                for (int i = 0; i < lenght - 1; i++) {
                    Method method = type.getMethod("get", Object.class);
                    method.setAccessible(true);
                    target = method.invoke(target, keys[i]);
                }
                
                Method method = type.getMethod("put", Object.class, Object.class);
                method.setAccessible(true);
                method.invoke(target, keys[lenght - 1], value);
                
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace(System.err);
            }
            
        } else {
            Class type = target.getClass();
            try {
                Method method = type.getMethod("put", Object.class, Object.class);
                method.setAccessible(true);
                method.invoke(target, key, value);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace(System.err);
            }
        }
    }
    
    public void putValueInObject(Object target, String keys, Object value) {
        
        Class targetClass = target.getClass();
        
        try {
            for (String key : keys.split("\\.")) {
                Method mt = targetClass.getMethod("get", Object.class);
                mt.setAccessible(true);
                target = mt.invoke(target, key);
            }
            
            targetClass = target.getClass();
            Method mt = targetClass.getMethod("put", Object.class, Object.class);
            mt.setAccessible(true);
            
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            
        }
    }
    
    public void processContainer(Object container, Object target) {
        listFields.clear();
        loadAllFields(container);
//        Container parent = (Container) getParentForm(container);

//        System.out.println(parent.getComponentCount());
//        loadAllFields(parent);
//        Field[] fields = getAllFields(parent);
    }
    
    private final List<Field> listFields = new ArrayList<>();
    
    public void loadAllFields(Object container) {
        Class type = container.getClass();
        try {
            Field[] fields = type.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object obj = field.get(container);
                
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace(System.err);
        }
    }
    
    public Object getParentForm(Object sourceComponent) {
        //Container parent = sourceComponent.getParent();
        Class type = sourceComponent.getClass();
//        if(sourceComponent instanceof Component){
        try {
            Method method = type.getMethod("getParent");
            method.setAccessible(true);
            
            Object parent = method.invoke(sourceComponent);
            if (parent != null) {
                getParentForm(parent);
            }
            return parent;
            
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ComponentBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
//        }
        throw new UnsupportedOperationException();
    }


    public void process(Object source, Object container) {
        Class type = source.getClass();
        
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ReadFields.class)) {
                ReadFields readFields = field.getAnnotation(ReadFields.class);
                try {
                    field.setAccessible(true);
                    Object objectField = field.get(source);
                    ComponentAdapter ca = getAdapterForComponent(field.get(source));
                    if (ca == null) {
                        continue;
                    }
                    
                    ca.setNComponentListeners(componentListeners);
                    ca.setFireListner(fireListener);
                    ca.process(readFields, objectField, container);
                    
                    putValue(source, container);
                    
                } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace(System.err);
                }
                
            }
        }
    }

//    public void processContainer(JComponent component, Object target) {
//
//    }
    public void clear(Object source, Object target) {
        Class type = target.getClass();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ReadFields.class)) {
                ReadFields readFields = field.getAnnotation(ReadFields.class);
                try {
                    field.setAccessible(true);
                    Object objectField = field.get(target);
                    
                    Class<? extends Object> typeTarget = target.getClass();
                    Method method = typeTarget.getMethod("get", Object.class);
                    method.setAccessible(true);
                    
                    Object value = method.invoke(target, readFields.field());
                    if ((value == null) || (value == source)) {
                        continue;
                    }
                    
                    ComponentAdapter ca = getAdapterForComponent(field.get(target));
                    if (ca == null) {
                        continue;
                    }
                    
                    ca.clear();
                } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                }
                
            }
        }
        
    }
    
    public void putValue(Object source, Object target) {
        Class type = source.getClass();
        Field[] fields = type.getDeclaredFields();
        
        for (Field field : fields) {
            if (field.isAnnotationPresent(ReadFields.class)) {
                ReadFields readFields = field.getAnnotation(ReadFields.class);
                try {
                    field.setAccessible(true);
                    Object objectField = field.get(source);
                    
                    Class<? extends Object> typeTarget = target.getClass();
                    Method method = typeTarget.getMethod("get", Object.class);
                    method.setAccessible(true);
                    Object value = target;
                    String keys = readFields.field();
                    
                    for (String key : keys.split("\\.")) {
                        value = method.invoke(value, key);
                    }
                    
                    if (value == null) {
                        continue;
                    }
                    
                    System.out.println(value.getClass());
                    
                    
                    ComponentAdapter ca = getAdapterForComponent(value);
//                    ComponentAdapter ca = getAdapterForComponent(field.get(source));
                    if (ca == null) {
                        continue;
                    }
//                    ca.setFireListner(fireListener);
                    ca.putValueToComponent(objectField, value);
                } catch (Exception e) {
                    
                }
            }
        }
        
    }
    
    

    public void setFireListener(boolean fireListener) {
        for (ComponentAdapter adapter : adapters) {
            adapter.setFireListner(fireListener);
//            if (adapter.isIsntanceOf(component)) {
//                try {
////                    return adapter.getClass().newInstance();
//                    return adapter;
//                } catch (Exception ex) {
//                    Logger.getLogger(ComponentBuilder.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
        }
        
        
        this.fireListener = fireListener;
    }
    
    
    public void removeNComponentListener(ComponentAdapter.NComponentListener cl) {
        componentListeners.remove(cl);
    }
    
    public void addNComponentListener(ComponentAdapter.NComponentListener cl) {
        if (!componentListeners.contains(cl)) {
            componentListeners.add(cl);
        }
    }
    
    public void registerComponentAdapter(ComponentAdapter componentAdapter) {
        if (!adapters.contains(componentAdapter)) {
            adapters.add(componentAdapter);
        }
    }
    
    public ComponentAdapter getAdapterForComponent(Object component) {
        for (ComponentAdapter adapter : adapters) {
            
            
            
            
            
            if (adapter.isIsntanceOf(component)) {
                try {
                    
                    //System.out.println(adapter);
                    
//                    return adapter.getClass().newInstance();
                    return adapter;
                } catch (Exception ex) {
                    Logger.getLogger(ComponentBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
    
}
