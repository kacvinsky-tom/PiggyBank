package ui;

import model.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAddEditTransaction extends AbstractAddEditAction {

    protected JTextField nameField, amountField, noteField;
    protected JComboBox<Object> transactionType;
    protected JDialog categoriesDialog;
    protected JSpinner spinner;
    protected JButton selectCatButton = new JButton("Select...");
    protected JButton confirmButton = new JButton("Save");
    protected JPanel checkBoxPanel;

    protected AbstractAddEditTransaction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog){
        super(frame, tablesManager, messageDialog);
        selectCatButton.addActionListener(this::createCheckBoxDialog);
        confirmButton.addActionListener(this::disposeDialog);
    }

    protected void disposeDialog(ActionEvent e){
        categoriesDialog.dispose();
    }

    protected java.util.List<Category> getCategoriesFromCheckBoxes(){
        List<Category> cats = new ArrayList<>();
        for (Component c : checkBoxPanel.getComponents()) {
            if (c instanceof JCheckBox) {
                cats.add(new Category(c.getName()));
            }
        }
        return cats;
    }

    protected void createCheckBoxDialog(ActionEvent e){
        categoriesDialog = new JDialog();
        categoriesDialog.setTitle("Select categories");
        categoriesDialog.setSize(new Dimension(170, 180));
        categoriesDialog.setLayout(new FlowLayout());
        categoriesDialog.setModal(true);
        categoriesDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        categoriesDialog.setLocationRelativeTo(dialog);
        JScrollPane pane = new JScrollPane(checkBoxPanel);
        pane.setPreferredSize(new Dimension(150, 100));
        categoriesDialog.add(pane);
        categoriesDialog.add(confirmButton);
        dialog.getRootPane().setDefaultButton(confirmButton);
        categoriesDialog.getContentPane().add(confirmButton);
        categoriesDialog.setResizable(false);
        categoriesDialog.setVisible(true);
    }

    protected void createTransactionDialog(String buttonTitle){
        dialog.add(new JLabel("Select category: "));
        dialog.add(selectCatButton);
        dialog.add(new JLabel("Select type: "));
        dialog.add(transactionType);
        dialog.add(new JLabel("Select date: "));
        dialog.add(spinner);
        JButton button = createButton(buttonTitle);
        dialog.getContentPane().add(button);
        dialog.getRootPane().setDefaultButton(button);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

}
