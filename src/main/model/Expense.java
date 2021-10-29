package model;


import exceptions.NotValidMonthException;
import exceptions.NotValidYearException;
import org.json.JSONObject;
import persistence.Writable;

import java.math.BigDecimal;
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

    // EFFECTS: returns the date the purchase was made
    public YearMonth getDate() {
        return date;
    }

    // EFFECTS: returns description of purchase
    public String getDescription() {
        return description;
    }

    // EFFECTS: returns price of purchase in dollars
    public int getPrice() {
        return price;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("year", year);
        json.put("month", month);
        json.put("description", description);
        json.put("price", price);
        return json;
    }
}