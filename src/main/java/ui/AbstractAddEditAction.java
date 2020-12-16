package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public abstract class AbstractAddEditAction extends AbstractAction {

    protected final JFrame frame;
    protected final TablesManager tablesManager;
    protected final MessageDialog messageDialog;
    protected JDialog dialog;
    protected int selectedTabIndex = 0;

    public AbstractAddEditAction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog, String string, Icon icon) {
        super(string, icon);
        this.frame = frame;
        this.tablesManager = tablesManager;
        this.messageDialog = messageDialog;
    }

    protected void updateSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }

    protected void createErrorDialog(String message){
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected JDialog createDialog(String string, int width, int height) {
        dialog = new JDialog();
        dialog.setTitle(string);
        dialog.setSize(new Dimension(width, height));
        dialog.setModal(true);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        return dialog;
    }

    protected JTextField createTextField(String label, String content, int cols) {
        dialog.add(new JLabel(label));
        JTextField textField = new JTextField(content, cols);
        textField.setSize(new Dimension(150, 30));
        dialog.add(textField);
        return textField;
    }
    protected JButton createButton(String string) {
        JButton button = new JButton(string);
        button.addActionListener(this::buttonActionPerformed);
        return button;
    }

    protected void buttonActionPerformed(ActionEvent actionEvent) {
    }

}
