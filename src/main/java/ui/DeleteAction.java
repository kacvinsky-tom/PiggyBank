package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

final class DeleteAction extends AbstractAction {
    private final TablesManager tablesManager;
    private int selectedTabIndex = 0;
    private final MessageDialog messageDialog;

    public DeleteAction(TablesManager tablesManager, MessageDialog messageDialog) {
        super("Delete", Icons.DELETE_ICON);
        this.tablesManager = tablesManager;
        this.messageDialog = messageDialog;
        this.setEnabled(false);
        putValue(SHORT_DESCRIPTION, "Deletes selected rows");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    public void updateSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int index = selectedTabIndex;
        if (index == 1) {
            deleteTransaction();
        } else if (index == 2) {
            deleteCategory();
        }
    }

    private void deleteCategory() {
        if (!messageDialog.showConfirmMessage(createDialogString(tablesManager.getCatJTable()), "Delete")){
            return;
        }
        Arrays.stream(tablesManager.getCatJTable().getSelectedRows())
                .map(tablesManager.getCatJTable()::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(e -> {
                    tablesManager.getTranTableModel().changeCategoryToDefault(e);
                    if (!tablesManager.getCatTableModel().deleteRow(e)){
                        messageDialog.showErrorMessage("You can't delete default category 'Others'!");
                    }
                });
        tablesManager.getStatTableModel().update();
    }

    private String createDialogString(JTable table){
        String message = "Are you sure you want to delete following items?\n";
        int count = 1;
        for (int i : table.getSelectedRows()){
            message += "\n       " + count + ". " + table.getValueAt(i, 0).toString();
            ++count;
        }
        return message;
    }

    private void deleteTransaction() {
        if (!messageDialog.showConfirmMessage(createDialogString(tablesManager.getTranJTable()), "Delete")){
            return;
        }
        Arrays.stream(tablesManager.getTranJTable().getSelectedRows())
                .map(tablesManager.getTranJTable()::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(tablesManager.getTranTableModel()::deleteRow);
        tablesManager.getStatTableModel().update();
        tablesManager.getStatBalTableModel().update();
    }
}
