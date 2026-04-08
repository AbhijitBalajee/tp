package seedu.duke.command;

import org.junit.jupiter.api.Test;
import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SearchCommandTest {

    @Test
    public void execute_search_noCrash() {
        ExpenseList list = new ExpenseList();
        list.addExpense(new Expense("Coffee", 5.0, "Food", LocalDate.now(), false));

        Ui ui = new Ui();
        SearchCommand cmd = new SearchCommand("coffee");

        assertDoesNotThrow(() -> cmd.execute(list, ui));
    }
}
