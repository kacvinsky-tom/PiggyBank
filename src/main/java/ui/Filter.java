package ui;

import model.Category;
import model.DateSpinnerType;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class Filter {
    private final JSpinner spinnerFrom;
    private final JSpinner spinnerTo;
    private JComboBox<Object> comboBox;
    private final JCheckBox cb_incomes, cb_spendings;
    private final TableRowSorter<TransactionsTable> sorter;
    private final TablesManager tablesManager;
    private int selectedTabIndex = 0;
    private final MessageDialog messageDialog;

    public Filter(JToolBar toolbar, TablesManager tablesManager, MessageDialog messageDialog) {
        this.tablesManager = tablesManager;
        this.messageDialog = messageDialog;

        sorter = new TableRowSorter<>(this.tablesManager.getTranTableModel());
        this.tablesManager.getTranJTable().setRowSorter(sorter);

        cb_incomes = new JCheckBox("Incomes", true);
        cb_incomes.addActionListener(this::filterActionPerformed);
        cb_spendings = new JCheckBox("Spendings", true);
        cb_spendings.addActionListener(this::filterActionPerformed);

        toolbar.addSeparator();
        toolbar.add(cb_incomes);
        toolbar.add(cb_spendings);
        toolbar.addSeparator();

        toolbar.add(createComboBox(this.tablesManager.getCatJTable()));
        comboBox.addActionListener(this::filterActionPerformed);
        toolbar.addSeparator();

        spinnerFrom = new DateSpinner(this.tablesManager, DateSpinnerType.FROM);
        spinnerFrom.addChangeListener(this::dateChangePerformed);

        spinnerTo = new DateSpinner(this.tablesManager, DateSpinnerType.TO);
        spinnerTo.addChangeListener(this::dateChangePerformed);

        toolbar.add(new JLabel("From:"));
        toolbar.add(spinnerFrom);
        toolbar.addSeparator();
        toolbar.add(new JLabel("To:"));
        toolbar.add(spinnerTo);
    }

    public void setSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }

    public boolean checkSpinnersValues(){
        Date startDate = (Date) spinnerFrom.getValue();
        Date endDate = (Date) spinnerTo.getValue();
        if (startDate.after(endDate)) {
            messageDialog.showMessage("Date 'From' shouldn't be older than date 'To'!", "Alert", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void dateChangePerformed(ChangeEvent changeEvent) {
        filterTable();
    }

    private JComboBox<Object> createComboBox(JTable categories) {
        var ctb = (CategoriesTable) categories.getModel();
        comboBox = new JComboBox<>(ctb.getCategories().toArray());
        comboBox.insertItemAt(new Category("All", null), 0);
        comboBox.setSelectedIndex(0);
        return comboBox;
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

    private void filterTable() {
        checkSpinnersValues();

        sorter.setRowFilter(null);

        Date startDate = editSpinnerDate((Date) spinnerFrom.getValue(), -1);
        Date endDate = editSpinnerDate((Date) spinnerTo.getValue(), 1);

        List<RowFilter<TransactionsTable, Integer>> filters = new ArrayList<>(5);
        filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.AFTER, startDate, 4));
        filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.BEFORE, endDate, 4));

        if (comboBox.getSelectedIndex() > 0) {
            filters.add(RowFilter.regexFilter(comboBox.getSelectedItem().toString(), 3));
        }
        if (cb_incomes.isSelected() && !cb_spendings.isSelected()) {
            filters.add(incomeFilter);
        }
        if (!cb_incomes.isSelected() && cb_spendings.isSelected()) {
            filters.add(spendingFilter);
        }

        RowFilter<TransactionsTable, Integer> rf = RowFilter.andFilter(filters);
        sorter.setRowFilter(rf);
    }

    private void filterActionPerformed(ActionEvent actionEvent) {
        filterTable();
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
