package seedu.duke;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// @@author Ariff1422
class ExpenseTest {

    private static final LocalDate FIXED_DATE = LocalDate.of(2026, 3, 22);

    // --- Constructor (4-arg): defaults ---

    @Test
    void constructor_fourArg_setsFieldsCorrectly() {
        Expense e = new Expense("Coffee", 4.50, "Food", FIXED_DATE);
        assertEquals("Coffee", e.getDescription());
        assertEquals(4.50, e.getAmount(), 0.001);
        assertEquals("Food", e.getCategory());
        assertEquals(FIXED_DATE, e.getDate());
    }

    @Test
    void constructor_fourArg_recurringDefaultsFalse() {
        Expense e = new Expense("Coffee", 4.50, "Food", FIXED_DATE);
        assertFalse(e.isRecurring());
    }

    // --- Constructor (5-arg): recurring flag ---

    @Test
    void constructor_fiveArg_recurringTrue() {
        Expense e = new Expense("Gym", 80.00, "Health", FIXED_DATE, true);
        assertTrue(e.isRecurring());
    }

    @Test
    void constructor_fiveArg_recurringFalse() {
        Expense e = new Expense("Gym", 80.00, "Health", FIXED_DATE, false);
        assertFalse(e.isRecurring());
    }

    // --- Setters ---

    @Test
    void setDescription_updatesDescription() {
        Expense e = new Expense("Coffee", 4.50, "Food", FIXED_DATE);
        e.setDescription("Latte");
        assertEquals("Latte", e.getDescription());
    }

    @Test
    void setAmount_updatesAmount() {
        Expense e = new Expense("Coffee", 4.50, "Food", FIXED_DATE);
        e.setAmount(5.00);
        assertEquals(5.00, e.getAmount(), 0.001);
    }

    @Test
    void setCategory_updatesCategory() {
        Expense e = new Expense("Coffee", 4.50, "Food", FIXED_DATE);
        e.setCategory("Beverage");
        assertEquals("Beverage", e.getCategory());
    }

    @Test
    void setDate_updatesDate() {
        Expense e = new Expense("Coffee", 4.50, "Food", FIXED_DATE);
        LocalDate newDate = LocalDate.of(2026, 4, 1);
        e.setDate(newDate);
        assertEquals(newDate, e.getDate());
    }

    @Test
    void setRecurring_toTrue_updatesFlag() {
        Expense e = new Expense("Coffee", 4.50, "Food", FIXED_DATE);
        e.setRecurring(true);
        assertTrue(e.isRecurring());
    }

    @Test
    void setRecurring_toFalse_updatesFlag() {
        Expense e = new Expense("Gym", 80.00, "Health", FIXED_DATE, true);
        e.setRecurring(false);
        assertFalse(e.isRecurring());
    }

    // --- toString ---

    @Test
    void toString_nonRecurring_noTag() {
        Expense e = new Expense("Coffee", 4.50, "Food", FIXED_DATE);
        String result = e.toString();
        assertTrue(result.contains("Coffee"));
        assertTrue(result.contains("$4.50"));
        assertTrue(result.contains("Food"));
        assertFalse(result.contains("[R]"));
    }

    @Test
    void toString_recurring_containsTag() {
        Expense e = new Expense("Gym", 80.00, "Health", FIXED_DATE, true);
        assertTrue(e.toString().contains("[R]"));
    }

    @Test
    void toString_containsDate() {
        Expense e = new Expense("Coffee", 4.50, "Food", FIXED_DATE);
        assertTrue(e.toString().contains(FIXED_DATE.toString()));
    }

    @Test
    void toString_formatsAmountToTwoDecimals() {
        Expense e = new Expense("Coffee", 4.5, "Food", FIXED_DATE);
        assertTrue(e.toString().contains("$4.50"));
    }
}
// @@author
