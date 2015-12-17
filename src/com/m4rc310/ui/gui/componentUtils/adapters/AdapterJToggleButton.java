/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.componentUtils.adapters;

import com.m4rc310.ui.gui.componentUtils.ComponentAdapter;
import java.awt.event.ItemEvent;
import javax.swing.JToggleButton;

/**
 *
 * @author mls
 */
public class AdapterJToggleButton extends ComponentAdapter<JToggleButton, Boolean>{

    @Override
    protected void initListeners() {
        component.addItemListener((ItemEvent e) -> {
            boolean isSelected =  e.getStateChange()== ItemEvent.SELECTED;
            fire(component, isSelected, readFields.field());
        });
    }

    @Override
    protected void putValueToComponent(JToggleButton component, Boolean value) {
        component.setSelected(value);
    }

    @Override
    protected void clear() {
        component.setSelected(false);
    }
    
}
