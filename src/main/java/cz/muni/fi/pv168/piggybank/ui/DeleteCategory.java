package cz.muni.fi.pv168.piggybank.ui;

import java.util.Arrays;
import java.util.Comparator;

public class DeleteCategory {
    private final TablesManager tablesManager;
    private final MessageDialog messageDialog;

    public DeleteCategory(TablesManager tablesManager, MessageDialog messageDialog){
        this.tablesManager = tablesManager;
        this.messageDialog = messageDialog;
    }

    public void delete() {
        String message = "Are you sure you want to delete following categories?\n";
        message += messageDialog.createItemsString(tablesManager.getCatJTable());
        if (!messageDialog.showConfirmMessage(message, "Delete")){
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
}
