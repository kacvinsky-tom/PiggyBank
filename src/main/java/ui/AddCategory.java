package ui;

import model.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AddCategory extends AddAction {

    private final JLabel categoryColorPanel = new JLabel();
    private JTextField nameField;

    public AddCategory(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super(frame, tablesManager, messageDialog);
        createCategoryDialog();
    }

    private void prepareColorPanel(JLabel colorLabel) {
        colorLabel.setBackground(Color.BLACK);
        colorLabel.setOpaque(true);
        colorLabel.setPreferredSize(new Dimension(40, 20));
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

    private void colorChooser(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(
                null,
                "Choose Background Color",
                Color.BLACK);
        if (newColor != null) {
            categoryColorPanel.setBackground(newColor);
        }
    }

    private void createCategoryDialog() {
        dialog = createDialog("new category", 380, 150);
        dialog.setLayout(new FlowLayout());

        prepareColorPanel(categoryColorPanel);
        JButton setColorButton = new JButton("Show Color Chooser...");
        setColorButton.addActionListener(this::colorChooser);
        nameField = createTextField("Enter name of the new category", "", 11);

        dialog.add(new JLabel("Color of the category:"));
        dialog.add(setColorButton);
        dialog.add(categoryColorPanel);
        dialog.add(createButton("Add"));

        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    @Override
    protected void buttonActionPerformed(ActionEvent actionEvent) {
        String name = nameField.getText();
        if (name.equals("")) {
            messageDialog.showErrorMessage("Enter name of the category!");
            return;
        }
        Category newCategory = new Category(name, categoryColorPanel.getBackground());
        if (checkCategoryExistence(newCategory)) {
            tablesManager.getCatTableModel().addRow(newCategory);
            dialog.dispose();
        }
    }
}
