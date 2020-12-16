package ui;

import model.Category;
import model.Transaction;
import enums.TransactionType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

final class AddAction extends AbstractAction {

    private final JFrame frame;
    private final TablesManager tablesManager;
    private final JLabel categoryColorPanel = new JLabel();
    private JTextField nameField, amountField, noteField;
    private JComboBox<Object> categoryBox, transactionType;
    private JDialog dialog;
    private JSpinner spinner;
    private int selectedTabIndex = 0;
    private final MessageDialog messageDialog;

    public AddAction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super("Add", Icons.ADD_ICON);
        this.frame = frame;
        this.tablesManager = tablesManager;
        this.messageDialog = messageDialog;
        putValue(SHORT_DESCRIPTION, "Adds new row");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    public void updateSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
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
        button.addActionListener(this::addButtonActionPerformedTransaction);
        return button;
    }

    private void addButtonActionPerformedTransaction(ActionEvent actionEvent) {
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountField.getText()).abs();
        } catch (NumberFormatException ex){
            messageDialog.showErrorMessage("Enter valid number into amount!");
            return;
        }
        Category category = tablesManager.getCatTableModel().getCategories().get(categoryBox.getSelectedIndex());
        TransactionType type = (TransactionType) transactionType.getItemAt(transactionType.getSelectedIndex());
        Date date = (Date) spinner.getValue();

        Transaction newTransaction = new Transaction(nameField.getText(), amount, category, date, noteField.getText(), type);
        tablesManager.getTranTableModel().addTransaction(newTransaction);
        dialog.dispose();
    }

    private List<Category> setDefaultCategoryBox(List<Category> list){
        List<Category> newList = new ArrayList<>();
        for (Category c : list){
            if (c.getName().equals("Others")){
                newList.add(0, c);
            } else {
                newList.add(c);
            }
        }
        return newList;
    }

    private void createTransactionDialog() {
        dialog = createDialog("transaction", 250, 330);
        nameField = createTextfield("Name");
        amountField = createTextfield("Amount");
        noteField = createTextfield("Note");
        categoryBox = new JComboBox<>(setDefaultCategoryBox(tablesManager.getCatTableModel().getCategories()).toArray());
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

    private boolean checkCategoryExistence(Category newCategory){
        for (Category c : tablesManager.getCatTableModel().getCategories()){
            if (c.getName().equals(newCategory.getName())){
                messageDialog.showErrorMessage("Category " + newCategory.getName() + " already exists!");
                return false;

            } else if (c.getColor().equals(newCategory.getColor())){
                messageDialog.showErrorMessage("Chosen color is already taken by another category!");
                return false;
            }
        }
        return true;
    }

    private void createCategoryDialog(){
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
            String name = newCategoryName.getText();
            if (name.equals("")){
                messageDialog.showErrorMessage("Enter name of the category!");
                return;
            }
            Category newCategory = new Category(name, categoryColorPanel.getBackground());
            if (checkCategoryExistence(newCategory)){
                tablesManager.getCatTableModel().addRow(newCategory);
                categoryDialog.dispose();
            }
        });
        categoryDialog.setLocationRelativeTo(frame);
        categoryDialog.setResizable(false);
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
        int index = selectedTabIndex;
        if (index <= 1) {
            addTransaction();
        } else if (index == 2) {
            addCategory();
        }
    }
}
