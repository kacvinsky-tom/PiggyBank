package ui;

import java.util.Arrays;
import java.util.Comparator;

public class DeleteCategory extends AbstractDeleteAction {

    public DeleteCategory(TablesManager tablesManager, MessageDialog messageDialog){
        super(tablesManager, messageDialog);
    }

    public void delete() {
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
}
