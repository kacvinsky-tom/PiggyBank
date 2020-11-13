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
    private final JToolBar toolBar;
    private final JTable statisticsTable;
    private final JTable transactionsTable;
    private final JTable categoriesTable;
    private final double income = new Random().ints(0,1000).findFirst().getAsInt();
    private final double expenses = new Random().ints(0,1000).findFirst().getAsInt();

    public MainWindow() {
        frame = createFrame();
        frame.setPreferredSize(new Dimension(700, 500));

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
        return frame;
    }

    private JTabbedPane createTabbedPane() {
        pane.add("Home", createHomePanel());
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

    private JPanel createHomePanel(){
        var panel = new JPanel(new GridBagLayout());
//        panel.setBackground(new Color(250, 255, 255));
        Font fontSubTitle = new Font("arial", Font.PLAIN, 30);
        Font fontTitle = new Font("arial", Font.BOLD, 40);
        Font fontNumbers = new Font("arial", Font.BOLD, 25);
        addJlabel("Your balance",1,0,0, 0, fontTitle, Color.black, panel);
        addJlabel("Income",0,1,40, 0, fontSubTitle, Color.black, panel);
        addJlabel("Margin",1,2,40, 0, fontSubTitle, Color.black, panel);
        addJlabel("Expenses",2,1,40, 10, fontSubTitle, Color.black, panel);
        addJlabel(income + "€",0,2,40, 0, fontNumbers, new Color(101, 168, 47), panel);
        addJlabel(income-expenses + "€",1,3,40, 0, fontNumbers, Color.black, panel);
        addJlabel(expenses + "€",2,2,40, 0, fontNumbers, new Color(168, 43, 43), panel);
        return panel;
    }


    private void addJlabel(String text, int gridx, int gridy, int ipadx, int ipady, Font font, Color color, JPanel panel){
        var label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(color);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = gridx;
        c.gridy = gridy;
        c.ipadx = ipadx;
        c.ipady = ipady;
        if (text.equals("Your balance"))
            c.insets = new Insets(0,0,50,0);

        if (text.equals("Margin")){
            c.insets = new Insets(0,0,10,0);
        }
        label.setFont(font);
        panel.add(label, c);
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
        statisticsTable.clearSelection();
        transactionsTable.clearSelection();
        categoriesTable.clearSelection();
        toolBar.setVisible(index > 1);
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        deleteAction.setEnabled(selectionModel.getSelectedItemsCount() != 0);
        editAction.setEnabled(selectionModel.getSelectedItemsCount() == 1);
    }
}
