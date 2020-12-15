package ui;

import data.CategoryDao;
import data.TransactionDao;
import model.CategoryCellRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;

public class TablesManager {
    private final StatisticsTable statisticsTableModel;
    private final StatisticsBalanceTable statisticsBalanceTableModel;
    private final CategoriesTable categoriesTableModel;
    private final TransactionsTable transactionsTableModel;

    private final JTable statisticsJTable;
    private final JTable statisticsBalanceJTable;
    private final JTable categoriesJTable;
    private final JTable transactionsJTable;

    public TablesManager(CategoryDao categoryDao, TransactionDao transactionDao){
        statisticsTableModel = new StatisticsTable();
        statisticsBalanceTableModel = new StatisticsBalanceTable();
        categoriesTableModel = new CategoriesTable(categoryDao);
        transactionsTableModel = new TransactionsTable(transactionDao, categoriesTableModel);

        statisticsJTable = createJTable(statisticsTableModel);
        statisticsBalanceJTable = createStatisticsBalanceJTable();
        categoriesJTable = createJTable(categoriesTableModel);
        transactionsJTable = createJTable(transactionsTableModel);

        categoriesJTable.setDefaultRenderer(Color.class, new CategoryCellRenderer());
    }

    public JTable getStatJTable() {
        return statisticsJTable;
    }

    public JTable getStatBalJTable() {
        return statisticsBalanceJTable;
    }

    public JTable getCatJTable() {
        return categoriesJTable;
    }

    public JTable getTranJTable() {
        return transactionsJTable;
    }

    public StatisticsTable getStatTableModel() {
        return statisticsTableModel;
    }

    public StatisticsBalanceTable getStatBalTableModel() {
        return statisticsBalanceTableModel;
    }

    public CategoriesTable getCatTableModel() {
        return categoriesTableModel;
    }

    public TransactionsTable getTranTableModel() {
        return transactionsTableModel;
    }

    private JTable createJTable(Object o){
        var table = new JTable((TableModel) o);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(20);
        return table;
    }

    private JTable createStatisticsBalanceJTable(){
        JTable table = new JTable(statisticsBalanceTableModel);

        table.setTableHeader(null);
        table.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        table.setFont(table.getFont().deriveFont(Font.BOLD));
        table.setCellSelectionEnabled(false);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);

        return table;
    }

    public void addListSelectionListenerToTables(ToolBar toolBar){
        statisticsJTable.getSelectionModel().addListSelectionListener(toolBar::rowSelectionChanged);
        transactionsJTable.getSelectionModel().addListSelectionListener(toolBar::rowSelectionChanged);
        categoriesJTable.getSelectionModel().addListSelectionListener(toolBar::rowSelectionChanged);
    }
}
