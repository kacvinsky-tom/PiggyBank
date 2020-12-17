package ui.filter;

import enums.TableType;
import ui.*;
import ui.TablesManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;

public class FilterAction {
    private final FilterPanel filterPanel;
    private final StatisticsFilter statisticsFilter;
    private final TransactionsFilter transactionsFilter;

    private int selectedTabIndex = 0;

    public FilterAction(TablesManager tablesManager, MessageDialog messageDialog) {
        this.filterPanel = new FilterPanel(tablesManager, messageDialog);
        this.statisticsFilter = new StatisticsFilter(tablesManager, filterPanel);
        this.transactionsFilter = new TransactionsFilter(tablesManager, filterPanel);
        setActionListeners();
    }

    public JPanel getFilterPanel() {
        return filterPanel;
    }

    public void updateSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
        filterPanel.updateCategoriesComboCox();
        filterPanel.setComponentsEnabled(selectedTabIndex);
    }

    private void setActionListeners(){
        filterPanel.getCheckBoxIncomes().addActionListener(this::filterActionPerformed);
        filterPanel.getCheckBoxSpending().addActionListener(this::filterActionPerformed);
        filterPanel.getCategoriesComboBox().addActionListener(this::filterActionPerformed);
        filterPanel.getSpinnerFrom().addChangeListener(this::dateChangePerformed);
        filterPanel.getSpinnerTo().addChangeListener(this::dateChangePerformed);
    }

    private void determineFilter(){
        if (selectedTabIndex == TableType.STATISTICS.ordinal()){
            statisticsFilter.filterTable();
        } else if (selectedTabIndex == TableType.TRANSACTIONS.ordinal()){
            transactionsFilter.filterTable();
        }
    }

    private void dateChangePerformed(ChangeEvent changeEvent) {
        determineFilter();
    }

    private void filterActionPerformed(ActionEvent actionEvent) {
        determineFilter();
    }
}
