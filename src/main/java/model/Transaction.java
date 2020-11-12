package model;
import java.time.LocalDate;

public class Transaction {
    private float amount;
    private String name;
    private LocalDate date;
    private String note;

    Transaction(float amount, String name, String note){
        this.amount = amount;
        this.name = name;
        this.note = note;
    }
    
}
