/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.componentUtils.adapters;

import com.m4rc310.ui.gui.componentUtils.ComponentAdapter;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author mls
 */
public class AdapterJPasswordField extends ComponentAdapter<JPasswordField, char[]> {

        private final DocumentListener documentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            warn();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            warn();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            warn();
        }

        public void warn() {
            String pass = new String(component.getPassword());
            
            fire(component, pass, readFields.field());
        }

    };
    
    @Override
    protected void initListeners() {
        component.getDocument().addDocumentListener(documentListener);
        
    }

    @Override
    protected void clear() {
        component.setText("");
    }

    @Override
    protected void putValueToComponent(JPasswordField component, char[] value) {
        component.setText(new String(value));
    }

}
