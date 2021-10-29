package persistence;

import model.Expense;
import model.ExpenseLog;
import model.MonthlyExpenses;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ExpenseLog el = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyExpenseLog() {
        JsonReader reader = new JsonReader("./data/testWriterEmptyExpenseLog.json");
        try {
            ExpenseLog el = reader.read();
            assertEquals("Jennifer's expense log", el.getName());
            assertEquals(0, el.length());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }


    @Test
    void testReaderGeneralExpenseLog() {
        JsonReader reader = new JsonReader("./data/testWriterGeneralExpenseLog.json");
        try {
            ExpenseLog el = reader.read();
            assertEquals("Jennifer's expense log", el.getName());
            assertEquals(1, el.length());
            List<MonthlyExpenses> monthlyExpensesList = el.getMonthlyExpense();
            checkMonthlyExpenses(monthlyExpensesList.get(0), 2021, 10);
            List<Expense> expenseList = el.getMonthlyExpenses(2021, 10).getExpense();
            checkExpenses(2021, 10,"phone", 1200, expenseList.get(0));
            checkExpenses(2021, 10, "notebook", 20, expenseList.get(1));

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}