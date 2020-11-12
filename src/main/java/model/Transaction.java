package model;
import java.time.LocalDate;
import java.util.Objects;

public class Transaction {
    private double amount;
    private String name;
    private LocalDate date;
    private String note;
    private Category category;

    public Transaction(String name, double amount, Category category, LocalDate date, String note){
        setName(name);
        setAmount(amount);
        setCategory(category);
        setDate(date);
        setNote(note);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount <= 0.0){
            throw new IllegalArgumentException("amount must not be 0 or less");
        }
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
}
