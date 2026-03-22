package seedu.duke.command;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import seedu.duke.SpendTrackException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddCommandTest {

    private ExpenseList expenses;
    private Ui ui;

    @BeforeEach
    void setUp() {
        expenses = new ExpenseList();
        ui = new Ui();
    }

    @Test
    void execute_validExpense_addsToList() throws SpendTrackException {
        AddCommand command = new AddCommand("Ramen dinner", 14.00, "Food", LocalDate.now());
        command.execute(expenses, ui);
        assertEquals(1, expenses.size());
    }

    @Test
    void execute_validExpense_storesCorrectDetails() throws SpendTrackException {
        AddCommand command = new AddCommand("MRT top-up", 10.00, "Transport", LocalDate.now());
        command.execute(expenses, ui);
        Expense stored = expenses.getExpense(0);
        assertEquals("MRT top-up", stored.getDescription());
        assertEquals(10.00, stored.getAmount());
        assertEquals("Transport", stored.getCategory());
    }

    @Test
    void execute_multipleExpenses_allAdded() throws SpendTrackException {
        new AddCommand("Ramen dinner", 14.00, "Food", LocalDate.now()).execute(expenses, ui);
        new AddCommand("MRT top-up", 10.00, "Transport", LocalDate.now()).execute(expenses, ui);
        new AddCommand("Hostel week 3", 85.00, "Accommodation", LocalDate.now()).execute(expenses, ui);
        assertEquals(3, expenses.size());
    }
}
