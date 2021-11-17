package test;

import model.Expense;
import model.ExpenseLog;
import model.MonthlyExpenses;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriteTest extends JsonTest {

    @Test
    void testWriterInvalidExpenseLog() {
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
    void testWriterEmptyExpenseLog() {
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
    void testWriterNotEmptyExpenseLog() {
        try {
            ExpenseLog el = new ExpenseLog("Jennifer's expense log");
            MonthlyExpenses m1 = new MonthlyExpenses(2021, 10);
            Expense e1 = new Expense(2021, 10, "phone", 1200);
            Expense e2 = new Expense(2021, 10, "notebook", 20);
            m1.addExpense(e1);
            m1.addExpense(e2);
            el.addMonthlyExpenses(m1);
            JsonWriter writer = new JsonWriter("./data/testWriterNotEmptyExpenseLog.json");
            writer.open();
            writer.write(el);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterNotEmptyExpenseLog.json");
            el = reader.read();
            assertEquals("Jennifer's expense log", el.getName());
            List<MonthlyExpenses> monthlyExpensesList = el.getMonthlyExpense();
            checkMonthlyExpenses(monthlyExpensesList.get(0), 2021, 10);
            List<Expense> expenseList = el.getMonthlyExpenses(2021, 10).getExpense();
            checkExpenses(2021, 10, "phone", 1200, expenseList.get(0));
            checkExpenses(2021, 10, "notebook", 20, expenseList.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
