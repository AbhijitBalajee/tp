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
import seedu.duke.Ui;
import seedu.duke.UndoManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UndoCommandTest {

    private static final PrintStream ORIGINAL_OUT = System.out;

    private ExpenseList expenses;
    private Ui ui;
    private UndoManager undoManager;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        expenses = new ExpenseList();
        ui = new Ui();
        undoManager = new UndoManager();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void restoreSystemOut() {
        System.setOut(ORIGINAL_OUT);
    }

    @Test
    void execute_undoAfterAdd_removesAddedExpense() throws SpendTrackException {
        expenses.addExpense(new Expense("Old", 5.00, "Food", LocalDate.now()));
        undoManager.saveSnapshot(expenses);
        expenses.addExpense(new Expense("New", 10.00, "Food", LocalDate.now()));
        assertEquals(2, expenses.size());

        new UndoCommand(undoManager).execute(expenses, ui);

        assertEquals(1, expenses.size());
        assertEquals("Old", expenses.getExpense(0).getDescription());
        assertTrue(outputStream.toString().contains("Last command undone successfully."));
    }

    @Test
    void execute_undoAfterDelete_restoresDeletedExpense() throws SpendTrackException {
        expenses.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.now()));
        undoManager.saveSnapshot(expenses);
        expenses.deleteExpense(0);
        assertEquals(0, expenses.size());

        new UndoCommand(undoManager).execute(expenses, ui);

        assertEquals(1, expenses.size());
        assertEquals("Lunch", expenses.getExpense(0).getDescription());
    }

    @Test
    void execute_nothingToUndo_showsMessage() {
        new UndoCommand(undoManager).execute(expenses, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("Nothing to undo."));
    }

    @Test
    void execute_twoConsecutiveUndos_secondShowsNothingToUndo() {
        expenses.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.now()));
        undoManager.saveSnapshot(expenses);
        expenses.addExpense(new Expense("Dinner", 20.00, "Food", LocalDate.now()));

        new UndoCommand(undoManager).execute(expenses, ui);
        new UndoCommand(undoManager).execute(expenses, ui);

        assertEquals(1, expenses.size());
        assertTrue(outputStream.toString().contains("Nothing to undo."));
    }

    @Test
    void execute_undoRestoresBudget() {
        expenses.setBudget(500.00);
        undoManager.saveSnapshot(expenses);
        expenses.setBudget(1000.00);

        new UndoCommand(undoManager).execute(expenses, ui);

        assertEquals(500.00, expenses.getBudget(), 0.01);
    }

    @Test
    void execute_undoAfterEdit_restoresOriginalExpense() {
        expenses.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.now()));
        undoManager.saveSnapshot(expenses);
        expenses.getExpense(0).setAmount(99.00);

        new UndoCommand(undoManager).execute(expenses, ui);

        assertEquals(10.00, expenses.getExpense(0).getAmount(), 0.01);
    }

    @Test
    void execute_snapshotIsDeepCopy_originalNotAffected() {
        expenses.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.now()));
        undoManager.saveSnapshot(expenses);
        expenses.getExpense(0).setDescription("Modified");
        expenses.addExpense(new Expense("Dinner", 20.00, "Food", LocalDate.now()));

        new UndoCommand(undoManager).execute(expenses, ui);

        assertEquals(1, expenses.size());
        assertEquals("Lunch", expenses.getExpense(0).getDescription());
    }

    @Test
    void mutatesData_returnsTrue() {
        assertTrue(new UndoCommand(undoManager).mutatesData());
    }
}
