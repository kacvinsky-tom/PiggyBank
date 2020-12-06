package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateAction extends AbstractAction {

    private final JFrame frame;
    private final JTabbedPane pane;
    private final JDialog dialog = new JDialog ();
    private final String message;

    DateAction(JTabbedPane pane, String message, JFrame frame){
        super(message);
        this.frame = frame;
        this.pane = pane;
        this.message = message;
        putValue(SHORT_DESCRIPTION, message);
    }

    private JSpinner createDateSpinner(){
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(dateModel);
        Calendar calendar = new GregorianCalendar();
        spinner.setValue(calendar.getTime());
        spinner.setVisible(true);
        return spinner;
    }

    private void onButtonActionPerformed(ActionEvent e){
        dialog.dispose();
    }

    private JButton createJButton(){
        JButton button = new JButton("Confirm");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(this::onButtonActionPerformed);
        return button;
    }

    private void createDateDialog(){
        dialog.setIconImage(Icons.SETTINGS_IMAGE);
        dialog.setTitle(message);
        dialog.setPreferredSize(new Dimension(220, 110));
        dialog.setLayout(new FlowLayout());

        dialog.setModal(true);
        dialog.setAlwaysOnTop(true);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);

        dialog.add(createDateSpinner());
        dialog.add(createJButton());


        dialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Casom dorobit detekovanie na ktory button bolo klinute ("From"/"To")
        createDateDialog();
    }
}
