package ui;

import model.Category;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class Filter {
    private final JSpinner spinner_from, spinner_to;
    private JComboBox<Object> comboBox;
    private final JCheckBox cb_incomes, cb_spendings;
    private final TableRowSorter<TransactionsTable> sorter;
    private final JTable transactions;
    private final TransactionsTable transactionsTableModel;

    public Filter(JToolBar toolbar, JTable transactions, JTable categories, TransactionsTable transactionsTableModel) {
        this.transactions = transactions;
        this.transactionsTableModel = transactionsTableModel;

        sorter = new TableRowSorter<>(transactionsTableModel);
        transactions.setRowSorter(sorter);

        cb_incomes = new JCheckBox("Incomes", true);
        cb_incomes.addActionListener(this::filterActionPerformed);
        cb_spendings = new JCheckBox("Spendings", true);
        cb_spendings.addActionListener(this::filterActionPerformed);

        toolbar.addSeparator();
        toolbar.add(cb_incomes);
        toolbar.add(cb_spendings);
        toolbar.addSeparator();

        toolbar.add(createComboBox(categories));
        comboBox.addActionListener(this::filterActionPerformed);
        toolbar.addSeparator();

        spinner_from = new DateSpinner(transactionsTableModel, true).getSpinner();
        spinner_from.addChangeListener(this::dateChangePerformed);
        spinner_to = new DateSpinner(transactionsTableModel, false).getSpinner();

        toolbar.add(new JLabel("From:"));
        toolbar.add(spinner_from);
        toolbar.addSeparator();
        toolbar.add(new JLabel("To:"));
        toolbar.add(spinner_to);
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

    private void createWrongDateDialog() {
        JOptionPane.showMessageDialog(new JFrame(), "Date 'From' cannot be older than date 'To'!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private Date getSpinnerDate(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, i);
        return c.getTime();
    }

    private void filterTable() {
        sorter.setRowFilter(null);

        Date startDate = getSpinnerDate((Date) spinner_from.getValue(), -1);
        Date endDate = getSpinnerDate((Date) spinner_to.getValue(), 1);

        if (startDate.after(endDate)) {
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
                return !transactions.getValueAt(entry.getIdentifier(), 1).toString().contains("-");
            } catch (ArrayIndexOutOfBoundsException ex) {
                return false;
            }
        }
    };

    RowFilter<TransactionsTable, Integer> spendingFilter = new RowFilter<>() {
        public boolean include(Entry<? extends TransactionsTable, ? extends Integer> entry) {
            try {
                return transactions.getValueAt(entry.getIdentifier(), 1).toString().contains("-");
            } catch (ArrayIndexOutOfBoundsException ex) {
                return false;
            }
        }
    };

}
