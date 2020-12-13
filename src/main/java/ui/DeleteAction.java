package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

final class DeleteAction extends AbstractAction {

    private final JTabbedPane pane;

    public DeleteAction(JTabbedPane pane) {
        super("Delete", Icons.DELETE_ICON);
        this.pane = pane;
        this.setEnabled(false);
        putValue(SHORT_DESCRIPTION, "Deletes selected rows");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    private JTable getJTable(int idx) {
        JScrollPane scrollPane = (JScrollPane) pane.getComponentAt(idx);
        JViewport viewport = scrollPane.getViewport();
        return (JTable) viewport.getView();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int index = pane.getSelectedIndex();
        if (index == 1) {
            deleteTransaction();
        } else if (index == 2) {
            deleteCategory();
        }
    }

    private void deleteCategory() {
        var categoriesTable =  getJTable(2);
        var categoriesTableModel = (CategoriesTable) categoriesTable.getModel();
        var transactionsTable =  getJTable(1);
        var transactionsTableModel = (TransactionsTable) transactionsTable.getModel();
        Arrays.stream(categoriesTable.getSelectedRows())
                .map(categoriesTable::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(e -> {transactionsTableModel.changeCategoryToDefault(e);categoriesTableModel.deleteRow(e);});
    }

    private void deleteTransaction() {
        var transactionsTable =  getJTable(1);
        var categoriesTable =  getJTable(2);
        var transactionsTableModel = (TransactionsTable) transactionsTable.getModel();
        var categoriesTableModel = (CategoriesTable) categoriesTable.getModel();
        Arrays.stream(transactionsTable.getSelectedRows())
                .map(transactionsTable::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(transactionsTableModel::deleteRow);
    }
}
