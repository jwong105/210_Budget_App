package ui;

import model.Expense;
import model.ExpenseLog;
import model.MonthlyExpenses;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

// Budget main window frame
class BudgetAppUI extends JFrame {
    private static final int WIDTH = 950;
    private static final int HEIGHT = 750;
    private ExpenseLog log;
    private JFrame frame;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/expenseLog.json";
    private DefaultListModel<Expense> expenseModel;
    private JPanel logoPanel;
    private JPanel buttonPanelLeft;

    // EFFECTS: constructs expense log and sets up button panel, and visual budget app window to display months
    public BudgetAppUI() throws FileNotFoundException {
        log = new ExpenseLog("Budget App");
        jsonWriter = new JsonWriter(JSON_STORE);
        frame = new JFrame();
        frame.setLayout(new BorderLayout());

        frame.setTitle("Budget App");
        frame.setSize(WIDTH, HEIGHT);

        expenseModel = new DefaultListModel<>();

        addSidePanel();
        addExpenseLogDisplayPanel(log, expenseModel);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: sets up visual budget app window to display list of dates in expense log
    private void addExpenseLogDisplayPanel(ExpenseLog log, DefaultListModel<Expense> expenseModel) {
        MonthlyExpensesUI monthlyExpensesUI = new MonthlyExpensesUI(log, expenseModel);
        frame.add(monthlyExpensesUI, BorderLayout.CENTER);
    }

    private JPanel addLogoPanel() {
        logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.add(addLogo());
        return logoPanel;
    }

    private JLabel addLogo() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("./image/Untitled.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel logoImage = new JLabel(new ImageIcon(image));
        return logoImage;
    }

    // MODIFIES: this
    // EFFECTS: adds control buttons to main window
    private JPanel addButtonPanel() {
        buttonPanelLeft = new JPanel();
        buttonPanelLeft.setLayout(new GridLayout(2, 1));
        buttonPanelLeft.add(new JButton(new SaveBudget()));
        buttonPanelLeft.add(new JButton(new LoadBudget()));
        return buttonPanelLeft;
    }

    private void addSidePanel() {
        JPanel sidePanel = new JPanel();
        GridLayout grid = new GridLayout(2,1);
        sidePanel.setLayout(grid);
        sidePanel.setSize(new Dimension(300,750));
        sidePanel.add(addLogoPanel());
        sidePanel.add(addButtonPanel());

        frame.getContentPane().add(sidePanel, BorderLayout.WEST);
    }

    // Save expense log
    private class SaveBudget extends AbstractAction {

        // Constructs button to save expense log
        SaveBudget() {
            super("        Save        ");
        }

        // MODIFIES: this
        // EFFECTS: action to be taken when user wants to save expense log
        @Override
        public void actionPerformed(ActionEvent evt) {
            jsonWriter = new JsonWriter(JSON_STORE);
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

    // Load expense log
    private class LoadBudget extends AbstractAction {

        // Constructs button to load expense log
        LoadBudget() {
            super("        Load        ");
        }

        // MODIFIES: this
        // EFFECTS: action to be taken when user wants to load expense log
        @Override
        public void actionPerformed(ActionEvent evt) {
//            JFileChooser fileChooser = new JFileChooser("./data");
//            int choice = fileChooser.showOpenDialog(frame);
//            if (choice == JFileChooser.APPROVE_OPTION) {
//                String file = fileChooser.getSelectedFile().getAbsolutePath();
//                jsonReader = new JsonReader(file);
            jsonReader = new JsonReader(JSON_STORE);
//            }
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

    // Starts the application
    public static void main(String[] args) {
        try {
            new BudgetAppUI();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Unable to run application: file not found",
                    "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

