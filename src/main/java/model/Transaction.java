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
    private List<Category> categories;
    private TransactionType type;

    public Transaction(String name, BigDecimal amount, List<Category> cats, Date date, String note, TransactionType type) {
        categories = new ArrayList<>();
        setType(type);
        setName(name);
        setAmount(amount);
        this.categories = cats;
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

    public String getCategoriesNames(){
        String cats = "";
        for (Category c : categories){
            if (categories.indexOf(c) != 0 || categories.indexOf(c) != categories.size() - 1){
                cats += ", ";
            }
            cats += c.getName();
        }
        return cats;
    }

    public List<Category> getCategories(){
        return categories;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
