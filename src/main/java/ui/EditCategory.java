package ui;

import model.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditCategory extends EditAction{

    private final JLabel categoryColorPanel = new JLabel();
    private JTextField nameField;
    private Category selectedCategory;

    public EditCategory(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super(frame, tablesManager, messageDialog);
        createCategoryDialog();
    }

    private void prepareColorPanel(JLabel colorLabel) {
        colorLabel.setBackground(selectedCategory.getColor());
        colorLabel.setOpaque(true);
        colorLabel.setPreferredSize(new Dimension(40, 20));
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
                messageDialog.showErrorMessage("Category " + name + " already exists!");
                return false;
            } else if (c.getColor().equals(color) && !c.equals(selectedCategory)){
                messageDialog.showErrorMessage("Chosen color is already taken by another category!");
                return false;
            }
        }
        return true;
    }

    private void setCategory(String name, Color color) {
        selectedCategory.setColor(color);
        selectedCategory.setName(name);
    }

    private void createCategoryDialog() {
        selectedCategory = tablesManager.getCatTableModel().getEntity(tablesManager.getCatJTable().getSelectedRow());
        prepareColorPanel(categoryColorPanel);

        dialog = createDialog("Edit category", 380, 150);
        dialog.setLayout(new FlowLayout());
        JButton setColorButton = new JButton("Show Color Chooser...");
        setColorButton.addActionListener(this::colorChooser);

        nameField = createTextField("Edit name:", selectedCategory.getName(), 11);
        if (selectedCategory.getName().equals("Others")) {
            nameField.setEditable(false);
        }
        dialog.add(new JLabel("Color of the category:"));
        dialog.add(setColorButton);
        dialog.add(categoryColorPanel);
        dialog.add(createButton("Confirm"));

        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    @Override
    protected void buttonActionPerformed(ActionEvent actionEvent) {
        if (nameField.getText().equals("")){
            messageDialog.showErrorMessage("Enter name of the category!");

        } else if (checkCategoryExistence(nameField.getText(), categoryColorPanel.getBackground())){
            setCategory(nameField.getText(), categoryColorPanel.getBackground());
            tablesManager.getCatTableModel().updateEntity(selectedCategory);
            dialog.dispose();
        }

    }
}
