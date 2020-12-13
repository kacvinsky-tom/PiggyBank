package model;

import java.util.Date;
import java.util.Objects;

public class Transaction {

    private Long id;
    private double amount;
    private String name;
    private Date date;
    private String note;
    private Category category;
    private TransactionType type;

    public Transaction(String name, double amount, Category category, Date date, String note, TransactionType type) {
        setType(type);
        setName(name);
        setAmount(amount);
        setCategory(category);
        setDate(date);
        setNote(note);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
