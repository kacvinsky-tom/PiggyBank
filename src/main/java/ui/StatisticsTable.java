package ui;

import javax.swing.table.AbstractTableModel;

public class StatisticsTable extends AbstractTableModel {

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Name";
            case 1:
                return "Amount";
            case 2:
                return "Amount in percent";
            case 3:
                return "Number of transactions";
            default:
                throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }
    }
}
