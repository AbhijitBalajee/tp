package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TotalCommandTest {

    private ExpenseList expenses;
    private Ui ui;

    @BeforeEach
    void setUp() {
        expenses = new ExpenseList();
        ui = new Ui();
    }

    @Test
    void execute_multipleExpenses_correctTotal() {
        expenses.addExpense(new Expense("Lunch", 10.50, "Food", LocalDate.now()));
        expenses.addExpense(new Expense("Bus", 2.00, "Transport", LocalDate.now()));
        expenses.addExpense(new Expense("Coffee", 5.50, "Food", LocalDate.now()));

        TotalCommand cmd = new TotalCommand();
        cmd.execute(expenses, ui);

        assertEquals(18.00, expenses.getTotal(), 0.01);
    }

    @Test
    void execute_noExpenses_totalIsZero() {
        TotalCommand cmd = new TotalCommand();
        cmd.execute(expenses, ui);

        assertEquals(0.0, expenses.getTotal(), 0.01);
    }

    @Test
    void execute_singleExpense_correctTotal() {
        expenses.addExpense(new Expense("Dinner", 25.00, "Food", LocalDate.now()));

        TotalCommand cmd = new TotalCommand();
        cmd.execute(expenses, ui);

        assertEquals(25.00, expenses.getTotal(), 0.01);
    }

    @Test
    void execute_afterExecution_doesNotModifyList() {
        expenses.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.now()));
        expenses.addExpense(new Expense("Bus", 2.00, "Transport", LocalDate.now()));
        int sizeBefore = expenses.size();

        new TotalCommand().execute(expenses, ui);

        assertEquals(sizeBefore, expenses.size());
    }

    @Test
    void getTotal_multipleExpenses_correctSum() {
        expenses.addExpense(new Expense("Lunch", 10.50, "Food", LocalDate.now()));
        expenses.addExpense(new Expense("Bus", 2.00, "Transport", LocalDate.now()));
        expenses.addExpense(new Expense("Coffee", 5.50, "Food", LocalDate.now()));

        assertEquals(18.00, expenses.getTotal(), 0.01);
    }

    @Test
    void getTotal_noExpenses_returnsZero() {
        assertEquals(0.0, expenses.getTotal(), 0.01);
    }
}
