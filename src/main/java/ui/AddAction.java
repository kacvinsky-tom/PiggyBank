package ui;

import data.CategoryDao;
import data.TransactionDao;
import model.Category;
import model.Transaction;
import model.TransactionType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

final class AddAction extends AbstractAction {

    private final JFrame frame;
    private final JTabbedPane pane;
    private final JLabel categoryColorPanel = new JLabel();
    private JTextField nameField, amountField, noteField;
    private JComboBox<Object> categoryBox, transactionType;
    private JDialog dialog;
    private JSpinner spinner;
    private CategoriesTable categoriesTableModel;

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
        button.addActionListener(this::addButtonActionPerformedTransaction);
        return button;
    }

    private void addButtonActionPerformedTransaction(ActionEvent actionEvent) {
        var categoriesTableModel = (CategoriesTable) getJTable(2).getModel();
        var transactionTableModel = (TransactionsTable) getJTable(1).getModel();

        double amount;
        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException ex){
            createWrongInputException();
            return;
        }
        Category category = categoriesTableModel.getCategories().get(categoryBox.getSelectedIndex());
        TransactionType type = (TransactionType) transactionType.getItemAt(transactionType.getSelectedIndex());
        Date date = (Date) spinner.getValue();

        Transaction newTransaction = new Transaction(nameField.getText(), amount, category, date, noteField.getText(), type);
        transactionTableModel.addTransaction(newTransaction);
        categoriesTableModel.updateCategory(category, newTransaction, true);
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
        JTable categoriesTable = getJTable(2);
        var categoriesTableModel = (CategoriesTable) categoriesTable.getModel();

        dialog = createDialog("transaction", 250, 330);
        nameField = createTextfield("Name");
        amountField = createTextfield("Amount");
        noteField = createTextfield("Note");
        categoryBox = new JComboBox<>(setDefaultCategoryBox(categoriesTableModel.getCategories()).toArray());
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
        for (Category c : categoriesTableModel.getCategories()){
            if (c.getName().equals(newCategory.getName())){
                JOptionPane.showMessageDialog(new JFrame(), "Category " + newCategory.getName() + " already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            } else if (c.getColor().equals(newCategory.getColor())){
                JOptionPane.showMessageDialog(new JFrame(), "Chosen color is already taken by another category!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private void createCategoryDialog(){
        JTable categoriesTable = getJTable(2);
        categoriesTableModel = (CategoriesTable) categoriesTable.getModel();

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
                JOptionPane.showMessageDialog(new JFrame(), "Enter name of the category!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Category newCategory = new Category(name, categoryColorPanel.getBackground());
            if (checkCategoryExistence(newCategory)){
                categoriesTableModel.addRow(newCategory);
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
        int index = pane.getSelectedIndex();
        if (index <= 1) {
            addTransaction();
        } else if (index == 2) {
            addCategory();
        }
    }
}
