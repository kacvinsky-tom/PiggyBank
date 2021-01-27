package ui;

import data.TransactionDao;
import model.Category;
import model.Transaction;
import enums.TransactionType;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TransactionsTable extends AbstractEntityTableModel<Transaction> {
    private static final I18N I18N = new I18N(TransactionsTable.class);

    private static final List<Column<?, Transaction>> COLUMNS = List.of(
            Column.readOnly(I18N.getString("name"), String.class, Transaction::getName),
            Column.readOnly(I18N.getString("amount"), BigDecimal.class, Transaction::getAmount),
            Column.readOnly(I18N.getString("type"), TransactionType.class, Transaction::getType),
            Column.readOnly(I18N.getString("category"), Category.class, Transaction::getCategory),
            Column.readOnly(I18N.getString("categoryColor"), Color.class, Transaction::getCategoryColor),
            Column.readOnly(I18N.getString("created"), Date.class, Transaction::getDate),
            Column.readOnly(I18N.getString("note"), String.class, Transaction::getNote)
    );

    private final TransactionDao transactionDao;
    private List<Transaction> transactions;
    private final CategoriesTable categoriesTable;
    private TransactionsFilter filter;

    TransactionsTable(TransactionDao transactionDao, CategoriesTable categoriesTable){
        super(COLUMNS);
        this.transactionDao = transactionDao;
        this.categoriesTable = categoriesTable;
        this.transactions = transactionDao.findAll();
        fireTableDataChanged();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public int getRowCount() {
        return transactions.size();
    }

    public void update(){
        fireTableDataChanged();
    }

    public void deleteRow(int rowIndex) {
        new RowDeleter(transactions.get(rowIndex)).execute();
    }

    public void setFilter(TransactionsFilter filter){
        this.filter = filter;
    }

    public void filterTransactions(){
        new RowFilter().execute();
    }

    public void changeCategoryToDefault(int rowIndex){
        var category = categoriesTable.getCategories().get(rowIndex);
        if (!category.getName().equals("Others")){
            new DefaultCategorySetter(transactions, category).execute();
        }
    }

    @Override
    protected Transaction getEntity(int rowIndex) {
        return transactions.get(rowIndex);
    }

    @Override
    protected void updateEntity(Transaction transaction) {
        new RowUpdater(transaction).execute();
    }

    public void addTransaction(Transaction transaction) {
        new RowAdder(transaction).execute();
    }

    private class RowDeleter extends SwingWorker<Boolean, Integer> {
        private final Transaction transaction;
        private List<Transaction> transactionsList;

        public RowDeleter(Transaction transaction) {
            this.transaction = transaction;
        }

        @Override
        protected Boolean doInBackground() {
            transactionDao.delete(transaction);
            transactionsList = transactionDao.findAll();
            return true;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            transactions = transactionsList;
            fireTableDataChanged();
        }
    }

    private class RowAdder extends SwingWorker<Boolean, Integer> {
        private final Transaction transaction;

        public RowAdder(Transaction transaction) {
            this.transaction = transaction;
        }

        @Override
        protected Boolean doInBackground() {
            transactionDao.delete(transaction);
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
            fireTableDataChanged();
        }
    }

    private class RowFilter extends SwingWorker<Boolean, Integer> {
        private List<Transaction> transactionsList;

        @Override
        protected Boolean doInBackground() {
            transactionsList = transactionDao.findAll();
            return true;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            transactions = transactionsList;
            transactions.removeIf(transaction -> !filter.checkTransaction(transaction));
            fireTableDataChanged();
        }
    }

    private class RowUpdater extends SwingWorker<Boolean, Integer> {
        private final Transaction transaction;

        public RowUpdater(Transaction transaction) {
            this.transaction = transaction;
        }

        @Override
        protected Boolean doInBackground() {
            transactionDao.update(transaction);
            return true;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            fireTableDataChanged();
        }
    }

    private class DefaultCategorySetter extends SwingWorker<Boolean, Integer> {
        private List<Transaction> transactionsList;
        private final Category category;

        public DefaultCategorySetter(List<Transaction> transactionsList, Category category) {
            this.transactionsList = transactionsList;
            this.category = category;
        }

        @Override
        protected Boolean doInBackground() {
            transactionsList.stream()
                    .filter(t ->  t.getCategory().getName().equals(category.getName()))
                    .forEach(t -> {t.setCategory(categoriesTable.getOthers());
                        transactionDao.update(t);});
            return true;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            transactions = transactionsList;
            fireTableDataChanged();
        }
    }

}
