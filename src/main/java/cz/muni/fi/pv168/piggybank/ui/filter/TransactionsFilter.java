package cz.muni.fi.pv168.piggybank.ui.filter;

import cz.muni.fi.pv168.piggybank.enums.TransactionType;
import cz.muni.fi.pv168.piggybank.model.Transaction;
import cz.muni.fi.pv168.piggybank.ui.TablesManager;

public class TransactionsFilter {
    private final TablesManager tablesManager;
    private final FilterPanel filterPanel;

    public TransactionsFilter(TablesManager tablesManager, FilterPanel filterPanel) {
        this.tablesManager = tablesManager;
        this.filterPanel = filterPanel;
        this.tablesManager.getTranTableModel().setFilter(this);
    }

    public boolean checkTransaction(Transaction t){
        return checkDate(t) && checkType(t) && checkType(t) && checkCategory(t);
    }

    public void filterTable() {
        tablesManager.getTranTableModel().filterTransactions();
    }

    private boolean checkCategory(Transaction transaction){
        try {
            if (filterPanel.getCategoriesComboBox().getSelectedItem().toString().equals("All")) {
                return true;
            }
        } catch (NullPointerException e){
            return true;
        }
        return transaction.getCategory().getName().equals(filterPanel.getCategoriesComboBox().getSelectedItem().toString());
    }

    private boolean checkType(Transaction transaction){
        if (!filterPanel.getCheckBoxIncomes().isSelected() && !filterPanel.getCheckBoxSpending().isSelected()){
            return true;
        } else if (filterPanel.getCheckBoxIncomes().isSelected() && transaction.getType() == TransactionType.INCOME){
            return true;
        } else if (filterPanel.getCheckBoxSpending().isSelected() && transaction.getType() == TransactionType.SPENDING){
            return true;
        }
        return false;
    }

    private boolean checkDate(Transaction transaction){
        return transaction.getDate().after(filterPanel.getDateFrom()) && transaction.getDate().before(filterPanel.getDateTo());
    }
}
