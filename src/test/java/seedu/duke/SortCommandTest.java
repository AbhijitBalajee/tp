package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.command.SortCommand;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SortCommandTest {

    @Test
    public void execute_sort_noCrash() throws Exception {
        ExpenseList list = new ExpenseList();
        list.addExpense(new Expense("A", 5.0, "Food", LocalDate.now(), false));
        list.addExpense(new Expense("B", 10.0, "Food", LocalDate.now(), false));

        Ui ui = new Ui();
        SortCommand cmd = new SortCommand();

        assertDoesNotThrow(() -> cmd.execute(list, ui));
    }
}
