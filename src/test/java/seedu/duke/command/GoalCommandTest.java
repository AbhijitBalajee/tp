package seedu.duke.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Parser;
import seedu.duke.Ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// @@author pranavjana
class GoalCommandTest {

    private static final PrintStream ORIGINAL_OUT = System.out;

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

    @AfterEach
    void restoreSystemOut() {
        System.setOut(ORIGINAL_OUT);
    }

    @Test
    void execute_setGoal_setsGoalAmount() {
        new GoalCommand(200.00).execute(expenses, ui);

        assertEquals(200.00, expenses.getGoal(), 0.01);
        assertTrue(outputStream.toString().contains("$200.00"));
    }

    @Test
    void execute_goalStatusNoGoalSet_showsNoGoalMessage() {
        new GoalCommand().execute(expenses, ui);

        assertTrue(outputStream.toString().contains("No savings goal set."));
    }

    @Test
    void execute_goalStatusUnderGoal_showsSavedAmount() {
        expenses.setGoal(200.00);
        expenses.addExpense(new Expense("Lunch", 50.00, "Food", LocalDate.now()));

        new GoalCommand().execute(expenses, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("$200.00"));
        assertTrue(output.contains("$50.00"));
        assertTrue(output.contains("$150.00"));
        assertTrue(output.contains("75%"));
    }

    @Test
    void execute_goalStatusExactlyAtGoal_showsZeroSaved() {
        expenses.setGoal(100.00);
        expenses.addExpense(new Expense("Lunch", 100.00, "Food", LocalDate.now()));

        new GoalCommand().execute(expenses, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("$0.00"));
        assertTrue(output.contains("0%"));
    }

    @Test
    void execute_goalStatusExceedsGoal_showsOverBy() {
        expenses.setGoal(100.00);
        expenses.addExpense(new Expense("Lunch", 150.00, "Food", LocalDate.now()));

        new GoalCommand().execute(expenses, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("Goal not reached"));
        assertTrue(output.contains("$50.00"));
    }

    @Test
    void execute_goalStatusNoExpenses_showsFullSaved() {
        expenses.setGoal(200.00);

        new GoalCommand().execute(expenses, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("$200.00"));
        assertTrue(output.contains("100%"));
    }

    @Test
    void parse_goalWithValidAmount_createsGoalCommand() throws SpendTrackException {
        Command command = Parser.parse("goal g/200");

        assertTrue(command instanceof GoalCommand);
    }

    @Test
    void parse_goalStatus_createsGoalCommand() throws SpendTrackException {
        Command command = Parser.parse("goal status");

        assertTrue(command instanceof GoalCommand);
    }

    @Test
    void parse_goalWithZeroAmount_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("goal g/0"));
    }

    @Test
    void parse_goalWithNegativeAmount_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("goal g/-50"));
    }

    @Test
    void parse_goalWithInvalidFormat_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("goal abc"));
    }

    @Test
    void parse_goalWithNonNumericAmount_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("goal g/abc"));
    }

    @Test
    void mutatesData_setGoal_returnsTrue() {
        assertTrue(new GoalCommand(100.00).mutatesData());
    }

    @Test
    void mutatesData_goalStatus_returnsFalse() {
        assertFalse(new GoalCommand().mutatesData());
    }
}
