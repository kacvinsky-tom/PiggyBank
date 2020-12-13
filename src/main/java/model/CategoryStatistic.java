package model;

public class CategoryStatistic {
    private final Category category;
    private int transactionsNumber;
    private double expenses = 0;
    private double income = 0;
    private double percentageInc = 0;
    private double percentageSpend = 0;
    private double sum = 0;

    public CategoryStatistic(Category category){
        this.category = category;
    }

}
