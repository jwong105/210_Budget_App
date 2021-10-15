package model;

import java.util.ArrayList;

// Represents a list of expenses made by the user
public class ExpenseLog {
    private ArrayList<MonthlyExpenses> expenseLog;

    // EFFECTS: constructs a new log of all expenses recorded for each month
    public ExpenseLog() {
        expenseLog = new ArrayList<>();
    }

    // REQUIRES: only add each monthly list of expenses to expense log once
    // MODIFIES: this
    // EFFECTS: adds monthly expense list with all of its recorded expenses to expense log
    public void addMonthlyExpenses(MonthlyExpenses m) {
        expenseLog.add(m);
    }

    // MODIFIES: this
    // EFFECTS: returns the monthly expense list if already added to expense log
    public MonthlyExpenses getMonthlyExpenses(String date) {
        for (MonthlyExpenses m : expenseLog) {
            if (date.equals(m.getDate())) {
                return m;
            }
        }
        return null;
    }

    // EFFECTS: returns the number of monthly expense lists in expense log
    public int length() {
        return expenseLog.size();
    }

    // EFFECTS: returns true if expense log contains the monthly expense list, false otherwise
    public boolean contains(MonthlyExpenses m) {
        return expenseLog.contains(m);
    }
}
