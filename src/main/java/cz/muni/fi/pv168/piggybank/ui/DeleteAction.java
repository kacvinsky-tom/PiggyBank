package cz.muni.fi.pv168.piggybank.ui;

import cz.muni.fi.pv168.piggybank.enums.TableType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

final class DeleteAction extends AbstractAction {
    private final DeleteCategory deleteCategory;
    private final DeleteTransaction deleteTransaction;
    private int selectedTabIndex = 0;

    public DeleteAction(TablesManager tablesManager, MessageDialog messageDialog) {
        super("Delete", Icons.DELETE_ICON);
        this.setEnabled(false);
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
        this.deleteCategory = new DeleteCategory(tablesManager, messageDialog);
        this.deleteTransaction = new DeleteTransaction(tablesManager, messageDialog);
    }

    public void updateSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
        if (selectedTabIndex == TableType.TRANSACTIONS.ordinal()){
            putValue(SHORT_DESCRIPTION, "Deletes selected transactions");
        } else if (selectedTabIndex == TableType.STATISTICS.ordinal()){
            putValue(SHORT_DESCRIPTION, null);
        } else {
            putValue(SHORT_DESCRIPTION, "Deletes selected categories");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (selectedTabIndex == 1) {
            deleteTransaction.delete();
        } else if (selectedTabIndex == 2) {
            deleteCategory.delete();
        }
    }
}
