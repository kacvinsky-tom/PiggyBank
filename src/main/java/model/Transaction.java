package model;
import javax.management.ValueExp;
import java.time.LocalDate;
import java.util.Objects;

public class Transaction {
    private float amount;
    private String name;
    private LocalDate date;
    private String note;
    private Category category;

    Transaction(float amount, String name, String note, Category category, LocalDate date){
        setAmount(amount);
        setName(name);
        setNote(note);
        setCategory(category);
        setDate(date);
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        if (amount == 0.0){
            throw new IllegalArgumentException("amount must not be null");
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
        if (category == null){
            category = new Category();
        }
        this.category = category;
    }
}
