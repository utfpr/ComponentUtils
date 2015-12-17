/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.tsi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcelo
 */
public class BundleActionsImpl implements BundleActions_ {

    private static final String BUNDLE = "bundle";
    private static final String BUNDLE_FILE_PROPERTIES = "src/resources/bundle.properties";
    private File fileProperties;
    private ResourceBundle bundle;
    private boolean existsBundleFile;
    private final List<LogListener> logListeners;

    public BundleActionsImpl() {
        prepareProperties();
        logListeners = new ArrayList<>();
    }

    private void prepareProperties() {
        try {
            bundle = ResourceBundle.getBundle(BUNDLE);
            fileProperties = getFileProperties();
            existsBundleFile = true;
        } catch (Exception e) {
            Logger.getLogger(BundleActionsImpl.class.getName()).info(e.getMessage());
            existsBundleFile = false;
        }
    }

    private File getFileProperties() throws IOException {
        String stringFile = getClass().getResource("").getFile();
        File file = new File(BUNDLE_FILE_PROPERTIES);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    // TODO apagar
    public static void main(String[] args) {
        System.out.println(new BundleActionsImpl().getStringBundle("mls"));
    }

    @Override
    public String getStringBundle(String key, Object... args) {
        String msg = key;
        if (key.isEmpty()) {
            return "";
        }
        try {
            if (bundle.containsKey(key)) {
                msg = bundle.getString(key);
            } else {
                putActionOnNotExistsKey(key, key);
            }
        } catch (Exception e) {
        }
        return MessageFormat.format(msg, args);
    }

    @Override
    public void putActionOnNotExistsKey(String key, String value) {
        Iterator<String> it = bundle.keySet().iterator();

        Properties props = new Properties();
        try (InputStream fis = new FileInputStream(fileProperties); OutputStream out = new FileOutputStream(fileProperties)) {
            props.load(fis);
            while (it.hasNext()) {
                String _key = it.next();
                props.put(_key, bundle.getString(_key));
            }
            props.put(key, value);
            String comentario = "Arquivo alterado automáticamente em {0}, para a inclusão da chave: {1}";
            props.store(out, MessageFormat.format(comentario, new Date(), key));
        } catch (Exception ex) {
            Logger.getLogger(BundleActionsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void logString(String key, Object... args) {
        String msg = MessageFormat.format(key, args);
//        String msg = MessageFormat.format(key, args);
        for (LogListener ll : logListeners) {
            ll.informeLog(msg);
        }
//        Logger.getLogger(BundleActionsImpl.class.getName()).log(Level.INFO, msg);
        System.out.println(msg);
    }

    @Override
    public void _addLogListener(LogListener ll) {
        if (!logListeners.contains(ll)) {
            logListeners.add(ll);
        }
    }
}
