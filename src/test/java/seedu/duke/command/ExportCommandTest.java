package seedu.duke.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExportCommandTest {

    private static final PrintStream ORIGINAL_OUT = System.out;
    private static final String TEST_EXPORT_PATH = "data/test_export.csv";

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
    void tearDown() {
        System.setOut(ORIGINAL_OUT);
        File testFile = new File(TEST_EXPORT_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void execute_emptyList_showsNoExpensesMessage() {
        new ExportCommand(TEST_EXPORT_PATH).execute(expenses, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("No expenses to export."));
        assertFalse(new File(TEST_EXPORT_PATH).exists());
    }

    @Test
    void execute_singleExpense_writesHeaderAndRow() throws IOException {
        expenses.addExpense(new Expense("Coffee", 3.50, "Food", LocalDate.of(2026, 3, 15)));

        new ExportCommand(TEST_EXPORT_PATH).execute(expenses, ui);

        File file = new File(TEST_EXPORT_PATH);
        assertTrue(file.exists());
        List<String> lines = Files.readAllLines(file.toPath());
        assertEquals(2, lines.size());
        assertEquals("Description,Amount,Category,Date,Recurring", lines.get(0));
        assertEquals("Coffee,3.50,Food,2026-03-15,false", lines.get(1));
    }

    @Test
    void execute_multipleExpenses_writesAllRows() throws IOException {
        expenses.addExpense(new Expense("Coffee", 3.50, "Food", LocalDate.of(2026, 3, 15)));
        expenses.addExpense(new Expense("Bus", 1.80, "Transport", LocalDate.of(2026, 3, 16)));

        new ExportCommand(TEST_EXPORT_PATH).execute(expenses, ui);

        List<String> lines = Files.readAllLines(new File(TEST_EXPORT_PATH).toPath());
        assertEquals(3, lines.size());
    }

    @Test
    void execute_showsSuccessMessage() {
        expenses.addExpense(new Expense("Coffee", 3.50, "Food", LocalDate.of(2026, 3, 15)));

        new ExportCommand(TEST_EXPORT_PATH).execute(expenses, ui);

        assertTrue(outputStream.toString().contains("Expenses exported to"));
    }

    @Test
    void formatCsvRow_descriptionWithComma_wrapsInQuotes() {
        Expense expense = new Expense("Lunch, with John", 12.00, "Food", LocalDate.of(2026, 3, 15));

        String row = ExportCommand.formatCsvRow(expense);

        assertTrue(row.startsWith("\"Lunch, with John\""));
    }

    @Test
    void formatCsvRow_descriptionWithDoubleQuotes_escapesQuotes() {
        Expense expense = new Expense("Book \"Java\"", 30.00, "Education", LocalDate.of(2026, 3, 15));

        String row = ExportCommand.formatCsvRow(expense);

        assertTrue(row.startsWith("\"Book \"\"Java\"\"\""));
    }

    @Test
    void formatCsvRow_normalDescription_noQuotes() {
        Expense expense = new Expense("Coffee", 3.50, "Food", LocalDate.of(2026, 3, 15));

        String row = ExportCommand.formatCsvRow(expense);

        assertEquals("Coffee,3.50,Food,2026-03-15,false", row);
    }

    @Test
    void formatCsvRow_recurringExpense_showsTrue() {
        Expense expense = new Expense("Netflix", 15.00, "Entertainment",
                LocalDate.of(2026, 3, 15), true);

        String row = ExportCommand.formatCsvRow(expense);

        assertTrue(row.endsWith("true"));
    }

    @Test
    void execute_descriptionWithComma_csvFileHasQuotedField() throws IOException {
        expenses.addExpense(new Expense("Lunch, with friends", 20.00, "Food",
                LocalDate.of(2026, 3, 15)));

        new ExportCommand(TEST_EXPORT_PATH).execute(expenses, ui);

        List<String> lines = Files.readAllLines(new File(TEST_EXPORT_PATH).toPath());
        assertEquals("\"Lunch, with friends\",20.00,Food,2026-03-15,false", lines.get(1));
    }
}
