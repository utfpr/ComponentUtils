/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.nfe.gui.actions;

import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.lang.annotation.Annotation;

/**
 *
 * @author Marcelo
 */
public interface DialogMethods {

    Object changeAnnotationValue(Annotation annotation, String key, Object newValue);
    
    
//    void setDialog(Dialog dialog);

    void changeLabel(String ref, String label);

    void changeText(String fieldName, String text);

    void clear();

    void clearComponents(String ref);

    void clearComponentsIn(Object... targets);

    void cloneObject(Object source, Object target);
  
    void dispose();

    void editable(boolean editable, String... fieldNames);

    void editableAll(boolean editable);

    void enable(boolean enable, String... fieldNames);

    void enableAll(boolean enable);

    void enableGroup(boolean enable, String... groups);
    
    void editableGroup(boolean enable, String... groups);

    void enabledTab(String name, int index, boolean enable);

    void grabFocus(String ref);

    void selectedTab(String name, int index);

    void setButtonDefault(String ref);

    void showPassword(String ref, boolean show);

    void update(Object... targets);

    void updateField(String... fieldName);

    void visible(boolean editable, String... fieldNames);
    
    void setActionListener(String ref, ActionListener actionListener);
}
