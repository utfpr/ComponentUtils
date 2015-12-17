/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.nfe.gui.actions;

import java.awt.Dialog;

/**
 *
 * @author marcelo
 */
public interface Action {

    void addGuiListener(Listener ls);
    void removeGuiListener(Listener ls);
    
    void setDialog(Dialog dialog);
    
    public void addAllListenerScanner(Object container);

//    public <A extends Listener> List<A> getListener(Class<A> classListener);

    interface Listener  {
    }
}
