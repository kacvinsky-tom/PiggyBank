package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

class AddAction extends AbstractAddEditAction {

    public AddAction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super(frame, tablesManager, messageDialog,"Add", Icons.ADD_ICON);
        putValue(SHORT_DESCRIPTION, "Adds new row");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int index = selectedTabIndex;
        if (index <= 1) {
            new AddTransaction(frame, tablesManager, messageDialog);
        } else if (index == 2) {
            new AddCategory(frame, tablesManager, messageDialog);
        }
    }
}
