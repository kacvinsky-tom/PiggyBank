package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

final class QuitAction extends AbstractAction {

    QuitAction() {
        super("Quit", null);
        putValue(SHORT_DESCRIPTION, "Terminates the application");
        putValue(MNEMONIC_KEY, KeyEvent.VK_Q);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
