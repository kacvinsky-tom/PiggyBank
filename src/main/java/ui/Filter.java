package ui;

import model.Category;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Filter {
    private final JSpinner spinner_from;
    private final JSpinner spinner_to;
    private final JComboBox comboBox;
    private final JCheckBox cb_incomes;
    private final JCheckBox cb_spendings;
    private final JTable table;
    private final TableRowSorter<TransactionsTable> sorter;

    public Filter(JToolBar toolbar, JTable table, JTable categories) {

        this.table = table;
        var model = table.getModel();
        sorter = new TableRowSorter<>((TransactionsTable) model);
        table.setRowSorter(sorter);

        cb_incomes = new JCheckBox("Incomes");
        cb_spendings = new JCheckBox("Spendings");
        toolbar.addSeparator();
        toolbar.add(cb_incomes, true);
        toolbar.add(cb_spendings, true);
        toolbar.addSeparator();

        //   cb_incomes.addActionListener(e -> sorter.setRowFilter(GreaterThan));
        //   cb_spendings.addActionListener(e -> sorter.setRowFilter(LessThan));

        var ctb = (CategoriesTable) categories.getModel();
        comboBox = new JComboBox<>(ctb.getCategories().toArray());
        //comboBox.addActionListener(e -> sorter.setRowFilter(categoryFilter));

        toolbar.add(comboBox);
        toolbar.addSeparator();

        spinner_from = createDateSpinner();
        spinner_to = createDateSpinner();
        toolbar.add(new JLabel("From:"));
        toolbar.add(spinner_from);
        toolbar.add(new JLabel("To:"));
        toolbar.add(spinner_to);
        toolbar.addSeparator();

        JButton dateButton = new JButton("Confirm");
        dateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateButton.addActionListener(this::filterButtonActionPerformed);
        toolbar.add(dateButton);

    }

    private JSpinner createDateSpinner() {
        JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        spinner.setVisible(true);
        return spinner;
    }

    private void filterButtonActionPerformed(ActionEvent actionEvent) {

        Date startDate = (Date) spinner_from.getValue();
        Date endDate = (Date) spinner_to.getValue();

        List<RowFilter<TransactionsTable, Integer>> filters = new ArrayList<>(4);
        filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.AFTER, startDate, 3));
        filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.BEFORE, endDate, 3));
        filters.add(categoryFilter);
        if (cb_incomes.isSelected() && !cb_spendings.isSelected()) {
            filters.add(GreaterThan);
        }
        if (!cb_incomes.isSelected() && cb_spendings.isSelected()) {
            filters.add(LessThan);
        }

        RowFilter<TransactionsTable, Integer> rf = RowFilter.andFilter(filters);
        sorter.setRowFilter(rf);
    }


    RowFilter<TransactionsTable, Integer> GreaterThan = new RowFilter<>() {
        public boolean include(Entry<? extends TransactionsTable, ? extends Integer> entry) {
            try {
                return !table.getValueAt(entry.getIdentifier(), 1).toString().contains("-");
            } catch (ArrayIndexOutOfBoundsException ex) {
                return true;
            }
        }
    };

    RowFilter<TransactionsTable, Integer> compare = new RowFilter<>() {
        public boolean include(Entry<? extends TransactionsTable, ? extends Integer> entry) {
            try {
                String string = table.getValueAt(entry.getIdentifier(), 1).toString();
                if (cb_incomes.isSelected()){
                    return cb_spendings.isSelected() || !string.contains("-");
                }
                else return cb_spendings.isSelected() && string.contains("-");
            } catch (ArrayIndexOutOfBoundsException ex) {
                return true;
            }
        }
    };

    RowFilter<TransactionsTable, Integer> LessThan = new RowFilter<>() {
        public boolean include(Entry<? extends TransactionsTable, ? extends Integer> entry) {
            try {
                return table.getValueAt(entry.getIdentifier(), 1).toString().contains("-") && cb_spendings.isSelected() || (cb_spendings.isSelected() && cb_incomes.isSelected());
            } catch (ArrayIndexOutOfBoundsException ex) {
                return true;
            }
        }
    };

    RowFilter<TransactionsTable, Integer> categoryFilter = new RowFilter<>() {
        public boolean include(Entry<? extends TransactionsTable, ? extends Integer> entry) {
            Category category = (Category) comboBox.getSelectedItem();
            try {
                return table.getValueAt(entry.getIdentifier(), 2).equals(category);
            } catch (ArrayIndexOutOfBoundsException ex) {
                return true;
            }
        }
    };

}
