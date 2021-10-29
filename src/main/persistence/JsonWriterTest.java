package persistence;

import model.Expense;
import model.ExpenseLog;
import model.MonthlyExpenses;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            ExpenseLog el = new ExpenseLog("Jennifer's expense log");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            ExpenseLog el = new ExpenseLog("Jennifer's expense log");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyExpenseLog.json");
            writer.open();
            writer.write(el);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyExpenseLog.json");
            el = reader.read();
            assertEquals("Jennifer's expense log", el.getName());
            assertEquals(0, el.length());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            ExpenseLog el = new ExpenseLog("Jennifer's expense log");
            MonthlyExpenses m1 = new MonthlyExpenses(2021, 10);
            Expense e1 = new Expense(2021, 10, "phone", 1200);
            Expense e2 = new Expense(2021, 10, "notebook", 20);
            m1.addExpense(e1);
            m1.addExpense(e2);
            el.addMonthlyExpenses(m1);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralExpenseLog.json");
            writer.open();
            writer.write(el);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralExpenseLog.json");
            el = reader.read();
            assertEquals("Jennifer's expense log", el.getName());
            List<MonthlyExpenses> monthlyExpensesList = el.getMonthlyExpense();
            checkMonthlyExpenses(monthlyExpensesList.get(0), 2021, 10);
            List<Expense> expenseList = el.getMonthlyExpenses(2021, 10).getExpense();
            checkExpenses(2021, 10,"phone", 1200, expenseList.get(0));
            checkExpenses(2021, 10, "notebook", 20, expenseList.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}