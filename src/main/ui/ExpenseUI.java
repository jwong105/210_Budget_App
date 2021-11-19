package ui;

import model.Expense;
import model.MonthlyExpenses;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


// List of expenses window frame
public class ExpenseUI extends JDialog {
    private DefaultListModel<Expense> expenseModel;
    private MonthlyExpenses monthList;
    private final JDialog frame;
    protected Dialog parentFrame;

    private static final String addString = "Add Expense";
    private static final String removeString = "Remove Expense";

    // EFFECTS: constructs expense list and sets up button panel, and visual expense window
    public ExpenseUI(MonthlyExpenses selectedItem) {
        frame = new JDialog(parentFrame, "Budget App", true);
        frame.setLayout(new BorderLayout());
        frame.setTitle("Expenses");

        this.monthList = selectedItem;
        expenseModel = new DefaultListModel<>();

        addButtonPanel();
        addExpenseDisplayPanel();

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    // EFFECTS: constructs a visual expense window to display list of expenses for each month
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
            if (monthList.getBudget() == 0) {
                String txtBudget = JOptionPane.showInputDialog(null,
                        "Budget (in whole numbers)?", "Enter budget", JOptionPane.QUESTION_MESSAGE);
                int budget = Integer.parseInt(txtBudget);
                monthList.setBudget(monthList.getYear(), monthList.getMonth(), budget);
                JOptionPane.showMessageDialog(null,
                        "The budget for " + monthList.getMonthString(monthList.getMonth()) + " "
                                + monthList.getYear() + " has been set to $" + monthList.getBudget(),
                        "Budget has been set", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Budget already set for this date", "System Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

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