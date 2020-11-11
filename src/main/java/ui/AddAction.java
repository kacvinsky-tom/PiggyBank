package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

final class AddAction extends AbstractAction {


    public AddAction() {
        super("Add", null);
        putValue(SHORT_DESCRIPTION, "Adds new row");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
