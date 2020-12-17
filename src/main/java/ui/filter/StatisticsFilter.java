package ui.filter;

import ui.TablesManager;

public class StatisticsFilter {
    private final FilterPanel filterPanel;

    public StatisticsFilter(TablesManager tablesManager, FilterPanel filterPanel){
        this.filterPanel = filterPanel;

    }

    public void filterTable(){
        filterPanel.checkSpinnersValues();
        // ToDo implement statistics filter
    }

}
