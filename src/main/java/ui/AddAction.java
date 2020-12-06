package ui;

import model.Category;
import model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

final class AddAction extends AbstractAction {

    private final JFrame frame;
    private final JTabbedPane pane;
    private final JLabel categoryColorPanel = new JLabel();
    private JDialog dialog;


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
        dialog.add(new JLabel("Choose date: "));
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
            double amount = Double.parseDouble(amountField.getText());
            String note = noteField.getText();
            Category category = categoriesTableModel.getCategories().get(categoryBox.getSelectedIndex());
            TransactionType type = transactionType.getItemAt(transactionType.getSelectedIndex());
            Date date = (Date) spinner.getValue();

            transactionTableModel.addTransaction(new Transaction(name, amount, category, date, note, type));

            category.setExpenses(category.getExpenses() + amount);

            dialog.dispose();
        });
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void prepareColorPanel(JLabel colorLabel){
        colorLabel.setBackground(Color.BLACK);
        colorLabel.setOpaque(true);
        colorLabel.setPreferredSize(new Dimension(40,20));
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

            String name = newCategoryName.getText();
            categoriesTableModel.addRow(new Category(name, categoryColorPanel.getBackground()));
            categoryDialog.dispose();
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
