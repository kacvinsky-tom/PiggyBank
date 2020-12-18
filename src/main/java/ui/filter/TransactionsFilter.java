package ui.filter;

import model.Transaction;
import ui.TablesManager;

public class TransactionsFilter {
    private final TablesManager tablesManager;
    private final FilterPanel filterPanel;

    public TransactionsFilter(TablesManager tablesManager, FilterPanel filterPanel) {
        this.tablesManager = tablesManager;
        this.filterPanel = filterPanel;
    }

    public void filterTable() {
        tablesManager.getTranTableModel().loadTransactions();
        for (Transaction t : tablesManager.getTranTableModel().getTransactions()){
            if (checkDate(t) && checkType(t) && ){
                continue;
            }

        }
    }

    private boolean checkType(Transaction transaction){
        if (filterPanel.getCheckBoxIncomes().isSelected())
    }

    private boolean checkDate(Transaction transaction){
        return transaction.getDate().after(filterPanel.getDateFrom()) && transaction.getDate().before(filterPanel.getDateTo());
    }
}
