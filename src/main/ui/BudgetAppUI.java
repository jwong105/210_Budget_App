package ui;

import model.Expense;
import model.ExpenseLog;
import model.MonthlyExpenses;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Represents application's main window frame.
 */
class BudgetAppUI extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private ExpenseLog log;
    private JFrame frame;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/expenseLog.json";
    private JList logList;
    private JPanel logPanel;
    private JTextArea logTextArea;
    private ArrayList<String> strings;
    private DefaultListModel<Expense> expenseModel;
    private DefaultListModel<DefaultListModel<Expense>> allExpenseListModel;

    /**
     * Constructor sets up button panel, and visual budget app window.
     */
    public BudgetAppUI() throws FileNotFoundException {
        log = new ExpenseLog("Jennifer's Budget App");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.addMouseListener(new DesktopFocusAction());

        frame.setTitle("Budget App");
        frame.setSize(WIDTH, HEIGHT);

        expenseModel = new DefaultListModel<>();
        allExpenseListModel = new DefaultListModel<>();

        addButtonPanel();
        addExpenseLogDisplayPanel(log, expenseModel);
        addExpenseDisplayPanel(log, expenseModel);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void addExpenseDisplayPanel(ExpenseLog log, DefaultListModel<Expense> expenseModel) {
        //Create and set up the window.
        ExpenseUI expenseUI = new ExpenseUI(log, expenseModel);
        frame.add(expenseUI, BorderLayout.EAST);
    }

    /**
     * Helper to set up visual alarm status window
     */
    private void addExpenseLogDisplayPanel(ExpenseLog log, DefaultListModel<Expense> expenseModel) {
        MonthlyExpensesUI monthlyExpensesUI = new MonthlyExpensesUI(log, expenseModel);
        frame.add(monthlyExpensesUI, BorderLayout.CENTER);
    }
//        logPanel = new JPanel();
//        logList = new JList();
//        logTextArea = new JTextArea();
//        frame.add(logPanel, BorderLayout.EAST);
//
//        logTextArea.setColumns(50);
//        logTextArea.setRows(75);
//
//        logPanel.add(logList);
//        logPanel.add(logTextArea);
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.add(logPanel);
//        frame.pack();
//        frame.setVisible(true);
//    }
//
//    private void logListValueChanged(javax.swing.event.ListSelectionEvent evt) {
//        int index = logList.getSelectedIndex();
//        for (String s: strings) {
//            if (index == strings.indexOf(s)) {
//                logList.getSelectedValue();
//            }
//        }
//    }

    /**
     * Helper to add control buttons.
     */
    private void addButtonPanel() {
        JPanel buttonPanelLeft = new JPanel();
        buttonPanelLeft.setLayout(new GridLayout(4, 1));
        buttonPanelLeft.add(new JButton(new SetBudget()));
//        buttonPanelLeft.add(new JButton(new AddExpense()));
//        buttonPanelLeft.add(new JButton(new RemoveExpense()));
        buttonPanelLeft.add(new JButton(new ViewBudgetRemaining()));
        buttonPanelLeft.add(new JButton(new SaveBudget()));
        buttonPanelLeft.add(new JButton(new LoadBudget()));

        frame.getContentPane().add(buttonPanelLeft, BorderLayout.WEST);
    }

    /**
     * Represents action to be taken when user wants to set the budget for a month
     */
    private class SetBudget extends AbstractAction {

        SetBudget() {
            super("Set Budget");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String txtMonth = JOptionPane.showInputDialog(null,
                    "Month, in format 1 to 12?", "Enter month", JOptionPane.QUESTION_MESSAGE);
            String txtYear = JOptionPane.showInputDialog(null,
                    "Year?", "Enter year", JOptionPane.QUESTION_MESSAGE);
            int month = Integer.parseInt(txtMonth);
            int year = Integer.parseInt(txtYear);

            MonthlyExpenses monthlyExpenses = log.getMonthlyExpenses(year, month);
            if (monthlyExpenses == null) {
                MonthlyExpenses newMonthlyExpense = new MonthlyExpenses(year, month);
                String txtBudget = JOptionPane.showInputDialog(null,
                        "Budget (in whole numbers)?", "Enter budget", JOptionPane.QUESTION_MESSAGE);
                int budget = Integer.parseInt(txtBudget);
                newMonthlyExpense.setBudget(year, month, budget);
                log.addMonthlyExpenses(newMonthlyExpense);
//                strings.add(log.getMonth(month) + " " + txtYear);
//
//                logList.setModel(new AbstractListModel() {
//
//                    @Override
//                    public int getSize() {
//                        return strings.size();
//                    }
//
//                    @Override
//                    public Object getElementAt(int i) {
//                        return strings.get(i);
//                    }
//                });
//                logList.addListSelectionListener(new ListSelectionListener() {
//
//                    @Override
//                    public void valueChanged(ListSelectionEvent evt) {
//                        logListValueChanged(evt);
//                    }
//                });
//
//            } else {
//                JOptionPane.showMessageDialog(null,
//                        "Budget already set for this date", "System Error", JOptionPane.ERROR_MESSAGE);
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
//            if (monthlyExpenses == null && price >= 0) {
//                JOptionPane.showMessageDialog(null, "Set a budget for this month first",
//                        "System Error", JOptionPane.ERROR_MESSAGE);
//            } else if (monthlyExpenses != null && price >= 0) {
//                Expense expense = new Expense(year, month, txtDescription, price);
//                monthlyExpenses.addExpense(expense);
//                insideStrings.add(txtDescription + ": price = $" + txtPrice);
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
//            if (monthlyExpenses == null) {
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

    /**
     * Represents action to be taken when user wants to set the budget for a month
     */
    private class SaveBudget extends AbstractAction {

        SaveBudget() {
            super("Save");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                jsonWriter.open();
                jsonWriter.write(log);
                jsonWriter.close();
                JOptionPane.showMessageDialog(null,
                        "Saved " + log.getName() + " to " + JSON_STORE, "Saved",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Unable to write to file: " + JSON_STORE,
                        "System Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Represents action to be taken when user wants to load the budget for a month
     */
    private class LoadBudget extends AbstractAction {

        LoadBudget() {
            super("Load");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                log = jsonReader.read();
                JOptionPane.showMessageDialog(null,
                        "Loaded " + log.getName() + " from " + JSON_STORE, "Loaded",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Unable to read from file: " + JSON_STORE,
                        "System Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Represents action to be taken when user wants to view the budget remaining for a month
     */
    private class ViewBudgetRemaining extends AbstractAction {

        ViewBudgetRemaining() {
            super("View Remaining Budget");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
        }
    }


    /**
     * Represents action to be taken when user clicks desktop
     * to switch focus. (Needed for key handling.)
     */
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            BudgetAppUI.this.requestFocusInWindow();
        }
    }

    // starts the application
    public static void main(String[] args) {
        try {
            new BudgetAppUI();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Unable to run application: file not found",
                    "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

