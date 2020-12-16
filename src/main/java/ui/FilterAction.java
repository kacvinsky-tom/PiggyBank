package ui;

import model.TableType;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;

public class FilterAction {
    private final TablesManager tablesManager;
    private final MessageDialog messageDialog;
    private final FilterPanel filterPanel;
    private final StatisticsFilter statisticsFilter;
    private final TransactionsFilter transactionsFilter;

    private int selectedTabIndex = 0;

    public FilterAction(TablesManager tablesManager, MessageDialog messageDialog) {
        this.tablesManager = tablesManager;
        this.messageDialog = messageDialog;
        this.filterPanel = new FilterPanel(tablesManager);
        this.statisticsFilter = new StatisticsFilter(tablesManager, filterPanel);
        this.transactionsFilter = new TransactionsFilter(tablesManager, filterPanel);
        setActionListeners();
    }

    public JPanel getFilterPanel() {
        return filterPanel;
    }

    public void updateSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }

    private void setActionListeners(){
        filterPanel.getCheckBoxIncomes().addActionListener(this::filterActionPerformed);
        filterPanel.getCheckBoxSpending().addActionListener(this::filterActionPerformed);
        filterPanel.getComboBox().addActionListener(this::filterActionPerformed);
        filterPanel.getSpinnerFrom().addChangeListener(this::dateChangePerformed);
        filterPanel.getSpinnerTo().addChangeListener(this::dateChangePerformed);
    }

    private void determineFilter(){
        if (selectedTabIndex == TableType.STATISTICS_TABLE.ordinal()){
            statisticsFilter.filterTable();
        } else if (selectedTabIndex == TableType.TRANSACTIONS_TABLE.ordinal()){
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
