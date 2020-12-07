package ui;

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

        cb_incomes = new JCheckBox("Incomes", true);
        cb_spendings = new JCheckBox("Spendings", true);
        toolbar.addSeparator();
        toolbar.add(cb_incomes);
        toolbar.add(cb_spendings);
        toolbar.addSeparator();

        var ctb = (CategoriesTable) categories.getModel();
        comboBox = new JComboBox<>(ctb.getCategories().toArray());
        toolbar.add(comboBox);
        toolbar.addSeparator();

        spinner_from = createDateSpinner();
        spinner_to = createDateSpinner();
        toolbar.add(new JLabel("From:"));
        toolbar.add(spinner_from);
        toolbar.addSeparator();
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
        filters.add(RowFilter.regexFilter(comboBox.getSelectedItem().toString(), 2));
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
                return false;
            }
        }
    };

    RowFilter<TransactionsTable, Integer> LessThan = new RowFilter<>() {
        public boolean include(Entry<? extends TransactionsTable, ? extends Integer> entry) {
            try {
                return table.getValueAt(entry.getIdentifier(), 1).toString().contains("-");
            } catch (ArrayIndexOutOfBoundsException ex) {
                return false;
            }
        }
    };

}
