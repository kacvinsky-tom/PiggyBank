package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

final class DeleteAction extends AbstractAction {

    private final JTabbedPane pane;

    public DeleteAction(JTabbedPane pane) {
        super("Delete", null);
        this.pane = pane;
        this.setEnabled(false);
        putValue(SHORT_DESCRIPTION, "Deletes selected rows");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        var employeeTableModel = (EmployeeTableModel) employeeTable.getModel();
//        Arrays.stream(employeeTable.getSelectedRows())
//                // view row index must be converted to model row index
//                .map(employeeTable::convertRowIndexToModel)
//                .boxed()
//                // We need to delete rows in descending order to not change index of rows
//                // which are not deleted yet
//                .sorted(Comparator.reverseOrder())
//                .forEach(employeeTableModel::deleteRow);
    }
}
