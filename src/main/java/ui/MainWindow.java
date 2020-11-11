package ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.BorderLayout;

public class MainWindow {

    private final JFrame frame;
    private final JTabbedPane pane = new JTabbedPane();
    private final Action quitAction = new QuitAction();
    private final Action addAction;
    private final Action deleteAction;
    private final Action editAction;

    public MainWindow() {
        frame = createFrame();
        frame.add(createToolbar(), BorderLayout.BEFORE_FIRST_LINE);
        frame.add(createTabbedPane(), BorderLayout.CENTER);
        addAction = new AddAction();
        deleteAction = new DeleteAction(pane);
        editAction = new EditAction(pane);
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
        pane.add("Home", createHomeTable());
        pane.add("Transactions", createTransactionTable());
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
        var toolbar = new JToolBar();
        toolbar.add(addAction);
        toolbar.add(deleteAction);
        toolbar.add(editAction);
        toolbar.addSeparator();
        toolbar.add(quitAction);
        toolbar.addSeparator();
        return toolbar;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        // here you can put the code for handling selection change
        deleteAction.setEnabled(selectionModel.getSelectedItemsCount() != 0);
        editAction.setEnabled(selectionModel.getSelectedItemsCount() == 1);
    }
}
