package seedu.duke;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import seedu.duke.command.ListCommand;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ListCommandTest {

    @Test
    public void listCommand_isNotExit() {
        ListCommand cmd = new ListCommand();
        assertFalse(cmd.isExit());
    }

    @Test
    public void listCommand_executesOnEmptyList() {
        ExpenseList expenses = new ExpenseList();
        // Just checks it doesn't throw on empty list
        ListCommand cmd = new ListCommand();
        cmd.execute(expenses, new Ui());
    }

    @Test
    public void listCommand_executesWithExpenses() {
        ExpenseList expenses = new ExpenseList();
        expenses.addExpense(new Expense("Lunch", 5.50, "Food", LocalDate.now()));
        expenses.addExpense(new Expense("Bus", 1.20, "Transport", LocalDate.now()));
        ListCommand cmd = new ListCommand();
        cmd.execute(expenses, new Ui());
    }
}
