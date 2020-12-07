package ui;

import data.TransactionDao;
import model.Transaction;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TransactionsTable extends AbstractTableModel {
    private final TransactionDao transactionDao;
    private final List<Transaction> transactions;

    TransactionsTable(TransactionDao transactionDao){
        this.transactionDao = transactionDao;
        this.transactions = new ArrayList<>(transactionDao.findAll());
    }

    @Override
    public int getRowCount() {
        return transactions.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    String editAmountFormat(double amount){
        if (amount < 0.0){
            return Double.toString(amount);
        }
        return " " + amount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var transaction = transactions.get(rowIndex);
        switch (columnIndex){
            case 0:
                return transaction.getName();
            case 1:
                return editAmountFormat(transaction.getAmount());
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

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Name";
            case 1:
                return "Amount";
            case 2:
                return "Category";
            case 3:
                return "Created";
            case 4:
                return "Note";
            default:
                throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }
    }

    public void deleteTransaction(int rowIndex) {
        transactions.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addTransaction(Transaction transaction) {
        transactionDao.create(transaction);
        int newRowIndex = transactions.size();
        transactions.add(transaction);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }
}
