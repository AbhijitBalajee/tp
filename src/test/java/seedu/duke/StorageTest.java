package seedu.duke;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// @@author Ariff1422
class StorageTest {

    private static final String TEST_FILE = "data/test_expenses.txt";
    private static final LocalDate DATE_A = LocalDate.of(2026, 3, 10);
    private static final LocalDate DATE_B = LocalDate.of(2026, 3, 15);

    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = new Storage(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    // --- Missing file ---

    @Test
    void load_missingFile_listRemainsEmpty() {
        ExpenseList list = new ExpenseList();
        storage.load(list);
        assertEquals(0, list.size());
    }

    @Test
    void load_missingFile_noBudgetSet() {
        ExpenseList list = new ExpenseList();
        storage.load(list);
        assertFalse(list.hasBudget());
    }

    // --- Round-trip: expenses ---

    @Test
    void saveAndLoad_singleExpense_roundTrip() {
        ExpenseList saved = new ExpenseList();
        saved.addExpense(new Expense("Coffee", 4.50, "Food", DATE_A));
        storage.save(saved);

        ExpenseList loaded = new ExpenseList();
        storage.load(loaded);

        assertEquals(1, loaded.size());
        assertEquals("Coffee", loaded.getExpense(0).getDescription());
        assertEquals(4.50, loaded.getExpense(0).getAmount(), 0.001);
        assertEquals("Food", loaded.getExpense(0).getCategory());
        assertEquals(DATE_A, loaded.getExpense(0).getDate());
    }

    @Test
    void saveAndLoad_multipleExpenses_allPreserved() {
        ExpenseList saved = new ExpenseList();
        saved.addExpense(new Expense("Coffee", 4.50, "Food", DATE_A));
        saved.addExpense(new Expense("Bus", 1.80, "Transport", DATE_B));
        saved.addExpense(new Expense("Textbook", 35.00, "Education", DATE_A));
        storage.save(saved);

        ExpenseList loaded = new ExpenseList();
        storage.load(loaded);

        assertEquals(3, loaded.size());
        assertEquals("Coffee", loaded.getExpense(0).getDescription());
        assertEquals("Bus", loaded.getExpense(1).getDescription());
        assertEquals("Textbook", loaded.getExpense(2).getDescription());
    }

    @Test
    void saveAndLoad_emptyList_loadsEmpty() {
        ExpenseList saved = new ExpenseList();
        storage.save(saved);

        ExpenseList loaded = new ExpenseList();
        storage.load(loaded);

        assertEquals(0, loaded.size());
    }

    // --- Round-trip: budget ---

    @Test
    void saveAndLoad_budget_preserved() {
        ExpenseList saved = new ExpenseList();
        saved.setBudget(500.00);
        storage.save(saved);

        ExpenseList loaded = new ExpenseList();
        storage.load(loaded);

        assertTrue(loaded.hasBudget());
        assertEquals(500.00, loaded.getBudget(), 0.001);
    }

    @Test
    void saveAndLoad_noBudget_notLoadedAsBudget() {
        ExpenseList saved = new ExpenseList();
        storage.save(saved);

        ExpenseList loaded = new ExpenseList();
        storage.load(loaded);

        assertFalse(loaded.hasBudget());
    }

    // --- Round-trip: budget history ---

    @Test
    void saveAndLoad_budgetHistory_preserved() {
        ExpenseList saved = new ExpenseList();
        saved.setBudget(300.00);
        saved.setBudget(500.00);
        storage.save(saved);

        ExpenseList loaded = new ExpenseList();
        storage.load(loaded);

        assertEquals(2, loaded.getBudgetHistory().size());
    }

    // --- Malformed lines ---

    @Test
    void load_malformedExpenseLine_skipsAndContinues() throws IOException {
        File file = new File(TEST_FILE);
        file.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("---EXPENSES---\n");
            fw.write("BAD_LINE_NO_PIPES\n");
            fw.write("Coffee|4.50|Food|2026-03-10\n");
            fw.write("---BUDGET---\n");
            fw.write("0.0\n");
            fw.write("---BUDGET-HISTORY---\n");
        }

        ExpenseList loaded = new ExpenseList();
        storage.load(loaded);

        assertEquals(1, loaded.size());
        assertEquals("Coffee", loaded.getExpense(0).getDescription());
    }

