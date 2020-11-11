package ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.BorderLayout;

public class MainWindow {

    private final JFrame frame;

    public MainWindow() {
        frame = createFrame();
        frame.add(createToolbar(), BorderLayout.BEFORE_FIRST_LINE);
        frame.add(createTabbedPane(), BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public void show() {
        frame.setVisible(true);
    }

    private JFrame createFrame() {
        var frame = new JFrame("Home page");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    private JTabbedPane createTabbedPane() {
        var tannedPane = new JTabbedPane();
        var transactionsModel = new TransactionsTable();
        var transactionsTable = new JTable(transactionsModel);
        tannedPane.add("Transactions", transactionsTable);

        var categoriesModel = new CategoriesTable();
        var categoriesTable = new JTable(categoriesModel);
        tannedPane.add("Categories", categoriesTable);
        return tannedPane;
    }

    private JToolBar createToolbar() {
        var toolbar = new JToolBar();
        toolbar.addSeparator();
        return toolbar;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        // here you can put the code for handling selection change
    }
}
