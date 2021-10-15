package model;

import java.util.ArrayList;
import java.util.List;

// Represents a list of expenses made by the user
public class MonthlyExpenses {
    private ArrayList<Expense> monthlyExpenses;
    private int budget;
    private int budgetRemaining;
    private String date;

    // EFFECTS: constructs a new expense list for the month
    public MonthlyExpenses(String date) {
        monthlyExpenses = new ArrayList<>();
        this.date = date;
        budget = 0;
    }

    // REQUIRES: budget > 0
    // MODIFIES: this
    // EFFECTS: sets a budget for the month
    public int setBudget(String date, int i) {
        if (this.date.equals(date)) {
            this.budget = i;
            budgetRemaining = budget;
            return budget;
        } else {
            return 0;
        }
    }

    // MODIFIES: this
    // EFFECTS: returns true if Expense can be added to the list of expenses for the month
    public void addExpense(Expense e) {
        if (this.date.equals(e.getDate())) {
            monthlyExpenses.add(e);
            budgetRemaining = budgetRemaining - e.getPrice();
        }
    }

    public Expense getExpense(String desc) {
        for (Expense e: monthlyExpenses) {
            if (desc.equals(e.getDescription())) {
                return e;
            }
        }
        return null;
    }

    public List<Expense> getExpenses() {
        return monthlyExpenses;
    }

    // EFFECTS: returns the budget remaining, negative if gone over budget
    public int getBudgetRemaining() {
        return budgetRemaining;
    }

    // EFFECTS: returns the date the monthly expenses were recorded
    public String getDate() {
        return this.date;
    }

    public int length() {
        return monthlyExpenses.size();
    }

    public boolean contains(Expense e) {
        return monthlyExpenses.contains(e);
    }
}


