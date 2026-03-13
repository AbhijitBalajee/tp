package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteCommandTest {

    private ExpenseList expenses;
    private Ui ui;

    @BeforeEach
    void setUp() {
        expenses = new ExpenseList();
        ui = new Ui();
        new AddCommand("Coffee", 4.50, "Food").execute(expenses, ui);
        new AddCommand("MRT top-up", 10.00, "Transport").execute(expenses, ui);
        new AddCommand("Textbook", 35.00, "Education").execute(expenses, ui);
    }

    @Test
    void execute_validIndex_removesExpense() {
        new DeleteCommand(2).execute(expenses, ui);
        assertEquals(2, expenses.size());
    }

    @Test
    void execute_validIndex_removesCorrectExpense() {
        new DeleteCommand(1).execute(expenses, ui);
        assertEquals("MRT top-up", expenses.getExpense(0).getDescription());
    }

    @Test
    void execute_lastIndex_removesLastExpense() {
        new DeleteCommand(3).execute(expenses, ui);
        assertEquals(2, expenses.size());
        assertEquals("MRT top-up", expenses.getExpense(1).getDescription());
    }
}
