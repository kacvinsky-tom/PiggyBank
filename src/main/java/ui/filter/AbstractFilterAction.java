package ui.filter;

import ui.TablesManager;
import ui.TransactionsTable;
import ui.filter.FilterPanel;

import javax.swing.*;

public abstract class AbstractFilterAction {
    private final TablesManager tablesManager;
    private final FilterPanel filterPanel;

    public AbstractFilterAction(TablesManager tablesManager, FilterPanel filterPanel){
        this.tablesManager = tablesManager;
        this.filterPanel = filterPanel;
    }

    RowFilter<TransactionsTable, Integer> categoriesFilter = new RowFilter<>() {
        public boolean include(Entry<? extends TransactionsTable, ? extends Integer> entry) {
            try {
                return tablesManager.getTranJTable().getValueAt(entry.getIdentifier(), 3).toString().equals(filterPanel.getCategoriesComboBox().getSelectedItem().toString());
            } catch (ArrayIndexOutOfBoundsException ex) {
                return false;
            }
        }

    };

    RowFilter<TransactionsTable, Integer> incomeFilter = new RowFilter<>() {
        public boolean include(Entry<? extends TransactionsTable, ? extends Integer> entry) {
            try {
                return tablesManager.getTranJTable().getValueAt(entry.getIdentifier(), 2).toString().equals("INCOME");
            } catch (ArrayIndexOutOfBoundsException ex) {
                return false;
            }
        }
    };

    RowFilter<TransactionsTable, Integer> spendingFilter = new RowFilter<>() {
        public boolean include(Entry<? extends TransactionsTable, ? extends Integer> entry) {
            try {
                return tablesManager.getTranJTable().getValueAt(entry.getIdentifier(), 2).toString().equals("SPENDING");
            } catch (ArrayIndexOutOfBoundsException ex) {
                return false;
            }
        }
    };
}
