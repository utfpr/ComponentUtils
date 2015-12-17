/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.componentUtils;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.text.JTextComponent;

/**
 *
 * @author mls
 */
public class JTextComponentButton extends JTextComponent {

    private final JButton jButtonChoose;

    public JTextComponentButton() {
        this.jButtonChoose = new JButton();
        this.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int width = 3;
        int height = 3;
        Rectangle rect = g.getClipBounds();//Returns the current clip region boundary rectangle
        int x = rect.width / 3;
        int y = rect.height / 2;
        for (int i = 0; i < 3;) {
            g.fillOval(++i * x, y, width, height);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dimension = JTextComponentButton.super.getPreferredSize();
        dimension.height -= 6;
        dimension.width = dimension.height;
        return dimension;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        //add(jButtonChoose);
    }

}
