package ui;

import data.CategoryDao;
import data.TransactionDao;
import model.CategoryCellRenderer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableModel;
import java.awt.*;


public class MainFrame {

    private final JFrame frame;

    private final JTabbedPane pane = new JTabbedPane();

    private final JTable statisticsTable;
    private final JTable transactionsTable;
    private final JTable categoriesTable;

    private final JToolBar toolBar;
    private final Action addAction;
    private final Action deleteAction;
    private final Action editAction;

    public MainFrame(CategoryDao categoryDao, TransactionDao transactionDao) {
        frame = createFrame();

        var statisticsTableModel = new StatisticsTable();
        statisticsTable = createTable(statisticsTableModel);

        var catTable = new CategoriesTable(categoryDao, statisticsTableModel);
        categoriesTable = createTable(catTable);
        categoriesTable.setDefaultRenderer(Color.class, new CategoryCellRenderer());

        TransactionsTable transactionsTableModel = new TransactionsTable(transactionDao, catTable);
        transactionsTable = createTable(transactionsTableModel);

        frame.add(createTabbedPane(), BorderLayout.CENTER);

        addAction = new AddAction(pane, frame);
        deleteAction = new DeleteAction(pane);
        editAction = new EditAction(pane, frame);
        toolBar = createToolbar();

        new Filter(toolBar, transactionsTable, categoriesTable, transactionsTableModel);

        frame.add(toolBar, BorderLayout.NORTH);
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
        frame.setPreferredSize(new Dimension(800, 500));
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
        toolBar.setFloatable(false);
        toolBar.setVisible(true);
        return toolBar;
    }

    private void disableFilterSettings(){
        toolBar.getComponentAtIndex(4).setEnabled(pane.getSelectedIndex() < 2);
        toolBar.getComponentAtIndex(5).setEnabled(pane.getSelectedIndex() < 2);
        toolBar.getComponentAtIndex(7).setEnabled(pane.getSelectedIndex() < 2);
        toolBar.getComponentAtIndex(9).setEnabled(pane.getSelectedIndex() < 2);
        toolBar.getComponentAtIndex(10).setEnabled(pane.getSelectedIndex() < 2);
        toolBar.getComponentAtIndex(12).setEnabled(pane.getSelectedIndex() < 2);
        toolBar.getComponentAtIndex(13).setEnabled(pane.getSelectedIndex() < 2);
        toolBar.getComponentAtIndex(15).setEnabled(pane.getSelectedIndex() < 2);
    }

    private void changeTab(ChangeEvent changeEvent){
        statisticsTable.clearSelection();
        transactionsTable.clearSelection();
        categoriesTable.clearSelection();
        disableFilterSettings();
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        deleteAction.setEnabled(selectionModel.getSelectedItemsCount() != 0 && pane.getSelectedIndex() > 0);
        editAction.setEnabled(selectionModel.getSelectedItemsCount() == 1 && pane.getSelectedIndex() > 0);
    }
}