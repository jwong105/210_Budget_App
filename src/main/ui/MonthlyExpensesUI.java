package ui;

import model.Expense;
import model.ExpenseLog;
import model.MonthlyExpenses;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

// This MonthlyExpensesUI references code from these StackOverflow links
// Link: [https://stackoverflow.com/questions/5609200/adding-an-actionlistener-to-a-jlist]
// Link: [https://docs.oracle.com/javase/tutorial/uiswing/components/list.html]

// Expense log display panel in main Budget window frame
public class MonthlyExpensesUI extends JPanel {
    private JList list;
    protected DefaultListModel<MonthlyExpenses> monthlyModel;
    private ExpenseLog log;
    private DefaultListModel<Expense> expenseModel;

    private static final String setString = "Add Date";
    private JTextField txtMonth;
    private JTextField txtYear;
    private JButton setButton;
    private JScrollPane listScrollPane;
    private int intMonth;
    private int intYear;


    // MODIFIES: this
    // EFFECTS:
    public MonthlyExpensesUI(ExpenseLog log) {
        super(new BorderLayout());

        monthlyModel = new DefaultListModel();
        this.log = log;

        //Create the list and put it in a scroll pane.
        list = new JList(monthlyModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(5);
        listScrollPane = new JScrollPane(list);

        setButton = new JButton(setString);
        AddListener addListener = new AddListener(setButton);
        setButton.setActionCommand(setString);
        setButton.addActionListener(addListener);
        setButton.setEnabled(false);

        txtMonth = new JTextField(10);
        txtMonth.addActionListener(addListener);
        txtMonth.getDocument().addDocumentListener(addListener);

        txtYear = new JTextField(10);
        txtYear.addActionListener(addListener);
        txtYear.getDocument().addDocumentListener(addListener);

        createExpenseLogPanel();
    }

    // MODIFIES: this
    // EFFECTS: sets up visual panel to display list of dates in expense log
    public void createExpenseLogPanel() {
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(txtMonth);
        txtMonth.setText("month, 1 to 12");
        buttonPane.add(txtYear);
        txtYear.setText("year");
        buttonPane.add(setButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    // MODIFIES: this
    // EFFECTS: action to be taken when user wants to add dates to expense log
    class AddListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            String month = txtMonth.getText();
            intMonth = Integer.parseInt(month);
            String year = txtYear.getText();
            intYear = Integer.parseInt(year);

            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            addToExpenseLog();

            txtMonth.requestFocusInWindow();
            txtMonth.setText("month, 1 to 12");

            txtYear.requestFocusInWindow();
            txtYear.setText("year");

            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);

        }

        // MODIFIES: this
        // EFFECTS: adds date to expense log and opens new expense frame when clicked
        public void addToExpenseLog() {
            MonthlyExpenses monthlyExpenses = log.getMonthlyExpenses(intYear, intMonth);
            if (monthlyExpenses == null) {
                MonthlyExpenses newMonthlyExpenses = new MonthlyExpenses(intYear, intMonth);
                log.addMonthlyExpenses(newMonthlyExpenses);
                monthlyModel.addElement(newMonthlyExpenses);
                MouseListener mouseListener = new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {

                            String string = list.getSelectedValue().toString();
                            if (string.equals(newMonthlyExpenses.toString())) {
                                new ExpenseUI(newMonthlyExpenses);
                            }
                        }
                    }
                };
                list.addMouseListener(mouseListener);
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

        // EFFECTS: handles empty texts when adding date to expense log
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    public void createWithLog(ExpenseLog log) {
        for (MonthlyExpenses me : log.getMonthlyExpense()) {
            monthlyModel.addElement(me);
            MouseListener mouseListener = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        String string = list.getSelectedValue().toString();
                        if (string.equals(me.toString())) {
                            new ExpenseUI(me);
                        }
                    }
                }
            };
            list.addMouseListener(mouseListener);
        }
    }
}
