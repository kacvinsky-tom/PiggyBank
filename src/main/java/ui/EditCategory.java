package ui;

import model.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditCategory extends AbstractAddEditCategory {

    private Category selectedCategory;

    public EditCategory(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super(frame, tablesManager, messageDialog);
    }

    private void setCategory(String name, Color color) {
        selectedCategory.setColor(color);
        selectedCategory.setName(name);
    }

    public void edit() {
        selectedCategory = tablesManager.getCatTableModel().getEntity(tablesManager.getCatJTable().getSelectedRow());
        prepareColorPanel(selectedCategory.getColor());
        dialog = createDialog("Edit category", 270, 150);
        nameField = createTextField("Name: ", selectedCategory.getName(), 18);

        if (selectedCategory.getName().equals("Others")) {
            nameField.setEnabled(false);
            nameField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    messageDialog.showAlertMessage("Name of the default category 'Others' is not editable!");
                }
            });
        }

        createCategoryDialog("Save");
    }

    @Override
    protected void buttonActionPerformed(ActionEvent actionEvent) {
        if (nameField.getText().equals("")){
            messageDialog.showErrorMessage("Enter name of the category!");

        } else if (!checkCategoryExistence(new Category(nameField.getText(), categoryColorPanel.getBackground()), false, selectedCategory)){
            setCategory(nameField.getText(), categoryColorPanel.getBackground());
            tablesManager.getCatTableModel().updateEntity(selectedCategory);
            tablesManager.getStatTableModel().update();
            dialog.dispose();
        }

    }
}
