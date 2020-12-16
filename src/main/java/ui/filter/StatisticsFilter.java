package ui.filter;

import ui.StatisticsTable;
import ui.TablesManager;

import javax.swing.table.TableRowSorter;

public class StatisticsFilter extends AbstractFilterAction {
    private final TableRowSorter<StatisticsTable> sorter;
    private final FilterPanel filterPanel;

    public StatisticsFilter(TablesManager tablesManager, FilterPanel filterPanel){
        super(tablesManager, filterPanel);
        sorter = new TableRowSorter<>(tablesManager.getStatTableModel());
        tablesManager.getStatJTable().setRowSorter(sorter);
        this.filterPanel = filterPanel;
    }

    public void filterTable(){
        filterPanel.checkSpinnersValues();
        sorter.setRowFilter(null);

    }

}
