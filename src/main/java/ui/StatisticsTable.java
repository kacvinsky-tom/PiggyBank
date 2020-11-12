package ui;

import model.Category;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class StatisticsTable extends AbstractTableModel {

    private final List<Category> categories = new ArrayList<>();

    StatisticsTable(){
        // following lines are just fo testing purposes
        /*
        Category c1 = new Category();
        c1.name = "Food";
        c1.expenses = -80;
        c1.income = 0;
        c1.sum = -80;
        c1.percentageAmount = 0;
        c1.transactionsNumber = 3;

        Category c2 = new Category();
        c2.name = "Sex services";
        c2.expenses = -20;
        c2.income = 60;
        c2.sum = 40;
        c2.percentageAmount = 40;
        c2.transactionsNumber = 2;

        Category c3 = new Category();
        c3.name = "Job";
        c3.expenses = 0;
        c3.income = 80;
        c3.sum = 80;
        c3.percentageAmount = 60;
        c3.transactionsNumber = 5;

        categories.add(c1);
        categories.add(c2);
        categories.add(c3);
        */
    }

    @Override
    public int getRowCount() {
        return categories.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    String editAmount(double amount){
        if (amount < 0){
            return Double.toString(amount);
        }
        return " " + amount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var category = categories.get(rowIndex);
        switch (columnIndex){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return null;
            default:
                throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }
    }


    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Category";
            case 1:
                return "Sum";
            case 2:
                return "Income";
            case 3:
                return "Expenses";
            case 4:
                return "Sum in %";
            case 5:
                return "Transactions";
            default:
                throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }
    }
}
