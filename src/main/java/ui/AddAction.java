package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

final class AddAction extends AbstractAction {

    private final JTabbedPane pane;

    public AddAction(JTabbedPane pane) {
        super("Add", Icons.ADD_ICON);
        this.pane = pane;
        putValue(SHORT_DESCRIPTION, "Adds new row");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    private JSpinner createDateSpinner(){
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(dateModel);
        Calendar calendar = new GregorianCalendar();
        spinner.setValue(calendar.getTime());
        spinner.setVisible(true);
        return spinner;
    }



    private void createTransactionDialog(){
        JDialog dialog = new JDialog ();
        dialog.setTitle("Add Transaction");
        dialog.setPreferredSize(new Dimension(500, 150));
        dialog.setLayout(new FlowLayout());

        dialog.setModal (true);
        dialog.setAlwaysOnTop (true);
        dialog.setModalityType (Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLocationRelativeTo(null);

        dialog.add(new JLabel("Name: "));
        JTextField nameField = new JTextField();
        nameField.setColumns(20);
        nameField.setSize(new Dimension(150,20));
        dialog.add(nameField);

        dialog.add(new JLabel("Amount: "));
        JTextField amountField = new JTextField();
        amountField.setColumns(8);
        amountField.setSize(new Dimension(150,20));
        dialog.add(amountField);

        dialog.add(new JLabel("Category: "));


        dialog.add(new JLabel("Choose date: "));
        dialog.add(createDateSpinner());

        dialog.pack();
        dialog.setVisible(true);
    }

    private void createCategoryDialog(){
        JDialog dialog = new JDialog ();
        dialog.setSize(new Dimension(500,200));
        dialog.setModal (true);
        dialog.setAlwaysOnTop (true);
        dialog.setModalityType (Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLocationRelativeTo(null);
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        dialog.add(p);
        JLabel l = new JLabel("Enter name of the new category:");
        JTextField l2 = new JTextField("");
        l2.setColumns(3);
        l2.setSize(new Dimension(150,20));
        p.add(l);
        p.add(l2);
        dialog.setVisible(true);
        dialog.pack();
    }

    private void addTransaction(){
        createTransactionDialog();
    }

    private void addCategory(){
        createCategoryDialog();

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
