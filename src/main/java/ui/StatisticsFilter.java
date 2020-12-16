package ui;

import javax.swing.table.TableRowSorter;

public class StatisticsFilter {
    private final TableRowSorter<StatisticsTable> sorter;
    private final TablesManager tablesManager;
    private final FilterPanel filterPanel;

    public StatisticsFilter(TablesManager tablesManager, FilterPanel filterPanel){
        sorter = new TableRowSorter<>(tablesManager.getStatTableModel());
        this.tablesManager = tablesManager;
        this.tablesManager.getStatJTable().setRowSorter(sorter);
        this.filterPanel = filterPanel;
    }

    public void filterTable(){

    }

}
