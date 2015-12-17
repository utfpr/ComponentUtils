/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.componentUtils;

import com.m4rc310.ui.gui.componentUtils.adapters.AdapterJTextComponent;
import javax.swing.JTextField;

/**
 *
 * @author marcelo
 */
public class Main {
    
    ComponentBuilder componentBuilder = new ComponentBuilder();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Main().init();
        
    }

    private void init() {
        componentBuilder.registerComponentAdapter(new AdapterJTextComponent());
        JTextField jtf = new JTextField();
        
        System.out.println(componentBuilder.getAdapterForComponent(jtf));
        
        
    }
}
