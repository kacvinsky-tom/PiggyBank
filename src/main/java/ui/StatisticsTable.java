package ui;

import data.StatisticDao;
import model.Category;
import model.CategoryStatistic;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StatisticsTable extends AbstractEntityTableModel<CategoryStatistic> {

    private static final List<Column<?, CategoryStatistic>> COLUMNS = List.of(
            Column.readOnly("Category", String.class, CategoryStatistic::getCategoryName),
            Column.readOnly("Transactions", Integer.class, CategoryStatistic::getTransactionsNumber),
            Column.readOnly("Income", BigDecimal.class, CategoryStatistic::getIncome),
            Column.readOnly("Expenses", BigDecimal.class, CategoryStatistic::getExpenses),
            Column.readOnly("% of total income", BigDecimal.class, CategoryStatistic::getPercentageInc),
            Column.readOnly("% of total spending", BigDecimal.class, CategoryStatistic::getPercentageSpend),
            Column.readOnly("Sum", BigDecimal.class, CategoryStatistic::getSum)
    );

    private List<CategoryStatistic> statistics;
    private final StatisticDao statisticDao;

    protected StatisticsTable(StatisticDao statisticDao) {
        super(COLUMNS);
        this.statisticDao = statisticDao;
        statistics = new ArrayList<>(statisticDao.setAll());
    }

    public void update(){
        statistics = statisticDao.setAll();
        fireTableDataChanged();
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
