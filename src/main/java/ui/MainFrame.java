package ui;

import data.CategoryDao;
import data.TransactionDao;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame {
    private final ToolBar toolBar;
    private final TabbedPane tabbedPane;

    public MainFrame(CategoryDao categoryDao, TransactionDao transactionDao) {
        TablesManager tablesManager = new TablesManager(categoryDao, transactionDao);
        toolBar = new ToolBar(this, tablesManager);
        tablesManager.addListSelectionListenerToTables(toolBar);
        tabbedPane = new TabbedPane(tablesManager, toolBar);
        setFrame();
    }

    private void setFrame() {
        this.setTitle("Piggy Bank - Personal cash flow manager");
        this.setIconImage(Icons.PIGGY_IMAGE);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 500));
        this.add(tabbedPane, BorderLayout.CENTER);
        this.add(toolBar, BorderLayout.NORTH);
        this.pack();
        this.setLocationRelativeTo(null);
    }
}
