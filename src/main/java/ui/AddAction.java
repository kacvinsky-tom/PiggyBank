package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

class AddAction extends AbstractAction {

    private final AddTransaction addTransaction;
    private final AddCategory addCategory;
    private int selectedTabIndex = 0;

    public AddAction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super("Add", Icons.ADD_ICON);
        putValue(SHORT_DESCRIPTION, "Adds new row");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
        this.addTransaction = new AddTransaction(frame, tablesManager, messageDialog);
        this.addCategory = new AddCategory(frame, tablesManager, messageDialog);
    }

    public void updateSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (selectedTabIndex <= 1) {
            this.addTransaction.add();
        } else if (selectedTabIndex == 2) {
            this.addCategory.add();
        }
    }
}
