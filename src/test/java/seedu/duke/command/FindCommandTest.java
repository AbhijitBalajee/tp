package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Parser;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// @@author Ariff1422
class FindCommandTest {

    private static final LocalDate DATE = LocalDate.of(2026, 3, 22);

    private ExpenseList expenses;
    private Ui ui;

    @BeforeEach
    void setUp() {
        expenses = new ExpenseList();
        ui = new Ui();
        expenses.addExpense(new Expense("Coffee", 4.50, "Food", DATE));
        expenses.addExpense(new Expense("Bus", 1.80, "Transport", DATE));
        expenses.addExpense(new Expense("Textbook", 35.00, "Education", DATE));
    }

    // --- Valid indices ---

    @Test
    void execute_firstIndex_doesNotThrow() {
        assertDoesNotThrow(() -> new FindCommand(1).execute(expenses, ui));
    }

    @Test
    void execute_lastIndex_doesNotThrow() {
        assertDoesNotThrow(() -> new FindCommand(3).execute(expenses, ui));
    }

    @Test
    void execute_middleIndex_doesNotThrow() {
        assertDoesNotThrow(() -> new FindCommand(2).execute(expenses, ui));
    }

    // --- Out-of-range indices ---

    @Test
    void execute_indexZero_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> new FindCommand(0).execute(expenses, ui));
    }

    @Test
    void execute_indexTooLarge_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> new FindCommand(99).execute(expenses, ui));
    }

    @Test
    void execute_negativeIndex_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> new FindCommand(-1).execute(expenses, ui));
    }

    // --- Empty list ---

    @Test
    void execute_emptyList_throwsException() {
        ExpenseList empty = new ExpenseList();
        assertThrows(SpendTrackException.class,
                () -> new FindCommand(1).execute(empty, ui));
    }

    // --- Does not mutate ---

    @Test
    void execute_doesNotMutateList() throws SpendTrackException {
        new FindCommand(2).execute(expenses, ui);
        assertTrue(expenses.size() == 3);
    }

    // --- isExit / mutatesData ---

    @Test
    void isExit_returnsFalse() {
        assertTrue(!new FindCommand(1).isExit());
    }

    @Test
    void mutatesData_returnsFalse() {
        assertTrue(!new FindCommand(1).mutatesData());
    }

    // --- Parser integration ---

    @Test
    void parser_validIndex_returnsFindCommand() throws SpendTrackException {
        Command cmd = Parser.parse("find 2");
        assertTrue(cmd instanceof FindCommand);
    }

    @Test
    void parser_nonNumericIndex_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("find abc"));
    }

    @Test
    void parser_missingIndex_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("find"));
    }

    @Test
    void parser_floatIndex_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("find 1.5"));
    }
}
// @@author
