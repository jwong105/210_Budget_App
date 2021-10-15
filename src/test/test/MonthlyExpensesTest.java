package test;

import model.Expense;
import model.ExpenseLog;
import model.MonthlyExpenses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MonthlyExpensesTest {
    Expense e1;
    Expense e2;
    Expense e3;
    Expense e4;
    Expense e5;

    MonthlyExpenses m1;
    MonthlyExpenses m2;
    MonthlyExpenses m3;

    ExpenseLog log;

    @BeforeEach
    void runBefore() {
        e1 = new Expense("October 2021", "phone", 1200);
        e2 = new Expense("October 2021", "notebook", 20);
        e3 = new Expense("September 2021", "groceries", 60);
        e4 = new Expense("September 2021", "Ikea Furniture", 300);
        e5 = new Expense("April 2019", "Backpack", 30);

        m1 = new MonthlyExpenses("October 2021");
        m2 = new MonthlyExpenses("September 2021");
        m3 = new MonthlyExpenses("April 2019");

        log = new ExpenseLog();
    }

    @Test
    void testAddExpense() {
        m1.addExpense(e1);
        assertEquals(1, m1.length());
        assertTrue(m1.contains(e1));

        m1.addExpense(e2);
        assertEquals(2, m1.length());
        assertTrue(m1.contains(e2));

        m1.addExpense(e3);
        assertEquals(2, m1.length());
        assertFalse(m1.contains(e3));
    }

    @Test
    void testGetExpense() {
        m1.addExpense(e1);
        assertEquals(e1, m1.getExpense("phone"));
    }

    @Test
    void testSetBudget() {
        assertEquals(365, m1.setBudget("October 2021", 365));
        assertEquals(0, m2.setBudget("October 2021", 365));
        assertEquals(1900, m3.setBudget("April 2019", 1900));
    }

    @Test
    void testGetBudgetRemaining() {
        assertEquals(1201, m1.setBudget("October 2021",1201));
        m1.addExpense(e1);
        assertEquals(1,m1.getBudgetRemaining());

        m1.addExpense(e2);
        assertEquals(-19, m1.getBudgetRemaining());

        m2.setBudget("September 2021", 360);
        m2.addExpense(e3);
        m2.addExpense(e4);
        assertEquals(0, m2.getBudgetRemaining());

        m3.setBudget("April 2019", 50);
        m3.addExpense(e5);
        assertEquals(20, m3.getBudgetRemaining());
    }

    @Test
    void testAddMonthlyExpenses() {
        m1.addExpense(e1);
        m1.addExpense(e2);
        log.addMonthlyExpenses(m1);
        assertEquals(1, log.length());
        assertTrue(log.contains(m1));

        log.addMonthlyExpenses(m2);
        assertEquals(2, log.length());
        assertTrue(log.contains(m2));
    }

    @Test
    void testGetMonthlyExpenses() {
        m1.addExpense(e1);
        m1.addExpense(e2);
        log.addMonthlyExpenses(m1);
        log.getMonthlyExpenses("October 2021");
        assertTrue(log.contains(m1));

        assertEquals(null, log.getMonthlyExpenses("September 2019"));

        log.addMonthlyExpenses(m2);
        log.getMonthlyExpenses("September 2021");
        assertTrue(log.contains(m2));
    }
}
