package ui;

import javax.swing.*;

public abstract class AbstractDeleteAction {
    protected final TablesManager tablesManager;
    protected final MessageDialog messageDialog;

    protected AbstractDeleteAction(TablesManager tablesManager, MessageDialog messageDialog){
        this.tablesManager = tablesManager;
        this.messageDialog = messageDialog;
    }
    protected String createDialogString(JTable table){
        String message = "Are you sure you want to delete following items?\n";
        int count = 1;
        for (int i : table.getSelectedRows()){
            message += "\n        " + count + ". " + table.getValueAt(i, 0).toString();
            ++count;
        }
        return message;
    }
}
