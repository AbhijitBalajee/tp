package seedu.duke.command;

import seedu.duke.ExpenseList;
import seedu.duke.Ui;

/**
 * Displays the total sum of all expenses.
 */
public class TotalCommand extends Command {

    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        double total = expenses.getTotal();
        ui.showTotal(total);
    }
}
