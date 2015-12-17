/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.componentUtils.impl;

import br.edu.utfpr.cm.tsi.utils.B;
import br.edu.utfpr.cm.tsi.utils.LogListener;
import br.edu.utfpr.cm.tsi.utils.LogServer;
import com.m4rc310.ui.gui.componentUtils.ComponentAdapter;
import com.m4rc310.ui.gui.componentUtils.ComponentBuilder;
import com.m4rc310.ui.gui.componentUtils.GuiUtils;
import com.m4rc310.ui.gui.componentUtils.ReadFields;
import com.m4rc310.ui.gui.utils.MD5Utils;
import com.m4rc310.ui.gui.utils.TableFormatter;
import com.m4rc310.utils.DateUtils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author marcelo
 * @version 1.0
 */
public class GuiUtilsImpl implements GuiUtils {

    private MListener mListeners;
    private FieldListener fieldListener;
    private Font font;
    private Font fontBorder;
    private final Map<JComponent, Boolean> mapComponentsEnabled;
    private final ComponentBuilder componentBuilder;
    private Object mapComponents;

    public GuiUtilsImpl() {
        this.mapComponentsEnabled = new HashMap<>();
        this.componentBuilder = new ComponentBuilder();
    }

    @Override
    public String getText(String text, Object... args) {
        if (mListeners != null) {
            return mListeners.getString(text, args);
        } else {
            return MessageFormat.format(text, args);
        }
    }

    @Override
    public void editableComponents(boolean editable, JTextComponent... components) {
        for (JTextComponent jtc : components) {
            jtc.setEditable(editable);
//            jtc.setFocusable(editable);
        }
    }

    @Override
    public void enableComponents4Store(boolean enable, JComponent... components) {
        for (JComponent jtc : components) {
            if (!mapComponentsEnabled.containsKey(jtc)) {
                mapComponentsEnabled.put(jtc, jtc.isEnabled());
                jtc.setEnabled(enable);
            } else {
                jtc.setEnabled(mapComponentsEnabled.get(jtc));
                mapComponentsEnabled.remove(jtc);
            }
        }

    }

    @Override
    public void enableComponents(boolean enable, JComponent... components) {
        for (JComponent jtc : components) {
            jtc.setEnabled(enable);
        }
    }

    private final Map<Component, Boolean> mapsComponents = new HashMap<>();

