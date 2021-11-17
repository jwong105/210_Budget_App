package model;

import org.json.JSONObject;
import persistence.Writable;

import java.time.YearMonth;

// Represents an expense made with a description of the purchase and the price paid in dollars
public class Expense implements Writable {
    private YearMonth date;
    private String description;
    private int price;
    private int year;
    private int month;

    // REQUIRES: price >= 0
    // EFFECTS: Expense has date purchased, small description of purchase and price paid in dollars
    public Expense(int year, int month, String description, int price) {
        this.date = YearMonth.of(year, month);
        this.description = description;
        this.price = price;
        this.year = year;
        this.month = month;
    }

    // getter
    public YearMonth getDate() {
        return date;
    }

    // getter
    public String getDescription() {
        return description;
    }

    // getter
    public int getPrice() {
        return price;
    }

    public String toString() {
        return description + ": $" + price;
    }

    // EFFECTS: returns Expense as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("year", year);
        json.put("month", month);
        json.put("description", description);
        json.put("price", price);
        return json;
    }
}