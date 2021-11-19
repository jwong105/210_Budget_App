package ui;

import model.Expense;
import model.ExpenseLog;
import model.MonthlyExpenses;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

    public ExpensePanelUI(MonthlyExpenses monthList, DefaultListModel<Expense> expenseModel) {
        super(new BorderLayout());
        this.expenseModel = expenseModel;
        this.monthList = monthList;

        list = new JList(this.expenseModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(5);
        listScrollPane = new JScrollPane(list);

        addButtons();
        createExpenseLogPanel();

    }

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
        buttonPane.add(txtMonth);
        txtMonth.setText("month, 1 to 12");
        buttonPane.add(txtYear);
        txtYear.setText("year");
        buttonPane.add(txtDescription);
        txtDescription.setText("desc");
        buttonPane.add(txtPrice);
        txtPrice.setText("price");
        buttonPane.add(addButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }


    class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String month = txtMonth.getText();
            String year = txtYear.getText();

            int intMonth = Integer.parseInt(month);
            int intYear = Integer.parseInt(year);

            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            String string = list.getSelectedValue().toString();
            ArrayList<Expense> remove = new ArrayList<>();
            for (Expense exp : monthList.getExpenses()) {
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
            String year = txtYear.getText();
            String description = txtDescription.getText();
            String price = txtPrice.getText();

            int intMonth = Integer.parseInt(month);
            int intYear = Integer.parseInt(year);
            int intPrice = Integer.parseInt(price);

            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            if (monthList.getBudget() == 0 && intPrice >= 0) {
                JOptionPane.showMessageDialog(null, "Set a budget for this month first",
                        "System Error", JOptionPane.ERROR_MESSAGE);
            } else if (intPrice >= 0) {
                Expense expense = new Expense(intYear, intMonth, description, intPrice);
                monthList.addExpense(expense);
                expenseModel.addElement(expense);
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a valid price",
                        "System Error", JOptionPane.ERROR_MESSAGE);
            }

//                DefaultListModel<Expense> expenseModel = new DefaultListModel<>();
//                for (Expense ex : monthlyExpenses.getExpense()) {
//                    expenseModel.addElement(ex);
//                }
//
//                list = new JList(expenseModel);
//                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//                list.setSelectedIndex(0);
//                list.setVisibleRowCount(5);


//            listModel.addElement(txtDescription.getText() + " : price = $" + txtPrice.getText());
            //If we just wanted to add to the end, we'd do this:
            //listModel.addElement(employeeName.getText());

            txtDescription.requestFocusInWindow();
            txtDescription.setText("desc");

            txtPrice.requestFocusInWindow();
            txtPrice.setText("price");

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

    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
                //No selection, disable fire button.
                removeButton.setEnabled(false);

            } else {
                //Selection, enable the fire button.
                removeButton.setEnabled(true);
            }
        }
    }
}

