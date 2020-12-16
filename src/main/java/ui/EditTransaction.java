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

public class EditTransaction extends AbstractAddEditAction {

    private JTextField nameField, amountField, noteField;
    private JSpinner spinner;
    private JComboBox<Object> categoryBox, transactionType;
    private Transaction selectedTransaction;

    public EditTransaction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super(frame, tablesManager, messageDialog);
    }

    private void initializeComponents() {
        selectedTransaction = tablesManager.getTranTableModel().getEntity(tablesManager.getTranJTable().getSelectedRow());

        dialog = createDialog("Edit transaction", 250, 330);
        nameField = createTextField("Name:", selectedTransaction.getName(), 20);
        amountField = createTextField("Amount:", String.valueOf(selectedTransaction.getAmount()), 20);
        noteField = createTextField("Note:", selectedTransaction.getNote(), 20);
        categoryBox = new JComboBox<>(tablesManager.getCatTableModel().getCategories().toArray());
        categoryBox.setSelectedItem(tablesManager.getCatTableModel().getOthers());
        transactionType = new JComboBox<>(TransactionType.values());
        spinner = new DateSpinner(tablesManager, DateSpinnerType.TO);
    }

    public void createTransactionDialog() {
        initializeComponents();

        dialog.add(new JLabel("Select category:"));
        categoryBox.setSelectedItem(selectedTransaction.getCategory());
        dialog.add(categoryBox);
        dialog.add(new JLabel("Select type:"));
        transactionType.setSelectedItem(selectedTransaction.getType());
        dialog.add(transactionType);
        dialog.add(new JLabel("Select date: "));
        spinner.setValue(selectedTransaction.getDate());
        dialog.add(spinner);
        dialog.getContentPane().add(createButton("Save"));

        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void updateTransaction(String name, double amount, Category category, Date date, String note, TransactionType type) {
        selectedTransaction.setName(name);
        selectedTransaction.setAmount(Math.abs(amount));
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
        double amount;
        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException ex) {
            messageDialog.showErrorMessage("Enter valid number into amount!");
            return;
        }
        Category category = tablesManager.getCatTableModel().getCategories().get(categoryBox.getSelectedIndex());
        TransactionType type = (TransactionType) transactionType.getItemAt(transactionType.getSelectedIndex());
        Date date = (Date) spinner.getValue();

        updateTransaction(nameField.getText(), amount, category, date, noteField.getText(), type);
        dialog.dispose();
    }

}
