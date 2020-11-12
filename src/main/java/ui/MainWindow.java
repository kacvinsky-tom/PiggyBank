package ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;

public class MainWindow {

    private final JFrame frame;
    private final JTabbedPane pane = new JTabbedPane();
    private final Action addAction;
    private final Action deleteAction;
    private final Action editAction;

    public MainWindow() {
        frame = createFrame();
        frame.setPreferredSize(new Dimension(700, 500));
        addAction = new AddAction(pane);
        deleteAction = new DeleteAction(pane);
        editAction = new EditAction(pane);
        frame.add(createToolbar(), BorderLayout.WEST);
        frame.add(createTabbedPane(), BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public void show() {
        frame.setVisible(true);
    }

    private JFrame createFrame() {
        var frame = new JFrame("Piggy Bank - Personal cash flow manager");
        ImageIcon img = new ImageIcon(getClass().getResource("/ui/app-icon.png"));
        frame.setIconImage(img.getImage());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    private JTabbedPane createTabbedPane() {
        pane.add("Home", createHomeTable());
        pane.add("Transactions", createTransactionTable());
        pane.add("Statistics", createCategoriesTable());
        pane.add("Categories", createCategoriesTable());
        return pane;
    }

    private JTable createHomeTable(){
        var homeModel = new HomeTable();
        var homeTable = new JTable(homeModel);
        homeTable.setAutoCreateRowSorter(true);
        homeTable.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        homeTable.setRowHeight(20);
        return homeTable;
    }

    private JTable createTransactionTable(){
        var transactionsModel = new TransactionsTable();
        var transactionsTable = new JTable(transactionsModel);
        transactionsTable.setAutoCreateRowSorter(true);
        transactionsTable.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        transactionsTable.setRowHeight(20);
        return transactionsTable;
    }

    private JTable createCategoriesTable(){
        var categoriesModel = new CategoriesTable();
        var categoriesTable = new JTable(categoriesModel);
        categoriesTable.setAutoCreateRowSorter(true);
        categoriesTable.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        categoriesTable.setRowHeight(20);
        return categoriesTable;
    }

    private JToolBar createToolbar() {
        var toolbar = new JToolBar(null,SwingConstants.VERTICAL);
        toolbar.add(addAction);
        toolbar.add(deleteAction);
        toolbar.add(editAction);
        toolbar.setFloatable(false);
        return toolbar;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        deleteAction.setEnabled(selectionModel.getSelectedItemsCount() != 0);
        editAction.setEnabled(selectionModel.getSelectedItemsCount() == 1);
    }
}
