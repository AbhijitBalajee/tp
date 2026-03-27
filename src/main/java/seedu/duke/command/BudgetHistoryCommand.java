package seedu.duke.command;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

/**
 * Displays the budget history in reverse chronological order.
 */
public class BudgetHistoryCommand extends Command {

    private static final Logger logger = Logger.getLogger(BudgetHistoryCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    /**
     * Executes the budget history command.
     *
     * @param expenses the current expense list
     * @param ui       the UI handler
     * @throws SpendTrackException if an error occurs
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        ArrayList<String> history = expenses.getBudgetHistory();
        logger.log(Level.INFO, "Displaying budget history with {0} entries", history.size());
        ui.showBudgetHistory(history);
    }
}
