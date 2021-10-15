package model;

import java.util.ArrayList;
import java.util.List;

// Represents a list of expenses made by the user during the month
public class MonthlyExpenses {
    private ArrayList<Expense> monthlyExpenses;
    private int budget;
    private int budgetRemaining;
    private String date;

    // EFFECTS: constructs a new monthly expenses list for the month with 0 budget
    public MonthlyExpenses(String date) {
        monthlyExpenses = new ArrayList<>();
        this.date = date;
        budget = 0;
    }

    // REQUIRES: budget > 0
    // MODIFIES: this
    // EFFECTS: sets a budget in dollars for the month
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
    // EFFECTS: adds expense to monthly expenses if month expense was made matches month of monthly expenses
    public void addExpense(Expense e) {
        if (this.date.equals(e.getDate())) {
            monthlyExpenses.add(e);
            budgetRemaining = budgetRemaining - e.getPrice();
        }
    }

    // EFFECTS: returns the expense if added to monthly expenses
    public Expense getExpense(String desc) {
        for (Expense e: monthlyExpenses) {
            if (desc.equals(e.getDescription())) {
                return e;
            }
        }
        return null;
    }

    // EFFECTS: returns monthly expenses
    public List<Expense> getExpenses() {
        return monthlyExpenses;
    }

    // EFFECTS: returns the budget remaining in dollars, this value will be negative if gone over budget
    public int getBudgetRemaining() {
        return budgetRemaining;
    }

    // EFFECTS: returns the month of the monthly expense list
    public String getDate() {
        return this.date;
    }

    // EFFECTS: returns the number of expenses recorded in monthly expenses
    public int length() {
        return monthlyExpenses.size();
    }

    // EFFECTS: returns true if monthly expenses contains the expense, false otherwise
    public boolean contains(Expense e) {
        return monthlyExpenses.contains(e);
    }
}


