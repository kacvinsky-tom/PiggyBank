package ui;

import model.Category;
import model.DateSpinnerType;

import javax.swing.*;

public class FilterPanel extends JPanel {
    private final JSpinner spinnerFrom, spinnerTo;
    private final JCheckBox checkBoxIncomes, checkBoxSpending;
    private JComboBox<Object> comboBox;

    public FilterPanel(TablesManager tablesManager){
        checkBoxIncomes = new JCheckBox("Income", true);
        checkBoxSpending = new JCheckBox("Spending", true);
        spinnerFrom = new DateSpinner(tablesManager, DateSpinnerType.FROM);
        spinnerTo = new DateSpinner(tablesManager, DateSpinnerType.TO);
        comboBox = createComboBox(tablesManager);
        setFilterPanel();
    }

    public JSpinner getSpinnerFrom() {
        return spinnerFrom;
    }

    public JSpinner getSpinnerTo() {
        return spinnerTo;
    }

    public JCheckBox getCheckBoxIncomes() {
        return checkBoxIncomes;
    }

    public JCheckBox getCheckBoxSpending() {
        return checkBoxSpending;
    }

    public JComboBox<Object> getComboBox() {
        return comboBox;
    }

    private void setFilterPanel(){
        this.add(new JLabel("|"));
        this.add(checkBoxIncomes);
        this.add(checkBoxSpending);
        this.add(new JLabel("|"));
        this.add(comboBox);
        this.add(new JLabel("| From:"));
        this.add(spinnerFrom);
        this.add(new JLabel("To:"));
        this.add(spinnerTo);
    }

    private JComboBox<Object> createComboBox(TablesManager tablesManager) {
        comboBox = new JComboBox<>(tablesManager.getCatTableModel().getCategories().toArray());
        comboBox.insertItemAt(new Category("All", null), 0);
        comboBox.setSelectedIndex(0);
        return comboBox;
    }
}
