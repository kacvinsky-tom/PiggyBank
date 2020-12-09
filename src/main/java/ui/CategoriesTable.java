package ui;

import data.CategoryDao;
import model.Category;
import model.Transaction;
import model.TransactionType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriesTable extends AbstractEntityTableModel<Category> {

    private static final List<Column<?, Category>> COLUMNS = List.of(
            Column.readOnly("Name", String.class, Category::getName),
            Column.readOnly("Color", Color.class, Category::getColor)
    );

    private final StatisticsTable statisticsTable;
    private final List <Category> categories;
    private final CategoryDao categoryDao;
    private Category others;
    private double incomeOverall = 0;
    private double expensesOverall = 0;

    public CategoriesTable(CategoryDao categoryDao, StatisticsTable statisticsTable) {
        super(COLUMNS);
        this.categoryDao = categoryDao;
        this.statisticsTable = statisticsTable;
        this.categories = new ArrayList<>(categoryDao.findAll());
        addDefaultCategory();
        updateSpending();
    }

    private void updateSpending(){
        incomeOverall = 0;
        expensesOverall = 0;
        for (Category category : categories){
            incomeOverall += category.getIncome();
            expensesOverall += category.getExpenses();
        }
    }

    private void updateCategories(){
        updateSpending();
        for (Category category : categories){
            updateCategoryPercentages(category);
            categoryDao.update(category);
        }
        statisticsTable.updateCategories();
    }

    private void updateCategoryPercentages(Category category){
        category.setPercentageInc(category.getIncome() * 100 / incomeOverall);
        category.setPercentageSpend(category.getExpenses() * 100 / expensesOverall);
    }

    private void addDefaultCategory(){
        List<Category> categories = categoryDao.findAll();
        for (Category category : categories){
            if (category.getName().equals("Others")){
                others = category;
                return;
            }
        }
        others = new Category("Others", Color.GRAY);
        this.categories.add(others);
        categoryDao.create(others);
    }

    @Override
    public int getRowCount() {
        return categories.size();
    }

    @Override
    protected Category getEntity(int rowIndex) {
        return categories.get(rowIndex);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Category getOthers(){
        return others;
    }

    public void addRow(Category category) {
        int newRowIndex = categories.size();
        categoryDao.create(category);
        categories.add(category);
        statisticsTable.updateCategories();
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    @Override
    protected void updateEntity(Category category) {
        categoryDao.update(category);
        updateCategories();
        fireTableDataChanged();
        statisticsTable.updateCategories();
    }

    public void updateCategory(Category category, Transaction transaction, boolean add){
        int sign;
        if (add){
            sign = 1;
        } else {
            sign = -1;
        }
        category.setTransactionsNumber(category.getTransactionsNumber() + sign);
        if (transaction.getType() == TransactionType.INCOME){
            category.setIncome(category.getIncome() + transaction.getAmount() * sign);
        } else {
            category.setExpenses(category.getExpenses() + transaction.getAmount() * sign);
        }
        category.setSum(category.getIncome() - category.getExpenses());
        updateCategories();
    }

    public void deleteRow(int rowIndex) {
        if(!categories.get(rowIndex).getName().equals("Others")){
            categoryDao.delete(categories.get(rowIndex));
            categories.remove(rowIndex);
            statisticsTable.updateCategories();
            fireTableRowsDeleted(rowIndex, rowIndex);
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "You can't delete default category 'Others'!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
