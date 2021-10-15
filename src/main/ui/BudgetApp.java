package ui;

import model.Expense;
import model.ExpenseLog;
import model.MonthlyExpenses;

import java.util.Scanner;

// This BudgetApp references code from this TellerApp repository
// Link: [https://github.students.cs.ubc.ca/CPSC210/TellerApp]

// Budget desktop application
public class BudgetApp {
    private ExpenseLog log;
    private Scanner input;

    // EFFECTS: runs the budget application
    public BudgetApp() {
        runBudget();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runBudget() {
        boolean keepGoing = true;
        String command = null;

        init();

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
            viewBudgetRemaining();
        } else if (command.equals("v")) {
            viewExpenses();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes Expense Log
    private void init() {
        log = new ExpenseLog();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect From:");
        System.out.println("\tb -> Set Budget");
        System.out.println("\ta -> Add Expense");
        System.out.println("\tr -> View Budget Remaining");
        System.out.println("\tv -> View Expenses For Month");
        System.out.println("\tq -> Quit");
    }

    // MODIFIES: this
    // EFFECTS: sets a budget for the month
    private void setBudget() {
        System.out.print("Enter the month to set your budget in the format of month year, eg. January 2021: ");
        String stringDate = input.next();

        System.out.print("What is your budget for this month (no decimals)? ");
        int budget = input.nextInt();

        MonthlyExpenses monthlyExpenses = log.getMonthlyExpenses(stringDate);
        if (monthlyExpenses == null) {
            MonthlyExpenses newMonthlyExpense = new MonthlyExpenses(stringDate);
            newMonthlyExpense.setBudget(stringDate, budget);
            log.addMonthlyExpenses(newMonthlyExpense);
        } else {
            System.out.println("A budget has already been set for this month. ");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds an expense to the corresponding month the expense was made
    private void addExpense() {
        System.out.print("Enter the month in the format, eg. January 2021, your expense was made: ");
        String stringDate = input.next();

        System.out.print("Enter a short description of your expense: ");
        String stringDesc = input.next();

        System.out.print("Enter the price spent for this expense in whole numbers (no decimals): ");
        int price = input.nextInt();

        if (price >= 0) {
            MonthlyExpenses monthlyExpenses = log.getMonthlyExpenses(stringDate);
            if (monthlyExpenses == null) {
                System.out.println("Please set a budget for this month before adding expenses.");
            } else {
                Expense expense = new Expense(stringDate, stringDesc, price);
                monthlyExpenses.addExpense(expense);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: return how much of the budget is remaining, value will be negative if budget limit has been surpassed
    private void viewBudgetRemaining() {
        System.out.print("Enter the month, eg. January 2021, for which you would like to view your remaining budget: ");
        String stringDate = input.next();

        MonthlyExpenses monthlyExpenses = log.getMonthlyExpenses(stringDate);
        if (monthlyExpenses == null) {
            System.out.println("Please set a budget for this month before adding expenses.");
        } else {
            int remainingBudget = monthlyExpenses.getBudgetRemaining();
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
        System.out.print("Enter the month, eg. January 2021, for which you would like to view all expenses made: ");
        String stringDate = input.next();

        MonthlyExpenses monthlyExpense = log.getMonthlyExpenses(stringDate);
        System.out.println("These are your expenses for " + stringDate);
        for (Expense e : monthlyExpense.getExpenses()) {
            int price = e.getPrice();
            String desc = e.getDescription();
            System.out.println(desc + ": price = $" + price);
        }
    }
}

