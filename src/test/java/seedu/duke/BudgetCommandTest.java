package seedu.duke;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.command.BudgetCommand;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BudgetCommandTest {

    private ExpenseList expenses;
    private Ui ui;

    @BeforeEach
    public void setUp() {
        expenses = new ExpenseList();
        ui = new Ui();
    }

    @Test
    public void budgetCommand_isNotExit() {
        BudgetCommand cmd = new BudgetCommand(500.00);
        assertFalse(cmd.isExit());
    }

    @Test
    public void budgetCommand_validAmount_setsCorrectly() throws SpendTrackException {
        BudgetCommand cmd = new BudgetCommand(500.00);
        cmd.execute(expenses, ui);
        assertEquals(500.00, expenses.getBudget(), 0.001);
    }

    @Test
    public void budgetCommand_negativeAmount_throwsException() {
        BudgetCommand cmd = new BudgetCommand(-100.00);
        assertThrows(SpendTrackException.class, () -> cmd.execute(expenses, ui));
    }

    @Test
    public void budgetCommand_zeroAmount_throwsException() {
        BudgetCommand cmd = new BudgetCommand(0.00);
        assertThrows(SpendTrackException.class, () -> cmd.execute(expenses, ui));
    }

    @Test
    public void budgetCommand_exceedsMax_throwsException() {
        BudgetCommand cmd = new BudgetCommand(2_000_000.00);
        assertThrows(SpendTrackException.class, () -> cmd.execute(expenses, ui));
    }

    @Test
    public void budgetCommand_withExistingExpenses_setsCorrectly() throws SpendTrackException {
        expenses.addExpense(new Expense("Lunch", 50.00, "Food", LocalDate.now()));
        BudgetCommand cmd = new BudgetCommand(30.00);
        cmd.execute(expenses, ui);
        assertEquals(30.00, expenses.getBudget(), 0.001);
    }
}
