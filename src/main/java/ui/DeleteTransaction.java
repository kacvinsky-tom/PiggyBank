package ui;

import java.util.Arrays;
import java.util.Comparator;

public class DeleteTransaction {
    private final TablesManager tablesManager;
    private final MessageDialog messageDialog;

    public DeleteTransaction(TablesManager tablesManager, MessageDialog messageDialog){
        this.tablesManager = tablesManager;
        this.messageDialog = messageDialog;
    }

    public void delete() {
        String message = "Are you sure you want to delete following transactions?\n";
        message += messageDialog.createItemsString(tablesManager.getCatJTable());
        if (!messageDialog.showConfirmMessage(message, "Delete")){
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
