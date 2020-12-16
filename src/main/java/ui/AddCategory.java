package ui;

import model.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AddCategory extends AbstractAddEditCategory {

    public AddCategory(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super(frame, tablesManager, messageDialog);
    }

    private int getRandomNumberInRGBRange(){
        return (int) ((Math.random() * 255));
    }

    protected Color generateRandomColor() {
        Color color;
        do {
            color = new Color(getRandomNumberInRGBRange(), getRandomNumberInRGBRange(), getRandomNumberInRGBRange());
        } while (checkCategoryExistence(new Category("", color), true, null));
        return color;
    }

    public void start() {
        prepareColorPanel(generateRandomColor());
        dialog = createDialog("Add category", 340, 150);
        nameField = createTextField("Enter name of the new category", "", 11);
        createCategoryDialog("Add");
    }

    @Override
    protected void buttonActionPerformed(ActionEvent actionEvent) {
        if (nameField.getText().equals("")) {
            messageDialog.showErrorMessage("Enter name of the category!");
        }
        Category newCategory = new Category(nameField.getText(), categoryColorPanel.getBackground());
        if (!checkCategoryExistence(newCategory, false, null)) {
            tablesManager.getCatTableModel().addRow(newCategory);
            tablesManager.getStatTableModel().update();
            dialog.dispose();
        }
    }
}
