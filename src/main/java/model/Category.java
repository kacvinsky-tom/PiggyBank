package model;

import java.awt.*;
import java.util.Objects;

public class Category {

    private Long id;
    private String name;
    private int transactionsNumber;
    private double expenses;
    private double income;
    private double sum;
    private Color color;

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

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    private double percentage;

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

    @Override
    public String toString() {
        return name;
    }
}