    @Override
    public void enableAllComponents(Object source, boolean enable) {
        Class c = source.getClass();
        try {
            if (enable) {
                mapsComponents.entrySet().stream().forEach((entrySet) -> {
                    Component key = entrySet.getKey();
                    Boolean value = entrySet.getValue();
                    key.setEnabled(value);
                    //mapsComponents.remove(key);
                });
                return;
            }

            Method m = c.getMethod("getComponents");
            m.setAccessible(true);

            Component[] components = (Component[]) m.invoke(source);
            for (Component component : components) {
                //setFontToAllComponentsContainer(component, font);
                enableAllComponents(component, enable);
                try {
                    mapsComponents.put(component, component.isEnabled());
                    m = component.getClass().getMethod("setEnabled", boolean.class);
                    m.setAccessible(true);
                    m.invoke(component, false);
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

                }
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            LogServer.getInstance().error(source, e.getMessage());
        }
    }

    @Override
    public void linkComponents(JComponent component, final JComponent... components) {

        LogServer.getInstance().debug(component, "efetuando linkagem de componentes");
        component.addPropertyChangeListener(new PropertyChangeListener() {
            private final JComponent[] _components = components;
            
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

                if ("enabled".equals(evt.getPropertyName())) {
                    JComponent jtc = (JComponent) evt.getSource();
                    for (JComponent jc : _components) {
                        jc.setEnabled(jtc.isEnabled());
                        LogServer.getInstance().debug(jc, "Alterando <enable> [{0}] do componente: {1}", jtc.isEnabled(), jc);
                    }
                }

                if ("editable".equals(evt.getPropertyName())) {
                    try {
                        JTextComponent jtc = (JTextComponent) evt.getSource();
                        for (JComponent jc : _components) {
                            LogServer.getInstance().debug(jc, "Alterando <editable> [{0}] do componente: {1}", jtc.isEnabled(), jc);
                            ((JTextComponent) jc).setEditable(jtc.isEditable());
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override
    public void setPrompt2jTextComponent(String prompt, JTextComponent jtc) {
        //final String _prompt = getText(prompt).trim();
        final String _prompt = prompt.trim();
        if (jtc.getText().isEmpty()) {

            Font oldFont = jtc.getFont();
            Font newFont = new Font(oldFont.getName(), Font.BOLD, oldFont.getSize());
            jtc.setFont(newFont);
            jtc.setForeground(Color.LIGHT_GRAY);

            jtc.setText(_prompt);
        }

        jtc.addFocusListener(new FocusAdapter() {
            boolean ischange = false;

            @Override
            public void focusLost(FocusEvent e) {
                JTextComponent c = (JTextComponent) e.getComponent();
                if (c.getText().isEmpty()) {
                    c.setText(_prompt);
                    Font oldFont = jtc.getFont();
                    Font newFont = new Font(oldFont.getName(), Font.BOLD, oldFont.getSize());
                    jtc.setFont(newFont);
                    c.setForeground(Color.LIGHT_GRAY);
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                JTextComponent c = (JTextComponent) e.getComponent();
//                if (c.getText().isEmpty()) {
//                    c.setText("");
//                    c.setCaretPosition(0);
//                    c.setForeground(Color.BLACK);
//                }
                if (_prompt.equals(c.getText())) {
                    c.setCaretPosition(0);
                    Font oldFont = jtc.getFont();
                    Font newFont = new Font(oldFont.getName(), Font.BOLD, oldFont.getSize());
                    jtc.setFont(newFont);
                    c.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

        jtc.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                JTextComponent c = (JTextComponent) e.getComponent();
                if (_prompt.equals(c.getText())) {
                    c.setText("");
                    Font oldFont = jtc.getFont();
                    Font newFont = new Font(oldFont.getName(), Font.TYPE1_FONT, oldFont.getSize());
                    jtc.setFont(newFont);
                    c.setForeground(Color.BLACK);
                }
//                
//                if(c.getText().isEmpty()){
//                    c.setText(_prompt);
//                    c.setForeground(Color.GRAY);
//                }

            }

        });

    }

    @Override
    public void validateField(boolean valid, JComponent field, String msg, Object... args) throws Exception {
        if (valid) {
            if (mListeners != null) {
                mListeners.messageError(field, getText(msg, args));
            } else {
                throw new Exception(msg);
            }
        }
    }

    @Override
    public void validateField(boolean valid, String msg, Object... args) throws Exception {
        validateField(valid, null, msg, args);
    }

    @Override
    public void setMessageErrorListener(MListener mel) {
        this.mListeners = mel;
    }

    @Override
    public JLabel getJLabel(String text) {
        JLabel label = new JLabel(getText(text));
        if (font != null) {
            label.setFont(font);
        }
        return label;
    }

    @Override
    public JSeparator getJSeparator() {
        JSeparator ret = new JSeparator(JSeparator.HORIZONTAL);
        return ret;
    }

    @Override
    public JSeparator getJSeparator(String title) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JLabel getJLabel(String text, Object component) {
        final JLabel jLabel = getJLabel(text);

        JComponent componentTarget = (JComponent) component;

        jLabel.setLabelFor(componentTarget);
        componentTarget.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                JComponent com = (JComponent) evt.getSource();
                jLabel.setEnabled(com.isEnabled());
            }
        });
        return jLabel;
    }

    @Override
    public JTextField getJTextField() {
        return getJTextField("");
    }

    @Override
    public JTextField getJTextField(String text) {
        JTextField jTextField = new JTextField(){
            @Override
            public void grabFocus() {
                super.grabFocus();
//                requestFocus();
            }
        };
        jTextField.setText(text);
        return jTextField;
    }

    @Override
    public void registerPrompt(JTextComponent com, String prompt) {
        final String finalPrompt = prompt;

        com.setText(prompt);

        com.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                JTextComponent c = (JTextComponent) e.getComponent();
                super.keyPressed(e);
            }

        });

    }

    @Override
    public JTextField getJTextField(String text, final String prompt) {

        final String finalPrompt = B.getString(prompt);

        JTextField jTextField = new JTextField() {
            @Override
            public String getText() {
                String text = super.getText();
                if (finalPrompt.equals(text)) {
                    if (!isFocusOwner()) {
                        return "";
                    }
                }
                return super.getText();
            }

        };

        jTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                JTextComponent c = (JTextComponent) e.getComponent();
                c.setForeground(c.getText().trim().isEmpty() ? Color.GRAY : Color.BLACK);
                super.keyReleased(e); //To change body of generated methods, choose Tools | Templates.
            }
        });

        jTextField.setIgnoreRepaint(true);

        setPrompt2jTextComponent(finalPrompt, jTextField);
        return jTextField;
    }

