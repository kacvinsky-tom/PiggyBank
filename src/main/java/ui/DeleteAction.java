package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

final class DeleteAction extends AbstractAction {
    private final TablesManager tablesManager;
    private int selectedTabIndex = 0;

    public DeleteAction(TablesManager tablesManager) {
        super("Delete", Icons.DELETE_ICON);
        this.tablesManager = tablesManager;
        this.setEnabled(false);
        putValue(SHORT_DESCRIPTION, "Deletes selected rows");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    public void setSelectedTabIndex(int selectedTabIndex) {
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
        Arrays.stream(tablesManager.getCatJTable().getSelectedRows())
                .map(tablesManager.getCatJTable()::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(e -> { tablesManager.getTranTableModel().changeCategoryToDefault(e);
                                tablesManager.getCatTableModel().deleteRow(e);});
    }

    private void deleteTransaction() {
        Arrays.stream(tablesManager.getTranJTable().getSelectedRows())
                .map(tablesManager.getTranJTable()::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(tablesManager.getTranTableModel()::deleteRow);
    }
}
