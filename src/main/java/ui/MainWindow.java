package ui;

import data.CategoryDao;
import data.TransactionDao;
import model.CategoryCellRenderer;
import model.StatisticsCellRenderer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableModel;
import java.awt.*;


public class MainWindow {

    private final JFrame frame;

    private final JTabbedPane pane = new JTabbedPane();

    private final JTable statisticsTable;
    private final JTable transactionsTable;
    private final JTable categoriesTable;

    private final JToolBar toolBar;
    private final Action addAction;
    private final Action deleteAction;
    private final Action editAction;
    private final Action filterAction;

    public MainWindow(CategoryDao categoryDao, TransactionDao transactionDao) {
        frame = createFrame();

        addAction = new AddAction(pane, frame);
        deleteAction = new DeleteAction(pane);
        editAction = new EditAction(pane);
        filterAction = new FilterAction(pane);
        toolBar = createToolbar();

        statisticsTable = createTable(new StatisticsTable());
        var catTable = new CategoriesTable(categoryDao);
        categoriesTable = createTable(catTable);
        transactionsTable = createTable(new TransactionsTable(transactionDao,catTable));

        Filter filter = new Filter(toolBar, transactionsTable);

        frame.add(toolBar, BorderLayout.NORTH);
        frame.add(createTabbedPane(), BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public void show() {
        frame.setVisible(true);
    }

    private JFrame createFrame() {
        var frame = new JFrame("Piggy Bank - Personal cash flow manager");
        frame.setIconImage(Icons.PIGGY_IMAGE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700, 500));
        return frame;
    }

    private JTabbedPane createTabbedPane() {
        pane.add("Statistics", new JScrollPane(statisticsTable));
        pane.add("Transactions", new JScrollPane(transactionsTable));
        pane.add("Categories", new JScrollPane(categoriesTable));
        pane.addChangeListener(this::changeTab);
        return pane;
    }

    private JTable createTable(Object o){
        var table = new JTable((TableModel) o);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        table.setRowHeight(20);
        return table;
    }

    private JToolBar createToolbar() {
        JToolBar toolBar = new JToolBar(null,SwingConstants.HORIZONTAL);
        toolBar.add(addAction);
        toolBar.add(deleteAction);
        toolBar.add(editAction);
        toolBar.addSeparator(new Dimension(0,30));
        toolBar.add(filterAction);
        toolBar.setFloatable(false);
        toolBar.setVisible(true);
        return toolBar;
    }

    private void changeTab(ChangeEvent changeEvent){
        var sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
        int index = sourceTabbedPane.getSelectedIndex();

        statisticsTable.clearSelection();
        transactionsTable.clearSelection();
        categoriesTable.clearSelection();
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        deleteAction.setEnabled(selectionModel.getSelectedItemsCount() != 0);
        editAction.setEnabled(selectionModel.getSelectedItemsCount() == 1);
    }
}
