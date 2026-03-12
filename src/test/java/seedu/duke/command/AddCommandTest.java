package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

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
    void execute_validExpense_addsToList() {
        AddCommand command = new AddCommand("Ramen dinner", 14.00, "Food");
        command.execute(expenses, ui);
        assertEquals(1, expenses.size());
    }

    @Test
    void execute_validExpense_storesCorrectDetails() {
        AddCommand command = new AddCommand("MRT top-up", 10.00, "Transport");
        command.execute(expenses, ui);
        Expense stored = expenses.getExpense(0);
        assertEquals("MRT top-up", stored.getDescription());
        assertEquals(10.00, stored.getAmount());
        assertEquals("Transport", stored.getCategory());
    }

    @Test
    void execute_multipleExpenses_allAdded() {
        new AddCommand("Ramen dinner", 14.00, "Food").execute(expenses, ui);
        new AddCommand("MRT top-up", 10.00, "Transport").execute(expenses, ui);
        new AddCommand("Hostel week 3", 85.00, "Accommodation").execute(expenses, ui);
        assertEquals(3, expenses.size());
    }
}
