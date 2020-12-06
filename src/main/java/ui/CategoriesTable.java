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
        this.categories.add(new Category("Others", Color.GRAY));
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void addRow(Category category) {
        int newRowIndex = categories.size();
        categoryDao.create(category);
        categories.add(category);
        fireTableRowsInserted(newRowIndex, newRowIndex);
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
