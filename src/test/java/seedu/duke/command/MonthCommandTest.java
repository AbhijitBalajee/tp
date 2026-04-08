package seedu.duke.command;

import org.junit.jupiter.api.Test;
import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MonthCommandTest {

    @Test
    public void execute_month_noCrash() {
        ExpenseList list = new ExpenseList();
        list.addExpense(new Expense("A", 5.0, "Food", LocalDate.now(), false));

        Ui ui = new Ui();
        MonthCommand cmd = new MonthCommand("2026-04");

        assertDoesNotThrow(() -> cmd.execute(list, ui));
    }
}
