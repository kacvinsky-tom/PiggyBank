package ui;

import model.Transaction;

import javax.swing.*;
import java.util.Date;
import java.util.List;

public class DateSpinner {

    private final TransactionsTable transactionsTable;
    private final JSpinner spinner;

    public DateSpinner(TransactionsTable transactionsTable, boolean bool){
        this.transactionsTable = transactionsTable;
        this.spinner = createDateSpinner(bool);
    }

    private JSpinner createDateSpinner(boolean from) {
        JSpinner spinner = new JSpinner();
        SpinnerDateModel spinnerDateModel = new SpinnerDateModel();
        spinner.setModel(spinnerDateModel);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        if (from) {
            setSpinner_from(spinnerDateModel);
        }
        spinner.setVisible(true);
        return spinner;
    }

    private void setSpinner_from(SpinnerDateModel spinnerDateModel) {
        List<Transaction> tranList = transactionsTable.getTransactions();
        if (!tranList.isEmpty()) {
            Date minDate = tranList.stream().map(Transaction::getDate).min(Date::compareTo).get();
            spinnerDateModel.setValue(minDate);
        }
    }

    public JSpinner getSpinner() {
        return spinner;
    }

}
