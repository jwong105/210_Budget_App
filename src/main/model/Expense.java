package model;

// Represents an expense made with a description of the purchase and the price paid in dollars
public class Expense {
    private String date;
    private String description;
    private int price;

    // REQUIRES: price >= 0
    // EFFECTS: Expense has date purchased, small description of purchase and price paid in dollars
    public Expense(String date, String description, int price) {
        this.date = date;
        this.description = description;
        this.price = price;
    }

    // EFFECTS: returns the date the purchase was made
    public String getDate() {
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
}