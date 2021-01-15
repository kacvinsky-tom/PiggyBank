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
        this.transactions = categoryTransactionDao.findAll();
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

    public void update() {
        fireTableDataChanged();
    }

    public void filterTransactions() {
        new Filter().execute();
    }

    public void removeCategoryFromTransactions(int rowIndex) {
        var category = categoriesTable.getCategories().get(rowIndex);
        new CategoryRemover(category).execute();
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
        new RowAdder(transaction).execute();
        updateCategoriesInTransaction(transaction.getCategories(), transaction); }

    public void updateCategoriesInTransaction(List<Category> categories, Transaction transaction) {
        new CategoriesUpdater(categories, transaction).execute();
    }

    private class RowDeleter extends SwingWorker<Boolean, Integer> {
        private final int rowIndex;

        public RowDeleter(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        @Override
        protected Boolean doInBackground() {
            transactionDao.delete(transactions.get(rowIndex));
            transactions = categoryTransactionDao.findAll();
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
        private final Transaction transaction;

        public RowAdder(Transaction transaction) {
            this.transaction = transaction;
        }

        @Override
        protected Boolean doInBackground() {
            transactionDao.create(transaction);
            return true;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            transactions.add(transaction);
            update();
        }
    }

    private class Filter extends SwingWorker<Boolean, Integer> {

        @Override
        protected Boolean doInBackground() {
            transactions = categoryTransactionDao.findAll();
            return true;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            transactions.removeIf(transaction -> !filter.checkTransaction(transaction));
            update();
        }
    }

    private class CategoriesUpdater extends SwingWorker<Boolean, Integer> {
        private final Transaction transaction;
        private final List<Category> categories;

        public CategoriesUpdater(List<Category> categories, Transaction transaction) {
            this.categories = categories;
            this.transaction = transaction;
        }

        @Override
        protected Boolean doInBackground() {
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

            transactions = categoryTransactionDao.findAll();
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

    private class CategoryRemover extends SwingWorker<Boolean, Integer> {
        private final Category category;

        public CategoryRemover(Category category) {
            this.category = category;
        }

        @Override
        protected Boolean doInBackground() {
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
