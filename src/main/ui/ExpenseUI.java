package ui;

import model.Expense;
import model.ExpenseLog;
import model.MonthlyExpenses;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


// List of expenses window frame
public class ExpenseUI extends JFrame {
    private DefaultListModel<Expense> expenseModel;
    private MonthlyExpenses monthList;
    private JFrame frame;

    private static final String addString = "Add Expense";
    private static final String removeString = "Remove Expense";

    // MODIFIES: this
    // EFFECTS: constructs expense list and sets up button panel, and visual expense window
    public ExpenseUI(MonthlyExpenses selectedItem, DefaultListModel<Expense> expenseModel) {
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setTitle("Expenses");
        frame.setSize(950, 750);

        this.expenseModel = expenseModel;
        this.monthList = selectedItem;

        addButtonPanel();
        addExpenseDisplayPanel();

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: sets up visual expense window to display list of expenses for each month
    private void addExpenseDisplayPanel() {
        ExpensePanelUI expensePanelUI = new ExpensePanelUI(monthList, expenseModel);
        frame.add(expensePanelUI, BorderLayout.CENTER);

    }

    // MODIFIES: this
    // EFFECTS: adds control buttons to expense window
    private void addButtonPanel() {
        JPanel buttonPanelLeft = new JPanel();
        buttonPanelLeft.setLayout(new GridLayout(2, 1));
        buttonPanelLeft.add(new JButton(new SetBudget()));
        buttonPanelLeft.add(new JButton(new ViewBudgetRemaining()));

        frame.getContentPane().add(buttonPanelLeft, BorderLayout.WEST);
    }

    // Set the budget for the month
    private class SetBudget extends AbstractAction {

        // Constructs button to set budget
        SetBudget() {
            super("Set Budget");
        }

        // MODIFIES: this
        // EFFECTS: action to be taken when user wants to set the budget for a month
        @Override
        public void actionPerformed(ActionEvent evt) {
            String txtMonth = JOptionPane.showInputDialog(null,
                    "Month, in format 1 to 12?", "Enter month", JOptionPane.QUESTION_MESSAGE);
            String txtYear = JOptionPane.showInputDialog(null,
                    "Year?", "Enter year", JOptionPane.QUESTION_MESSAGE);
            int month = Integer.parseInt(txtMonth);
            int year = Integer.parseInt(txtYear);

            if (monthList.getBudget() == 0) {
                String txtBudget = JOptionPane.showInputDialog(null,
                        "Budget (in whole numbers)?", "Enter budget", JOptionPane.QUESTION_MESSAGE);
                int budget = Integer.parseInt(txtBudget);
                monthList.setBudget(year, month, budget);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Budget already set for this date", "System Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

//    /**
//     * Represents action to be taken when user wants to set the budget for a month
//     */
//    private class AddExpense extends AbstractAction {
//
//        AddExpense() {
//            super("Add Expense");
//        }
//
//        @Override
//        public void actionPerformed(ActionEvent evt) {
//            String txtMonth = JOptionPane.showInputDialog(null,
//                    "Month, in format 1 to 12?", "Enter month of purchase", JOptionPane.QUESTION_MESSAGE);
//            String txtYear = JOptionPane.showInputDialog(null, "Year?",
//                    "Enter year of purchase", JOptionPane.QUESTION_MESSAGE);
//            int month = Integer.parseInt(txtMonth);
//            int year = Integer.parseInt(txtYear);
//
//            String txtDescription = JOptionPane.showInputDialog(null,
//                    "Short description?", "Enter description of purchase", JOptionPane.QUESTION_MESSAGE);
//            String txtPrice = JOptionPane.showInputDialog(null,
//                    "Price (in whole numbers)?", "Enter price of purchase", JOptionPane.QUESTION_MESSAGE);
//            int price = Integer.parseInt(txtPrice);
//
//            MonthlyExpenses monthlyExpenses = log.getMonthlyExpenses(year, month);
//            if (monthlyExpenses.getBudget() == 0 && price >= 0) {
//                JOptionPane.showMessageDialog(null, "Set a budget for this month first",
//                        "System Error", JOptionPane.ERROR_MESSAGE);
//            } else if (price >= 0) {
//                Expense expense = new Expense(year, month, txtDescription, price);
//                monthlyExpenses.addExpense(expense);
//            } else {
//                JOptionPane.showMessageDialog(null, "Please enter a valid price",
//                        "System Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }
//
//    /**
//     * Represents action to be taken when user wants to set the budget for a month
//     */
//    private class RemoveExpense extends AbstractAction {
//
//        RemoveExpense() {
//            super("Remove Expense");
//        }
//
//        @Override
//        public void actionPerformed(ActionEvent evt) {
//            String txtMonth = JOptionPane.showInputDialog(null,
//                    "Month, in format 1 to 12?", "Enter month of purchase", JOptionPane.QUESTION_MESSAGE);
//            String txtYear = JOptionPane.showInputDialog(null, "Year?",
//                    "Enter year of purchase", JOptionPane.QUESTION_MESSAGE);
//            int month = Integer.parseInt(txtMonth);
//            int year = Integer.parseInt(txtYear);
//
//            String txtDescription = JOptionPane.showInputDialog(null,
//                    "Short description?", "Enter description of purchase", JOptionPane.QUESTION_MESSAGE);
//            String txtPrice = JOptionPane.showInputDialog(null,
//                    "Price (in whole numbers)?", "Enter price of purchase", JOptionPane.QUESTION_MESSAGE);
//            int price = Integer.parseInt(txtPrice);
//
//            MonthlyExpenses monthlyExpenses = log.getMonthlyExpenses(year, month);
//            if (monthlyExpenses.getBudget() == 0) {
//                JOptionPane.showMessageDialog(null, "Set a budget for this month first",
//                        "System Error", JOptionPane.ERROR_MESSAGE);
//            } else if (month == monthlyExpenses.getMonth() && year == monthlyExpenses.getYear()) {
//                ArrayList<Expense> remove = new ArrayList<>();
//                for (Expense e : monthlyExpenses.getExpenses()) {
//                    if (txtDescription.equals(e.getDescription()) && price == e.getPrice()) {
//                        remove.add(e);
//                        monthlyExpenses.addBackExpense(e);
//                    }
//                }
//                monthlyExpenses.getExpenses().removeAll(remove);
//            } else {
//                JOptionPane.showMessageDialog(null, "Please enter a valid expense",
//                        "System Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }

    // View the remaining budget for the month
    private class ViewBudgetRemaining extends AbstractAction {

        // Constructs button to view remaining budget
        ViewBudgetRemaining() {
            super("View Budget Remaining");
        }

        // MODIFIES: this
        // EFFECTS: action to be taken when user wants to view the remaining budget for the month
        @Override
        public void actionPerformed(ActionEvent evt) {
            String txtMonth = JOptionPane.showInputDialog(null,
                    "Month, in format 1 to 12?", "Enter month of purchase", JOptionPane.QUESTION_MESSAGE);
            String txtYear = JOptionPane.showInputDialog(null, "Year?",
                    "Enter year of purchase", JOptionPane.QUESTION_MESSAGE);
            int month = Integer.parseInt(txtMonth);
            int year = Integer.parseInt(txtYear);

            if (monthList.getBudget() == 0) {
                JOptionPane.showMessageDialog(null, "Set a budget for this month first",
                        "System Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int remainingBudget = monthList.getBudgetRemaining();
                if (remainingBudget >= 0) {
                    JOptionPane.showMessageDialog(null,
                            "You have $" + remainingBudget + " remaining in your budget.",
                            "Within Budget", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "You have gone over your set budget for this month by $" + remainingBudget,
                            "Exceeded Budget", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
}