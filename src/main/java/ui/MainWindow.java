package ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Random;


public class MainWindow {

    private final JFrame frame;
    private final JTabbedPane pane = new JTabbedPane();
    private final Action addAction;
    private final Action deleteAction;
    private final Action editAction;
    private final JToolBar toolBar = new JToolBar(null,SwingConstants.VERTICAL);
    private final double income = new Random().ints(0,1000).findFirst().getAsInt();
    private final double expenses = new Random().ints(0,1000).findFirst().getAsInt();

    public MainWindow() {
        frame = createFrame();
        frame.setPreferredSize(new Dimension(700, 500));
        addAction = new AddAction(pane);
        deleteAction = new DeleteAction(pane);
        editAction = new EditAction(pane);
        toolBar = createToolbar();
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
        return frame;
    }

    private JTabbedPane createTabbedPane() {
        pane.add("Home", (Component) createTable(new HomeTable(), false));
        pane.add("Statistics", (Component) createTable(new StatisticsTable(), true));
        pane.add("Transactions", (Component) createTable(new TransactionsTable(), true));
        pane.add("Categories", (Component) createTable(new CategoriesTable(), true));
        pane.addChangeListener(this::changeTable);
        return pane;
    }

    private Object createTable(Object o, Boolean makeScroll){
        var table = new JTable((TableModel) o);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        table.setRowHeight(20);
        if (makeScroll){
            return new JScrollPane(table);
        }
        return table;
    }

    private JToolBar createToolbar() {
        JToolBar toolBar = new JToolBar(null,SwingConstants.VERTICAL);
        toolBar.add(addAction);
        toolBar.add(deleteAction);
        toolBar.add(editAction);
        toolBar.setVisible(false);
        return toolBar;
    }

    private void createToolbar() {
        toolBar.add(addAction);
        toolBar.add(deleteAction);
        toolBar.add(editAction);
        toolBar.setFloatable(false);
        toolBar.setVisible(false);
    }

    private void changeTable(ChangeEvent changeEvent){
        var sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
        int index = sourceTabbedPane.getSelectedIndex();
        toolBar.setVisible(index > 1);
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        deleteAction.setEnabled(selectionModel.getSelectedItemsCount() != 0);
        editAction.setEnabled(selectionModel.getSelectedItemsCount() == 1);
    }
}
