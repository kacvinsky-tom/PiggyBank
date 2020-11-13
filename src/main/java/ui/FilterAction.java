package ui;

import model.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FilterAction extends AbstractAction{

    private final JTabbedPane pane;
    private JDialog dialog;
    private JComboBox<String> comboBox;

    public FilterAction(JTabbedPane pane) {
        super("Filter");
        this.pane = pane;
        putValue(SHORT_DESCRIPTION, "Filter categories");
    }

    private void createDialog(){
        dialog = new JDialog();
        dialog.setIconImage(Icons.FILTER_IMAGE);
        dialog.setTitle("Choose category");
        dialog.setSize(new Dimension(250,100));
        dialog.setModal (true);
        dialog.setAlwaysOnTop (true);
        dialog.setModalityType (Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLocationRelativeTo(null);
        createJComboBox();
        dialog.add(createJPanel());
        dialog.setVisible(true);
        dialog.pack();
    }

    private JPanel createJPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(comboBox);
        panel.add(createJButton());
        return panel;
    }

    private void createJComboBox(){
        comboBox = new JComboBox<>();
        addItemsToComboBox();
        comboBox.setMaximumSize(new Dimension(150,25));
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private JButton createJButton(){
        JButton button = new JButton("Confirm");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(this::onButtonActionPerformed);
        return button;
    }

    private void onButtonActionPerformed(ActionEvent e){
        var table = getJTable();
        var transactionsTable = (TransactionsTable) table.getModel();
        String selectedCategory = (String) comboBox.getSelectedItem();
        assert selectedCategory != null;
        for (int i = 0; i < table.getRowCount(); i++){
            String s = ((Category)table.getValueAt(i,2)).getName();
            if(!selectedCategory.equals(s)){
                transactionsTable.deleteTransaction(i);
            }
        }
        dialog.dispose();
    }

    private void addItemsToComboBox(){
        var table = getJTable();
        for (int i = 0; i < table.getRowCount(); i++){
            comboBox.addItem(((Category)table.getValueAt(i,2)).getName());
        }
    }

    private JTable getJTable(){
        JScrollPane scrollPane = (JScrollPane) pane.getComponentAt(2);
        JViewport viewport = scrollPane.getViewport();
        return (JTable)viewport.getView();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        createDialog();
    }
}
