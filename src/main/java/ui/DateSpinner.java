package ui;

import model.DateSpinnerType;
import model.Transaction;

import javax.swing.*;
import java.util.Date;
import java.util.List;

public class DateSpinner {

    private final TablesManager tablesManager;
    private final DateSpinnerType type;

    public JSpinner getSp() {
        return sp;
    }

    private final JSpinner sp;

    public DateSpinner(TablesManager tablesManager, DateSpinnerType type){
        this.tablesManager = tablesManager;
        this.type = type;
        sp = setDateSpinner();
    }

    private JSpinner setDateSpinner() {
        JSpinner sp = new JSpinner();
        SpinnerDateModel spinnerDateModel = new SpinnerDateModel();
        sp.setModel(spinnerDateModel);
        sp.setEditor(new JSpinner.DateEditor(sp, "dd/MM/yyyy"));
        if (type == DateSpinnerType.FROM) {
            setFromSpinner(spinnerDateModel);
        }
        sp.setVisible(true);
        return sp;
    }

    private void setFromSpinner(SpinnerDateModel spinnerDateModel) {
        List<Transaction> tranList = tablesManager.getTranTableModel().getTransactions();
        if (!tranList.isEmpty()) {
            Date minDate = tranList.stream().map(Transaction::getDate).min(Date::compareTo).get();
            spinnerDateModel.setValue(minDate);
        }
    }
}
