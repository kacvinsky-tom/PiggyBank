package ui;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;

public class TransactionsFilter {
    private final TableRowSorter<TransactionsTable> sorter;
    private final TablesManager tablesManager;
    private final FilterPanel filterPanel;

    public TransactionsFilter(TablesManager tablesManager, FilterPanel filterPanel){
        sorter = new TableRowSorter<>(tablesManager.getTranTableModel());
        this.tablesManager = tablesManager;
        this.tablesManager.getTranJTable().setRowSorter(sorter);
        this.filterPanel = filterPanel;
    }

    public void filterTable() {
        filterPanel.checkSpinnersValues();

        List<RowFilter<TransactionsTable, Integer>> filters = new ArrayList<>(5);
        filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.AFTER, filterPanel.getDateFrom(), 4));
        filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.BEFORE, filterPanel.getDateTo(), 4));

        if (filterPanel.getCategoriesComboBox().getSelectedIndex() > 0) {
            filters.add(RowFilter.regexFilter(filterPanel.getCategoriesComboBox().getSelectedItem().toString(), 3));
        }
        if (filterPanel.getCheckBoxIncomes().isSelected() && !filterPanel.getCheckBoxSpending().isSelected()) {
            filters.add(incomeFilter);
        }
        if (!filterPanel.getCheckBoxIncomes().isSelected() && filterPanel.getCheckBoxSpending().isSelected()) {
            filters.add(spendingFilter);
        }

        RowFilter<TransactionsTable, Integer> rf = RowFilter.andFilter(filters);
        sorter.setRowFilter(rf);
    }

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
