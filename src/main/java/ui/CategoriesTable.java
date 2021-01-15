package ui;

import data.CategoryDao;
import model.Category;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriesTable extends AbstractEntityTableModel<Category> {

    private static final List<Column<?, Category>> COLUMNS = List.of(
            Column.readOnly("Name", String.class, Category::getName),
            Column.readOnly("Color", Color.class, Category::getColor)
    );

    private final List<Category> categories;
    private final CategoryDao categoryDao;
    private static Category others;

    public CategoriesTable(CategoryDao categoryDao) {
        super(COLUMNS);
        this.categoryDao = categoryDao;
        this.categories = new ArrayList<>(categoryDao.findAll());
        addDefaultCategory();
    }

    private void updateCategories() {
        new CategoriesUpdater().execute();
    }

    private void addDefaultCategory() {
        for (Category category : categories) {
            if (category.getName().equals("Others")) {
                others = category;
                return;
            }
        }
        others = new Category("Others", Color.GRAY);
        new RowAdder(others).execute();
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

    public List<String> getCategoriesNames(){
        List<String> cats = new ArrayList<>();
        for (Category c : categories){
            cats.add(c.getName());
        }
        return cats;
    }

    public Category getOthers() {
        return others;
    }

    public void addRow(Category category) {
        new RowAdder(category).execute();
    }

    @Override
    protected void updateEntity(Category category) {
        new CategoryUpdater(category).execute();
        updateCategories();
    }

    public boolean deleteRow(int rowIndex) {
        if (!categories.get(rowIndex).getName().equals("Others")) {
            new RowDeleter(categories.get(rowIndex)).execute();
            return true;
        }
        return false;

    }

    private class CategoryUpdater extends SwingWorker<Boolean, Integer> {
        private final Category category;

        public CategoryUpdater(Category category){
            this.category = category;
        }

        @Override
        protected Boolean doInBackground() {
            categoryDao.update(category);
            return true;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class CategoriesUpdater extends SwingWorker<Boolean, Integer> {

        @Override
        protected Boolean doInBackground() {
            for (Category category : categories) {
                categoryDao.update(category);
            }
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

    private class RowAdder extends SwingWorker<Boolean, Integer> {
        private final Category category;

        public RowAdder(Category category){
            this.category = category;
        }

        @Override
        protected Boolean doInBackground() {
            categoryDao.create(category);
            return true;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            categories.add(category);
            fireTableDataChanged();
        }
    }

    private class RowDeleter extends SwingWorker<Boolean, Integer> {
        private final Category category;

        public RowDeleter(Category category){
            this.category = category;
        }

        @Override
        protected Boolean doInBackground() {
            categoryDao.delete(category);
            return true;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            categories.remove(category);
            fireTableDataChanged();
        }
    }
}
