package ui;

import model.Category;
import model.Transaction;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class Filter {
    private final JSpinner spinner_from;
    private final JSpinner spinner_to;
    private JComboBox comboBox;
    private final JCheckBox cb_incomes;
    private final JCheckBox cb_spendings;
    private final TableRowSorter<TransactionsTable> sorter;
    private final TablesManager tablesManager;
    private int selectedTabIndex = 0;

    public Filter(JToolBar toolbar, TablesManager tablesManager) {
        this.tablesManager = tablesManager;
        sorter = new TableRowSorter<>(tablesManager.getTranTableModel());
        tablesManager.getTranJTable().setRowSorter(sorter);

        cb_incomes = new JCheckBox("Incomes", true);
        cb_spendings = new JCheckBox("Spendings", true);

        toolbar.addSeparator();
        toolbar.add(cb_incomes);
        toolbar.add(cb_spendings);
        toolbar.addSeparator();

        toolbar.add(createComboBox(tablesManager.getCatJTable()));
        toolbar.addSeparator();

        spinner_from = createDateSpinner(true);
        spinner_to = createDateSpinner(false);

        toolbar.add(new JLabel("From:"));
        toolbar.add(spinner_from);
        toolbar.addSeparator();
        toolbar.add(new JLabel("To:"));
        toolbar.add(spinner_to);
        toolbar.addSeparator();

        toolbar.add(createButton());
    }

    public void setSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }

    private void disableFilterSettings(){
        // TODO IMPLEMENT DISABLING FILTERS ON CERTAIN TABS USING 'selectedTabIndex' AND 'TableType'
    }

    private JComboBox createComboBox(JTable categories) {
        var ctb = (CategoriesTable) categories.getModel();
        comboBox = new JComboBox(ctb.getCategories().toArray());
        comboBox.insertItemAt(new Category("All", null), 0);
        comboBox.setSelectedIndex(0);
        return comboBox;
    }

    private JButton createButton() {
        JButton button = new JButton("Update settings");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(this::filterButtonActionPerformed);
        return button;
    }

    private JSpinner createDateSpinner(boolean from) {
        JSpinner spinner = new JSpinner();
        SpinnerDateModel spinnerDateModel = new SpinnerDateModel();
        spinner.setModel(spinnerDateModel);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        if (from){
            List<Transaction> tranList = tablesManager.getTranTableModel().getTransactions();
            if (tranList.isEmpty()){
                spinner.setVisible(true);
                return spinner;
            }
            Date time = tranList.get(0).getDate();
            for (Transaction t : tranList){
                if (t.getDate().compareTo(time) < 0){
                    time = t.getDate();
                }
            }
            spinnerDateModel.setValue(time);
        }
        spinner.setVisible(true);
        return spinner;
    }

    private void createWrongDateDialog(){
        JOptionPane.showMessageDialog(new JFrame(), "Date 'From' cannot be older than date 'To'!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void filterButtonActionPerformed(ActionEvent actionEvent) {
        sorter.setRowFilter(null);

        Date startDate = (Date) spinner_from.getValue();
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.MILLISECOND, -1);
        startDate = c.getTime();


        Date endDate = (Date) spinner_to.getValue();
        c.setTime(endDate);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        endDate = c.getTime();

        if (startDate.after(endDate)){
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

    RowFilter<TransactionsTable, Integer> incomeFilter = new RowFilter<>() {
        public boolean include(Entry<? extends TransactionsTable, ? extends Integer> entry) {
            try {
                return !tablesManager.getTranTableModel().getValueAt(entry.getIdentifier(), 1).toString().contains("-");
            } catch (ArrayIndexOutOfBoundsException ex) {
                return false;
            }
        }
    };

    RowFilter<TransactionsTable, Integer> spendingFilter = new RowFilter<>() {
        public boolean include(Entry<? extends TransactionsTable, ? extends Integer> entry) {
            try {
                return tablesManager.getTranTableModel().getValueAt(entry.getIdentifier(), 1).toString().contains("-");
            } catch (ArrayIndexOutOfBoundsException ex) {
                return false;
            }
        }
    };

}
