package ui;

import model.Expense;
import model.MonthlyExpenses;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// This ExpensePanelUI references code from these StackOverflow links
// Link: [https://docs.oracle.com/javase/tutorial/uiswing/components/list.html]
// Link: [https://docs.oracle.com/javase/tutorial/displayCode.html?code=
// https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDemoProject/src/components/ListDemo.java]

// Expense display panel in ExpenseUI Dialog
public class ExpensePanelUI extends JPanel {
    private JList list;
    private DefaultListModel<Expense> expenseModel;

    private static final String addString = "Add Expense";
    private static final String removeString = "Remove Expense";
    private JButton removeButton;
    private JTextField txtMonth;
    private JTextField txtYear;
    private JTextField txtDescription;
    private JTextField txtPrice;
    private JButton addButton;
    private JScrollPane listScrollPane;
    private MonthlyExpenses monthList;

    // EFFECTS: constructs expense list panel
    public ExpensePanelUI(MonthlyExpenses monthList, DefaultListModel<Expense> expenseModel) {
        super(new BorderLayout());
        this.monthList = monthList;
        this.expenseModel = expenseModel;

        for (Expense exp: monthList.getExpenses()) {
            expenseModel.addElement(exp);
        }

        list = new JList(this.expenseModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(5);
        listScrollPane = new JScrollPane(list);

        addButtons();
        createExpenseLogPanel();
    }

    // MODIFIES: this
    // EFFECTS: sets up buttons to input expense information
    public void addButtons() {
        addButton = new JButton(addString);
        AddListener addListener = new AddListener(addButton);
        addButton.setActionCommand(addString);
        addButton.addActionListener(addListener);
        addButton.setEnabled(false);

        removeButton = new JButton(removeString);
        removeButton.setActionCommand(removeString);
        removeButton.addActionListener(new RemoveListener());

        txtMonth = new JTextField(10);
        txtMonth.addActionListener(addListener);
        txtMonth.getDocument().addDocumentListener(addListener);

        txtYear = new JTextField(10);
        txtYear.addActionListener(addListener);
        txtYear.getDocument().addDocumentListener(addListener);

        txtDescription = new JTextField(10);
        txtDescription.addActionListener(addListener);
        txtDescription.getDocument().addDocumentListener(addListener);

        txtPrice = new JTextField(10);
        txtPrice.addActionListener(addListener);
        txtPrice.getDocument().addDocumentListener(addListener);
    }

    // MODIFIES: this
    // EFFECTS: sets up visual panel to display list of expenses
    public void createExpenseLogPanel() {
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        buttonPane.add(removeButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(txtDescription);
        txtDescription.setText("desc");
        buttonPane.add(txtPrice);
        txtPrice.setText("price");
        buttonPane.add(addButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    // RemoveListener to remove expenses from expense panel
    class RemoveListener implements ActionListener {

        // MODIFIES: this, ExpenseUI
        // EFFECTS: action to be taken when user wants to remove expenses
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            String string = list.getSelectedValue().toString();
            ArrayList<Expense> remove = new ArrayList<>();

            for (Expense exp: monthList.getExpenses()) {
                if (string.equals(exp.toString())) {
                    remove.add(exp);
                    monthList.addBackExpense(exp);
                }
                monthList.getExpenses().removeAll(remove);
                expenseModel.remove(index);
            }

            int size = expenseModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                removeButton.setEnabled(false);

            } else { //Select an index.
                if (index == expenseModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    // AddListener to input expenses into expense panel
    class AddListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        // MODIFIES: this
        // EFFECTS: adds listener button
        public AddListener(JButton button) {
            this.button = button;
        }

        // MODIFIES: this
        // EFFECTS: action to be taken when user wants to add expenses to month
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            if (index == -1) {
                index = 0;
            } else {
                index++;
            }
            addToExpenseList();

            txtDescription.requestFocusInWindow();
            txtDescription.setText("desc");

            txtPrice.requestFocusInWindow();
            txtPrice.setText("price");

            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        // MODIFIES: this, ExpenseUI
        // EFFECTS: adds expenses to month
        public void addToExpenseList() {
            String description = txtDescription.getText();
            String price = txtPrice.getText();

            int intPrice = Integer.parseInt(price);

            if (monthList.getBudget() == 0 && intPrice >= 0) {
                JOptionPane.showMessageDialog(null, "Set a budget for this month first",
                        "System Error", JOptionPane.ERROR_MESSAGE);
            } else if (intPrice >= 0) {
                Expense expense = new Expense(monthList.getYear(), monthList.getMonth(), description, intPrice);
                monthList.addExpense(expense);
                expenseModel.addElement(expense);
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a valid price",
                        "System Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // EFFECTS: inserts update for DocumentListener
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        // EFFECTS: removes update for DocumentListener
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        // EFFECTS: updates when inserted text changes for DocumentListener
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        // EFFECTS: enables add date button
        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        // EFFECTS: returns true when text field is empty
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    // EFFECTS: enables remove button if item is selected
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
                removeButton.setEnabled(false);
            } else {
                removeButton.setEnabled(true);
            }
        }
    }
}

