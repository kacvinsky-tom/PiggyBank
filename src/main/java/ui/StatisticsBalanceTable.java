package ui;

import javax.swing.table.AbstractTableModel;

public class StatisticsBalanceTable extends AbstractTableModel {

    private double balance = 1000;  // assigned only for testing purposes

    public StatisticsBalanceTable(){
        updateBalance();
    }

    public void updateBalance(){
        // TODO IMPLEMENT BALANCE UPDATING
    }

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
                return balance;
            default:
                return null;    // add some exception
        }
    }
}