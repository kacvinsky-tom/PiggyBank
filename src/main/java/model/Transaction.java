package model;

import enums.TransactionType;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Transaction {

    private Long id;
    private BigDecimal amount;
    private String name;
    private Date date;
    private String note;
    private final List<Category> categories;
    private TransactionType type;

    public Transaction(String name, BigDecimal amount, Category category, Date date, String note, TransactionType type) {
        categories = new ArrayList<Category>();
        setType(type);
        setName(name);
        setAmount(amount);
        addCategory(category);
        setDate(date);
        setNote(note);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    public void setAmount(BigDecimal amount) {
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

    public String getNote(){
        return note;
    }

    public Color getCategoryColor(){
        return categories.get(0).getColor();
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCategory(Category category, int index){
        categories.set(index, category);
    }

    public List<Category> getCategories(){
        return categories;
    }
    public Category getCategory() {
        return categories.get(categories.size() - 1);
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
