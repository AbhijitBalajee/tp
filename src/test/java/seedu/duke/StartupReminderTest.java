package seedu.duke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.command.AddCommand;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the startup reminder (Ui.showLastExpense) and its integration
 * with Storage load + SpendTrack startup flow.
 */
// @@author Ariff1422
class StartupReminderTest {

    private static final LocalDate DATE = LocalDate.of(2026, 3, 22);

    private ExpenseList expenses;
    private Ui ui;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() throws SpendTrackException {
        expenses = new ExpenseList();
        ui = new Ui();

        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        new AddCommand("Coffee",  4.50,  "Food",      DATE).execute(expenses, ui);
        new AddCommand("Bus",     1.80,  "Transport", DATE).execute(expenses, ui);
        new AddCommand("Textbook", 35.00, "Education", DATE).execute(expenses, ui);
    }

    // --- showLastExpense output content ---

    @Test
    void showLastExpense_containsDescription() {
        Expense last = expenses.getExpense(expenses.size() - 1);
        out.reset();
        ui.showLastExpense(last);
        String output = out.toString();
        assertTrue(output.contains("Textbook"));
    }

    @Test
    void showLastExpense_containsAmount() {
        Expense last = expenses.getExpense(expenses.size() - 1);
        out.reset();
        ui.showLastExpense(last);
        String output = out.toString();
        assertTrue(output.contains("35.00"));
    }

    @Test
    void showLastExpense_containsCategory() {
        Expense last = expenses.getExpense(expenses.size() - 1);
        out.reset();
        ui.showLastExpense(last);
        String output = out.toString();
        assertTrue(output.contains("Education"));
    }

    @Test
    void showLastExpense_containsDate() {
        Expense last = expenses.getExpense(expenses.size() - 1);
        out.reset();
        ui.showLastExpense(last);
        String output = out.toString();
        assertTrue(output.contains(DATE.toString()));
    }

    // --- Correct item is the last one ---

    @Test
    void lastExpense_isLastAddedItem() {
        Expense last = expenses.getExpense(expenses.size() - 1);
        assertTrue(last.getDescription().equals("Textbook"));
    }

    // --- Non-empty list logic ---

    @Test
    void nonEmptyList_hasLastExpense() {
        assertTrue(expenses.size() > 0);
    }

    @Test
    void emptyList_noLastExpense_sizeIsZero() {
        ExpenseList empty = new ExpenseList();
        assertFalse(empty.size() > 0);
    }

    // --- showLastExpense output for first item ---

    @Test
    void showLastExpense_firstExpense_containsCorrectData() throws SpendTrackException {
        ExpenseList single = new ExpenseList();
        new AddCommand("Kopi", 1.20, "Food", DATE).execute(single, ui);
        Expense last = single.getExpense(single.size() - 1);
        out.reset();
        ui.showLastExpense(last);
        String output = out.toString();
        assertTrue(output.contains("Kopi"));
        assertTrue(output.contains("1.20"));
    }

    // --- Recurring expense shown correctly ---

    @Test
    void showLastExpense_recurringExpense_includesRecurringInToString() throws SpendTrackException {
        ExpenseList list = new ExpenseList();
        new AddCommand("Gym", 80.00, "Health", DATE, true).execute(list, ui);
        Expense last = list.getExpense(0);
        assertTrue(last.isRecurring());
        assertTrue(last.toString().contains("[R]"));
    }
}
// @@author
