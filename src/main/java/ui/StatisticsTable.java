package ui;

import data.StatisticDao;
import model.CategoryStatistic;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatisticsTable extends AbstractEntityTableModel<CategoryStatistic> {

    private static final List<Column<?, CategoryStatistic>> COLUMNS = List.of(
            Column.readOnly("Category", String.class, CategoryStatistic::getCategoryName),
            Column.readOnly("Transactions", Integer.class, CategoryStatistic::getTransactionsCounter),
            Column.readOnly("Income", BigDecimal.class, CategoryStatistic::getIncome),
            Column.readOnly("Expenses", BigDecimal.class, CategoryStatistic::getExpenses),
            Column.readOnly("% of total income", BigDecimal.class, CategoryStatistic::getPercentageInc),
            Column.readOnly("% of total spending", BigDecimal.class, CategoryStatistic::getPercentageSpend),
            Column.readOnly("Sum", BigDecimal.class, CategoryStatistic::getSum)
    );

    private List<CategoryStatistic> statistics;
    private final StatisticDao statisticDao;
    private Date dateFrom;
    private Date dateTo;

    protected StatisticsTable(StatisticDao statisticDao) {
        super(COLUMNS);
        this.statisticDao = statisticDao;
        statistics = new ArrayList<>();
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public void update(){
        new Updater().execute();
    }

    @Override
    public int getRowCount() {
        return statistics.size();
    }

    @Override
    protected CategoryStatistic getEntity(int rowIndex) {
        return statistics.get(rowIndex);
    }

    private class Updater extends SwingWorker<Boolean, Integer> {

        @Override
        protected Boolean doInBackground() {
            statistics = statisticDao.setAll(dateFrom, dateTo);
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
}
