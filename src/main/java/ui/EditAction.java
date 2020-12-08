package ui;

import model.Category;
import model.Transaction;
import model.TransactionType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Date;

final class EditAction extends AbstractAction {

    private final JFrame frame;
    private final JTabbedPane pane;
    private final JLabel categoryColorPanel = new JLabel();
    private JDialog dialog;
    private JTextField nameField, amountField, noteField;
    private JSpinner spinner;
    private JComboBox<Object> categoryBox, transactionType;
    private Transaction selectedTransaction;

    public EditAction(JTabbedPane pane, JFrame frame) {
        super("Edit", Icons.EDIT_ICON);
        this.pane = pane;
        this.frame = frame;
        this.setEnabled(false);
        putValue(SHORT_DESCRIPTION, "Edits selected row");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    private JDialog createDialog(String string, int width, int height) {
        dialog = new JDialog();
        dialog.setTitle("Edit " + string);
        dialog.setSize(new Dimension(width, height));
        dialog.setModal(true);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        return dialog;
    }

    private JTable getJTable(int idx) {
        JScrollPane scrollPane = (JScrollPane) pane.getComponentAt(idx);
        JViewport viewport = scrollPane.getViewport();
        return (JTable) viewport.getView();
    }

    private JTextField createTextfield(String label, String content) {
        dialog.add(new JLabel("Change " + label + ": "));
        JTextField textField = new JTextField(content);
        textField.setColumns(20);
        textField.setSize(new Dimension(150, 20));
        dialog.add(textField);
        return textField;
    }

    private JSpinner createDateSpinner() {
        JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        spinner.setVisible(true);
        return spinner;
    }

    private void createWrongInputException() {
        JOptionPane.showMessageDialog(new JFrame(), "Enter valid number into amount!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private JButton createButton() {
        JButton button = new JButton("Edit");
        button.addActionListener(this::editButtonActionPerformed);
        return button;
    }

    private void editButtonActionPerformed(ActionEvent actionEvent) {
        var categoriesTableModel = (CategoriesTable) getJTable(2).getModel();

        String name = nameField.getText();
        String note = noteField.getText();
        double amount;
        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException ex) {
            dialog.dispose();
            createWrongInputException();
            editTransaction();
            return;
        }
        Category category = categoriesTableModel.getCategories().get(categoryBox.getSelectedIndex());
        TransactionType type = (TransactionType) transactionType.getItemAt(transactionType.getSelectedIndex());
        Date date = (Date) spinner.getValue();

        updateTransaction(name, amount, category, date, note, type);
        dialog.dispose();
    }

    private void updateTransaction(String name, double amount, Category category, Date date, String note, TransactionType type) {
        selectedTransaction.getCategory().setExpenses(selectedTransaction.getCategory().getExpenses() - selectedTransaction.getAmount());

        selectedTransaction.setName(name);
        selectedTransaction.setAmount(amount);
        selectedTransaction.setCategory(category);
        selectedTransaction.setDate(date);
        selectedTransaction.setNote(note);
        selectedTransaction.setType(type);

        var transactionTableModel = (TransactionsTable) getJTable(1).getModel();
        transactionTableModel.updateEntity(selectedTransaction);
        int rowIndex = transactionTableModel.getTransactions().indexOf(selectedTransaction);
        transactionTableModel.fireTableRowsUpdated(rowIndex, rowIndex);

        category.setExpenses(category.getExpenses() + amount);
    }

    private void setTransactionDialog() {
        dialog.setLayout(new FlowLayout());

        dialog.add(new JLabel("Change category:"));
        categoryBox.setSelectedItem(selectedTransaction.getCategory());
        dialog.add(categoryBox);

        dialog.add(new JLabel("Change type:"));
        transactionType.setSelectedItem(selectedTransaction.getType());
        dialog.add(transactionType);

        dialog.add(new JLabel("Change date: "));
        spinner.setValue(selectedTransaction.getDate());
        dialog.add(spinner);

        dialog.getContentPane().add(createButton());

        dialog.setResizable(false);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void createTransactionDialog() {
        JTable transactionsTable = getJTable(1);
        var transactionsTableModel = (TransactionsTable) transactionsTable.getModel();
        JTable categoriesTable = getJTable(2);
        var categoriesTableModel = (CategoriesTable) categoriesTable.getModel();

        dialog = createDialog("transaction", 250, 330);
        selectedTransaction = transactionsTableModel.getEntity(transactionsTable.getSelectedRow());
        nameField = createTextfield("name", selectedTransaction.getName());
        amountField = createTextfield("amount", String.valueOf(selectedTransaction.getAmount()));
        noteField = createTextfield("note", selectedTransaction.getNote());
        categoryBox = new JComboBox<>(categoriesTableModel.getCategories().toArray());
        transactionType = new JComboBox<>(TransactionType.values());
        spinner = createDateSpinner();

        setTransactionDialog();
    }

    private void editTransaction()  {
        createTransactionDialog();
    }

    private void editCategory() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int index = pane.getSelectedIndex();
        if (index <= 1) {
            editTransaction();
        } else if (index == 2) {
            editCategory();
        }
    }
}
