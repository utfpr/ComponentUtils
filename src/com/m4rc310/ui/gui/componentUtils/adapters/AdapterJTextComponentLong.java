/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.componentUtils.adapters;

import com.m4rc310.ui.gui.componentUtils.ComponentAdapter;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

/**
 *
 * @author mls
 */
public class AdapterJTextComponentLong extends ComponentAdapter<JTextComponent, Long> {

//    public void includeListeners() {
//        Document doc = component.getDocument();
//        doc.addDocumentListener(documentListener);
//    }
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
            fire(component, component.getText(), readFields.field());
        }

    };

    @Override
    protected void initListeners() {
        component.getDocument().addDocumentListener(documentListener);
        component.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
//                JTextComponent c = (JTextComponent) input;
//                c.setText(processFormat(c.getText()));
                return true;
            }
        });
    }

    @Override
    protected void putValueToComponent(JTextComponent component, Long value) {
        String slong = value + "";
        component.setText(slong);
    }
    @Override
    protected void clear() {
        component.setText("");
    }
    

}
