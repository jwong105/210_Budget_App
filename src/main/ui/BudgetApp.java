package ui;

import model.Expense;
import model.ExpenseLog;
import model.MonthlyExpenses;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// This BudgetApp references code from this GitHub repository
// Link: [https://github.students.cs.ubc.ca/CPSC210/TellerApp]

// Budget desktop application
public class BudgetApp {
    private static final String JSON_STORE = "./data/expenseLog.json";
    private ExpenseLog log;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;


    // EFFECTS: constructs workroom and runs application
    public BudgetApp() throws FileNotFoundException {
        input = new Scanner(System.in);
        log = new ExpenseLog("Jennifer's expense log");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        input.useDelimiter("\n");
        runBudget();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runBudget() {
        boolean keepGoing = true;
        String command = null;

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("a")) {
            addExpense();
        } else if (command.equals("b")) {
            setBudget();
        } else if (command.equals("r")) {
            removeExpense();
        } else if (command.equals("v")) {
            viewBudgetRemaining();
        } else if (command.equals("e")) {
            viewExpenses();
        } else if (command.equals("s")) {
            saveBudget();
        } else if (command.equals("l")) {
            loadBudget();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect From:");
        System.out.println("\tb -> Set Budget");
        System.out.println("\ta -> Add Expense");
        System.out.println("\tr -> Remove Expense");
        System.out.println("\tv -> View Budget Remaining");
        System.out.println("\te -> View Expenses For Month");
        System.out.println("\ts -> Save Expenses For Month To File");
        System.out.println("\tl -> Load Expenses For Month From File");
        System.out.println("\tq -> Quit");
    }

    // MODIFIES: this
    // EFFECTS: sets a budget for the month
    private void setBudget() {
        System.out.print("Enter the year to set your budget: ");
        int year = input.nextInt();

        System.out.print("Enter the month, from 1 to 12, to set your budget: ");
        int month = input.nextInt();

        System.out.print("What is your budget for this month (no decimals)? ");
        int budget = input.nextInt();

        MonthlyExpenses monthlyExpenses = log.getMonthlyExpenses(year, month);
        if (monthlyExpenses == null) {
            MonthlyExpenses newMonthlyExpense = new MonthlyExpenses(year, month);
            newMonthlyExpense.setBudget(year, month, budget);
            log.addMonthlyExpenses(newMonthlyExpense);
        } else {
            System.out.println("A budget has already been set for this month. ");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds an expense to the corresponding month the expense was made
    private void addExpense() {
        System.out.print("Enter the year your expense was made: ");
        int year = input.nextInt();

        System.out.print("Enter the month, from 1 to 12, your expense was made: ");
        int month = input.nextInt();

        System.out.print("Enter a short description of your expense: ");
        String stringDesc = input.next();

        System.out.print("Enter the price spent for this expense in whole numbers (no decimals): ");
        int price = input.nextInt();

        if (price >= 0) {
            MonthlyExpenses monthlyExpenses = log.getMonthlyExpenses(year, month);
            if (monthlyExpenses == null) {
                System.out.println("Please set a budget for this month before adding expenses.");
            } else {
                Expense expense = new Expense(year, month, stringDesc, price);
                monthlyExpenses.addExpense(expense);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: removes an expense from the corresponding month the expense was made
    private void removeExpense() {
        System.out.print("Enter the year your expense was made: ");
        int year = input.nextInt();

        System.out.print("Enter the month, from 1 to 12, your expense was made: ");
        int month = input.nextInt();

        System.out.print("Enter a short description of your expense: ");
        String stringDesc = input.next();

        System.out.print("Enter the price spent for this expense in whole numbers (no decimals): ");
        int price = input.nextInt();

        MonthlyExpenses monthlyExpenses = log.getMonthlyExpenses(year, month);
        YearMonth date = YearMonth.of(year, month);
        if (monthlyExpenses == null) {
            System.out.println("Please set a budget for this month before adding expenses.");
        } else if (date.equals(monthlyExpenses.getDate())) {
            List<Expense> remove = new ArrayList<>();
            for (Expense e : monthlyExpenses.getExpenses()) {
                if (stringDesc.equals(e.getDescription()) && price == e.getPrice()) {
                    remove.add(e);
                    monthlyExpenses.addBackExpense(e);
                }
            }
            monthlyExpenses.getExpenses().removeAll(remove);
        }
    }

    // MODIFIES: this
    // EFFECTS: return how much of the budget is remaining, value will be negative if budget limit has been surpassed
    private void viewBudgetRemaining() {
        System.out.print("Enter the year for which you would like to view your remaining budget: ");
        int year = input.nextInt();

        System.out.print("Enter the month, from 1 to 12, for which you would like to view your remaining budget: ");
        int month = input.nextInt();

        MonthlyExpenses monthlyExpenses = log.getMonthlyExpenses(year, month);
        if (monthlyExpenses == null) {
            System.out.println("Please set a budget for this month before adding expenses.");
        } else {
            double remainingBudget = monthlyExpenses.getBudgetRemaining();
            if (remainingBudget >= 0) {
                System.out.println("You have $" + remainingBudget + " remaining in your budget.");
            } else {
                System.out.println("You have gone over your set budget for this month by $" + remainingBudget);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: view all expenses recorded for the month
    private void viewExpenses() {
        System.out.print("Enter the year for which you would like to view all expenses made: ");
        int year = input.nextInt();

        System.out.print("Enter the month, from 1 to 12, for which you would like to view all expenses made: ");
        int month = input.nextInt();

        MonthlyExpenses monthlyExpenses = log.getMonthlyExpenses(year, month);
        if (monthlyExpenses == null) {
            System.out.println("Please set a budget for this month before adding expenses.");
        } else {
            System.out.println("These are your expenses for " + log.getMonth(month) + " " + year + ": ");
            for (Expense e : monthlyExpenses.getExpenses()) {
                double price = e.getPrice();
                String desc = e.getDescription();
                System.out.println(desc + ": price = $" + price);
            }
        }
    }

    // EFFECTS: saves the workroom to file
    private void saveBudget() {
        try {
            jsonWriter.open();
            jsonWriter.write(log);
            jsonWriter.close();
            System.out.println("Saved " + log.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadBudget() {
        try {
            log = jsonReader.read();
            System.out.println("Loaded " + log.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}

