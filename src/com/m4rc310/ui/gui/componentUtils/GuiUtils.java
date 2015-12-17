/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.componentUtils;

import com.m4rc310.ui.gui.utils.TableFormatter;
import java.awt.Color;
import java.awt.Container;
//import com.m4rc310.ui.gui.utils.jtable.MTableModel2;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
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
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

/**
 *
 * @author marcelo
 */
public interface GuiUtils {
    //  Ações auxiliares para componentes

    void editableComponents(boolean editable, JTextComponent... components);

    void enableComponents4Store(boolean enable, JComponent... components);

    void enableComponents(boolean enable, JComponent... components);
    
    void enableAllComponents(Object source, boolean enable);
    

    void linkComponents(JComponent component, JComponent... components);

    void setPrompt2jTextComponent(String prompt, JTextComponent jtc);

    void validateField(boolean valid, JComponent field, String msg, Object... args) throws Exception;

    void validateField(boolean valid, String msg, Object... args) throws Exception;

    void setMessageErrorListener(MListener mel);

    void setTableFormatter(TableFormatter tf);

//    void setFieldSearch(JTextComponent component, boolean any, MTableModel2 tableModel);
    void memorizeTextField(JTextComponent... components);

    void linkActionForJButton(JButton jButton, Object target, String method, Object... args);

    void linkActionListenerForJComponent(JComponent jComponent, Object target, String method, Object... args);

    void putFont4Labels(Font font);
    
    void setFontToType(Font font,Color foreground, Object target, Class... types);

    String getText(String text, Object... args);

    //  JLabels
    JLabel getJLabel(String text);

    JLabel getJLabel(String text, Object componentTarget);

    // JTextFields
    JPasswordField getJPasswodField();
    JPasswordField getJPasswodField(String pass);
    
    JTextField getJTextField();

    JTextField getJTextField(String text);

    JTextField getJTextField(String text, String prompt);
    
    
    void registerPrompt(JTextComponent com, String text);
    

    // Containers
    JPanel getJPanel();

    JPanel getJPanel(String title);

    JPanel getJPanel(LayoutManager layoutManager);

    JPanel getJPanel(LayoutManager layoutManager, String title);

    JSeparator getJSeparator();
    JSeparator getJSeparator(String title);

    JScrollPane getJScrollPane();

    // JButtons
    JButton getJButton(String text);

    JButton getJButton(String text, ActionListener al);

    JCheckBox getJCheckBox(String text);

    // JTables
    JTable getJTable();

    JTable getJTable(TableModel model);

    JTabbedPane getJTabbedPane();

    JTabbedPane getJTabbedPane(String... panels);

    public JComboBox getJComboBox(String string);

    public void setText4JTextComponent(String cpfCnpj, JTextComponent... components);

    void setFontToAllComponentsContainer(Object o, Font font);
    
    void setForeground(Color color,Object... components);
    
    public void setFont(Font font, JComponent... components);

    public void setFontBorder(Font font);

    public void formatJTextComponent(String format, JTextComponent... components);

    public JTextArea getJTextArea();

    public JTextArea getJTextArea(String text);

    void setBorderToComponent(Font font, String title, JComponent... components);

    void setBorderToComponent(String title, JComponent... components);

    public void setIcon(Object jc, String iconfile);

    public void setIcon(Object jc, String iconfile, int w, int h);

    public void formateDate(JTextComponent jTextFieldDate, String format);

    public void putActionListener(JComponent target, ActionListener actionListener);

    public void putKeyListener(KeyListener keyListener, JComponent... components);

    public void removeKeyListener(KeyListener keyListener, JComponent... components);

    public void processAnnotations(Object source, Object target);

    public void loadValuesFromObjectJson(Object source, Object target);

    void setFieldListener(FieldListener fl);

    void putLayoutManager(LayoutManager layoutManager, JComponent... components);
    
    void grabFocus(Object target,String fieldKey);
    
    void setHorizontalAlignment(int type,Object... components);
    
    void setVisible(Object container, String key, boolean visible);
    
    void setEnabled(Object container, String key, boolean visible);

    // Listeners 
    public interface FieldListener {

        void updateValue(String field, Object value);
    }

    interface MListener {

        void messageError(Object source, String mesage);

        String getString(String key, Object... args);

//        void search(JTextComponent component, boolean anny, MTableModel2 tableModel);
    }
}
