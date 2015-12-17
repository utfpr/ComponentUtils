/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.utils;

import java.util.List;

/**
 *
 * @author marcelo
 * @param <T>
 */
public interface TableFormatter<T> {

    public void addCollumn(String name, int width);

    public void addCollumns(String... name);
    
    public void setValue(int i, Object value);

    public Object[] desmemberObject(T o) ;
    
    public List<CollumnsComponent> getCollumnsComponents();
}