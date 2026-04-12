// @@author AbhijitBalajee
package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link BudgetResetCommand} and {@link BudgetHistoryCommand}.
 */
class BudgetResetHistoryCommandTest {

    private ExpenseList expenses;
    private Ui ui;

    @BeforeEach
    void setUp() {
        expenses = new ExpenseList();
        ui = new Ui();
    }

    @Test
    void resetCommand_mutatesData_returnsTrue() {
        assertTrue(new BudgetResetCommand().mutatesData());
    }

    @Test
    void historyCommand_mutatesData_returnsFalse() {
        assertFalse(new BudgetHistoryCommand().mutatesData());
    }

    @Test
    void reset_withNoBudget_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> new BudgetResetCommand().execute(expenses, ui));
    }

    @Test
    void reset_withBudget_clearsBudgetAndAppendsHistory() throws SpendTrackException {
        expenses.setBudget(100.00);
        int historySize = expenses.getBudgetHistory().size();
        new BudgetResetCommand().execute(expenses, ui);
        assertEquals(0.0, expenses.getBudget(), 0.001);
        assertFalse(expenses.hasBudget());
        assertEquals(historySize + 1, expenses.getBudgetHistory().size());
    }

    @Test
    void historyCommand_emptyHistory_doesNotThrow() {
        assertDoesNotThrow(() -> new BudgetHistoryCommand().execute(expenses, ui));
    }

    @Test
    void historyCommand_withHistory_doesNotThrow() throws SpendTrackException {
        expenses.setBudget(50.00);
        assertDoesNotThrow(() -> new BudgetHistoryCommand().execute(expenses, ui));
    }
}
// @@author
