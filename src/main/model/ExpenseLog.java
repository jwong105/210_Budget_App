package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.text.DateFormatSymbols;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// This ExpenseLog references code from this StackOverflow website
// Link: [https://stackoverflow.com/questions/1038570/how-can-i-convert-an-integer-to-localized-month-name-in-java]

// Represents a list of expenses made by the user
public class ExpenseLog implements Writable {
    private ArrayList<MonthlyExpenses> expenseLog;
    private String name;

    // EFFECTS: takes a name and constructs a new log of all expenses recorded for each month
    public ExpenseLog(String name) {
        this.name = name;
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
    public MonthlyExpenses getMonthlyExpenses(int year, int month) {
        for (MonthlyExpenses m : expenseLog) {
            YearMonth date = YearMonth.of(year, month);
            if (date.equals(m.getDate())) {
                return m;
            }
        }
        return null;
    }

    // getter
    public String getName() {
        return name;
    }

    // EFFECTS: returns the number of monthly expense lists in expense log
    public int length() {
        return expenseLog.size();
    }

    // EFFECTS: returns true if expense log contains the monthly expense list, false otherwise
    public boolean contains(MonthlyExpenses m) {
        return expenseLog.contains(m);
    }

    // getter
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    // EFFECTS: returns an unmodifiable list of MonthlyExpenses in the ExpenseLog
    public List<MonthlyExpenses> getMonthlyExpense() {
        return Collections.unmodifiableList(expenseLog);
    }

    // EFFECTS: returns ExpenseLog as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("monthly expenses", monthlyExpensesToJson());
        return json;
    }

    // EFFECTS: returns MonthlyExpenses in this ExpenseLog as a JSON array
    private JSONArray monthlyExpensesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (MonthlyExpenses monthlyExpenses: expenseLog) {
            jsonArray.put(monthlyExpenses.toJson());
        }

        return jsonArray;
    }
}
