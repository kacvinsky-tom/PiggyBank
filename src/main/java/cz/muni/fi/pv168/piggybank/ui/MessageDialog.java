package cz.muni.fi.pv168.piggybank.ui;

import javax.swing.*;

public class MessageDialog {
    private final JFrame parentFrame;

    public MessageDialog(JFrame parentFrame){
        this.parentFrame = parentFrame;
    }

    public void showAlertMessage(String message){
        JOptionPane.showMessageDialog(parentFrame, message, "Alert", JOptionPane.WARNING_MESSAGE);
    }

    public void showErrorMessage(String message){
        JOptionPane.showMessageDialog(parentFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean showConfirmMessage(String message, String title){
        return JOptionPane.showConfirmDialog(parentFrame, message, title, JOptionPane.OK_CANCEL_OPTION) == 0;
    }

    public String createItemsString(JTable table){
        String message = "";
        int count = 1;
        for (int i : table.getSelectedRows()){
            message += "\n        " + count + ". " + table.getValueAt(i, 0).toString();
            ++count;
        }
        return message;
    }
}
