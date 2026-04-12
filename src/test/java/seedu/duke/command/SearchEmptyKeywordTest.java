// @@author AfshalG
package seedu.duke.command;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchEmptyKeywordTest {

    @Test
    void execute_emptyKeyword_throwsException() {
        ExpenseList list = new ExpenseList();
        list.addExpense(new Expense("Coffee", 5.0, "Food", LocalDate.now(), false));
        list.addExpense(new Expense("Latte", 5.0, "Food", LocalDate.now(), false));

        SearchCommand cmd = new SearchCommand("");

        SpendTrackException e = assertThrows(SpendTrackException.class,
                () -> cmd.execute(list, new Ui()));
        assertTrue(e.getMessage().toLowerCase().contains("keyword"),
                "Error should mention missing keyword, got: " + e.getMessage());
    }

    @Test
    void execute_whitespaceOnlyKeyword_throwsException() {
        ExpenseList list = new ExpenseList();
        list.addExpense(new Expense("Coffee", 5.0, "Food", LocalDate.now(), false));

        SearchCommand cmd = new SearchCommand("   ");

        assertThrows(SpendTrackException.class, () -> cmd.execute(list, new Ui()));
    }

    @Test
    void execute_validKeyword_doesNotThrow() {
        ExpenseList list = new ExpenseList();
        list.addExpense(new Expense("Coffee", 5.0, "Food", LocalDate.now(), false));

        SearchCommand cmd = new SearchCommand("coffee");
        assertDoesNotThrow(() -> cmd.execute(list, new Ui()));
    }
}
