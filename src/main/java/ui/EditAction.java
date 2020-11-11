package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

final class EditAction extends AbstractAction {

    private final JTabbedPane pane;

    public EditAction(JTabbedPane pane) {
        super("Edit", Icons.EDIT_ICON);
        this.pane = pane;
        this.setEnabled(false);
        putValue(SHORT_DESCRIPTION, "Edits selected row");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = ((JTable) pane.getSelectedComponent()).getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows.length);
        }
        JOptionPane.showMessageDialog(pane,
                "This operation is not implemented yet",
                "Warning", JOptionPane.WARNING_MESSAGE);
    }
}
