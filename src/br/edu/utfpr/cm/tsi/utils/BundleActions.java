/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.tsi.utils;

/**
 *
 * @author marcelo
 */
public interface BundleActions {

    String getStringBundle(String key, Object... args);
    String getStringBundleSource(Object source, String key, Object... args);
    
    void putActionOnNotExistsKey(String key, String value);
    
    interface LogListener{
        void informeLog(String text);
    }
    
}
