/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.componentUtils.impl;

import com.m4rc310.ui.gui.componentUtils.ComponentBuilder;
import com.m4rc310.ui.gui.componentUtils.GuiUtils;
import com.m4rc310.ui.gui.utils.GuiManager;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 *
 * @author marcelo
 */
public abstract class MJDialog extends JDialog implements 
        com.m4rc310.ui.gui.componentUtils.impl.MJDialogActions,
        GuiUtils.MListener {

    protected GuiUtils gui;
    protected final ComponentBuilder componentBuilder;
    protected final GuiManager guiManager;
    

    public MJDialog() {
        this.componentBuilder = new ComponentBuilder();
        this.guiManager = GuiManager.getInstance();
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
        _initListeners();
//        initLayout();
//        initListeners();
    }

//    public abstract void initLayout(); 
//    public abstract void initListeners(); 
    
    private void _initListeners() {
        gui = new GuiUtilsImpl();
        if (this instanceof GuiUtils.MListener) {
            GuiUtils.MListener mel = (GuiUtils.MListener) this;
            gui.setMessageErrorListener(mel);
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (close()) {
                    dispose();
                } else {
                    dispatchEvent(new WindowEvent(MJDialog.this, WindowEvent.WINDOW_STATE_CHANGED));
                }
            }
        });
    }

    
    @Override
    public void showWindow() {
        setVisible(true);
    }

    @Override
    public void closeWindow() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    protected boolean close() {
        return true;
    }

    @Override
    public void memorizeBounds() {
        String key = String.format("%s", MJDialog.this.getClass().getName());
        Preferences p = Preferences.userNodeForPackage(getClass()).node(key);
        String bounds = p.get("bounds", null);
        if (bounds != null) {
            setBounds(decodeBounds(bounds));
        }

        addWindowListener(new WindowAdapter() {
            String key = String.format("%s", MJDialog.this.getClass().getName());
//            Preferences p = Preferences.userRoot().node(key);
            Preferences p = Preferences.userNodeForPackage(getClass()).node(key);

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Window win = e.getWindow();
                    p.put("bounds", encodeRectangle(win.getBounds()));
                    p.flush();
                } catch (BackingStoreException ex) {
                    Logger.getLogger(MJDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    @Override
    public void abiliteCloseOnESC() {
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        MJDialog.this.getRootPane().registerKeyboardAction((ActionEvent e) -> {
            MJDialog.this.dispatchEvent(new WindowEvent(MJDialog.this, WindowEvent.WINDOW_CLOSING));
        }, stroke, WIDTH);
    }

    private Rectangle decodeBounds(String bounds) {
        Rectangle rect = null;
        String[] array = bounds.split(",");
        if (array.length == 4) {
            try {
                rect = new Rectangle();
                rect.x = Integer.parseInt(array[0]);
                rect.y = Integer.parseInt(array[1]);
                rect.width = Integer.parseInt(array[2]);
                rect.height = Integer.parseInt(array[3]);
            } catch (NumberFormatException nfe) {
                rect = null;
            }
        }
        return rect;
    }

    public static String encodeRectangle(Rectangle bounds) {
        StringBuilder buf = new StringBuilder();
        buf.append(bounds.x);
        buf.append(',');
        buf.append(bounds.y);
        buf.append(',');
        buf.append(bounds.width);
        buf.append(',');
        buf.append(bounds.height);
        return buf.toString();
    }

    protected void fire(Object target, String method, Object... args) {
        Class clas = target.getClass();
        for (Method mt : clas.getDeclaredMethods()) {
            if (method.equals(mt.getName())) {
                try {
                    mt.setAccessible(true);
                    mt.invoke(target, args);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(MJDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void messageError(Object source, String mesage) {
        JOptionPane.showMessageDialog(rootPane, mesage, getString("titulo.messagem.erro"), JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public String getString(String key, Object... args) {
        return MessageFormat.format(key, args);
    }
}
