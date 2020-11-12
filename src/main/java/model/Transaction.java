package model;
import java.time.LocalDate;

public class Transaction {
    private float amount;
    private String name;
    private LocalDate date;
    private String note;
    private Category category;

    Transaction(float amount, String name, String note, Category category){
        this.amount = amount;
        this.name = name;
        this.note = note;
        this.category = category;
        this.date = LocalDate.now();
    }

}
