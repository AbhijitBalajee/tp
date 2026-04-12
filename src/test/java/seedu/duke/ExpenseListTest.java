package seedu.duke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

// @@author Ariff1422
class ExpenseListTest {

    private static final LocalDate DATE = LocalDate.of(2026, 3, 22);
    private ExpenseList list;

    @BeforeEach
    void setUp() {
        list = new ExpenseList();
    }

    // --- Initial state ---

    @Test
    void newList_isEmpty() {
        assertEquals(0, list.size());
    }

    @Test
    void newList_hasNoBudget() {
        assertFalse(list.hasBudget());
        assertEquals(0.0, list.getBudget(), 0.001);
    }

    @Test
    void newList_budgetHistoryEmpty() {
        assertTrue(list.getBudgetHistory().isEmpty());
    }

    // --- addExpense / getExpense / size ---

    @Test
    void addExpense_singleItem_sizeIsOne() {
        list.addExpense(new Expense("Coffee", 4.50, "Food", DATE));
        assertEquals(1, list.size());
    }

    @Test
    void addExpense_multipleItems_sizeIncrements() {
        list.addExpense(new Expense("Coffee", 4.50, "Food", DATE));
        list.addExpense(new Expense("Bus", 1.80, "Transport", DATE));
        assertEquals(2, list.size());
    }

    @Test
    void getExpense_returnsCorrectItem() {
        Expense e = new Expense("Coffee", 4.50, "Food", DATE);
        list.addExpense(e);
        assertSame(e, list.getExpense(0));
    }

    @Test
    void getExpenses_returnsUnderlyingList() {
        list.addExpense(new Expense("Coffee", 4.50, "Food", DATE));
        ArrayList<Expense> inner = list.getExpenses();
        assertEquals(1, inner.size());
    }

    // --- setExpense ---

    @Test
    void setExpense_replacesItemAtIndex() {
        list.addExpense(new Expense("Coffee", 4.50, "Food", DATE));
        Expense replacement = new Expense("Latte", 5.00, "Food", DATE);
        list.setExpense(0, replacement);
        assertSame(replacement, list.getExpense(0));
    }

    @Test
    void setExpense_doesNotChangeSize() {
        list.addExpense(new Expense("Coffee", 4.50, "Food", DATE));
        list.setExpense(0, new Expense("Latte", 5.00, "Food", DATE));
        assertEquals(1, list.size());
    }

    // --- deleteExpense ---

    @Test
    void deleteExpense_removesItemAtIndex() {
        list.addExpense(new Expense("Coffee", 4.50, "Food", DATE));
        list.addExpense(new Expense("Bus", 1.80, "Transport", DATE));
        list.deleteExpense(0);
        assertEquals(1, list.size());
        assertEquals("Bus", list.getExpense(0).getDescription());
    }

    @Test
    void deleteExpense_returnsRemovedItem() {
        Expense e = new Expense("Coffee", 4.50, "Food", DATE);
        list.addExpense(e);
        Expense removed = list.deleteExpense(0);
        assertSame(e, removed);
    }

    @Test
    void deleteExpense_lastItem_listBecomesEmpty() {
        list.addExpense(new Expense("Coffee", 4.50, "Food", DATE));
        list.deleteExpense(0);
        assertEquals(0, list.size());
    }

    // --- getTotal ---

    @Test
    void getTotal_emptyList_returnsZero() {
        assertEquals(0.0, list.getTotal(), 0.001);
    }

    @Test
    void getTotal_singleItem_returnsAmount() {
        list.addExpense(new Expense("Coffee", 4.50, "Food", DATE));
        assertEquals(4.50, list.getTotal(), 0.001);
    }

    @Test
    void getTotal_multipleItems_returnsSum() {
        list.addExpense(new Expense("Coffee", 4.50, "Food", DATE));
        list.addExpense(new Expense("Bus", 1.80, "Transport", DATE));
        list.addExpense(new Expense("Textbook", 35.00, "Education", DATE));
        assertEquals(41.30, list.getTotal(), 0.001);
    }

    // --- Budget ---

    @Test
    void setBudget_updatesBudgetAndHasBudgetTrue() {
        list.setBudget(500.00);
        assertTrue(list.hasBudget());
        assertEquals(500.00, list.getBudget(), 0.001);
    }

    @Test
    void setBudget_appendsBudgetHistory() {
        list.setBudget(300.00);
        assertEquals(1, list.getBudgetHistory().size());
    }

    @Test
    void setBudget_multipleTimesAddsMultipleHistoryEntries() {
        list.setBudget(300.00);
        list.setBudget(500.00);
        assertEquals(2, list.getBudgetHistory().size());
    }

    @Test
    void setBudgetDirectly_setsBudgetWithoutHistory() {
        list.setBudgetDirectly(400.00);
        assertEquals(400.00, list.getBudget(), 0.001);
        assertTrue(list.getBudgetHistory().isEmpty());
    }

    @Test
    void resetBudget_setBudgetToZero() {
        list.setBudget(500.00);
        list.resetBudget();
        assertEquals(0.0, list.getBudget(), 0.001);
        assertFalse(list.hasBudget());
    }

    @Test
    void resetBudget_budgetHistoryPreserved() {
        list.setBudget(500.0);
        list.resetBudget();
        assertEquals(2, list.getBudgetHistory().size());
        String resetEntry = list.getBudgetHistory().get(1);
        assertTrue(resetEntry.endsWith("|0.0"));
    }

    // --- Budget History ---

    @Test
    void addBudgetHistory_addsEntry() {
        list.addBudgetHistory("2026-03-22|500.00");
        assertEquals(1, list.getBudgetHistory().size());
        assertEquals("2026-03-22|500.00", list.getBudgetHistory().get(0));
    }

    @Test
    void addBudgetHistory_multipleEntries_preservesOrder() {
        list.addBudgetHistory("2026-03-01|300.00");
        list.addBudgetHistory("2026-03-22|500.00");
        assertEquals("2026-03-01|300.00", list.getBudgetHistory().get(0));
        assertEquals("2026-03-22|500.00", list.getBudgetHistory().get(1));
    }
}
// @@author
