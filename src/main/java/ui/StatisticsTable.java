package ui;

import data.CategoryDao;
import data.TransactionDao;
import model.Category;

import java.util.ArrayList;
import java.util.List;

public class StatisticsTable extends AbstractEntityTableModel<Category> {


    private static final List<Column<?, Category>> COLUMNS = List.of(
            Column.readOnly("Category", String.class, Category::getName),
            Column.readOnly("Sum", Double.class, Category::getSum),
            Column.readOnly("Income", Double.class, Category::getIncome),
            Column.readOnly("Expenses", Double.class, Category::getExpenses),
            Column.readOnly("% of income", Double.class, Category::getPercentageInc),
            Column.readOnly("% of spending", Double.class, Category::getPercentageSpend),
            Column.readOnly("Transactions", Integer.class, Category::getTransactionsNumber)
    );

    private final List<Category> categories;

    StatisticsTable(CategoryDao categoryDao){
        super(COLUMNS);
        this.categories = new ArrayList<>(categoryDao.findAll());
    }

    @Override
    public int getRowCount() {
        return categories.size();
    }

    @Override
    protected Category getEntity(int rowIndex) {
        return categories.get(rowIndex);
    }
}
