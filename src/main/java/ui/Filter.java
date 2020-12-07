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
    private final TableRowSorter<TransactionsTable> sorter;

    public Filter(JToolBar toolbar, JTable table) {

        var model = (TransactionsTable) table.getModel();
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        spinner_from = createDateSpinner();
        spinner_to = createDateSpinner();
        toolbar.add(new JLabel("Date From:"));
        toolbar.add(spinner_from);
        toolbar.add(new JLabel("To:"));
        toolbar.add(spinner_to);

        JButton dateButton = createJButton();
        toolbar.add(dateButton);
    }

    private JSpinner createDateSpinner() {
        JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        spinner.setVisible(true);
        return spinner;
    }

    private JButton createJButton() {
        JButton button = new JButton("Confirm");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(this::dateButtonActionPerformed);
        return button;
    }

    private void dateButtonActionPerformed(ActionEvent actionEvent) {

        Date startDate = (Date) spinner_from.getValue();
        Date endDate = (Date) spinner_to.getValue();

        List<RowFilter<TransactionsTable, Integer>> filters = new ArrayList<>(2);
        filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.AFTER, startDate, 3));
        filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.BEFORE, endDate, 3));
        RowFilter<TransactionsTable, Integer> rf = RowFilter.andFilter(filters);
        sorter.setRowFilter(rf);
    }

}
