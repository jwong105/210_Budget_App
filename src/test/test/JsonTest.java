package test;

import model.Expense;
import model.MonthlyExpenses;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkMonthlyExpenses(MonthlyExpenses m, int year, int month) {
        assertEquals(year, m.getYear());
        assertEquals(month, m.getMonth());
    }

    protected void checkExpenses(int year, int month, String description, int price, Expense e) {
        assertEquals(description, e.getDescription());
        assertEquals(price,e.getPrice());
        assertEquals(YearMonth.of(year, month), e.getDate());
    }
}