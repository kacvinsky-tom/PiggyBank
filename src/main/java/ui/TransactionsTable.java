package ui;

import model.Category;
import model.Transaction;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionsTable extends AbstractTableModel {

    private List<Transaction> transactions = new ArrayList<>() {
    };

    //neskor vytvorit konstruktor s transactions ktore budu nacitane z jsona

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

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        var transaction = transactions.get(rowIndex);
        switch (columnIndex) {
            case 0:
                transaction.setName((String) value);
                break;
            case 1:
                transaction.setAmount((float) value);
                break;
            case 2:
                transaction.setCategory((Category) value);
                break;
            case 3:
                transaction.setDate((LocalDate) value);
                break;
            case 4:
                transaction.setNote((String) value);
                break;
            default:
                throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 4:
                return String.class;
            case 1:
                return float.class;
            case 2:
                return Category.class;
            case 3:
                return LocalDate.class;
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
                return "Date of create";
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
        int newRowIndex = transactions.size();
        transactions.add(transaction);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }
}
