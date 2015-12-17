/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.utils;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author marcelo
 */
public class MTableModel3<T> extends DefaultTableModel {

    private List<T> values;
    private TableFormatterAdapter<T> formatter;
    private CellChangeListener cellChangeListener;

    public MTableModel3() {
        values = new ArrayList<>();
    }

    public void setValues(List<T> values) {
        this.values = values;
        fireTableDataChanged();
    }

    public void removeAll() {
        values.clear();
        fireTableDataChanged();
    }

    public void clear() {
        values.clear();
        fireTableDataChanged();
    }

    public void addValue(T obj) {
        values.add(obj);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        try {
            return values.size();
        } catch (Exception e) {
            return super.getRowCount();
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        try {
            T value = values.get(row);
            return formatter.desmemberObject(value)[column];
        } catch (Exception e) {
            return values.get(row);
        }
    }

    public T getValue(int row) {
        return values.get(row);
    }

    public List<T> getValues() {
        return values;
    }

    @Override
    public int getColumnCount() {
        try {
            return formatter.getCollumnsComponents().size();
        } catch (Exception e) {
            return 1;
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        try {
            T value = values.get(row);

            fireCellChange(value, aValue, row, column);

        } catch (Exception e) {
            e.printStackTrace();
//            super.setValueAt(aValue, row, column); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        try {
            if (getRowCount() > 0) {
                Object value = getValueAt(0, columnIndex);
                return value.getClass();
            }
            return String.class;
        } catch (Exception e) {
            return String.class;
        }
    }

    @Override
    public String getColumnName(int column) {
        try {
            return formatter.getCollumnsComponents().get(column).getCollumnName();
        } catch (Exception e) {
            return "ND";
        }
    }

    public void setFormatter(TableFormatterAdapter<T> formatter) {
        this.formatter = formatter;
        fireTableStructureChanged();
    }

    public TableFormatterAdapter<T> getFormatter() {
        return formatter;
    }

    public void resizeTable(JTable jTable) {

        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
//        
//        int jtableWidth = jTable.getWidth();
        int jtableWidth = jTable.getWidth();
        
//       jTable.setPreferredScrollableViewportSize(new Dimension(1900, 500));
       

        int sum = 0;
        for (CollumnsComponent cc : formatter.getCollumnsComponents()) {
            sum += cc.getCollumnWidth();
        }

//        System.out.println(sum);

        for (int i = 0; i < formatter.getCollumnsComponents().size(); i++) {
            int j = ((CollumnsComponent) formatter.getCollumnsComponents().get(i)).getCollumnWidth();
            TableColumnModel columnModel = jTable.getColumnModel();
            JLabel jLabel = new JLabel(String.format("%-" + j + "d", 9));
            int width = jLabel.getPreferredSize().width;
            
            if (j == 0) {
                columnModel.getColumn(i).setPreferredWidth(jtableWidth);
                continue;
            }


            columnModel.getColumn(i).setPreferredWidth(width);
            jtableWidth = jtableWidth - width;
            try {
                if (jtableWidth > 0) {
                    columnModel.getColumn(i + 1).setPreferredWidth(jtableWidth);
                }
            } catch (Exception e) {
            }
        }
    }

    public void setCellChangeListener(CellChangeListener cellChangeListener) {
        this.cellChangeListener = cellChangeListener;
    }

    private void fireCellChange(Object target, Object value, int row, int column) {
        if (cellChangeListener != null) {
            cellChangeListener.change(target, value, row, column);
        }
    }

    public interface CellChangeListener<T> {

        void change(T target, Object value, int row, int column);
    }

}
