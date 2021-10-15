package model;

import java.util.ArrayList;

// Represents a list of expenses made by the user
public class ExpenseLog {
    private ArrayList<MonthlyExpenses> expenseLog;

    // EFFECTS: constructs a new log of all expenses recorded for each month
    public ExpenseLog() {
        expenseLog = new ArrayList<>();
    }

    // REQUIRES: only one entry into expense log per date
    public void addMonthlyExpenses(MonthlyExpenses m) {
        expenseLog.add(m);
    }

    public MonthlyExpenses getMonthlyExpenses(String date) {
        for (MonthlyExpenses m : expenseLog) {
            if (date.equals(m.getDate())) {
                return m;
            }
        }
        return null;
    }

    public int length() {
        return expenseLog.size();
    }

    public boolean contains(MonthlyExpenses m) {
        return expenseLog.contains(m);
    }
}
