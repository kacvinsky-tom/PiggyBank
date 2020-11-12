package ui;

import model.Category;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class CategoriesTable extends AbstractTableModel {

    private List <Category> categories;

    public CategoriesTable() {
        this.categories = new ArrayList<>();
        this.categories.add(new Category("Others"));
    }

    @Override
    public int getRowCount() {
        return categories.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var category = categories.get(rowIndex);
        if (columnIndex == 0) {
            return category.getName();
        }
        throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Name";
        }
        throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
    }
}
