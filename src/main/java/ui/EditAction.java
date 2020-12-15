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
    private final TablesManager tablesManager;
    private final JLabel categoryColorPanel = new JLabel();
    private JDialog dialog;
    private JTextField nameField, amountField, noteField;
    private JSpinner spinner;
    private JComboBox<Object> categoryBox, transactionType;
    private Transaction selectedTransaction;
    private Category selectedCategory;
    private int selectedTabIndex = 0;
    private final MessageDialog messageDialog;

    public EditAction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super("Edit", Icons.EDIT_ICON);
        this.frame = frame;
        this.tablesManager = tablesManager;
        this.messageDialog = messageDialog;
        this.setEnabled(false);
        putValue(SHORT_DESCRIPTION, "Edits selected row");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    public void setSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }

    private JDialog createDialog(String string, int width, int height) {
        dialog = new JDialog();
        dialog.setTitle("Edit " + string);
        dialog.setSize(new Dimension(width, height));
        dialog.setModal(true);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        return dialog;
    }

    private JTextField createTextField(String label, String content) {
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

    private JButton createButton() {
        JButton button = new JButton("Edit");
        button.addActionListener(this::editButtonActionPerformedTransaction);
        return button;
    }

    private void editButtonActionPerformedTransaction(ActionEvent actionEvent) {
        double amount;
        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException ex) {
            messageDialog.showMessage("Enter valid number into amount!",  JOptionPane.ERROR_MESSAGE);
            return;
        }
        Category category = tablesManager.getCatTableModel().getCategories().get(categoryBox.getSelectedIndex());
        TransactionType type = (TransactionType) transactionType.getItemAt(transactionType.getSelectedIndex());
        Date date = (Date) spinner.getValue();

        updateTransaction(nameField.getText(), amount, category, date, noteField.getText(), type);
        dialog.dispose();
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
        dialog = createDialog("transaction", 250, 330);
        selectedTransaction = tablesManager.getTranTableModel().getEntity(tablesManager.getTranJTable().getSelectedRow());
        nameField = createTextField("name", selectedTransaction.getName());
        amountField = createTextField("amount", String.valueOf(selectedTransaction.getAmount()));
        noteField = createTextField("note", selectedTransaction.getNote());
        categoryBox = new JComboBox<>(tablesManager.getCatTableModel().getCategories().toArray());
        transactionType = new JComboBox<>(TransactionType.values());
        spinner = createDateSpinner();

        setTransactionDialog();
    }

    private void prepareColorPanel(JLabel colorLabel){
        colorLabel.setBackground(selectedCategory.getColor());
        colorLabel.setOpaque(true);
        colorLabel.setPreferredSize(new Dimension(40,20));
    }

    private void colorChooser(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(
                null,
                "Choose Background Color",
                selectedCategory.getColor());
        if (newColor != null) {
            categoryColorPanel.setBackground(newColor);
        }
    }

    private boolean checkCategoryExistence(String name, Color color){
        for (Category c : tablesManager.getCatTableModel().getCategories()){
            if (c.getName().equals(name) && !c.equals(selectedCategory)){
                messageDialog.showMessage("Category " + name + " already exists!",  JOptionPane.ERROR_MESSAGE);
                return false;
            } else if (c.getColor().equals(color) && !c.equals(selectedCategory)){
                messageDialog.showMessage("Chosen color is already taken by another category!",  JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private void setCategory(String name, Color color){
        selectedCategory.setColor(color);
        selectedCategory.setName(name);
    }

    private void createCategoryDialog(){
        selectedCategory = tablesManager.getCatTableModel().getEntity(tablesManager.getCatJTable().getSelectedRow());

        prepareColorPanel(categoryColorPanel);

        JDialog categoryDialog = createDialog("new category", 380, 150);

        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new FlowLayout());
        JButton setColorButton = new JButton("Show Color Chooser...");
        setColorButton.addActionListener(this::colorChooser);

        categoryDialog.add(categoryPanel);
        JTextField newCategoryName = new JTextField(selectedCategory.getName());
        newCategoryName.setPreferredSize(new Dimension(150, 30));

        if (selectedCategory.getName().equals("Others")){
            categoryPanel.add(new JLabel("You can't change name of the default category 'Others'."));
        } else {
            categoryPanel.add(new JLabel("Enter name of the new category:"));
            categoryPanel.add(newCategoryName);
        }

        categoryPanel.add(new JLabel("Color of the category:"));
        categoryPanel.add(setColorButton);
        categoryPanel.add(categoryColorPanel);

        JButton confirmButton = new JButton("Confirm");
        categoryPanel.add(confirmButton);
        confirmButton.addActionListener(e -> {
            if (newCategoryName.getText().equals("")){
                messageDialog.showMessage("Enter name of the category!", JOptionPane.ERROR_MESSAGE);

            } else if (checkCategoryExistence(newCategoryName.getText(), categoryColorPanel.getBackground())){
                setCategory(newCategoryName.getText(), categoryColorPanel.getBackground());
                tablesManager.getCatTableModel().updateEntity(selectedCategory);
                categoryDialog.dispose();
            }
        });
        categoryDialog.setLocationRelativeTo(frame);
        categoryDialog.setResizable(false);
        categoryDialog.setVisible(true);
    }

    private void editTransaction()  {
        createTransactionDialog();
    }

    private void editCategory() {
        createCategoryDialog();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int index = selectedTabIndex;
        if (index == 1) {
            editTransaction();
        } else if (index == 2) {
            editCategory();
        }
    }
}
