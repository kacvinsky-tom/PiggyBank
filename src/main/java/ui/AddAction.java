package ui;

import model.Category;
import model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

final class AddAction extends AbstractAction {

    private final JTabbedPane pane;
    private final JLabel categoryColorPanel = new JLabel();
    private JDialog dialog;


    public AddAction(JTabbedPane pane) {
        super("Add", Icons.ADD_ICON);
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
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(dateModel);
        Calendar calendar = new GregorianCalendar();
        spinner.setValue(calendar.getTime());
        spinner.setVisible(true);
        return spinner;
    }

    private JDialog createDialog(String string, int width, int height) {
        dialog = new JDialog();
        dialog.setTitle("Add " + string);
        dialog.setSize(new Dimension(width, height));

        dialog.setModal(true);
        dialog.setAlwaysOnTop(true);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLocationRelativeTo(null);

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
        dialog = createDialog("new transaction", 270, 400);
        dialog.setLayout(new FlowLayout());

        JTable transactionsTable = getJTable(2);
        JTable categoriesTable = getJTable(3);

        var transactionTableModel = (TransactionsTable) transactionsTable.getModel();
        var categoriesTableModel = (CategoriesTable) categoriesTable.getModel();

        JTextField nameField = createTextfield("Name");
        JTextField amountField = createTextfield("Amount");
        JTextField noteField = createTextfield("Note");

        var combobox = new JComboBox<>(categoriesTableModel.getCategories().toArray());
        dialog.add(combobox);

        JSpinner spinner = createDateSpinner();
        dialog.add(spinner);

        JButton add = new JButton("Add");
        dialog.getContentPane().add(add);

        add.addActionListener(e -> {

            String name = nameField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String note = noteField.getText();
            Category category = categoriesTableModel.getCategories().get(combobox.getSelectedIndex());

            transactionTableModel.addTransaction(new Transaction(name, amount, category, LocalDate.now(), note));
            category.setExpenses(category.getExpenses() + amount);

            dialog.dispose();
        });
        dialog.setVisible(true);
    }

    private void createCategoryDialog(){
        JTable categoriesTable = getJTable(3);
        var categoriesTableModel = (CategoriesTable) categoriesTable.getModel();

        categoryColorPanel.setBackground(Color.BLACK);
        categoryColorPanel.setOpaque(true);
        categoryColorPanel.setPreferredSize(new Dimension(40,20));

        JDialog categoryDialog = createDialog("new category", 380, 150);

        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new FlowLayout());
        JButton setColorButton = new JButton("Show Color Chooser...");
        setColorButton.addActionListener(this::colorChooser);

        categoryDialog.add(categoryPanel);
        JTextField newCategoryName = new JTextField("");
        newCategoryName.setPreferredSize(new Dimension(150,30));

        categoryPanel.add(new JLabel("Enter name of the new category:"));
        categoryPanel.add(newCategoryName);

        categoryPanel.add(new JLabel("Color of the category:"));
        categoryPanel.add(setColorButton);
        categoryPanel.add(categoryColorPanel);

        JButton confirmButton = new JButton("Confirm");
        categoryPanel.add(confirmButton);
        confirmButton.addActionListener(e -> {

            String name = newCategoryName.getText();
            categoriesTableModel.addRow(new Category(name, Color.LIGHT_GRAY));
            categoryDialog.dispose();
        });

        categoryDialog.setVisible(true);
        categoryDialog.pack();
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
        if (index == 2) {
            addTransaction();
        } else if (index == 3) {
            addCategory();
        }
    }
}
