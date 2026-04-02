// @@author AfshalG
package seedu.duke.command;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Parser;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateTaggingTest {

    private ExpenseList expenses;
    private Ui ui;

    @BeforeEach
    void setUp() {
        expenses = new ExpenseList();
        ui = new Ui();
    }

    @Test
    void addCommand_noDateProvided_defaultsToToday() throws SpendTrackException {
        AddCommand cmd = new AddCommand("Coffee", 3.50, "Food", LocalDate.now());
        cmd.execute(expenses, ui);
        assertEquals(LocalDate.now(), expenses.getExpense(0).getDate());
    }

    @Test
    void addCommand_withSpecificDate_storesCorrectDate() throws SpendTrackException {
        LocalDate date = LocalDate.of(2026, 3, 15);
        AddCommand cmd = new AddCommand("Lunch", 12.00, "Food", date);
        cmd.execute(expenses, ui);
        assertEquals(date, expenses.getExpense(0).getDate());
    }

    @Test
    void expense_getDate_returnsCorrectDate() {
        LocalDate date = LocalDate.of(2026, 1, 10);
        Expense expense = new Expense("Bus", 1.50, "Transport", date);
        assertEquals(date, expense.getDate());
    }

    @Test
    void expense_toString_includesDate() {
        LocalDate date = LocalDate.of(2026, 3, 20);
        Expense expense = new Expense("Coffee", 3.50, "Food", date);
        assertEquals("[Food] Coffee - $3.50 (2026-03-20)", expense.toString());
    }

    @Test
    void parser_addWithDateFlag_parsesCorrectly() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Lunch a/10.00 c/Food date/2026-03-15");
        cmd.execute(expenses, ui);
        assertEquals(LocalDate.of(2026, 3, 15), expenses.getExpense(0).getDate());
    }

    @Test
    void parser_addWithoutDateFlag_defaultsToToday() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/Food");
        cmd.execute(expenses, ui);
        assertEquals(LocalDate.now(), expenses.getExpense(0).getDate());
    }

    @Test
    void parser_addWithInvalidDate_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Lunch a/10.00 c/Food date/not-a-date"));
    }

    @Test
    void parser_addFlagsInDifferentOrder_parsesCorrectly() throws SpendTrackException {
        Command cmd = Parser.parse("add c/Food a/3.50 d/Coffee");
        cmd.execute(expenses, ui);
        Expense stored = expenses.getExpense(0);
        assertEquals("Coffee", stored.getDescription());
        assertEquals(3.50, stored.getAmount());
        assertEquals("Food", stored.getCategory());
    }

    @Test
    void parser_addDateFlagFirst_parsesCorrectly() throws SpendTrackException {
        Command cmd = Parser.parse("add date/2026-03-15 d/Lunch a/10.00 c/Food");
        cmd.execute(expenses, ui);
        Expense stored = expenses.getExpense(0);
        assertEquals("Lunch", stored.getDescription());
        assertEquals(LocalDate.of(2026, 3, 15), stored.getDate());
    }
}
