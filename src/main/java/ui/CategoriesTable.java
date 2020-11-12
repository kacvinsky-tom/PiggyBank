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
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var category = categories.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return category.getName();
            case 1:
                return category.getSum();
            default:
                throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }
    }
}
