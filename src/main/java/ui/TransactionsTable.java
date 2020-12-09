package ui;

import data.TransactionDao;
import model.Category;
import model.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionsTable extends AbstractEntityTableModel<Transaction> {

    private static final List<Column<?, Transaction>> COLUMNS = List.of(
            Column.readOnly("Name", String.class, Transaction::getName),
            Column.readOnly("Amount", Double.class, Transaction::getAmount),
            Column.readOnly("Category", Category.class, Transaction::getCategory),
            Column.readOnly("Created", Date.class, Transaction::getDate),
            Column.readOnly("Note", String.class, Transaction::getNote)
    );

    private final TransactionDao transactionDao;
    private final List<Transaction> transactions;
    private final CategoriesTable categoriesTable;

    TransactionsTable(TransactionDao transactionDao, CategoriesTable categoriesTable){
        super(COLUMNS);
        this.transactionDao = transactionDao;
        this.categoriesTable = categoriesTable;
        this.transactions = new ArrayList<>(transactionDao.findAll());
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Transaction getTransaction(int index){
        return transactions.get(index);
    }

    @Override
    public int getRowCount() {
        return transactions.size();
    }

    public void deleteRow(int rowIndex) {
        transactionDao.delete(transactions.get(rowIndex));
        transactions.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void changeCategoryToDefault(int rowIndex){
        var category = categoriesTable.getCategories().get(rowIndex);
        if (!category.getName().equals("Others")){
            for (var transaction : transactions) {
                if(transaction.getCategory().getName().equals(category.getName())){
                    transaction.setCategory(categoriesTable.getOthers());
                    transactionDao.update(transaction);
                }
            }
        }
    }

    @Override
    protected Transaction getEntity(int rowIndex) {
        return transactions.get(rowIndex);
    }

    @Override
    protected void updateEntity(Transaction transaction) {
        transactionDao.update(transaction);
    }

    public void addTransaction(Transaction transaction) {
        transactionDao.create(transaction);
        int newRowIndex = transactions.size();
        transactions.add(transaction);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }
}
