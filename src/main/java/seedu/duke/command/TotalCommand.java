package seedu.duke.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.duke.ExpenseList;
import seedu.duke.Ui;

/**
 * Displays the total sum of all expenses.
 */
public class TotalCommand extends Command {
    private static final Logger logger = Logger.getLogger(TotalCommand.class.getName());

    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        logger.log(Level.INFO, "Executing total command with {0} expenses", expenses.size());
        double total = expenses.getTotal();
        logger.log(Level.INFO, "Calculated total expenses: {0}", total);
        ui.showTotal(total);
    }
}
