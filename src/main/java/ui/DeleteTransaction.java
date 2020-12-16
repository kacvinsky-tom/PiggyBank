package ui;

import java.util.Arrays;
import java.util.Comparator;

public class DeleteTransaction extends AbstractDeleteAction {

    public DeleteTransaction(TablesManager tablesManager, MessageDialog messageDialog){
        super(tablesManager, messageDialog);
    }

    public void delete() {
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
