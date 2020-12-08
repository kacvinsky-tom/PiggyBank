package ui;

import data.CategoryDao;
import model.Category;
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

    private void addDefaultCategory(){
        List<Category> categories = categoryDao.findAll();
        for (Category category : categories){
            if (category.getName().equals("Others")){
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

    public void deleteRow(int rowIndex) {
        if(!categories.get(rowIndex).getName().equals("Others")){
            categoryDao.delete(categories.get(rowIndex));
            categories.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }
}
