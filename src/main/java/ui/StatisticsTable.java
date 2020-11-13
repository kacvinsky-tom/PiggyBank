package ui;

import model.Category;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class StatisticsTable extends AbstractTableModel {

    private final List<Category> categories = new ArrayList<>();

    StatisticsTable(){
        // following lines are just fo testing purposes
        Category c1 = new Category("Food");
        c1.setExpenses(-80);
        c1.setIncome(0);
        c1.setSum(-80);
        c1.setTransactionsNumber(3);
        c1.setPercentage(0);

        Category c2 = new Category("Sex services");
        c2.setExpenses(-20);
        c2.setIncome(60);
        c2.setSum(40);
        c2.setTransactionsNumber(2);
        c2.setPercentage(40);

        Category c3 = new Category("Job");
        c3.setExpenses(0);
        c3.setIncome(80);
        c3.setSum(80);
        c3.setTransactionsNumber(5);
        c3.setPercentage(60);

        categories.add(c1);
        categories.add(c2);
        categories.add(c3);

    }

    @Override
    public int getRowCount() {
        return categories.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    String editAmountFormat(double amount){
        if (amount < 0.0){
            return Double.toString(amount);
        }
        return " " + amount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var category = categories.get(rowIndex);
        switch (columnIndex){
            case 0:
                return category.getName();
            case 1:
                return editAmountFormat(category.getSum());
            case 2:
                return editAmountFormat(category.getIncome());
            case 3:
                return editAmountFormat(category.getExpenses());
            case 4:
                return category.getPercentage();
            case 5:
                return category.getTransactionsNumber();
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
                return "Proportion in %";
            case 5:
                return "Transactions";
            default:
                throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }
    }
}
