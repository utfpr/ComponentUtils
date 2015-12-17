/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.componentUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author mls
 * @param <C>
 * @param <O>
 */
public abstract class ComponentAdapter<C, O> {

    private boolean fireListner = true;

    public interface NComponentListener {

        void returnValue(Object component, Object target, Object value, String key);
        //void valueToComponent(Object component, Object value, String key);
    }

    protected final Collection<C> registered;
    protected Collection<NComponentListener> listeners;

    protected C component;
    protected O object;
    protected ReadFields readFields;

    public ComponentAdapter() {
        this.registered = new ArrayList<>();
        this.listeners = new ArrayList<>();
    }

    boolean isIsntanceOf(Object o) {
//        return getClassForComponent().isInstance(o);
//        return getClassForValue().isInstance(o);
        return o.getClass() == getClassForValue();
        
    }

    public void process(ReadFields readFields, C component, O object) {
        this.component = component;
        this.object = object;
        this.readFields = readFields;
//        processFields(component, readFields);
        processFields();
        initListeners();
    }

    private void processFields() {
        try {
            Class cc = component.getClass();
            Method mt = cc.getMethod("setEnabled", boolean.class);
            mt.setAccessible(true);
            mt.invoke(component, readFields.enable());

            mt = cc.getMethod("setVisible", boolean.class);
            mt.setAccessible(true);
            mt.invoke(component, readFields.visible());

            mt = cc.getMethod("setEditable", boolean.class);
            mt.setAccessible(true);
            mt.invoke(component, readFields.editable());

            int alignment;

            //new JTextField().setHorizontalAlignment(alignment);
            switch (readFields.alignment()) {
                case CENTER:
                    alignment = JTextField.CENTER;
                    break;
                case LEFT:
                    alignment = JTextField.LEFT;
                    break;
                case RIGTH:
                default:
                    alignment = JTextField.RIGHT;
                    break;
            }

            mt = cc.getMethod("setHorizontalAlignment", int.class);
            mt.setAccessible(true);
            mt.invoke(component, alignment);

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        }
    }

    protected abstract void initListeners();

    protected abstract void clear();

    protected abstract void putValueToComponent(C component, O value);

    public void setNComponentListeners(Collection<NComponentListener> listeners) {
        this.listeners = listeners;
    }

    public void addNComponentListeners(NComponentListener cl) {
        if (!listeners.contains(cl)) {
            listeners.add(cl);
        }
    }

    public void removeNComponentListeners(NComponentListener cl) {
        this.listeners.remove(cl);
    }

    protected void fire(Object component, Object value, String key) {

        System.out.println(fireListner);
        listeners.stream().forEach((listener) -> {
            if (fireListner) {
//                System.out.println("--");
                listener.returnValue(component, object, value, key);
            }
        });
    }

    public void setFireListner(boolean fireListner) {
        this.fireListner = fireListner;
    }

    public Class getClassForComponent() {
        return (Class<C>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class getClassForValue() {
        return (Class<O>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

}
