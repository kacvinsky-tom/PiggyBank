package ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableModel;
import java.awt.*;


public class MainWindow {

    private final JFrame frame;

    private final JTabbedPane pane = new JTabbedPane();

    private final HomePanel homePanel;
    private final JTable statisticsTable;
    private final JTable transactionsTable;
    private final JTable categoriesTable;

    private final JToolBar toolBar;
    private final Action addAction;
    private final Action deleteAction;
    private final Action editAction;
    private final Action filterAction;
    private final Action dateFromAction;
    private final Action dateToAction;

    public MainWindow() {
        frame = createFrame();

        homePanel = new HomePanel();

        addAction = new AddAction(pane);
        deleteAction = new DeleteAction(pane);
        editAction = new EditAction(pane);
        filterAction = new FilterAction(pane);
        dateFromAction = new DateAction(pane, "Date From");
        dateToAction = new DateAction(pane, "Date To");
        toolBar = createToolbar();

        statisticsTable = createTable(new StatisticsTable());
        transactionsTable = createTable(new TransactionsTable());
        categoriesTable = createTable(new CategoriesTable());

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
        pane.add("Home", homePanel.getPanel());
        pane.add("Statistics", new JScrollPane(statisticsTable));
        pane.add("Transactions", new JScrollPane(transactionsTable));
        pane.add("Categories", new JScrollPane(categoriesTable));
        pane.addChangeListener(this::changeTable);
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
        toolBar.add(dateFromAction);
        toolBar.add(dateToAction);
        toolBar.setFloatable(false);
        toolBar.setVisible(false);
        return toolBar;
    }

    private void changeTable(ChangeEvent changeEvent){
        var sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
        int index = sourceTabbedPane.getSelectedIndex();
        toolBar.setVisible(index >= 1);

        statisticsTable.clearSelection();
        transactionsTable.clearSelection();
        categoriesTable.clearSelection();

        toolBar.getComponentAtIndex(0).setVisible(index == 2 || index == 3);
        toolBar.getComponentAtIndex(1).setVisible(index == 2 || index == 3);
        toolBar.getComponentAtIndex(2).setVisible(index == 2 || index == 3);
        toolBar.getComponentAtIndex(3).setVisible(index == 2);
        toolBar.getComponentAtIndex(4).setVisible(index == 1 || index == 2);
        toolBar.getComponentAtIndex(5).setVisible(index == 1 || index == 2);
        toolBar.getComponentAtIndex(6).setVisible(index == 1 || index == 2);
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        deleteAction.setEnabled(selectionModel.getSelectedItemsCount() != 0);
        editAction.setEnabled(selectionModel.getSelectedItemsCount() == 1);
    }
}
