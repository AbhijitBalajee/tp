package seedu.duke.command;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TotalCommandTest {

    @Test
    public void getTotal_multipleExpenses_correctSum() {
        ExpenseList expenses = new ExpenseList();
        expenses.addExpense(new Expense("Lunch", 10.50, "Food", LocalDate.now()));
        expenses.addExpense(new Expense("Bus", 2.00, "Transport", LocalDate.now()));
        expenses.addExpense(new Expense("Coffee", 5.50, "Food", LocalDate.now()));

        assertEquals(18.00, expenses.getTotal(), 0.01);
    }

    @Test
    public void getTotal_noExpenses_returnsZero() {
        ExpenseList expenses = new ExpenseList();
        assertEquals(0.0, expenses.getTotal(), 0.01);
    }
}
