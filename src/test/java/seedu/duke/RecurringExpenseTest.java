package seedu.duke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.command.AddCommand;
import seedu.duke.command.ListCommand;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// @@author AbhijitBalajee
public class RecurringExpenseTest {

    private ExpenseList expenses;
    private Ui ui;

    @BeforeEach
    void setUp() throws SpendTrackException {
        expenses = new ExpenseList();
        ui = new Ui();
        new AddCommand("Netflix", 18.00, "Entertainment",
                LocalDate.now(), true).execute(expenses, ui);
        new AddCommand("Coffee", 4.50, "Food",
                LocalDate.now(), false).execute(expenses, ui);
    }

    @Test
    void expense_defaultRecurring_isFalse() {
        Expense e = new Expense("Test", 5.00, "Food", LocalDate.now());
        assertFalse(e.isRecurring());
    }

    @Test
    void expense_setRecurringTrue_isTrue() {
        Expense e = new Expense("Netflix", 18.00, "Entertainment", LocalDate.now(), true);
        assertTrue(e.isRecurring());
    }

    @Test
    void expense_toString_showsRecurringTag() {
        Expense e = new Expense("Netflix", 18.00, "Entertainment", LocalDate.now(), true);
        assertTrue(e.toString().contains("[R]"));
    }

    @Test
    void expense_toString_noTagWhenNotRecurring() {
        Expense e = new Expense("Coffee", 4.50, "Food", LocalDate.now(), false);
        assertFalse(e.toString().contains("[R]"));
    }

    @Test
    void addCommand_recurringTrue_storesFlag() {
        assertTrue(expenses.getExpense(0).isRecurring());
    }

    @Test
    void addCommand_recurringFalse_storesFlag() {
        assertFalse(expenses.getExpense(1).isRecurring());
    }

    @Test
    void listCommand_recurringOnly_doesNotThrow() {
        ListCommand cmd = new ListCommand(true);
        assertDoesNotThrow(() -> cmd.execute(expenses, ui));
    }

    @Test
    void listCommand_all_doesNotThrow() {
        ListCommand cmd = new ListCommand(false);
        assertDoesNotThrow(() -> cmd.execute(expenses, ui));
    }

    @Test
    void parse_addRecurringTrue_parsesCorrectly() throws SpendTrackException {
        Parser.parse("add d/Netflix a/18.00 c/Entertainment recurring/true");
    }

    @Test
    void parse_addRecurringFalse_parsesCorrectly() throws SpendTrackException {
        Parser.parse("add d/Coffee a/4.50 c/Food recurring/false");
    }

    @Test
    void parse_addInvalidRecurring_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Netflix a/18.00 c/Entertainment recurring/yes"));
    }

    @Test
    void parse_listRecurring_parsesCorrectly() throws SpendTrackException {
        Parser.parse("list recurring");
    }

    @Test
    void parse_addNoRecurring_defaultsFalse() throws SpendTrackException {
        ExpenseList list = new ExpenseList();
        new AddCommand("Coffee", 4.50, "Food", LocalDate.now()).execute(list, ui);
        assertFalse(list.getExpense(0).isRecurring());
    }

    @Test
    void expenseList_recurringCount_correct() {
        long count = expenses.getExpenses().stream()
                .filter(Expense::isRecurring)
                .count();
        assertEquals(1, count);
    }
}
// @@author
