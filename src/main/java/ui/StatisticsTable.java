package ui;

import model.Category;
import model.CategoryStatistic;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StatisticsTable extends AbstractEntityTableModel<CategoryStatistic> {

    private static final List<Column<?, CategoryStatistic>> COLUMNS = List.of(
            Column.readOnly("Category", String.class, CategoryStatistic::getCategoryName),
            Column.readOnly("Transactions", Integer.class, CategoryStatistic::getTransactionsNumber),
            Column.readOnly("Income", Double.class, CategoryStatistic::getIncome),
            Column.readOnly("Expenses", Double.class, CategoryStatistic::getExpenses),
            Column.readOnly("% of total income", Double.class, CategoryStatistic::getPercentageInc),
            Column.readOnly("% of total spending", Double.class, CategoryStatistic::getPercentageSpend),
            Column.readOnly("Sum", Double.class, CategoryStatistic::getSum)
    );

    private final List<CategoryStatistic> statistics;

    StatisticsTable(){
        super(COLUMNS);
        Category c11 = new Category("Food", Color.BLUE);
        CategoryStatistic c1 = new CategoryStatistic(c11);
        Category c22 = new Category("Medicine", Color.BLACK);
        CategoryStatistic c2 = new CategoryStatistic(c22);
        statistics = new ArrayList<>();
        statistics.add(c1);
        statistics.add(c2);
    }

    @Override
    public int getRowCount() {
        return statistics.size();
    }

    @Override
    protected CategoryStatistic getEntity(int rowIndex) {
        return statistics.get(rowIndex);
    }
}
