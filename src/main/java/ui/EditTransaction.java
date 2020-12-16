package ui;

import enums.DateSpinnerType;
import enums.TransactionType;
import model.Category;
import model.Transaction;
import ui.filter.DateSpinner;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Date;

public class EditTransaction extends AbstractAddEditTransaction {

    private Transaction selectedTransaction;

    public EditTransaction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super(frame, tablesManager, messageDialog);
    }

    private void initializeComponents() {
        selectedTransaction = tablesManager.getTranTableModel().getEntity(tablesManager.getTranJTable().getSelectedRow());
        dialog = createDialog("Edit transaction", 230, 330);
        nameField = createTextField("Name:", selectedTransaction.getName(), 20);
        amountField = createTextField("Amount:", String.valueOf(selectedTransaction.getAmount()), 20);
        noteField = createTextField("Note:", selectedTransaction.getNote(), 20);
        categoryBox = new JComboBox<>(tablesManager.getCatTableModel().getCategories().toArray());
        categoryBox.setSelectedItem(selectedTransaction.getCategory());
        transactionType = new JComboBox<>(TransactionType.values());
        spinner = new DateSpinner(tablesManager, DateSpinnerType.TO);
        spinner.setValue(selectedTransaction.getDate());
        transactionType.setSelectedItem(selectedTransaction.getType());
    }

    public void edit() {
        initializeComponents();
        createTransactionDialog("Save");
    }

    private void updateTransaction(String name, BigDecimal  amount, Category category, Date date, String note, TransactionType type) {
        selectedTransaction.setName(name);
        selectedTransaction.setAmount(amount);
        selectedTransaction.setCategory(category);
        selectedTransaction.setDate(date);
        selectedTransaction.setNote(note);
        selectedTransaction.setType(type);

        tablesManager.getTranTableModel().updateEntity(selectedTransaction);
        int rowIndex = tablesManager.getTranTableModel().getTransactions().indexOf(selectedTransaction);
        tablesManager.getTranTableModel().fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    protected void buttonActionPerformed(ActionEvent actionEvent) {
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountField.getText().replace(",", ".")).abs();
        } catch (NumberFormatException ex) {
            messageDialog.showErrorMessage("Enter valid number into amount!");
            return;
        }
        Category category = tablesManager.getCatTableModel().getCategories().get(categoryBox.getSelectedIndex());
        TransactionType type = (TransactionType) transactionType.getItemAt(transactionType.getSelectedIndex());
        Date date = (Date) spinner.getValue();

        updateTransaction(nameField.getText(), amount, category, date, noteField.getText(), type);
        tablesManager.getStatTableModel().update();
        tablesManager.getStatBalTableModel().update();
        dialog.dispose();
    }

}
