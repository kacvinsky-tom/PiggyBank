package ui;

import data.TransactionDao;
import model.Transaction;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionsTable extends AbstractTableModel {
    private final TransactionDao transactionDao;
    private final List<Transaction> transactions;
    private final CategoriesTable categoriesTable;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy"); //NEVYMAZAVAT

    TransactionsTable(TransactionDao transactionDao, CategoriesTable categoriesTable){
        this.transactionDao = transactionDao;
        this.transactions = new ArrayList<>(transactionDao.findAll());
        this.categoriesTable = categoriesTable;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public int getRowCount() {
        return transactions.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    public void deleteRow(int rowIndex) {
        transactionDao.delete(transactions.get(rowIndex));
        transactions.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void changeCategory(int rowIndex){
        var category = categoriesTable.getCategories().get(rowIndex);
        if (!category.getName().equals("Others")){
            for (var transaction : transactions) {
                if(transaction.getCategory().getName().equals(category.getName())){
                    transaction.setCategory(categoriesTable.getOthers());
//                    transactionDao.update(transaction); UNCOMMENT WHEN TRANSACTIONS DATABASE WILL HAVE NAME OF CATEGORY INSTEAD OF ID
                }
            }
        }
    }

    private Transaction getTransaction(int rowIndex){
        return transactions.get(rowIndex);
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
                //return dateFormat.format(transaction.getDate());  funguje ale ked klikned na update settings,
                // tak sa nezobrazia ziadne transakcie...ktovie preco
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
