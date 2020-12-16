package ui.filter;

import ui.TablesManager;
import ui.TransactionsTable;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;

public class TransactionsFilter extends AbstractFilterAction {
    private final TableRowSorter<TransactionsTable> sorter;
    private final FilterPanel filterPanel;

    public TransactionsFilter(TablesManager tablesManager, FilterPanel filterPanel) {
        super(tablesManager, filterPanel);
        sorter = new TableRowSorter<>(tablesManager.getTranTableModel());
        tablesManager.getTranJTable().setRowSorter(sorter);
        this.filterPanel = filterPanel;
    }

    public void filterTable() {
        filterPanel.checkSpinnersValues();
        sorter.setRowFilter(null);

        List<RowFilter<TransactionsTable, Integer>> filters = new ArrayList<>(6);
        filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.AFTER, filterPanel.getDateFrom(), 5));
        filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.BEFORE, filterPanel.getDateTo(), 5));

        if (filterPanel.getCategoriesComboBox().getSelectedIndex() > 0) {
            filters.add(categoriesFilter);
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
}
