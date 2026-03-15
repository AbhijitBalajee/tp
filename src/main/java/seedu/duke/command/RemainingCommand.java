package seedu.duke.command;

import java.util.logging.Logger;

import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

/**
 * Displays the remaining balance after deducting expenses from the budget.
 */
public class RemainingCommand extends Command {

    private static final Logger logger = Logger.getLogger(RemainingCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    /**
     * Executes the remaining command by displaying the remaining budget balance.
     *
     * @param expenses the expense list containing budget and expenses
     * @param ui the UI for displaying output
     * @throws SpendTrackException if no budget has been set
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        logger.info("Executing remaining command");
        if (!expenses.hasBudget()) {
            logger.warning("Remaining command failed: no budget set");
            throw new SpendTrackException("No budget set. Use 'budget <amount>' to set one first.");
        }
        double remaining = expenses.getBudget() - expenses.getTotal();
        logger.info("Remaining balance: " + remaining);
        ui.showRemaining(expenses.getBudget(), expenses.getTotal(), remaining);
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