    @Override
    public JPanel getJPanel() {
        JPanel panel = new JPanel();

        return panel;
    }

    @Override
    public JPanel getJPanel(String title) {
        final String ftitle = getText(title);
        JPanel jPanel = getJPanel();

        Font bfont = fontBorder;

        jPanel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
//                if ("foreground".equals(evt.getPropertyName())) {
//                    JPanel jp = (JPanel) evt.getSource();
//                    Border border = jp.getBorder();
//                    //LineBorder border = new LineBorder(Color.RED, 3, true);
//                    TitledBorder tborder = new TitledBorder(border, "Titled border", TitledBorder.CENTER,
//                            TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.BOLD, 14), Color.BLUE);
//                    jp.setBorder(border);
//                }

                if ("font".equals(evt.getPropertyName())) {
                    Font f = (Font) evt.getNewValue();
                    JPanel jp = (JPanel) evt.getSource();
                    Border border = jp.getBorder();
                    //UIManager.put("TitledBorder.font", new Font("Tahoma", Font.BOLD, 20));
                    border = BorderFactory.createTitledBorder(border, ftitle,
                            TitledBorder.DEFAULT_JUSTIFICATION,
                            TitledBorder.DEFAULT_POSITION, f);

                    jp.setBorder(border);
                }

            }
        });

        Border border = jPanel.getBorder();
        border = BorderFactory.createTitledBorder(border, ftitle,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, fontBorder);
        jPanel.setBorder(border);

        return jPanel;
    }

    @Override
    public JButton getJButton(String text) {
        JButton jButton = new JButton() {
        };
        text = getText(text);
        if (text.contains("__")) {
            String mnemonic = text.substring(text.indexOf("__"), 3);

        }

        return jButton;
    }

    @Override
    public JButton getJButton(String text, ActionListener al) {
        JButton jButton = getJButton(text);
        jButton.addActionListener(al);
        return jButton;
    }

    @Override
    public JTable getJTable() {
        return new JTable();
    }

    @Override
    public JTable getJTable(TableModel model) {
        JTable jTable = getJTable();
        jTable.setModel(model);
        return jTable;
    }

    @Override
    public JScrollPane getJScrollPane() {
        return new JScrollPane();
    }

    // TODO Remover
    @Deprecated
    @Override
    public void setTableFormatter(TableFormatter tf) {
    }

