package ui;

import model.Expense;
import model.ExpenseLog;
import model.MonthlyExpenses;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/* ListDemo.java requires no other files. */
public class MonthlyExpensesUI extends JPanel {
    private JList list;
    private DefaultListModel<MonthlyExpenses> monthlyModel;
    private ExpenseLog log;
    private DefaultListModel<Expense> expenseModel;

    private static final String setString = "Add Date";
    private JTextField txtMonth;
    private JTextField txtYear;

    public MonthlyExpensesUI(ExpenseLog log, DefaultListModel<Expense> expenseModel) {
        super(new BorderLayout());

        monthlyModel = new DefaultListModel();
        this.log = log;
        this.expenseModel = expenseModel;

        //Create the list and put it in a scroll pane.
        list = new JList(monthlyModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        JButton setButton = new JButton(setString);
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

        //Create a panel that uses BoxLayout.
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
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    //This listener is shared by the text field and the hire button.
    class AddListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            String month = txtMonth.getText();
            int intMonth = Integer.parseInt(month);
            String year = txtYear.getText();
            int intYear = Integer.parseInt(year);

            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            // create new monthly expense if it doesn't exist
            MonthlyExpenses monthlyExpenses = log.getMonthlyExpenses(intYear, intMonth);
            if (monthlyExpenses == null) {
                MonthlyExpenses newMonthlyExpenses = new MonthlyExpenses(intYear, intMonth);
                log.addMonthlyExpenses(newMonthlyExpenses);
                monthlyModel.addElement(newMonthlyExpenses);
            }

            //If we just wanted to add to the end, we'd do this:
            //listModel.addElement(employeeName.getText());

            txtMonth.requestFocusInWindow();
            txtMonth.setText("month, 1 to 12");

            txtYear.requestFocusInWindow();
            txtYear.setText("year");

            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }
//
//    /**
//     * Create the GUI and show it.  For thread safety,
//     * this method should be invoked from the
//     * event-dispatching thread.
//     */
//    private static void createAndShowGUI() {
//        //Create and set up the window.
//        JFrame frame = new JFrame("Monthly Expenses");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        //Create and set up the content pane.
//        JComponent newContentPane = new ExpenseUI();
//        newContentPane.setOpaque(true); //content panes must be opaque
//        frame.setContentPane(newContentPane);
//
//        //Display the window.
//        frame.pack();
//        frame.setVisible(true);
//    }
}
