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

    private final List <Category> categories;
    private final CategoryDao categoryDao;
    private Category others;

    public CategoriesTable(CategoryDao categoryDao) {
        super(COLUMNS);
        this.categoryDao = categoryDao;
        this.categories = new ArrayList<>(categoryDao.findAll());
        addDefaultCategory();
    }

    private void updateCategories(){
        for (Category category : categories){
            categoryDao.update(category);
        }
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
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    @Override
    protected void updateEntity(Category category) {
        categoryDao.update(category);
        updateCategories();
        fireTableDataChanged();
    }

    public void deleteRow(int rowIndex) {
        if(!categories.get(rowIndex).getName().equals("Others")){
            categoryDao.delete(categories.get(rowIndex));
            categories.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        } else {
            // replace new JFrame() with parent frame
            JOptionPane.showMessageDialog(new JFrame(), "You can't delete default category 'Others'!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
