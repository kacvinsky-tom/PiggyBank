package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

class EditAction extends AbstractAction {

    private final EditCategory editCategory;
    private final EditTransaction editTransaction;
    private int selectedTabIndex = 0;

    public EditAction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super("Edit", Icons.EDIT_ICON);
        this.setEnabled(false);
        putValue(SHORT_DESCRIPTION, "Edits selected row");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
        this.editCategory = new EditCategory(frame, tablesManager, messageDialog);
        this.editTransaction = new EditTransaction(frame, tablesManager, messageDialog);
    }

    public void updateSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (selectedTabIndex <= 1) {
            this.editTransaction.start();
        } else if (selectedTabIndex == 2) {
            this.editCategory.start();
        }
    }

}
