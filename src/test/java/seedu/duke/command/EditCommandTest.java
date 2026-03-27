package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EditCommandTest {

    private ExpenseList expenses;
    private Ui ui;

    @BeforeEach
    void setUp() throws SpendTrackException {
        expenses = new ExpenseList();
        ui = new Ui();
        new AddCommand("Coffee", 4.50, "Food", LocalDate.now()).execute(expenses, ui);
        new AddCommand("Bus", 1.20, "Transport", LocalDate.now()).execute(expenses, ui);
    }

    // ── Happy path ────────────────────────────────────────────────────────────

    @Test
    void execute_editDescription_updatesCorrectly() throws SpendTrackException {
        new EditCommand(1, "Latte", null, null, null, null).execute(expenses, ui);
        assertEquals("Latte", expenses.getExpense(0).getDescription());
    }

    @Test
    void execute_editAmount_updatesCorrectly() throws SpendTrackException {
        new EditCommand(1, null, 6.00, null, null, null).execute(expenses, ui);
        assertEquals(6.00, expenses.getExpense(0).getAmount(), 0.001);
    }

    @Test
    void execute_editCategory_updatesCorrectly() throws SpendTrackException {
        new EditCommand(1, null, null, "Drinks", null, null).execute(expenses, ui);
        assertEquals("Drinks", expenses.getExpense(0).getCategory());
    }

    @Test
    void execute_editDate_updatesCorrectly() throws SpendTrackException {
        LocalDate newDate = LocalDate.of(2026, 1, 1);
        new EditCommand(1, null, null, null, newDate, null).execute(expenses, ui);
        assertEquals(newDate, expenses.getExpense(0).getDate());
    }

    @Test
    void execute_editMultipleFields_updatesAll() throws SpendTrackException {
        new EditCommand(1, "Tea", 2.50, "Drinks", null, null).execute(expenses, ui);
        assertEquals("Tea", expenses.getExpense(0).getDescription());
        assertEquals(2.50, expenses.getExpense(0).getAmount(), 0.001);
        assertEquals("Drinks", expenses.getExpense(0).getCategory());
    }

    @Test
    void execute_editAllFields_updatesAll() throws SpendTrackException {
        LocalDate newDate = LocalDate.of(2026, 1, 1);
        new EditCommand(1, "Tea", 2.50, "Drinks", newDate, null).execute(expenses, ui);
        assertEquals("Tea", expenses.getExpense(0).getDescription());
        assertEquals(2.50, expenses.getExpense(0).getAmount(), 0.001);
        assertEquals("Drinks", expenses.getExpense(0).getCategory());
        assertEquals(newDate, expenses.getExpense(0).getDate());
    }

    @Test
    void execute_editSmallestValidAmount_succeeds() throws SpendTrackException {
        new EditCommand(1, null, 0.01, null, null, null).execute(expenses, ui);
        assertEquals(0.01, expenses.getExpense(0).getAmount(), 0.001);
    }

    @Test
    void execute_editLargeAmount_succeeds() throws SpendTrackException {
        new EditCommand(1, null, 999999.99, null, null, null).execute(expenses, ui);
        assertEquals(999999.99, expenses.getExpense(0).getAmount(), 0.001);
    }

    @Test
    void execute_editRecurringToTrue_updatesCorrectly() throws SpendTrackException {
        new EditCommand(1, null, null, null, null, true).execute(expenses, ui);
        assertEquals(true, expenses.getExpense(0).isRecurring());
    }

    @Test
    void execute_editRecurringToFalse_updatesCorrectly() throws SpendTrackException {
        new EditCommand(1, null, null, null, null, true).execute(expenses, ui);
        new EditCommand(1, null, null, null, null, false).execute(expenses, ui);
        assertEquals(false, expenses.getExpense(0).isRecurring());
    }

    // ── Unchanged fields ──────────────────────────────────────────────────────

    @Test
    void execute_unchangedFields_stayTheSame() throws SpendTrackException {
        new EditCommand(2, null, null, "Commute", null, null).execute(expenses, ui);
        assertEquals("Bus", expenses.getExpense(1).getDescription());
        assertEquals(1.20, expenses.getExpense(1).getAmount(), 0.001);
        assertEquals("Commute", expenses.getExpense(1).getCategory());
    }

    @Test
    void execute_editOnlyDescription_otherFieldsUnchanged() throws SpendTrackException {
        new EditCommand(1, "Espresso", null, null, null, null).execute(expenses, ui);
        assertEquals("Espresso", expenses.getExpense(0).getDescription());
        assertEquals(4.50, expenses.getExpense(0).getAmount(), 0.001);
        assertEquals("Food", expenses.getExpense(0).getCategory());
    }

    @Test
    void execute_editSecondExpense_doesNotAffectFirst() throws SpendTrackException {
        new EditCommand(2, "Train", null, null, null, null).execute(expenses, ui);
        assertEquals("Coffee", expenses.getExpense(0).getDescription());
    }

    @Test
    void execute_afterEdit_listSizeUnchanged() throws SpendTrackException {
        new EditCommand(1, "Latte", null, null, null, null).execute(expenses, ui);
        assertEquals(2, expenses.size());
    }

    @Test
    void execute_editRecurring_otherFieldsUnchanged() throws SpendTrackException {
        new EditCommand(1, null, null, null, null, true).execute(expenses, ui);
        assertEquals("Coffee", expenses.getExpense(0).getDescription());
        assertEquals(4.50, expenses.getExpense(0).getAmount(), 0.001);
        assertEquals("Food", expenses.getExpense(0).getCategory());
    }

    // ── Invalid index ─────────────────────────────────────────────────────────

    @Test
    void execute_invalidIndex_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                new EditCommand(99, "Test", null, null, null, null).execute(expenses, ui));
    }

    @Test
    void execute_zeroIndex_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                new EditCommand(0, "Test", null, null, null, null).execute(expenses, ui));
    }

    @Test
    void execute_negativeIndex_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                new EditCommand(-1, "Test", null, null, null, null).execute(expenses, ui));
    }

    // ── No fields provided ────────────────────────────────────────────────────

    @Test
    void execute_noFieldsProvided_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                new EditCommand(1, null, null, null, null, null).execute(expenses, ui));
    }

    // ── Invalid description ───────────────────────────────────────────────────

    @Test
    void execute_emptyDescription_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                new EditCommand(1, "", null, null, null, null).execute(expenses, ui));
    }

    @Test
    void execute_blankDescription_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                new EditCommand(1, "   ", null, null, null, null).execute(expenses, ui));
    }

    // ── Invalid amount ────────────────────────────────────────────────────────

    @Test
    void execute_zeroAmount_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                new EditCommand(1, null, 0.0, null, null, null).execute(expenses, ui));
    }

    @Test
    void execute_negativeAmount_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                new EditCommand(1, null, -5.00, null, null, null).execute(expenses, ui));
    }

    @Test
    void execute_verySmallNegativeAmount_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                new EditCommand(1, null, -0.01, null, null, null).execute(expenses, ui));
    }
}
