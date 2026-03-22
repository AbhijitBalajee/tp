package seedu.duke.command;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeleteCommandTest {

    private ExpenseList expenses;
    private Ui ui;

    @BeforeEach
    void setUp() throws SpendTrackException {
        expenses = new ExpenseList();
        ui = new Ui();
        new AddCommand("Coffee", 4.50, "Food", LocalDate.now()).execute(expenses, ui);
        new AddCommand("MRT top-up", 10.00, "Transport", LocalDate.now()).execute(expenses, ui);
        new AddCommand("Textbook", 35.00, "Education", LocalDate.now()).execute(expenses, ui);
    }

    @Test
    void execute_validIndex_removesExpense() throws SpendTrackException {
        new DeleteCommand(2).execute(expenses, ui);
        assertEquals(2, expenses.size());
    }

    @Test
    void execute_validIndex_removesCorrectExpense() throws SpendTrackException {
        new DeleteCommand(1).execute(expenses, ui);
        assertEquals("MRT top-up", expenses.getExpense(0).getDescription());
    }

    @Test
    void execute_lastIndex_removesLastExpense() throws SpendTrackException {
        new DeleteCommand(3).execute(expenses, ui);
        assertEquals(2, expenses.size());
        assertEquals("MRT top-up", expenses.getExpense(1).getDescription());
    }

    @Test
    void execute_indexTooLarge_throwsException() {
        assertThrows(SpendTrackException.class, () -> new DeleteCommand(99).execute(expenses, ui));
    }

    @Test
    void execute_indexZero_throwsException() {
        assertThrows(SpendTrackException.class, () -> new DeleteCommand(0).execute(expenses, ui));
    }
}
