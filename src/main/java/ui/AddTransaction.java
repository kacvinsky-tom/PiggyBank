package ui;

import enums.DateSpinnerType;
import enums.TransactionType;
import model.Category;
import model.Transaction;
import ui.filter.DateSpinner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;

public class AddTransaction extends AddAction {

    private JTextField nameField, amountField, noteField;
    private JComboBox<Object> categoryBox, transactionType;
    private JSpinner spinner;

    public AddTransaction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super(frame, tablesManager, messageDialog);
        initializeComponents();
        createTransactionDialog();
    }

    private void initializeComponents() {
        dialog = createDialog("transaction", 250, 330);
        nameField = createTextField("Name:", "", 20);
        amountField = createTextField("Amount:", "", 20);
        noteField = createTextField("Note:", "", 20);
        categoryBox = new JComboBox<>(tablesManager.getCatTableModel().getCategories().toArray());
        categoryBox.setSelectedItem(tablesManager.getCatTableModel().getOthers());
        transactionType = new JComboBox<>(TransactionType.values());
        spinner = new DateSpinner(tablesManager, DateSpinnerType.TO);

    }

    private void createTransactionDialog() {
        dialog.setLayout(new FlowLayout());

        dialog.add(new JLabel("Select category:"));
        dialog.add(categoryBox);
        dialog.add(new JLabel("Select type:"));
        dialog.add(transactionType);
        dialog.add(new JLabel("Select date: "));
        dialog.add(spinner);
        dialog.getContentPane().add(createButton("Add"));

        dialog.setResizable(false);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    @Override
    protected void buttonActionPerformed(ActionEvent actionEvent) {
        double amount;
        try {
            amount = Math.abs(Double.parseDouble(amountField.getText()));
        } catch (NumberFormatException ex) {
            createErrorDialog("Enter valid number into amount!");
            return;
        }
        Category category = tablesManager.getCatTableModel().getCategories().get(categoryBox.getSelectedIndex());
        TransactionType type = (TransactionType) transactionType.getItemAt(transactionType.getSelectedIndex());
        Date date = (Date) spinner.getValue();

        Transaction newTransaction = new Transaction(nameField.getText(), amount, category, date, noteField.getText(), type);
        tablesManager.getTranTableModel().addTransaction(newTransaction);
        dialog.dispose();
    }
}
