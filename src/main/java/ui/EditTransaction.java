package ui;

import enums.DateSpinnerType;
import enums.TransactionType;
import model.Category;
import model.Transaction;
import ui.filter.DateSpinner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class EditTransaction extends AbstractAddEditTransaction {

    private Transaction selectedTransaction;

    public EditTransaction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super(frame, tablesManager, messageDialog);
    }

    private void initializeComponents() {
        selectedTransaction = tablesManager.getTranTableModel().getEntity(tablesManager.getTranJTable().getSelectedRow());
        dialog = createDialog("Edit transaction", 230, 330);
        nameField = createTextField("Name:", selectedTransaction.getName(), 17);
        amountField = createTextField("Amount:", String.valueOf(selectedTransaction.getAmount()), 17);
        noteField = createTextField("Note:", selectedTransaction.getNote(), 17);
        transactionType = new JComboBox<>(TransactionType.values());
        spinner = new DateSpinner(tablesManager, DateSpinnerType.TO);
        spinner.setValue(selectedTransaction.getDate());
        transactionType.setSelectedItem(selectedTransaction.getType());
        checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
        for (String c : tablesManager.getCatTableModel().getCategoriesNames()){
            for (String n : tablesManager.getCatTableModel().getCategoriesNames()){
                if (c.equals(n)){
                    checkBoxPanel.add(new Checkbox(c, true));
                } else {
                    checkBoxPanel.add(new Checkbox(c, false));
                }
            }
        }
    }

    public void edit() {
        initializeComponents();
        createTransactionDialog("Save");
    }

    private void updateTransaction(String name, BigDecimal  amount, List<Category> categories, Date date, String note, TransactionType type) {
        selectedTransaction.setName(name);
        selectedTransaction.setAmount(amount);
        selectedTransaction.updateCategories(categories);
        selectedTransaction.setDate(date);
        selectedTransaction.setNote(note);
        selectedTransaction.setType(type);
        tablesManager.getTranTableModel().updateEntity(selectedTransaction);
    }

    private int getCategoryIndex(Transaction transaction){
        int index = 0;
        for (Category t : tablesManager.getCatTableModel().getCategories()){
            if (transaction.getCategory().getName().equals(t.getName())){
                return index;
            }
            ++index;
        }
        return -1;
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

        TransactionType type = (TransactionType) transactionType.getItemAt(transactionType.getSelectedIndex());
        Date date = (Date) spinner.getValue();

        updateTransaction(nameField.getText(), amount, getCategoriesFromCheckBoxes(), date, noteField.getText(), type);
        tablesManager.getStatTableModel().update();
        tablesManager.getStatBalTableModel().update();
        tablesManager.getTranTableModel().filterTransactions();
        dialog.dispose();
    }

}
