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
        e1 = new Expense(2021, 10, "phone", 1200);
        e2 = new Expense(2021, 10, "notebook", 20);
        e3 = new Expense(2021, 9, "groceries", 60);
        e4 = new Expense(2021, 9, "Ikea Furniture", 300);
        e5 = new Expense(2019, 4, "Backpack", 30);

        m1 = new MonthlyExpenses(2021, 10);
        m2 = new MonthlyExpenses(2021, 9);
        m3 = new MonthlyExpenses(2019, 4);

        log = new ExpenseLog("Jennifer's expense log");
    }

    @Test
    void testAddExpense() {
        m1.addExpense(e1);
        assertEquals(1, m1.length());
        assertTrue(m1.contains(e1));
        assertEquals("phone", e1.getDescription());

        m1.addExpense(e2);
        assertEquals(2, m1.length());
        assertTrue(m1.contains(e2));
        assertEquals("notebook", e2.getDescription());

        m1.addExpense(e3);
        assertEquals(2, m1.length());
        assertFalse(m1.contains(e3));
    }

    @Test
    void testRemoveExpenses() {
        m1.addExpense(e1);
        m1.addExpense(e2);
        m1.removeExpenses("phone", 1200, e1);

        assertEquals(1, m1.length());
        assertTrue(m1.contains(e2));
        assertTrue(!m1.contains(e1));
        assertEquals("notebook", e2.getDescription());

        m1.removeExpenses("notebook", 400, e2);
        assertTrue(m1.contains(e2));

        m1.removeExpenses("phone", 20, e2);
        assertTrue(m1.contains(e2));

        m1.removeExpenses("notebook", 20, e2);
        assertTrue(!m1.contains(e2));
        assertEquals(0, m1.length());
    }

    @Test
    void testAddBackExpense() {
        m1.setBudget(2021, 10, 365);
        m1.addExpense(e1);
        m1.addExpense(e2);
        m1.addBackExpense(e1);
        m1.removeExpenses("phone", 1200, e1);
        assertEquals(365 - 20, m1.getBudgetRemaining());

        m1.addBackExpense(e2);
        m1.removeExpenses("notebook", 20, e2);
        assertEquals(365, m1.getBudgetRemaining());

        m2.setBudget(2021, 9, 100);
        m2.addBackExpense(e3);
        assertEquals(100, m2.getBudgetRemaining());
    }

    @Test
    void testSetBudget() {
        m1.setBudget(2021, 10, 365);
        m2.setBudget(2021, 10, 365);
        m3.setBudget(2019, 4, 1900);
        assertEquals(365, m1.getBudget());
        assertEquals(0, m2.getBudget());
        assertEquals(1900, m3.getBudget());
    }

    @Test
    void testGetBudgetRemaining() {
        m1.setBudget(2021, 10,1201);
        assertEquals(1201,m1.getBudget() );
        m1.addExpense(e1);
        assertEquals(1,m1.getBudgetRemaining());

        m1.addExpense(e2);
        assertEquals(-19, m1.getBudgetRemaining());

        m2.setBudget(2021, 9, 360);
        m2.addExpense(e3);
        m2.addExpense(e4);
        assertEquals(360 - (300 + 60), m2.getBudgetRemaining());

        m3.setBudget(2019, 4, 50);
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
        assertEquals("October", log.getMonth(10));
        assertEquals("Jennifer's expense log", log.getName());

        log.addMonthlyExpenses(m2);
        assertEquals(2, log.length());
        assertTrue(log.contains(m2));
        assertEquals("September", log.getMonth(9));
        assertEquals("Jennifer's expense log", log.getName());
    }

    @Test
    void testGetMonthlyExpenses() {
        m1.addExpense(e1);
        m1.addExpense(e2);
        log.addMonthlyExpenses(m1);
        log.getMonthlyExpenses(2021, 10);
        assertTrue(log.contains(m1));
        m1.getExpenses();
        assertTrue(m1.contains(e1));
        assertTrue(m1.contains(e2));
        m1.removeExpenses("phone", 1200, e1);
        assertFalse(m1.contains(e1));

        assertEquals(null, log.getMonthlyExpenses(2019, 9));

        m2.addExpense(e2);
        log.addMonthlyExpenses(m2);
        log.getMonthlyExpenses(2021, 9);
        assertTrue(log.contains(m2));
        assertFalse(m2.contains(e3));
        assertFalse(m2.contains(e2));
    }
}
