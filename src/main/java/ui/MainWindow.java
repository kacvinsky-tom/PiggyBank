package ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;


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

    public MainWindow() {
        frame = createFrame();

        homePanel = new HomePanel();

        addAction = new AddAction(pane);
        deleteAction = new DeleteAction(pane);
        editAction = new EditAction(pane);
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
        ImageIcon img = new ImageIcon(getClass().getResource("/ui/app-icon.png"));
        frame.setIconImage(img.getImage());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700, 500));
        return frame;
    }

    private JSpinner createDateSpinner(){
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(dateModel);
        Calendar calendar = new GregorianCalendar();
        spinner.setValue(calendar.getTime());
        spinner.setVisible(true);
        return spinner;
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
        toolBar.setFloatable(false);
        toolBar.setVisible(false);
        return toolBar;
    }

    private void changeTable(ChangeEvent changeEvent){
        var sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
        int index = sourceTabbedPane.getSelectedIndex();
        toolBar.setVisible(index > 1);

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
