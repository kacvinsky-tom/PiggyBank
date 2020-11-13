package ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableModel;
import java.awt.*;


public class MainWindow {

    private final JFrame frame;

    private final HomePanel homePanel;

    private final JTabbedPane pane = new JTabbedPane();
    private final JTable statisticsTable;
    private final JTable transactionsTable;
    private final JTable categoriesTable;

    private final Action addAction;
    private final Action deleteAction;
    private final Action editAction;
    private final Action filterAction;
    private final JToolBar toolBar;

    public MainWindow() {
        frame = createFrame();
        frame.setPreferredSize(new Dimension(700, 500));

        homePanel = new HomePanel();

        addAction = new AddAction(pane);
        deleteAction = new DeleteAction(pane);
        editAction = new EditAction(pane);
        filterAction = new FilterAction(pane);
        toolBar = createToolbar();

        statisticsTable = createTable(new StatisticsTable());
        transactionsTable = createTable(new TransactionsTable());
        categoriesTable = createTable(new CategoriesTable());

        frame.add(toolBar, BorderLayout.WEST);
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
        JToolBar toolBar = new JToolBar(null,SwingConstants.VERTICAL);
        toolBar.add(addAction);
        toolBar.add(deleteAction);
        toolBar.add(editAction);
        toolBar.addSeparator();
        toolBar.add(filterAction);
        toolBar.setFloatable(false);
        toolBar.setVisible(false);
        return toolBar;
    }

    private void changeTable(ChangeEvent changeEvent){
        var sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
        int index = sourceTabbedPane.getSelectedIndex();
        toolBar.setVisible(index > 1);
        toolBar.getComponentAtIndex(3).setVisible(index == 2);

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
