/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.componentUtils.controls;

import com.m4rc310.ui.gui.componentUtils.impl.MJDialog;
import javax.swing.JTree;

/**
 *
 * @author Marcelo
 */
public class JDialogGuiControl extends MJDialog{
    private JTree jTreeDialogs;

    public JDialogGuiControl() {
        initLayout();
        initListeners();
    }

    private void initLayout() {
        
        pack();
    }

    private void initListeners() {
    }
    
    public static void main(String[] args) {
        new JDialogGuiControl().showWindow();
    }
}
