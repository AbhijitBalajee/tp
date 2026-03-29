package seedu.duke.command;

import java.time.LocalDate;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EnhancedSummaryTest {

    private ExpenseList expenses;
    private Ui ui;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        expenses = new ExpenseList();
        ui = new Ui();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void execute_multipleCategories_showsTransactionCount() {
        expenses.addExpense(new Expense("Coffee", 3.50, "Food", LocalDate.now()));
        expenses.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.now()));
        expenses.addExpense(new Expense("Bus", 1.20, "Transport", LocalDate.now()));
        new SummaryCommand().execute(expenses, ui);
        String output = outputStream.toString();
        assertTrue(output.contains("2 txns"));
        assertTrue(output.contains("1 txn"));
    }

    @Test
    void execute_singleCategory_showsAverage() {
        expenses.addExpense(new Expense("Coffee", 3.00, "Food", LocalDate.now()));
        expenses.addExpense(new Expense("Lunch", 9.00, "Food", LocalDate.now()));
        new SummaryCommand().execute(expenses, ui);
        String output = outputStream.toString();
        assertTrue(output.contains("avg $6.00"));
    }

    @Test
    void execute_multipleInCategory_showsMax() {
        expenses.addExpense(new Expense("Coffee", 3.50, "Food", LocalDate.now()));
        expenses.addExpense(new Expense("Lunch", 15.00, "Food", LocalDate.now()));
        expenses.addExpense(new Expense("Dinner", 10.00, "Food", LocalDate.now()));
        new SummaryCommand().execute(expenses, ui);
        String output = outputStream.toString();
        assertTrue(output.contains("max $15.00"));
    }

    @Test
    void execute_singleExpense_showsCorrectStats() {
        expenses.addExpense(new Expense("Bus", 1.20, "Transport", LocalDate.now()));
        new SummaryCommand().execute(expenses, ui);
        String output = outputStream.toString();
        assertTrue(output.contains("1 txn"));
        assertTrue(output.contains("avg $1.20"));
        assertTrue(output.contains("max $1.20"));
    }

    @Test
    void execute_emptyList_doesNotShowStats() {
        new SummaryCommand().execute(expenses, ui);
        String output = outputStream.toString();
        assertFalse(output.contains("txn"));
        assertFalse(output.contains("avg"));
    }

    @Test
    void execute_multipleCategories_sortedDescendingByTotal() {
        expenses.addExpense(new Expense("Bus", 1.20, "Transport", LocalDate.now()));
        expenses.addExpense(new Expense("Coffee", 3.50, "Food", LocalDate.now()));
        expenses.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.now()));
        expenses.addExpense(new Expense("Movie", 8.00, "Entertainment", LocalDate.now()));
        new SummaryCommand().execute(expenses, ui);
        String output = outputStream.toString();
        int foodIndex = output.indexOf("Food");
        int entertainmentIndex = output.indexOf("Entertainment");
        int transportIndex = output.indexOf("Transport");
        assertTrue(foodIndex < entertainmentIndex);
        assertTrue(entertainmentIndex < transportIndex);
    }

    @Test
    void execute_multipleCategories_showsCorrectPercentages() {
        expenses.addExpense(new Expense("Coffee", 50.00, "Food", LocalDate.now()));
        expenses.addExpense(new Expense("Bus", 50.00, "Transport", LocalDate.now()));
        new SummaryCommand().execute(expenses, ui);
        String output = outputStream.toString();
        assertTrue(output.contains("50%"));
    }
}
