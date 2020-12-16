package ui;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionsFilter {
    private final TableRowSorter<TransactionsTable> sorter;
    private final TablesManager tablesManager;
    private final FilterPanel filterPanel;

    public TransactionsFilter(TablesManager tablesManager, FilterPanel filterPanel){
        sorter = new TableRowSorter<>(tablesManager.getTranTableModel());
        this.tablesManager = tablesManager;
        this.tablesManager.getStatJTable().setRowSorter(sorter);
        this.filterPanel = filterPanel;
    }

    private Date editSpinnerDate(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, i);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public boolean checkSpinnersValues(){
        Date startDate = (Date) filterPanel.getSpinnerFrom().getValue();
        Date endDate = (Date) filterPanel.getSpinnerTo().getValue();
        if (startDate.after(endDate)) {
            messageDialog.showAlertMessage("Date 'From' shouldn't be older than date 'To'!");
            return false;
        }
        return true;
    }

    public void filterTable() {
        checkSpinnersValues();

        sorter.setRowFilter(null);

        Date startDate = editSpinnerDate((Date) filterPanel.getSpinnerFrom().getValue(), -1);
        Date endDate = editSpinnerDate((Date) filterPanel.getSpinnerTo().getValue(), 1);

        List<RowFilter<TransactionsTable, Integer>> filters = new ArrayList<>(5);
        filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.AFTER, startDate, 4));
        filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.BEFORE, endDate, 4));

        if (filterPanel.getComboBox().getSelectedIndex() > 0) {
            filters.add(RowFilter.regexFilter(filterPanel.getComboBox().getSelectedItem().toString(), 3));
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
