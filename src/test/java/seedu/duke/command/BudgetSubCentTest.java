// @@author AfshalG
package seedu.duke.command;

import org.junit.jupiter.api.Test;

import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BudgetSubCentTest {

    @Test
    void execute_subCentBudget_throwsException() {
        // 0.001 is > 0 but rounds to $0.00 in display — should be rejected
        BudgetCommand cmd = new BudgetCommand(0.001);
        SpendTrackException e = assertThrows(SpendTrackException.class,
                () -> cmd.execute(new ExpenseList(), new Ui()));
        assertTrue(e.getMessage().contains("0.01") || e.getMessage().toLowerCase().contains("cent"),
                "Error should mention minimum, got: " + e.getMessage());
    }

    @Test
    void execute_verySmallBudget_throwsException() {
        BudgetCommand cmd = new BudgetCommand(0.004);
        assertThrows(SpendTrackException.class,
                () -> cmd.execute(new ExpenseList(), new Ui()));
    }

    @Test
    void execute_oneCentBudget_succeeds() {
        // 0.01 is the minimum valid budget
        BudgetCommand cmd = new BudgetCommand(0.01);
        assertDoesNotThrow(() -> cmd.execute(new ExpenseList(), new Ui()));
    }

    @Test
    void execute_normalBudget_succeeds() {
        BudgetCommand cmd = new BudgetCommand(500.0);
        assertDoesNotThrow(() -> cmd.execute(new ExpenseList(), new Ui()));
    }
}
