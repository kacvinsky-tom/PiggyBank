package model;

public class CategoryStatistic {
    private final Category category;
    private int transactionsNumber = 0;
    private double expenses = 0;
    private double income = 0;
    private double percentageInc = 0;
    private double percentageSpend = 0;
    private double sum = 0;

    public CategoryStatistic(Category category){
        this.category = category;
    }

    public String getCategoryName(){
        return category.getName();
    }

    public Category getCategory() {
        return category;
    }

    public int getTransactionsNumber() {
        return transactionsNumber;
    }

    public void setTransactionsNumber(int transactionsNumber) {
        this.transactionsNumber = transactionsNumber;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getPercentageInc() {
        return percentageInc;
    }

    public void setPercentageInc(double percentageInc) {
        this.percentageInc = percentageInc;
    }

    public double getPercentageSpend() {
        return percentageSpend;
    }

    public void setPercentageSpend(double percentageSpend) {
        this.percentageSpend = percentageSpend;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
