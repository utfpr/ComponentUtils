/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListPanel extends JPanel {

    private static final int N = 5;
    private final DefaultListModel dlm = new DefaultListModel();
    private final JList list = new JList(dlm);

    public ListPanel() {
        super(new GridLayout());
        for (int i = 0; i < N * N; i++) {
            String name = "Cell:" + String.format("%02d", i);
            dlm.addElement(name);            
        }
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(N);
        list.setCellRenderer(new ListRenderer());
        list.addListSelectionListener(new SelectionHandler());
        this.add(list);
    }

    private class ListRenderer extends DefaultListCellRenderer {

        public ListRenderer() {
            this.setBorder(BorderFactory.createLineBorder(Color.red));
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object
            value, int index, boolean isSelected, boolean cellHasFocus) {
            JComponent jc =  (JComponent) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
            jc.setBorder(BorderFactory.createEmptyBorder(N, N, N, N));
            return jc;
        }
    }

    private class SelectionHandler implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                //System.out.println(Arrays.toString(list.getSelectedValues()));
            }
        }
    }

    private void display() {
        JFrame f = new JFrame("ListPanel");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ListPanel().display();
            }
        });
    }
}
