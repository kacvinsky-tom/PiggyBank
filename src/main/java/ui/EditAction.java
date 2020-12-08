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

    private JTextField createTextfield(String string) {
        dialog.add(new JLabel(string + ": "));
        JTextField textField = new JTextField();
        textField.setColumns(20);
        textField.setSize(new Dimension(150, 20));
        dialog.add(textField);
        return textField;
    }

    private JSpinner createDateSpinner() {
        dialog.add(new JLabel("Choose date: "));
        JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        spinner.setVisible(true);
        return spinner;
    }

    private void createWrongInputException(){
        JOptionPane.showMessageDialog(new JFrame(), "Enter valid number into amount!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void createTransactionDialog() {
        dialog = createDialog("new transaction", 250, 330);
        dialog.setLayout(new FlowLayout());

        JTable transactionsTable = getJTable(1);
        JTable categoriesTable = getJTable(2);

        var transactionTableModel = (TransactionsTable) transactionsTable.getModel();
        var categoriesTableModel = (CategoriesTable) categoriesTable.getModel();

        JTextField nameField = createTextfield("Name");
        JTextField amountField = createTextfield("Amount");
        JTextField noteField = createTextfield("Note");

        var categoryBox = new JComboBox<>(categoriesTableModel.getCategories().toArray());
        var transactionType = new JComboBox<>(TransactionType.values());

        dialog.add(new JLabel("Choose category:"));
        dialog.add(categoryBox);
        dialog.add(new JLabel("Choose type:"));
        dialog.add(transactionType);

        JSpinner spinner = createDateSpinner();
        dialog.add(spinner);

        JButton add = new JButton("Add");
        dialog.getContentPane().add(add);

        add.addActionListener(e -> {

            String name = nameField.getText();
            double amount;
            try {
                amount = Double.parseDouble(amountField.getText());
            } catch (NumberFormatException ex){
                dialog.dispose();
                createWrongInputException();
                createTransactionDialog();
                return;
            }
            String note = noteField.getText();
            Category category = categoriesTableModel.getCategories().get(categoryBox.getSelectedIndex());
            TransactionType type = transactionType.getItemAt(transactionType.getSelectedIndex());

            Date date = (Date) spinner.getValue();
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);

            transactionTableModel.addTransaction(new Transaction(name, amount, category, date, note, type));

            category.setExpenses(category.getExpenses() + amount);

            dialog.dispose();
        });
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void editTransaction(){
        var transactionsTable =  getJTable(1);
        var transactionsTableModel = (TransactionsTable) transactionsTable.getModel();
        //createTransactionDialog(transactionsTable.getSelectedRow());
    }

    private void editCategory(){

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
