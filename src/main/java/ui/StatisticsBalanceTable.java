package ui;

import data.StatisticDao;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;

public class StatisticsBalanceTable extends AbstractTableModel {

    private BigDecimal balance;
    private final StatisticDao statisticDao;

    public StatisticsBalanceTable(StatisticDao statisticDao){
        this.statisticDao = statisticDao;
        update();
    }

    public void update(){
        balance = statisticDao.getIncome().subtract(statisticDao.getExpense());
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0:
                return "Balance";
            case 1:
                return balance;
            default:
                return null;    // TODO ADD SOME EXCEPTION HERE
        }
    }
}