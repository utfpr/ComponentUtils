/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.componentUtils.adapters;

import com.m4rc310.ui.gui.componentUtils.ComponentAdapter;
import javax.swing.JLabel;

/**
 *
 * @author mls
 */
public class AdapterJLabel extends ComponentAdapter<JLabel, Object> {

    @Override
    protected void initListeners() {
    }

    @Override
    protected void putValueToComponent(JLabel component, Object value) {
        component.setText(processFormat(value));
    }

    private String processFormat(Object value) {
        try {
            String format = readFields.format();
            if (!format.isEmpty()) {
                value = String.format(format, value);
            }
            return value.toString();
        } catch (Exception e) {
            return value.toString();
        }
    }

    @Override
    protected void clear() {
        component.setText("");
    }

}
