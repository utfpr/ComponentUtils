/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.tsi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 *
 * @author marcelo
 */
public class BundleActionsImpl2 implements BundleActions {

    private static final String BUNDLE = "bundle";
    private static final String BUNDLE_FILE_PROPERTIES = "src/bundle.properties";
    private ResourceBundle bundle;

    public BundleActionsImpl2() {
        init();
    }

    private void init() {
        try {
            File file = new File(BUNDLE_FILE_PROPERTIES);
            file.createNewFile();
            bundle = ResourceBundle.getBundle(BUNDLE);
        } catch (Exception e) {
            Logger.getLogger(BundleActionsImpl.class.getName()).info(e.getMessage());
        }
    }

    public static void main(String[] args) {
        String value = new BundleActionsImpl2().getStringBundle("ola10", "Marcelo");
        System.out.println(value);
    }

    @Override
    public String getStringBundle(String key, Object... args) {

        if (key.length() < 4) {
            return key;
        }

        try {
            if (bundle.containsKey(key)) {
                key = bundle.getString(key);
            } else {
                Properties props = new Properties();
                OutputStream os;
                try (InputStream is = new FileInputStream(BUNDLE_FILE_PROPERTIES)) {
                    props.load(is);
                    props.putIfAbsent(key, key);
                    os = new FileOutputStream(BUNDLE_FILE_PROPERTIES);
                    props.store(os, "(c) Marcelo Lopes");
                }
                os.close();
            }
        } catch (Exception e) {
//            return getStringBundle(key, args);
//            Logger.getLogger(BundleActionsImpl.class.getName()).info(e.getMessage());
        }
        return MessageFormat.format(key, args);
    }

    @Override
    public void putActionOnNotExistsKey(String key, String value) {
        Properties props = new Properties();
        try (InputStream fis = new FileInputStream(BUNDLE_FILE_PROPERTIES);
                OutputStream out = new FileOutputStream(BUNDLE_FILE_PROPERTIES)) {
            props.load(fis);
            props.put(key, String.format("%s_mmm", value));
            props.store(out, "...");
        } catch (Exception ex) {
            //Logger.getLogger(BundleActionsImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getStringBundleSource(Object source, String key, Object... args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
