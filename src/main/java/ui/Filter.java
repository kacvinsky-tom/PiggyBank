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

    public Filter(JToolBar toolbar, TablesManager tablesManager) {
        this.tablesManager = tablesManager;

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

        //spinnerFrom = new DateSpinner(this.tablesManager, DateSpinnerType.FROM);
        spinnerFrom = getDateSpinner();
        spinnerFrom.addChangeListener(this::dateChangePerformedFrom);

        //spinnerTo = new DateSpinner(this.tablesManager, DateSpinnerType.TO);
        spinnerTo = getDateSpinner();
        spinnerTo.addChangeListener(this::dateChangePerformedTo);

        toolbar.add(new JLabel("From:"));
        toolbar.add(spinnerFrom);
        toolbar.addSeparator();
        toolbar.add(new JLabel("To:"));
        toolbar.add(spinnerTo);
    }

    private JSpinner getDateSpinner() {
        JSpinner s = new JSpinner();
        SpinnerDateModel spinnerDateModel = new SpinnerDateModel();
        s.setModel(spinnerDateModel);
        s.setEditor(new JSpinner.DateEditor(s, "dd/MM/yyyy"));
        s.setVisible(true);
        return s;
    }

    public void setSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }

    private void dateChangePerformedFrom(ChangeEvent changeEvent) {
        filterTable();
    }

    private void dateChangePerformedTo(ChangeEvent changeEvent) {
        filterTable();
    }

    private JComboBox<Object> createComboBox(JTable categories) {
        var ctb = (CategoriesTable) categories.getModel();
        comboBox = new JComboBox<>(ctb.getCategories().toArray());
        comboBox.insertItemAt(new Category("All", null), 0);
        comboBox.setSelectedIndex(0);
        return comboBox;
    }

    private void createWrongDateDialog() {
        JOptionPane.showMessageDialog(new JFrame(), "Date 'From' cannot be older than date 'To'!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private Date getSpinnerDate(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, i);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    private void filterTable() {
        sorter.setRowFilter(null);

        Date startDate = getSpinnerDate((Date) spinnerFrom.getValue(), -1);
        Date endDate = getSpinnerDate((Date) spinnerTo.getValue(), 1);
        System.out.println(startDate);
        System.out.println(endDate);

        if (startDate.after(endDate)) {     // TODO KONTROLOVAT ESTE PRED UPRAVENIM DATUMU
            createWrongDateDialog();
            return;
        }

        List<RowFilter<TransactionsTable, Integer>> filters = new ArrayList<>(4);
        filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.AFTER, startDate, 3));
        filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.BEFORE, endDate, 3));

        if (comboBox.getSelectedIndex() > 0) {
            filters.add(RowFilter.regexFilter(comboBox.getSelectedItem().toString(), 2));
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
                return !tablesManager.getTranJTable().getValueAt(entry.getIdentifier(), 1).toString().contains("-");
            } catch (ArrayIndexOutOfBoundsException ex) {
                return false;
            }
        }
    };

    RowFilter<TransactionsTable, Integer> spendingFilter = new RowFilter<>() {
        public boolean include(Entry<? extends TransactionsTable, ? extends Integer> entry) {
            try {
                return tablesManager.getTranJTable().getValueAt(entry.getIdentifier(), 1).toString().contains("-");
            } catch (ArrayIndexOutOfBoundsException ex) {
                return false;
            }
        }
    };

}
