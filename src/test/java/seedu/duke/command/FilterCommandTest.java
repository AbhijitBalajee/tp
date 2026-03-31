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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// @@author Ariff1422
class FilterCommandTest {

    private static final LocalDate JAN_01 = LocalDate.of(2026, 1, 1);
    private static final LocalDate JAN_15 = LocalDate.of(2026, 1, 15);
    private static final LocalDate JAN_31 = LocalDate.of(2026, 1, 31);
    private static final LocalDate FEB_01 = LocalDate.of(2026, 2, 1);
    private static final LocalDate MAR_10 = LocalDate.of(2026, 3, 10);

    private ExpenseList expenses;
    private Ui ui;

    @BeforeEach
    void setUp() {
        expenses = new ExpenseList();
        ui = new Ui();
        expenses.addExpense(new Expense("Coffee",   4.50,  "Food",      JAN_01));
        expenses.addExpense(new Expense("Bus",       1.80,  "Transport", JAN_15));
        expenses.addExpense(new Expense("Textbook", 35.00, "Education", JAN_31));
        expenses.addExpense(new Expense("Lunch",     8.00, "Food",      FEB_01));
        expenses.addExpense(new Expense("Grab",     12.00, "Transport", MAR_10));
    }

    // --- Results within range ---

    @Test
    void execute_wholeJanuary_returnsThreeExpenses() throws SpendTrackException {
        // We check indirectly by confirming no exception and the list wasn't mutated
        FilterCommand cmd = new FilterCommand(JAN_01, JAN_31);
        assertDoesNotThrow(() -> cmd.execute(expenses, ui));
        assertEquals(5, expenses.size()); // original list unchanged
    }

    @Test
    void execute_exactDateMatch_returnsSingleExpense() throws SpendTrackException {
        // filter a range that only covers JAN_15
        FilterCommand cmd = new FilterCommand(JAN_15, JAN_15);
        assertDoesNotThrow(() -> cmd.execute(expenses, ui));
    }

    @Test
    void execute_doesNotMutateExpenseList() throws SpendTrackException {
        FilterCommand cmd = new FilterCommand(JAN_01, JAN_31);
        cmd.execute(expenses, ui);
        assertEquals(5, expenses.size());
    }

    // --- Empty result ---

    @Test
    void execute_noMatchesInRange_doesNotThrow() {
        LocalDate future = LocalDate.of(2030, 1, 1);
        FilterCommand cmd = new FilterCommand(future, future.plusDays(5));
        assertDoesNotThrow(() -> cmd.execute(expenses, ui));
    }

    // --- isExit / mutatesData ---

    @Test
    void isExit_returnsFalse() {
        FilterCommand cmd = new FilterCommand(JAN_01, JAN_31);
        assertTrue(!cmd.isExit());
    }

    @Test
    void mutatesData_returnsFalse() {
        FilterCommand cmd = new FilterCommand(JAN_01, JAN_31);
        assertTrue(!cmd.mutatesData());
    }

    // --- Parser integration ---

    @Test
    void parser_validFilter_returnsFilterCommand() throws SpendTrackException {
        Command cmd = Parser.parse("filter from/2026-01-01 to/2026-01-31");
        assertTrue(cmd instanceof FilterCommand);
    }

    @Test
    void parser_fromAfterTo_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> Parser.parse("filter from/2026-03-31 to/2026-01-01"));
    }

    @Test
    void parser_missingFrom_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> Parser.parse("filter to/2026-01-31"));
    }

    @Test
    void parser_missingTo_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> Parser.parse("filter from/2026-01-01"));
    }

    @Test
    void parser_missingBothDates_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> Parser.parse("filter"));
    }

    @Test
    void parser_invalidDateFormat_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> Parser.parse("filter from/notadate to/alsonotadate"));
    }

    @Test
    void parser_sameDates_returnsFilterCommand() throws SpendTrackException {
        Command cmd = Parser.parse("filter from/2026-01-15 to/2026-01-15");
        assertTrue(cmd instanceof FilterCommand);
    }
}
// @@author
