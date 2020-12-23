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

public class AddTransaction extends AbstractAddEditTransaction {

    public AddTransaction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super(frame, tablesManager, messageDialog);
    }

    private void initializeComponents() {
        dialog = createDialog("Add transaction", 230, 330);
        nameField = createTextField("Name:", "", 17);
        amountField = createTextField("Amount:", "", 17);
        noteField = createTextField("Note:", "", 17);
        categoryBox = new JComboBox<>(tablesManager.getCatTableModel().getCategories().toArray());
        categoryBox.setSelectedItem(tablesManager.getCatTableModel().getOthers());
        transactionType = new JComboBox<>(TransactionType.values());
        spinner = new DateSpinner(tablesManager, DateSpinnerType.TO);
    }

    public void add() {
        initializeComponents();
        createTransactionDialog("Add");
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

        Transaction newTransaction = new Transaction(nameField.getText(), amount, category, date, noteField.getText(), type);
        tablesManager.getTranTableModel().addTransaction(newTransaction);
        tablesManager.getStatTableModel().update();
        tablesManager.getStatBalTableModel().update();
        tablesManager.getTranTableModel().filterTransactions();
        dialog.dispose();
    }
}