package model;

import java.util.ArrayList;

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
        if (this.date == date) {
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
        if (this.date == e.getDate()) {
            monthlyExpenses.add(e);
            budgetRemaining = budgetRemaining - e.getPrice();
        }
    }

    // MODIFIES: this
    // EFFECTS: returns true if expense is recorded in monthly expenses and can be removed
    public void removeExpense(Expense e) {
        if (this.date == e.getDate()) {
            monthlyExpenses.remove(e);
            budgetRemaining = budgetRemaining + e.getPrice();
        }
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
}


//method for get all expenses
//method for if all expenses are over set budget (boolean)
// method for setting initial budget (like initial balance so that owuld be its own field)
//field for what the spent money so far is
// subtract expense from set budget
//method for get how much left in budget
//can display negative values, this will represent over budget
//method for looking through budget list according to description