//    @Override
//    public void setFieldSearch(final JTextComponent component, final boolean anny, final MTableModel2 tableModel) {
//        if (this.mListeners != null) {
//            component.setBackground(new Color(255, 253, 231));
//            component.addKeyListener(new KeyAdapter() {
//                @Override
//                public void keyReleased(KeyEvent e) {
//                    if ((e.getKeyCode() == KeyEvent.VK_F2)) {
//                        mListeners.search((JTextComponent) e.getComponent(), anny, tableModel);
//                    }
//                }
//            });
//
//            component.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    if (e.getClickCount() == 2) {
//                        mListeners.search((JTextComponent) e.getComponent(), anny, tableModel);
//                    }
//                }
//            });
//        }
//
//    }
    @Override
    public void memorizeTextField(JTextComponent... components) {
        final String _key = MD5Utils.getHashMD5(getClass().getName());

        for (final JTextComponent jtc : components) {
            jtc.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    try {
                        Preferences p = Preferences.userRoot().node(_key);
                        JTextComponent comp = (JTextComponent) e.getComponent();
                        String key = comp.getClass().getName();
                        p.put(key, comp.getText());
                        p.flush();
                    } catch (Exception ex) {
                    }
                }
            });

            jtc.getRootPane().addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("Frame.active".equals(evt.getPropertyName())) {
                        Preferences p = Preferences.userRoot().node(_key);
                        String key = jtc.getClass().getName();
                        jtc.setText(p.get(key, ""));
                    }
                }
            });

        }
    }

    @Override
    public void linkActionForJButton(JButton jButton, Object target, String method, Object... args) {
        final Object _target = target;
        final String _method = method;
        final Object[] _args = args;

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Class cls = _target.getClass();
                try {
                    for (Method method : cls.getDeclaredMethods()) {
                        if (method.getName().equals(_method)) {
                            method.setAccessible(true);
                            method.invoke(_target, _args);
                        }
                    }
                } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    ex.printStackTrace(System.err);
                }

            }
        });
    }

    @Override
    public void linkActionListenerForJComponent(JComponent jComponent, Object target, String method, Object... args) {
        jComponent.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println(evt.getPropertyName());
            }
        });
    }

    @Override
    public JComboBox getJComboBox(String string) {
        JComboBox ret = new JComboBox();
        return ret;
    }

    @Override
    public JCheckBox getJCheckBox(String text) {
        JCheckBox jcb = new JCheckBox(getText(text));
        return jcb;
    }

    @Override
    public void putFont4Labels(Font font) {
        this.font = font;
    }

    @Override
    public void setFontToType(Font font, Color foreground, Object target, Class... types) {
        Class clasz = target.getClass();
        try {
            Method m = clasz.getMethod("getComponents");
            m.setAccessible(true);
            Component[] components = (Component[]) m.invoke(target);
            for (Component component : components) {
                setFontToType(font, foreground, component, types);
//
                Class componentClass = component.getClass();
                for (Class type : types) {
                    if (type == componentClass) {
                        Method method = componentClass.getMethod("setFont", Font.class);
                        method.setAccessible(true);
                        method.invoke(component, font);
                        method = componentClass.getMethod("setForeground", Color.class);
                        method.setAccessible(true);
                        method.invoke(component, foreground);
                    }
                }
            }

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setText4JTextComponent(String text, JTextComponent... components) {
        for (JTextComponent jtc : components) {
            jtc.setText(text);
        }
    }

    @Override
    public JTabbedPane getJTabbedPane() {
        JTabbedPane r = new JTabbedPane();
        return r;
    }

    @Override
    public JTabbedPane getJTabbedPane(String... panels) {
        JTabbedPane r = getJTabbedPane();
        for (String s : panels) {
            r.add(new JPanel(), s);
        }

        return r;
    }

    @Override
    public void setFont(Font font, JComponent... components) {
        for (JComponent jc : components) {
            jc.setFont(font);
        }
    }

    @Override
    public void setFontBorder(Font font) {
        this.fontBorder = font;
    }

    @Override
    public void formatJTextComponent(final String format, JTextComponent... components) {
        for (JTextComponent jtc : components) {
            jtc.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    JTextComponent _jtc = (JTextComponent) e.getComponent();
                    _jtc.setText(format(format, _jtc.getText()));
                }
            });
        }
    }

    private String format(String pattern, Object value) {
        MaskFormatter mask;
        try {

            mask = new MaskFormatter(pattern);
//            mask.setValueContainsLiteralCharacters(false);

//            mask.setPlaceholderCharacter('*');
            return mask.valueToString(value);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JTextArea getJTextArea() {
        JTextArea ret = getJTextArea(null);
        return ret;
    }

    @Override
    public JTextArea getJTextArea(String text) {
        return new JTextArea(text);
    }

    @Override
    public void setIcon(Object jc, String iconfile) {
        try {
            ImageIcon ic = new ImageIcon(iconfile);
            Method m = jc.getClass().getMethod("setIcon", Icon.class);
            m.setAccessible(true);
            m.invoke(jc, ic);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        }

        //setIcon(jc, iconfile, 50, 40);
    }

    @Override
    public void setIcon(Object jc, String iconfile, int w, int h) {
        try {

//            URL url = new URL(iconfile);
//            ImageIcon imc = new ImageIcon(iconfile);
            ImageIcon img = new ImageIcon(iconfile);

//            ImageIcon img = new ImageIcon(im);
            Image dimg = img.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            Class clss = jc.getClass();
            Method m = clss.getMethod("setIcon", Icon.class);
            m.setAccessible(true);
            m.invoke(jc, new ImageIcon(dimg));
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void formateDate(JTextComponent jTextComponentDate, String format) {
        InputVerifier inputVerifier = new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                try {
                    JTextComponent jtc = (JTextComponent) input;
                    Date dt = DateUtils.getDate(jtc.getText(), format);

                } catch (Exception e) {
                }
                return true;
            }
        };
    }

    @Override
    public void putActionListener(JComponent target, ActionListener actionListener) {
        boolean contains = false;
        for (ActionListener al : target.getListeners(ActionListener.class)) {
            if (actionListener.equals(al)) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            Class clas = target.getClass();
            try {
                Method m = clas.getMethod("addActionListener", ActionListener.class);
                m.invoke(target, actionListener);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(GuiUtilsImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void putKeyListener(KeyListener keyListener, JComponent... components) {
        for (JComponent component : components) {
            boolean contains = false;
            for (KeyListener kl : component.getKeyListeners()) {
                if (kl.equals(keyListener)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                component.addKeyListener(keyListener);
            }
        }

    }

    @Override
    public void removeKeyListener(KeyListener keyListener, JComponent... components) {
        for (JComponent component : components) {
            for (KeyListener kl : component.getKeyListeners()) {
                if (kl.equals(keyListener)) {
                    component.removeKeyListener(kl);
                }
            }
        }
    }

    @Override
    public void setBorderToComponent(Font font, String title, JComponent... components) {
        title = getText(title);
        for (JComponent component : components) {
            Border border = BorderFactory.createTitledBorder(title);
            border = BorderFactory.createTitledBorder(border, title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, font);
            component.setBorder(border);
        }
    }

    @Override
    public void setBorderToComponent(String title, JComponent... components) {
        title = getText(title);
        for (JComponent component : components) {
            Border border = BorderFactory.createTitledBorder(title);
            component.setBorder(border);
        }
    }

    @Override
    public void loadValuesFromObjectJson(Object source, Object target) {
        Class type = target.getClass();
        for (Field field : type.getDeclaredFields()) {
            if (!field.isAnnotationPresent(ReadFields.class)) {
                continue;
            }
            final ReadFields ann = field.getAnnotation(ReadFields.class);
            try {
                Class typeSource = source.getClass();
                Method m = typeSource.getMethod("get", Object.class);
                m.setAccessible(true);
                Object o = m.invoke(source, ann.field());

                String format = ann.format();

                field.setAccessible(true);
                Object objectField = field.get(target);

                if (objectField instanceof JTextComponent) {
                    Method mt = objectField.getClass().getMethod("setText", String.class);
                    mt.setAccessible(true);

                    String value = "";

                    if (!format.isEmpty()) {
                        if (o == null) {
                            value = "";
                        } else {
                            value = String.format(format, o);
                        }
                    } else {
                        value = String.format("%s", o);
                    }

                    mt.invoke(objectField, value);
                }

                //new JCheckBox().setSelected();
                if (objectField instanceof JToggleButton) {
                    Method mt = objectField.getClass().getMethod("setSelected", boolean.class);
                    mt.setAccessible(true);
                    mt.invoke(objectField, o);
                }

//                if (objectField instanceof JTextComponent) {
//                    ((JTextComponent)objectField).setText(o+"");
//                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void processAnnotations(Object source, Object target) {
        Class type = source.getClass();
        for (Field field : type.getDeclaredFields()) {
            if (!field.isAnnotationPresent(ReadFields.class)) {
                continue;
            }

            final ReadFields ann = field.getAnnotation(ReadFields.class);

            field.setAccessible(true);

            try {
                Object objectField = field.get(source);
                ComponentAdapter ca = componentBuilder.getAdapterForComponent(objectField);
                if (ca == null) {
                    continue;
                }
                //ca.process(objectField, target);

            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void putLayoutManager(LayoutManager layoutManager, JComponent... components) {
        for (JComponent component : components) {
            component.setLayout(layoutManager);
        }
    }

    private void fireFieldListener(String field, Object value) {
        if (this.fieldListener != null) {
            fieldListener.updateValue(field, value);
        }
    }

    @Override
    public void setFieldListener(FieldListener fl) {
        this.fieldListener = fl;
    }

    @Override
    public JPanel getJPanel(LayoutManager layoutManager) {
        JPanel ret = getJPanel();
        ret.setLayout(layoutManager);
        return ret;
    }

    @Override
    public JPanel getJPanel(LayoutManager layoutManager, String title) {
        JPanel ret = getJPanel(title);
        ret.setLayout(layoutManager);
        return ret;
    }

    @Override
    public void setFontToAllComponentsContainer(Object o, Font font) {
        Class c = o.getClass();
        try {
            Method m = c.getMethod("getComponents");
            m.setAccessible(true);

            Component[] components = (Component[]) m.invoke(o);
            for (Component component : components) {
                setFontToAllComponentsContainer(component, font);
                try {
                    m = component.getClass().getMethod("setFont", Font.class);
                    m.setAccessible(true);
                    m.invoke(component, font);
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    LogServer.getInstance().error(e);
                }
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            LogServer.getInstance().error(e);
        }
    }

    @Override
    public void setForeground(Color color, Object... components) {
        for (Object component : components) {
            try {
                Method m = component.getClass().getMethod("setForeground", Color.class);
                m.setAccessible(true);
                m.invoke(component, color);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                LogServer.getInstance().error(e);
            }
        }

    }

    @Override
    public void grabFocus(Object target, String fieldKey) {
        Class c = target.getClass();
        try {
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ReadFields.class)) {
                    ReadFields rf = field.getAnnotation(ReadFields.class);
                    String key = rf.field();
                    if (key.equals(fieldKey)) {
                        field.setAccessible(true);
                        Object o = field.get(target);
                        Method m = o.getClass().getMethod("requestFocus");
                        m.setAccessible(true);
                        m.invoke(o);

                    }
                }
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            LogServer.getInstance().error(e);
        }
    }

    @Override
    public void setHorizontalAlignment(int type, Object... components) {
        for (Object component : components) {
            Class clasz = component.getClass();
            try {
                Method method = clasz.getMethod("setHorizontalAlignment", int.class);
                method.setAccessible(true);
                method.invoke(component, type);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                LogServer.getInstance().error(e);
            }
        }
    }

    @Override
    public void setEnabled(Object container, String key, boolean visible) {
        Class c = container.getClass();
        try {
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ReadFields.class)) {
                    ReadFields rf = field.getAnnotation(ReadFields.class);
                    String skey = rf.field();
                    if (skey.equals(key)) {
                        field.setAccessible(true);
                        Object o = field.get(container);
//                        new JTextField().setEnabled(visible);
                        Method m = o.getClass().getMethod("setEnabled", boolean.class);
                        m.setAccessible(true);
                        m.invoke(o, visible);
                    }
                }
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            LogServer.getInstance().error(e);
        }
    }

    @Override
    public void setVisible(Object container, String key, boolean visible) {
        Class c = container.getClass();
        try {
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ReadFields.class)) {
                    ReadFields rf = field.getAnnotation(ReadFields.class);
                    String skey = rf.field();
                    if (skey.equals(key)) {
                        field.setAccessible(true);
                        Object o = field.get(container);
                        Method m = o.getClass().getMethod("setVisible", boolean.class);
                        m.setAccessible(true);
                        m.invoke(o, visible);
                    }
                }
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            LogServer.getInstance().error(e);
        }
    }

    @Override
    public JPasswordField getJPasswodField() {
        JPasswordField jpf = new JPasswordField();
        return jpf;
    }

    @Override
    public JPasswordField getJPasswodField(String pass) {
        JPasswordField jpf = getJPasswodField();
        jpf.setText(getText(pass));
        return jpf;
    }

}
