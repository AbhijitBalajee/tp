package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RemainingCommandTest {

    private ExpenseList expenses;
    private Ui ui;

    @BeforeEach
    public void setUp() {
        expenses = new ExpenseList();
        ui = new Ui();
    }

    @Test
    public void remainingCommand_isNotExit() {
        assertFalse(new RemainingCommand().isExit());
    }

    @Test
    public void remainingCommand_noBudgetSet_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                new RemainingCommand().execute(expenses, ui));
    }

    @Test
    public void remainingCommand_withBudgetAndExpenses_executesWithoutError() throws SpendTrackException {
        expenses.setBudget(100.00);
        expenses.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.now()));
        new RemainingCommand().execute(expenses, ui);
    }
}
