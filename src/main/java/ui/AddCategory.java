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

    private boolean checkColorExistence(Color color){
        for (Category c : tablesManager.getCatTableModel().getCategories()){
            if (c.getColor().equals(color)){
                return true;
            }
        }
        return false;
    }

    private Color generateRandomColor() {
        Color color;
        do {
            color = new Color(getRandomNumberInRGBRange(), getRandomNumberInRGBRange(), getRandomNumberInRGBRange());
        } while (checkColorExistence(color));
        return color;
    }

    public void add() {
        prepareColorPanel(generateRandomColor());
        dialog = createDialog("Add category", 270, 150);
        nameField = createTextField("Name: ", "", 17);
        createCategoryDialog("Add");
    }

    @Override
    protected void buttonActionPerformed(ActionEvent actionEvent) {
        if (nameField.getText().equals("")) {
            messageDialog.showErrorMessage("Enter name of the category!");
            return;
        }
        Category newCategory = new Category(nameField.getText(), categoryColorPanel.getBackground());
        if (!checkCategoryExistence(newCategory, null)) {
            tablesManager.getCatTableModel().addRow(newCategory);
            tablesManager.getStatTableModel().update();
            dialog.dispose();
        }
    }
}
