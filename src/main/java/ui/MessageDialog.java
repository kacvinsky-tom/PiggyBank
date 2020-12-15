package ui;

import javax.swing.*;

public class MessageDialog {
    private final JFrame parentFrame;

    public MessageDialog(JFrame parentFrame){
        this.parentFrame = parentFrame;
    }

    public void showMessage(String message, int option){
        String title;
        if (option == JOptionPane.ERROR_MESSAGE){
            title = "Error";
        } else {
            title = "Alert";
        }
        JOptionPane.showMessageDialog(parentFrame, message, title, option);
    }
}