    @Test
    void load_malformedAmountInExpenseLine_skipsLine() throws IOException {
        File file = new File(TEST_FILE);
        file.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("---EXPENSES---\n");
            fw.write("Coffee|notanumber|Food|2026-03-10\n");
            fw.write("Bus|1.80|Transport|2026-03-15\n");
            fw.write("---BUDGET---\n");
            fw.write("0.0\n");
            fw.write("---BUDGET-HISTORY---\n");
        }

        ExpenseList loaded = new ExpenseList();
        storage.load(loaded);

        assertEquals(1, loaded.size());
        assertEquals("Bus", loaded.getExpense(0).getDescription());
    }

    @Test
    void load_malformedDateInExpenseLine_skipsLine() throws IOException {
        File file = new File(TEST_FILE);
        file.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("---EXPENSES---\n");
            fw.write("Coffee|4.50|Food|not-a-date\n");
            fw.write("Bus|1.80|Transport|2026-03-15\n");
            fw.write("---BUDGET---\n");
            fw.write("0.0\n");
            fw.write("---BUDGET-HISTORY---\n");
        }

        ExpenseList loaded = new ExpenseList();
        storage.load(loaded);

        assertEquals(1, loaded.size());
        assertEquals("Bus", loaded.getExpense(0).getDescription());
    }

    @Test
    void load_malformedBudgetValue_budgetNotSet() throws IOException {
        File file = new File(TEST_FILE);
        file.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("---EXPENSES---\n");
            fw.write("---BUDGET---\n");
            fw.write("notanumber\n");
            fw.write("---BUDGET-HISTORY---\n");
        }

        ExpenseList loaded = new ExpenseList();
        storage.load(loaded);

        assertFalse(loaded.hasBudget());
    }

    @Test
    void load_zeroBudgetValue_budgetNotSet() throws IOException {
        File file = new File(TEST_FILE);
        file.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("---EXPENSES---\n");
            fw.write("---BUDGET---\n");
            fw.write("0.0\n");
            fw.write("---BUDGET-HISTORY---\n");
        }

        ExpenseList loaded = new ExpenseList();
        storage.load(loaded);

        assertFalse(loaded.hasBudget());
    }

    // --- Field-level round-trip ---

    @Test
    void saveAndLoad_expenseAmount_preservesPrecision() {
        ExpenseList saved = new ExpenseList();
        saved.addExpense(new Expense("Item", 12.99, "Misc", DATE_A));
        storage.save(saved);

        ExpenseList loaded = new ExpenseList();
        storage.load(loaded);

        assertEquals(12.99, loaded.getExpense(0).getAmount(), 0.001);
    }

    @Test
    void saveAndLoad_expenseDate_preservedAsLocalDate() {
        ExpenseList saved = new ExpenseList();
        saved.addExpense(new Expense("Item", 5.00, "Misc", DATE_B));
        storage.save(saved);

        ExpenseList loaded = new ExpenseList();
        storage.load(loaded);

        assertEquals(DATE_B, loaded.getExpense(0).getDate());
    }

    @Test
    void saveAndLoad_expenseCategory_preserved() {
        ExpenseList saved = new ExpenseList();
        saved.addExpense(new Expense("Grab", 12.00, "Transport", DATE_A));
        storage.save(saved);

        ExpenseList loaded = new ExpenseList();
        storage.load(loaded);

        assertEquals("Transport", loaded.getExpense(0).getCategory());
    }
}
// @@author
