package ui;

import data.CategoryDao;
import model.CategoryStatistic;

import java.util.List;

public class StatisticsTable extends AbstractEntityTableModel<CategoryStatistic> {

    private static final List<Column<?, CategoryStatistic>> COLUMNS = List.of(
            Column.readOnly("Category", String.class, CategoryStatistic::getCategoryName),
            Column.readOnly("Sum", Double.class, CategoryStatistic::getSum),
            Column.readOnly("Income", Double.class, CategoryStatistic::getIncome),
            Column.readOnly("Expenses", Double.class, CategoryStatistic::getExpenses),
            Column.readOnly("% of income", Double.class, CategoryStatistic::getPercentageInc),
            Column.readOnly("% of spending", Double.class, CategoryStatistic::getPercentageSpend),
            Column.readOnly("Transactions", Integer.class, CategoryStatistic::getTransactionsNumber)
    );

    private List<CategoryStatistic> statistics;

    StatisticsTable(CategoryDao categoryDao){
        super(COLUMNS);
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
