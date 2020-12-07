package ui;

import data.CategoryDao;
import model.Category;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriesTable extends AbstractTableModel {

    private final List <Category> categories;
    private final CategoryDao categoryDao;

    public CategoriesTable(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
        this.categories = new ArrayList<>(categoryDao.findAll());
        addDefaultCategory();
    }

    private void addDefaultCategory(){
        List<Category> categories = categoryDao.findAll();
        for (Category category : categories){
            if (category.getName().equals("Others")){
                return;
            }
        }
        Category others = new Category("Others", Color.GRAY);
        this.categories.add(others);
        categoryDao.create(others);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public int addRow(Category category) {
        int newRowIndex = categories.size();
        for (Category c : categories){
            if (c.getName().equals(category.getName())){
                return 1;
            }
            if (category.getColor().equals(c.getColor())){
                return 2;
            }
        }
        categoryDao.create(category);
        categories.add(category);
        fireTableRowsInserted(newRowIndex, newRowIndex);
        return 0;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex){
            case 0:
                return String.class;
            case 1:
                return Color.class;
        }
        throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
    }

    @Override
    public int getRowCount() {
        return categories.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var category = categories.get(rowIndex);
        switch (columnIndex){
            case 0:
                return category.getName();
            case 1:
                return category.getColor();
        }
        throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex){
            case 0:
                return "Name";
            case 1:
                return "Color";
        }
        throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
    }
}
