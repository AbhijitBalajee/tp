package seedu.duke.command;

import org.junit.jupiter.api.Test;
import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class LastCommandTest {

    @Test
    public void execute_last_noCrash() {
        ExpenseList list = new ExpenseList();
        list.addExpense(new Expense("A", 5.0, "Food", LocalDate.now(), false));
        list.addExpense(new Expense("B", 10.0, "Food", LocalDate.now(), false));

        Ui ui = new Ui();
        LastCommand cmd = new LastCommand(1);

        assertDoesNotThrow(() -> cmd.execute(list, ui));
    }
}
