package ui;

import model.Transaction;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TransactionsTable extends AbstractTableModel {

    private List<Transaction> transactions;

    @Override
    public int getRowCount() {
        return transactions.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var transaction = transactions.get(rowIndex);
        switch (columnIndex){
            case 0:
                return transaction.getName();
            case 1:
                return transaction.getAmount();
            case 2:
                return transaction.getCategory();
            case 3:
                return transaction.getDate();
            case 4:
                return transaction.getNote();
            default:
                throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }
    }
}
