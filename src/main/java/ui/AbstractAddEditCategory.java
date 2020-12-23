package ui;

import model.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public abstract class AbstractAddEditCategory extends AbstractAddEditAction {

    protected final JLabel categoryColorPanel = new JLabel();
    protected JTextField nameField;
    protected Color preselectedColor;

    protected AbstractAddEditCategory(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog){
        super(frame, tablesManager, messageDialog);
    }

    protected void createCategoryDialog(String buttonTitle){
        JButton setColorButton = new JButton("Show Color Chooser...");
        setColorButton.addActionListener(this::colorChooser);
        dialog.add(new JLabel("Color: "));
        dialog.add(setColorButton);
        dialog.add(categoryColorPanel);
        JButton button = createButton(buttonTitle);
        dialog.add(button);
        dialog.getRootPane().setDefaultButton(button);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    protected boolean checkCategoryExistence(Category newCategory, Category except){
        for (Category c : tablesManager.getCatTableModel().getCategories()){
            if (except != null){
                if (except.getColor().equals(c.getColor()) && except.getName().equals(c.getName())){
                    continue;
                }
            }
            if (c.getName().equals(newCategory.getName())){
                messageDialog.showErrorMessage("Category " + newCategory.getName() + " already exists!");
                return true;

            } else if (c.getColor().equals(newCategory.getColor())){
                messageDialog.showErrorMessage("Chosen color is already taken by another category!");
                return true;
            }
        }
        return false;
    }

    protected void colorChooser(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(
                null,
                "Choose Background Color",
                preselectedColor);
        if (newColor != null) {
            categoryColorPanel.setBackground(newColor);
        }
    }

    protected void prepareColorPanel(Color color) {
        categoryColorPanel.setBackground(color);
        categoryColorPanel.setOpaque(true);
        categoryColorPanel.setPreferredSize(new Dimension(40, 20));
    }

}