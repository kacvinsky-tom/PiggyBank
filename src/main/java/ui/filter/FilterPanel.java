package ui.filter;

import model.Category;
import enums.DateSpinnerType;
import enums.TableType;
import ui.MessageDialog;
import ui.TablesManager;

import javax.swing.*;
import java.util.*;

public class FilterPanel extends JPanel {
    private final JSpinner spinnerFrom, spinnerTo;
    private final JCheckBox checkBoxIncomes, checkBoxSpending;
    private JComboBox<Object> categoriesComboBox;
    private final MessageDialog messageDialog;
    private final TablesManager tablesManager;

    public FilterPanel(TablesManager tablesManager, MessageDialog messageDialog){
        this.messageDialog = messageDialog;
        this.tablesManager = tablesManager;
        checkBoxIncomes = new JCheckBox("Income", true);
        checkBoxSpending = new JCheckBox("Spending", true);
        spinnerFrom = new DateSpinner(tablesManager, DateSpinnerType.FROM);
        spinnerTo = new DateSpinner(tablesManager, DateSpinnerType.TO);
        categoriesComboBox = createComboBox();
        setFilterPanel();
    }

    public JSpinner getSpinnerFrom() {
        return spinnerFrom;
    }

    public JSpinner getSpinnerTo() {
        return spinnerTo;
    }

    public Date getDateFrom() {
        return editSpinnerDate((Date) spinnerFrom.getValue(), -1);
    }

    public Date getDateTo() {
        return editSpinnerDate((Date) spinnerTo.getValue(), 1);
    }

    public JCheckBox getCheckBoxIncomes() {
        return checkBoxIncomes;
    }

    public JCheckBox getCheckBoxSpending() {
        return checkBoxSpending;
    }

    public JComboBox<Object> getCategoriesComboBox() {
        return categoriesComboBox;
    }

    private void setFilterPanel(){
        this.add(new JLabel("|"));
        this.add(checkBoxIncomes);
        this.add(checkBoxSpending);
        this.add(new JLabel("|"));
        this.add(categoriesComboBox);
        this.add(new JLabel("| From:"));
        this.add(spinnerFrom);
        this.add(new JLabel("To:"));
        this.add(spinnerTo);
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

    public void setComponentsEnable(int selectedTable){
        if (selectedTable == TableType.TRANSACTIONS_TABLE.ordinal()){
            checkBoxIncomes.setEnabled(true);
            checkBoxSpending.setEnabled(true);
            spinnerFrom.setEnabled(true);
            spinnerTo.setEnabled(true);
            categoriesComboBox.setEnabled(true);

        } else if (selectedTable == TableType.STATISTICS_TABLE.ordinal()){
            checkBoxIncomes.setEnabled(true);
            checkBoxSpending.setEnabled(true);
            spinnerFrom.setEnabled(true);
            spinnerTo.setEnabled(true);
            categoriesComboBox.setEnabled(false);

        } else if (selectedTable == TableType.CATEGORIES_TABLE.ordinal()){
            checkBoxIncomes.setEnabled(false);
            checkBoxSpending.setEnabled(false);
            spinnerFrom.setEnabled(false);
            spinnerTo.setEnabled(false);
            categoriesComboBox.setEnabled(false);
        }
    }

    public void checkSpinnersValues(){
        Date startDate = (Date) spinnerFrom.getValue();
        Date endDate = (Date) spinnerTo.getValue();
        if (startDate.after(endDate)) {
            messageDialog.showAlertMessage("Date 'From' shouldn't be older than date 'To'!");
        }
    }

    private JComboBox<Object> createComboBox() {
        categoriesComboBox = new JComboBox<>(this.tablesManager.getCatTableModel().getCategories().toArray());
        categoriesComboBox.insertItemAt(new Category("All", null), 0);
        categoriesComboBox.setSelectedIndex(0);
        categoriesComboBox.setEnabled(false);
        return categoriesComboBox;
    }

    public void updateCategoriesComboCox(){
        categoriesComboBox.removeAllItems();
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>(fillComboBox());
        categoriesComboBox.setModel(model);
    }


    public String[] fillComboBox(){
        List<Category> categories = this.tablesManager.getCatTableModel().getCategories();
        String[] resultArray = new String[categories.size() + 1];
        resultArray[0] = "All";
        for (int i = 0; i < categories.size(); i++)
        {
            resultArray[i + 1] = categories.get(i).getName();
        }
        return resultArray;

    }
}
