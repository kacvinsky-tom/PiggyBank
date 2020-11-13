package ui;

import model.Category;
import model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

final class AddAction extends AbstractAction {

    private final JTabbedPane pane;
    private JLabel categoryColorPanel = new JLabel();
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
        dialog.setPreferredSize(new Dimension(width, height));
        dialog.setLayout(new FlowLayout());

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

    private ListSelectionModel createCategorySelector(JTable categoriesSelectionTable) {
        dialog.add(new JLabel("Category: "));
        categoriesSelectionTable.setPreferredScrollableViewportSize(new Dimension(100, 100));
        categoriesSelectionTable.setFillsViewportHeight(true);
        categoriesSelectionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dialog.add(categoriesSelectionTable);
        return categoriesSelectionTable.getSelectionModel();
    }


    private void createTransactionDialog() {
        dialog = createDialog("Transaction", 270, 400);

        JTable transactionsTable = getJTable(2);
        JTable categoriesSelectionTable = new JTable(getJTable(3).getModel());
        var transactionTableModel = (TransactionsTable) transactionsTable.getModel();
        var categoriesTableModel = (CategoriesTable) categoriesSelectionTable.getModel();

        JTextField nameField = createTextfield("Name");
        JTextField amountField = createTextfield("Amount");
        JTextField noteField = createTextfield("Note");

        ListSelectionModel rowSM = createCategorySelector(categoriesSelectionTable);
        JSpinner spinner = createDateSpinner();
        dialog.add(spinner);

        JButton add = new JButton("Add");
        dialog.getContentPane().add(add);

        add.addActionListener(e -> {

            String name = nameField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String note = noteField.getText();
            int selectedRow = rowSM.getMinSelectionIndex();
            Category category = categoriesTableModel.getCategories().get(selectedRow);

            transactionTableModel.addTransaction(new Transaction(name, amount, category, LocalDate.now(), note));
            category.setExpenses(category.getExpenses() + amount);

            dialog.dispose();
        });
        dialog.pack();
        dialog.setVisible(true);
    }

    private void createCategoryDialog(){
        JTable categoriesTable = getJTable(3);
        var categoriesTableModel = (CategoriesTable) categoriesTable.getModel();

        categoryColorPanel.setBackground(Color.BLACK);
        categoryColorPanel.setOpaque(true);
        categoryColorPanel.setPreferredSize(new Dimension(40,20));

        JDialog categoryDialog = new JDialog ();
        categoryDialog.setSize(new Dimension(380,200));
        categoryDialog.setModal (true);
        categoryDialog.setModalityType (Dialog.ModalityType.APPLICATION_MODAL);
        categoryDialog.setLocationRelativeTo(null);

        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new FlowLayout());
        categoryPanel.setBorder(BorderFactory.createTitledBorder("Add new category"));

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
            categoriesTableModel.addRow(new Category(name));
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
