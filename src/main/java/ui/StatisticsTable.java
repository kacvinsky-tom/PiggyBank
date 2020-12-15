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

    private final List<CategoryStatistic> statistics = new ArrayList<>();

    protected StatisticsTable() {
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
