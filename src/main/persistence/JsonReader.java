package persistence;

import model.Expense;
import model.ExpenseLog;
import model.MonthlyExpenses;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// This JsonReader references code from this GitHub repository
// Link: [https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo]

// Represents a reader that reads ExpenseLog from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ExpenseLog read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseExpenseLog(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses ExpenseLog from JSON object and returns it
    private ExpenseLog parseExpenseLog(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        ExpenseLog el = new ExpenseLog(name);
        addLog(el, jsonObject);
        return el;
    }

    // MODIFIES: el
    // EFFECTS: parses ExpenseLog from JSON object and adds them to ExpenseLog
    private void addLog(ExpenseLog el, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("monthly expenses");
        for (Object json : jsonArray) {
            JSONObject nextMonthlyExpenses = (JSONObject) json;
            addMonthlyExpenses(el, nextMonthlyExpenses);
        }
    }

    // MODIFIES: el
    // EFFECTS: parses MonthlyExpenses from JSON object and adds them to ExpenseLog
    private void addMonthlyExpenses(ExpenseLog el, JSONObject jsonObject) {
        int year = jsonObject.getInt("year");
        int month = jsonObject.getInt("month");
        int budget = jsonObject.getInt("budget");
        int budgetRemaining = jsonObject.getInt("budget remaining");
        MonthlyExpenses monthlyExpenses = new MonthlyExpenses(year, month);
        monthlyExpenses.setYear(year);
        monthlyExpenses.setMonth(month);
        monthlyExpenses.forceBudget(budget);
        JSONArray jsonArray = jsonObject.getJSONArray("expense");
        for (Object json : jsonArray) {
            JSONObject nextExpense = (JSONObject) json;
            addExpense(monthlyExpenses, nextExpense);
        }
        monthlyExpenses.forceBudgetRemaining(budgetRemaining);
        el.addMonthlyExpenses(monthlyExpenses);
    }

    // MODIFIES: el
    // EFFECTS: parses Expense from JSON object and adds it to MonthlyExpenses
    private void addExpense(MonthlyExpenses monthlyExpenses, JSONObject jsonObject) {
        int year = jsonObject.getInt("year");
        int month = jsonObject.getInt("month");
        String description = jsonObject.getString("description");
        int price = jsonObject.getInt("price");
        Expense expense = new Expense(year, month, description, price);
        monthlyExpenses.addExpense(expense);
    }
}
