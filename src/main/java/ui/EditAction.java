package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

class EditAction extends AbstractAddEditAction {

    public EditAction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super(frame, tablesManager, messageDialog, "Edit", Icons.EDIT_ICON);
        this.setEnabled(false);
        putValue(SHORT_DESCRIPTION, "Edits selected row");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int index = selectedTabIndex;
        if (index <= 1) {
            new EditTransaction(frame, tablesManager, messageDialog);
        } else if (index == 2) {
            new EditCategory(frame, tablesManager, messageDialog);
        }
    }

}
