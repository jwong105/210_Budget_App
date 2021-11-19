package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.text.DateFormatSymbols;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Represents a list of expenses made by the user during the month
public class MonthlyExpenses implements Writable {
    private ArrayList<Expense> monthlyExpenses;
    private int budget;
    private int budgetRemaining;
    private YearMonth date;
    private int year;
    private int month;

    // EFFECTS: constructs a new monthly expenses list for the month with 0 budget
    public MonthlyExpenses(int year, int month) {
        monthlyExpenses = new ArrayList<>();
        this.date = YearMonth.of(year, month);
        budget = 0;
        this.year = year;
        this.month = month;
    }

    // REQUIRES: budget > 0
    // MODIFIES: this
    // EFFECTS: sets a budget in dollars for the month
    public void setBudget(int year, int month, int i) {
        YearMonth date = YearMonth.of(year, month);
        if (this.date.equals(date)) {
            this.budget = i;
            this.budgetRemaining = budget;
        }
    }

    // MODIFIES: this
    // EFFECTS: adds expense to monthly expenses if month expense was made matches month of monthly expenses
    public void addExpense(Expense e) {
        if (this.date.equals(e.getDate())) {
            monthlyExpenses.add(e);
            this.budgetRemaining = budgetRemaining - e.getPrice();
        }
    }

    // MODIFIES: this
    // EFFECTS: removes expense from monthly expenses if month expense was made matches month of monthly expenses
    public void removeExpenses(String description, int price, Expense e) {
        if (description.equals(e.getDescription()) && price == e.getPrice()) {
            monthlyExpenses.remove(e);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds back price from removed expense to budget remaining
    public void addBackExpense(Expense e) {
        if (monthlyExpenses.contains(e)) {
            this.budgetRemaining = budgetRemaining + e.getPrice();
        }
    }

    // EFFECTS: returns monthly expenses
    public ArrayList<Expense> getExpenses() {
        return monthlyExpenses;
    }

    // getter
    public int getBudgetRemaining() {
        return budgetRemaining;
    }

    // getter
    public YearMonth getDate() {
        return this.date;
    }

    // EFFECTS: returns the number of expenses recorded in monthly expenses
    public int length() {
        return monthlyExpenses.size();
    }

    // getter
    public int getYear() {
        return year;
    }

    //getter
    public int getBudget() {
        return budget;
    }

    // setter
    public void setYear(int year) {
        this.year = year;
    }

    // getter
    public int getMonth() {
        return month;
    }

    // setter
    public void setMonth(int month) {
        this.month = month;
    }

    // MODIFIES: this
    // EFFECTS: sets the budget to the budget passed in
    public void forceBudget(int budget) {
        this.budget = budget;
    }

    // MODIFIES: this
    // EFFECTS: sets the remainingBudget to the remainingBudget passed in
    public void forceBudgetRemaining(int budgetRemaining) {
        this.budgetRemaining = budgetRemaining;
    }

    // EFFECTS: returns true if monthly expenses contains the expense, false otherwise
    public boolean contains(Expense e) {
        return monthlyExpenses.contains(e);
    }

    // EFFECTS: returns an unmodifiable list of Expenses in this MonthlyExpense
    public List<Expense> getExpense() {
        return Collections.unmodifiableList(monthlyExpenses);
    }

    // getter
    public String getMonthString(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    // EFFECTS: returns monthly expense as a string
    public String toString() {
        return this.getMonthString(month) + " " + year;
    }

    // EFFECTS: returns MonthlyExpenses.; as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("year", year);
        json.put("month", month);
        json.put("budget", budget);
        json.put("budget remaining", budgetRemaining);
        json.put("expense", expenseToJson());
        return json;
    }

    // EFFECTS: returns Expenses in this MonthlyExpense as a JSON array
    private JSONArray expenseToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Expense e : monthlyExpenses) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }
}


