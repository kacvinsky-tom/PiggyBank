package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

final class AddAction extends AbstractAction {

    private final JTabbedPane pane;

    public AddAction(JTabbedPane pane) {
        super("Add", Icons.ADD_ICON);
        this.pane = pane;
        putValue(SHORT_DESCRIPTION, "Adds new row");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    private void createTransactionFrame(){
        JDialog dialog = new JDialog ();
        dialog.setPreferredSize(new Dimension(300, 150));
        dialog.setModal (true);
        dialog.setAlwaysOnTop (true);
        dialog.setModalityType (Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void createCategoryFrame(){
        JDialog dialog = new JDialog ();
        dialog.setPreferredSize(new Dimension(300, 150));
        dialog.setModal (true);
        dialog.setAlwaysOnTop (true);
        dialog.setModalityType (Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setVisible(true);

    }

    private void addTransaction(){
        createTransactionFrame();
    }

    private void addCategory(){
        createCategoryFrame();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int index = pane.getSelectedIndex();
        if (index == 2){
            addTransaction();
        } else if (index == 3) {
            addCategory();
        }
    }
}
