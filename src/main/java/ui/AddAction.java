package ui;

import model.Category;
import model.Transaction;
import model.TransactionType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Date;

final class AddAction extends AbstractAction {

    private final JFrame frame;
    private final JTabbedPane pane;
    private final JLabel categoryColorPanel = new JLabel();
    private JTextField nameField, amountField, noteField;
    private JComboBox<Object> categoryBox, transactionType;
    private JDialog dialog;
    private JSpinner spinner;

    public AddAction(JTabbedPane pane, JFrame frame) {
        super("Add", Icons.ADD_ICON);
        this.frame = frame;
        this.pane = pane;
        putValue(SHORT_DESCRIPTION, "Adds new row");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    private JTable getJTable(int idx) {
        JScrollPane scrollPane = (JScrollPane) pane.getComponentAt(idx);
        JViewport viewport = scrollPane.getViewport();
        return (JTable) viewport.getView();
    }

    private JSpinner createDateSpinner() {
        JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        spinner.setVisible(true);
        return spinner;
    }

    private JDialog createDialog(String string, int width, int height) {
        dialog = new JDialog();
        dialog.setTitle("Add " + string);
        dialog.setSize(new Dimension(width, height));
        dialog.setModal(true);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        return dialog;
    }

    private void createWrongInputException(){
        JOptionPane.showMessageDialog(new JFrame(), "Enter valid number into amount!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private JTextField createTextfield(String string) {
        dialog.add(new JLabel(string + ": "));
        JTextField textField = new JTextField();
        textField.setColumns(20);
        textField.setSize(new Dimension(150, 20));
        dialog.add(textField);
        return textField;
    }

    private JButton createButton() {
        JButton button = new JButton("Add");
        button.addActionListener(this::addButtonActionPerformed);
        return button;
    }

    private void addButtonActionPerformed(ActionEvent actionEvent) {
        var categoriesTableModel = (CategoriesTable) getJTable(2).getModel();
        var transactionTableModel = (TransactionsTable) getJTable(1).getModel();

        String name = nameField.getText();
        String note = noteField.getText();
        double amount;
        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException ex){
            dialog.dispose();
            createWrongInputException();
            addTransaction();
            return;
        }
        Category category = categoriesTableModel.getCategories().get(categoryBox.getSelectedIndex());
        TransactionType type = (TransactionType) transactionType.getItemAt(transactionType.getSelectedIndex());
        Date date = (Date) spinner.getValue();

        transactionTableModel.addTransaction(new Transaction(name, amount, category, date, note, type));
        category.setExpenses(category.getExpenses() + amount);
        dialog.dispose();
    }

    private void createTransactionDialog() {
        JTable categoriesTable = getJTable(2);
        var categoriesTableModel = (CategoriesTable) categoriesTable.getModel();

        dialog = createDialog("transaction", 250, 330);
        nameField = createTextfield("Name");
        amountField = createTextfield("Amount");
        noteField = createTextfield("Note");
        categoryBox = new JComboBox<>(categoriesTableModel.getCategories().toArray());
        transactionType = new JComboBox<>(TransactionType.values());
        spinner = createDateSpinner();

        setTransactionDialog();
    }

    private void setTransactionDialog() {
        dialog.setLayout(new FlowLayout());

        dialog.add(new JLabel("Select category:"));
        dialog.add(categoryBox);
        dialog.add(new JLabel("Select type:"));
        dialog.add(transactionType);
        dialog.add(new JLabel("Select date: "));
        dialog.add(spinner);
        dialog.getContentPane().add(createButton());

        dialog.setResizable(false);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void prepareColorPanel(JLabel colorLabel){
        colorLabel.setBackground(Color.BLACK);
        colorLabel.setOpaque(true);
        colorLabel.setPreferredSize(new Dimension(40,20));
    }

    private void createCategoryExistsDialog(String name){
        JOptionPane.showMessageDialog(new JFrame(), "Category " + name + " already exists!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void createColorExistsDialog(){
        JOptionPane.showMessageDialog(new JFrame(), "Chosen color is already taken by another category!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void createCategoryDialog(){
        JTable categoriesTable = getJTable(2);
        var categoriesTableModel = (CategoriesTable) categoriesTable.getModel();

        prepareColorPanel(categoryColorPanel);

        JDialog categoryDialog = createDialog("new category", 380, 150);

        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new FlowLayout());
        JButton setColorButton = new JButton("Show Color Chooser...");
        setColorButton.addActionListener(this::colorChooser);

        categoryDialog.add(categoryPanel);
        JTextField newCategoryName = new JTextField("");
        newCategoryName.setPreferredSize(new Dimension(150, 30));

        categoryPanel.add(new JLabel("Enter name of the new category:"));
        categoryPanel.add(newCategoryName);

        categoryPanel.add(new JLabel("Color of the category:"));
        categoryPanel.add(setColorButton);
        categoryPanel.add(categoryColorPanel);

        JButton confirmButton = new JButton("Confirm");
        categoryPanel.add(confirmButton);
        confirmButton.addActionListener(e -> {
            categoryDialog.dispose();
            String name = newCategoryName.getText();
            int result = categoriesTableModel.addRow(new Category(name, categoryColorPanel.getBackground()));
            if (result == 1){
                createCategoryExistsDialog(name);
                createCategoryDialog();
            } else if (result == 2){
                createColorExistsDialog();
                createCategoryDialog();
            }
        });
        categoryDialog.setLocationRelativeTo(frame);
        categoryDialog.setVisible(true);

    }

    private void colorChooser(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(
                null,
                "Choose Background Color",
                Color.BLACK);
        if (newColor != null) {
            categoryColorPanel.setBackground(newColor);
        }
    }

    private void addTransaction() {
        createTransactionDialog();
    }

    private void addCategory() {
        createCategoryDialog();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int index = pane.getSelectedIndex();
        if (index <= 1) {
            addTransaction();
        } else if (index == 2) {
            addCategory();
        }
    }
}
