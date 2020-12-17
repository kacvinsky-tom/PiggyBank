package ui;

import data.TransactionDao;
import model.Category;
import model.Transaction;
import enums.TransactionType;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionsTable extends AbstractEntityTableModel<Transaction> {

    private static final List<Column<?, Transaction>> COLUMNS = List.of(
            Column.readOnly("Name", String.class, Transaction::getName),
            Column.readOnly("Amount", BigDecimal.class, Transaction::getAmount),
            Column.readOnly("Type", TransactionType.class, Transaction::getType),
            Column.readOnly("Category", Category.class, Transaction::getCategory),
            Column.readOnly("Category color", Color.class, Transaction::getCategoryColor),
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

    @Override
    public int getRowCount() {
        return transactions.size();
    }

    public void deleteRow(int rowIndex) {
        transactionDao.delete(transactions.get(rowIndex));
        transactions.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void update(){
        fireTableDataChanged();
    }

    public void changeCategoryToDefault(int rowIndex){
        var category = categoriesTable.getCategories().get(rowIndex);
        if (!category.getName().equals("Others")){
            transactions.stream()
                    .filter(t ->  t.getCategory().getName().equals(category.getName()))
                    .forEach(t -> {t.setCategory(categoriesTable.getOthers());
                    fireTableRowsUpdated(rowIndex, rowIndex);
                    transactionDao.update(t);});
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
