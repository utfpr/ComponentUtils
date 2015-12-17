/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marcelo
 * @param <T>
 */
public abstract class TableFormatterAdapter<T> implements TableFormatter<T> {

    private final List<CollumnsComponent> collumns;
    private FormatterListener<T> formatterListener;

    public TableFormatterAdapter() {
        collumns = new ArrayList<>();
    }

    public void setFormatterListener(FormatterListener<T> formatterListener) {
        this.formatterListener = formatterListener;
    }

    @Override
    public void addCollumn(String name, int width) {
        collumns.add(new CollumnsComponent(name, width));
    }

    @Override
    public void addCollumns(String... name) {
        for (String n : name) {
            addCollumn(n, 0);
        }
    }

    @Override
    public List<CollumnsComponent> getCollumnsComponents() {
        return collumns;
    }

    @Override
    public void setValue(int i, Object value) {

    }

    @Override
    public Object[] desmemberObject(T o) {
        if (formatterListener != null) {
            return formatterListener.desmemberObject(o);
        }
        return getObjects(o);
    }

    protected Object[] getObjects(Object... objects) {
        return objects;
    }

    public interface FormatterListener<T> {

        Object[] desmemberObject(T o);
    }

}
