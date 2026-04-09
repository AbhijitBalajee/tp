package seedu.duke.command;

import org.junit.jupiter.api.Test;
import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ReportCommandTest {

    @Test
    public void execute_report_noCrash() throws Exception {
        ExpenseList list = new ExpenseList();
        list.addExpense(new Expense("A", 5.0, "Food", LocalDate.now(), false));

        Ui ui = new Ui();
        ReportCommand cmd = new ReportCommand("2026-04");

        assertDoesNotThrow(() -> cmd.execute(list, ui));
    }
}
