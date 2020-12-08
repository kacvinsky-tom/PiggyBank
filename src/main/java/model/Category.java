package model;

import java.awt.*;
import java.util.Objects;

public class Category {

    private Long id;
    private String name;
    private Color color;
    private int transactionsNumber;
    private double expenses;
    private double income;
    private double percentageInc;
    private double percentageSpend;
    private double sum;


    public Category(String name, Color color){
        setName(name);
        setColor(color);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
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

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
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

    @Override
    public String toString() {
        return name;
    }
}
