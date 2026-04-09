package seedu.duke.command;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// @@author Ariff1422
class DeleteCommandTest {

    private ExpenseList expenses;
    private final InputStream originalIn = System.in;

    @AfterEach
    void restoreSystemIn() {
        System.setIn(originalIn);
    }

    private Ui uiWithInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        return new Ui();
    }

    @BeforeEach
    void setUp() throws SpendTrackException {
        expenses = new ExpenseList();
        Ui ui = uiWithInput("yes\nyes\nyes\n");
        new AddCommand("Coffee", 4.50, "Food", LocalDate.now()).execute(expenses, ui);
        new AddCommand("MRT top-up", 10.00, "Transport", LocalDate.now()).execute(expenses, ui);
        new AddCommand("Textbook", 35.00, "Education", LocalDate.now()).execute(expenses, ui);
    }

    @Test
    void execute_validIndex_removesExpense() throws SpendTrackException {
        new DeleteCommand(2).execute(expenses, uiWithInput("yes\n"));
        assertEquals(2, expenses.size());
    }

    @Test
    void execute_validIndex_removesCorrectExpense() throws SpendTrackException {
        new DeleteCommand(1).execute(expenses, uiWithInput("yes\n"));
        assertEquals("MRT top-up", expenses.getExpense(0).getDescription());
    }

    @Test
    void execute_lastIndex_removesLastExpense() throws SpendTrackException {
        new DeleteCommand(3).execute(expenses, uiWithInput("yes\n"));
        assertEquals(2, expenses.size());
        assertEquals("MRT top-up", expenses.getExpense(1).getDescription());
    }

    @Test
    void execute_cancelled_listUnchanged() throws SpendTrackException {
        new DeleteCommand(1).execute(expenses, uiWithInput("no\n"));
        assertEquals(3, expenses.size());
    }

    @Test
    void execute_invalidResponse_treatedAsCancel() throws SpendTrackException {
        new DeleteCommand(1).execute(expenses, uiWithInput("maybe\n"));
        assertEquals(3, expenses.size());
    }

    @Test
    void execute_indexTooLarge_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> new DeleteCommand(99).execute(expenses, uiWithInput("yes\n")));
    }

    @Test
    void execute_indexZero_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> new DeleteCommand(0).execute(expenses, uiWithInput("yes\n")));
    }
}
// @@author
