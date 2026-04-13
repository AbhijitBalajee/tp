package seedu.duke;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// @@author pranavjana
class BudgetCheckerTest {

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
    void check_noBudgetSet_noOutput() {
        expenses.addExpense(new Expense("Lunch", 50.00, "Food", LocalDate.now()));

        BudgetChecker.check(expenses, ui);

        String output = outputStream.toString();
        assertFalse(output.contains("[WARNING]"));
        assertFalse(output.contains("[ALERT]"));
    }

    @Test
    void check_spendingUnderNinetyPercent_noOutput() {
        expenses.setBudget(100.00);
        expenses.addExpense(new Expense("Lunch", 50.00, "Food", LocalDate.now()));

        BudgetChecker.check(expenses, ui);

        String output = outputStream.toString();
        assertFalse(output.contains("[WARNING]"));
        assertFalse(output.contains("[ALERT]"));
    }

    @Test
    void check_spendingAtExactlyNinetyPercent_showsWarning() {
        expenses.setBudget(100.00);
        expenses.addExpense(new Expense("Lunch", 90.00, "Food", LocalDate.now()));

        BudgetChecker.check(expenses, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("[WARNING]"));
        assertTrue(output.contains("$90.00"));
        assertTrue(output.contains("$100.00"));
    }

    @Test
    void check_spendingBetweenNinetyAndHundredPercent_showsWarning() {
        expenses.setBudget(100.00);
        expenses.addExpense(new Expense("Lunch", 95.00, "Food", LocalDate.now()));

        BudgetChecker.check(expenses, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("[WARNING]"));
        assertFalse(output.contains("[ALERT]"));
    }

    @Test
    void check_spendingAtExactlyBudget_showsWarning() {
        expenses.setBudget(100.00);
        expenses.addExpense(new Expense("Lunch", 100.00, "Food", LocalDate.now()));

        BudgetChecker.check(expenses, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("[WARNING]"));
        assertFalse(output.contains("[ALERT]"));
    }

    @Test
    void check_spendingExceedsBudget_showsAlert() {
        expenses.setBudget(100.00);
        expenses.addExpense(new Expense("Lunch", 120.00, "Food", LocalDate.now()));

        BudgetChecker.check(expenses, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("[ALERT]"));
        assertTrue(output.contains("$120.00"));
        assertTrue(output.contains("$100.00"));
    }

    @Test
    void check_multipleExpensesExceedBudget_showsAlert() {
        expenses.setBudget(100.00);
        expenses.addExpense(new Expense("Lunch", 60.00, "Food", LocalDate.now()));
        expenses.addExpense(new Expense("Dinner", 50.00, "Food", LocalDate.now()));

        BudgetChecker.check(expenses, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("[ALERT]"));
        assertTrue(output.contains("$110.00"));
    }

    @Test
    void check_multipleExpensesTriggerWarning_showsWarning() {
        expenses.setBudget(100.00);
        expenses.addExpense(new Expense("Lunch", 50.00, "Food", LocalDate.now()));
        expenses.addExpense(new Expense("Dinner", 42.00, "Food", LocalDate.now()));

        BudgetChecker.check(expenses, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("[WARNING]"));
        assertTrue(output.contains("$92.00"));
    }

    @Test
    void check_spendingJustBelowNinetyPercent_noOutput() {
        expenses.setBudget(100.00);
        expenses.addExpense(new Expense("Lunch", 89.99, "Food", LocalDate.now()));

        BudgetChecker.check(expenses, ui);

        String output = outputStream.toString();
        assertFalse(output.contains("[WARNING]"));
        assertFalse(output.contains("[ALERT]"));
    }

    @Test
    void check_zeroExpenses_noOutput() {
        expenses.setBudget(100.00);

        BudgetChecker.check(expenses, ui);

        String output = outputStream.toString();
        assertFalse(output.contains("[WARNING]"));
        assertFalse(output.contains("[ALERT]"));
    }

    @Test
    void check_budgetResetAfterExceeding_noOutput() {
        expenses.setBudget(100.00);
        expenses.addExpense(new Expense("Lunch", 120.00, "Food", LocalDate.now()));
        expenses.resetBudget();

        BudgetChecker.check(expenses, ui);

        String output = outputStream.toString();
        assertFalse(output.contains("[WARNING]"));
        assertFalse(output.contains("[ALERT]"));
    }

    // @@author AfshalG
    @Test
    void check_oldMonthExpensesExceedBudget_noAlert() {
        expenses.setBudget(100.00);
        expenses.addExpense(new Expense("Old lunch", 120.00, "Food", LocalDate.now().minusMonths(1)));

        BudgetChecker.check(expenses, ui);

        String output = outputStream.toString();
        assertFalse(output.contains("[WARNING]"));
        assertFalse(output.contains("[ALERT]"));
    }

    @Test
    void check_oldMonthExpensesAtNinetyPercent_noWarning() {
        expenses.setBudget(100.00);
        expenses.addExpense(new Expense("Old lunch", 95.00, "Food", LocalDate.now().minusMonths(1)));

        BudgetChecker.check(expenses, ui);

        String output = outputStream.toString();
        assertFalse(output.contains("[WARNING]"));
        assertFalse(output.contains("[ALERT]"));
    }

    @Test
    void check_currentMonthExceedsMixedWithOldExpenses_showsAlertForCurrentOnly() {
        expenses.setBudget(100.00);
        expenses.addExpense(new Expense("Old item", 500.00, "Misc", LocalDate.now().minusMonths(2)));
        expenses.addExpense(new Expense("This month", 110.00, "Food", LocalDate.now()));

        BudgetChecker.check(expenses, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("[ALERT]"));
        assertTrue(output.contains("$110.00"));
    }

    @Test
    void check_currentMonthUnderBudgetDespiteHighAllTimeTotal_noOutput() {
        expenses.setBudget(100.00);
        expenses.addExpense(new Expense("Old item", 200.00, "Misc", LocalDate.now().minusMonths(1)));
        expenses.addExpense(new Expense("Old item 2", 300.00, "Misc", LocalDate.now().minusMonths(3)));
        expenses.addExpense(new Expense("This month", 50.00, "Food", LocalDate.now()));

        BudgetChecker.check(expenses, ui);

        String output = outputStream.toString();
        assertFalse(output.contains("[WARNING]"));
        assertFalse(output.contains("[ALERT]"));
    }
    // @@author
}
