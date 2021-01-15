package ui;

import data.CategoryTransactionDao;
import data.TransactionDao;
import enums.TransactionType;
import model.Category;
import model.Transaction;
import ui.filter.TransactionsFilter;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TransactionsTable extends AbstractEntityTableModel<Transaction> {

    private static final List<Column<?, Transaction>> COLUMNS = List.of(
            Column.readOnly("Name", String.class, Transaction::getName),
            Column.readOnly("Amount", BigDecimal.class, Transaction::getAmount),
            Column.readOnly("Type", TransactionType.class, Transaction::getType),
            Column.readOnly("Category", String.class, Transaction::getCategoriesNames),
            Column.readOnly("Category color", Color.class, Transaction::getCategoryColor),
            Column.readOnly("Created", Date.class, Transaction::getDate),
            Column.readOnly("Note", String.class, Transaction::getNote)
    );

    private final TransactionDao transactionDao;
    private final CategoryTransactionDao categoryTransactionDao;
    private List<Transaction> transactions;
    private final CategoriesTable categoriesTable;
    private TransactionsFilter filter;

    TransactionsTable(TransactionDao transactionDao, CategoryTransactionDao categoryTransactionDao, CategoriesTable categoriesTable) {
        super(COLUMNS);
        this.transactionDao = transactionDao;
        this.categoryTransactionDao = categoryTransactionDao;
        this.categoriesTable = categoriesTable;
        loadTransactions();
        update();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public int getRowCount() {
        return transactions.size();
    }

    public void deleteRow(int rowIndex) {
        new RowDeleter(rowIndex).execute();
    }

    public void setFilter(TransactionsFilter filter) {
        this.filter = filter;
    }

    public void loadTransactions() {
        this.transactions = categoryTransactionDao.findAll();
    }

    public void update() {
        fireTableDataChanged();
    }

    public void filterTransactions() {
        loadTransactions();
        transactions.removeIf(transaction -> !filter.checkTransaction(transaction));
        update();
    }

    public void removeCategoryFromTransactions(int rowIndex) {
        var category = categoriesTable.getCategories().get(rowIndex);
        for (Transaction transaction : transactions){
            if (transaction.getCategories().contains(category)){
                transaction.getCategories().remove(category);
                categoryTransactionDao.deleteOnlyOneRecord(transaction, category);
                if (transaction.getCategories().isEmpty()){
                    categoryTransactionDao.create(transaction, new Category("Others"));
                    transaction.getCategories().add(new Category("Others"));
                }
            }
        }
        // ToDO Maybe add loadTransactions()
        update();
    }

    @Override
    protected Transaction getEntity(int rowIndex) {
        return transactions.get(rowIndex);
    }

    @Override
    protected void updateEntity(Transaction transaction) {
        transactionDao.update(transaction);
        updateCategoriesInTransaction(transaction.getCategories(), transaction);
        update();
    }

    public void addTransaction(Transaction transaction) {
        transactionDao.create(transaction);
        transactions.add(transaction);
        updateCategoriesInTransaction(transaction.getCategories(), transaction);
        update();
    }

    public void updateCategoriesInTransaction(List<Category> categories, Transaction transaction) {
        boolean exist = false;
        for (Category c : categories){
            for (Category cc : transaction.getCategories()){
                if (c.getName().equals(cc.getName())){
                    transaction.getCategories().remove(cc);
                    exist = true;
                    break;
                }
            }
            if (!exist){
                categoryTransactionDao.create(transaction, c);
            }
            exist = false;
        }

        for (Category c : transaction.getCategories()){
            categoryTransactionDao.deleteOnlyOneRecord(transaction, c);
        }

        loadTransactions();
        update();
    }

    private class RowDeleter extends SwingWorker<Boolean, Integer> {
        private final int rowIndex;

        public RowDeleter(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        @Override
        protected Boolean doInBackground() {
            transactionDao.delete(transactions.get(rowIndex));
            loadTransactions();
            return true;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            update();
        }
    }

    private class RowAdder extends SwingWorker<Boolean, Integer> {
        private final int rowIndex;

        public RowAdder(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        @Override
        protected Boolean doInBackground() {
            transactionDao.delete(transactions.get(rowIndex));
            loadTransactions();
            return true;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            update();
        }
    }
}
