/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.componentUtils;

import com.m4rc310.ui.gui.componentUtils.impl.GuiUtilsImpl;
import javax.swing.JPanel;

/**
 *
 * @author mls
 */
public abstract class MJPanel extends JPanel{
    protected final GuiUtils g ;
    protected final ComponentBuilder componentBuilder;

    public MJPanel() {
        this.componentBuilder = new ComponentBuilder();
        this.g = new GuiUtilsImpl(){
            @Override
            public String getText(String text, Object... args) {
                return getString(text, args);
            }
        };
        
    }
    
//    public abstract void initLayout();
    
    protected String getString(String text, Object... args){
        return text;
    }
}
