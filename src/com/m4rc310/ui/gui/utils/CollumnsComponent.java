/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.utils;

/**
 *
 * @author marcelo
 */
public class CollumnsComponent {
        private String collumnName;
        private int collumnWidth;

        public CollumnsComponent(String columnName) {
            this.collumnName = columnName;
        }

        public CollumnsComponent(String collumnName, int collumnWidth) {
            this.collumnName = collumnName;
            this.collumnWidth = collumnWidth;
        }

        public String getCollumnName() {
            return collumnName;
        }

        public int getCollumnWidth() {
            return collumnWidth;
        }
}
