package ui;

import javax.swing.*;

public abstract class AbstractAddEditTransaction extends AbstractAddEditAction {

    protected JTextField nameField, amountField, noteField;
    protected JComboBox<Object> categoryBox, transactionType;
    protected JSpinner spinner;
    private static final I18N I18N = new I18N(AbstractAddEditTransaction.class);

    protected AbstractAddEditTransaction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog){
        super(frame, tablesManager, messageDialog);
    }

    protected void createTransactionDialog(String buttonTitle){
        dialog.add(new JLabel(I18N.getString("categoryLabel")));
        dialog.add(categoryBox);
        dialog.add(new JLabel(I18N.getString("typeLabel")));
        dialog.add(transactionType);
        dialog.add(new JLabel(I18N.getString("dateLabel")));
        dialog.add(spinner);
        JButton button = createButton(buttonTitle);
        dialog.getContentPane().add(button);
        dialog.getRootPane().setDefaultButton(button);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

}
