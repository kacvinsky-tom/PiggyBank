package ui;

import model.Transaction;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TransactionsTable extends AbstractTableModel {

    private List<Transaction> transactions;

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}
