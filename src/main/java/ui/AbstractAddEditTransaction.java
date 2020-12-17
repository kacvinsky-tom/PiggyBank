package ui;

import javax.swing.*;

public abstract class AbstractAddEditTransaction extends AbstractAddEditAction {

    protected JTextField nameField, amountField, noteField;
    protected JComboBox<Object> categoryBox, transactionType;
    protected JSpinner spinner;

    protected AbstractAddEditTransaction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog){
        super(frame, tablesManager, messageDialog);
    }

    protected void createTransactionDialog(String buttonTitle){
        dialog.add(new JLabel("Select category: "));
        dialog.add(categoryBox);
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
