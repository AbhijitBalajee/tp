package seedu.duke.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClearCommandTest {

    private static final PrintStream ORIGINAL_OUT = System.out;
    private static final InputStream ORIGINAL_IN = System.in;

    private ExpenseList expenses;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        expenses = new ExpenseList();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(ORIGINAL_OUT);
        System.setIn(ORIGINAL_IN);
    }

    private Ui createUiWithInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        return new Ui();
    }

    @Test
    void execute_emptyList_showsNoExpensesMessage() {
        Ui ui = createUiWithInput("");

        new ClearCommand().execute(expenses, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("No expenses to clear."));
        assertEquals(0, expenses.size());
    }

    @Test
    void execute_userConfirmsYes_clearsAllExpenses() {
        expenses.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.now()));
        expenses.addExpense(new Expense("Bus", 2.00, "Transport", LocalDate.now()));
        Ui ui = createUiWithInput("yes\n");

        new ClearCommand().execute(expenses, ui);

        assertEquals(0, expenses.size());
        String output = outputStream.toString();
        assertTrue(output.contains("All expenses cleared."));
        assertTrue(output.contains("2 expense(s) removed"));
    }

    @Test
    void execute_userConfirmsYesUpperCase_clearsAllExpenses() {
        expenses.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.now()));
        Ui ui = createUiWithInput("YES\n");

        new ClearCommand().execute(expenses, ui);

        assertEquals(0, expenses.size());
        String output = outputStream.toString();
        assertTrue(output.contains("All expenses cleared."));
    }

    @Test
    void execute_userConfirmsMixedCase_clearsAllExpenses() {
        expenses.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.now()));
        Ui ui = createUiWithInput("YeS\n");

        new ClearCommand().execute(expenses, ui);

        assertEquals(0, expenses.size());
    }

    @Test
    void execute_userDeniesWithNo_cancelsClear() {
        expenses.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.now()));
        Ui ui = createUiWithInput("no\n");

        new ClearCommand().execute(expenses, ui);

        assertEquals(1, expenses.size());
        String output = outputStream.toString();
        assertTrue(output.contains("Clear cancelled."));
    }

    @Test
    void execute_userEntersRandomText_cancelsClear() {
        expenses.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.now()));
        Ui ui = createUiWithInput("maybe\n");

        new ClearCommand().execute(expenses, ui);

        assertEquals(1, expenses.size());
        String output = outputStream.toString();
        assertTrue(output.contains("Clear cancelled."));
    }

    @Test
    void execute_userEntersEmptyInput_cancelsClear() {
        expenses.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.now()));
        Ui ui = createUiWithInput("\n");

        new ClearCommand().execute(expenses, ui);

        assertEquals(1, expenses.size());
        String output = outputStream.toString();
        assertTrue(output.contains("Clear cancelled."));
    }

    @Test
    void execute_showsConfirmationPrompt() {
        expenses.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.now()));
        Ui ui = createUiWithInput("no\n");

        new ClearCommand().execute(expenses, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("Are you sure you want to delete all expenses?"));
        assertTrue(output.contains("(yes/no)"));
    }

    @Test
    void mutatesData_returnsTrue() {
        assertTrue(new ClearCommand().mutatesData());
    }
}
