package ui;

import javax.swing.table.AbstractTableModel;

public class StatisticsBalanceTable extends AbstractTableModel {

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0:
                return "Balance";
            case 1:
                return 1000;
            default:
                return 9;
        }
    }
}