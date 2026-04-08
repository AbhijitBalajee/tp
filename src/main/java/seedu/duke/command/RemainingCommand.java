// @@author jainsaksham2006
package seedu.duke.command;

import java.util.logging.Logger;

import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

/**
 * Displays the remaining balance after deducting total expenses from the set budget.
 */
public class RemainingCommand extends Command {

    private static final Logger logger = Logger.getLogger(RemainingCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    /**
     * Executes the remaining command.
     * Calculates remaining balance as:
     * remaining = budget - total expenses
     *
     * @param expenses the expense list containing budget and expenses
     * @param ui the UI used to display output
     * @throws SpendTrackException if no budget has been set
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        logger.info("Executing RemainingCommand");

        if (!expenses.hasBudget()) {
            logger.warning("No budget set for RemainingCommand");
            throw new SpendTrackException("No budget set. Use 'budget <amount>' to set one first.");
        }

        double budget = expenses.getBudget();
        double total = expenses.getTotal();
        double remaining = budget - total;

        logger.info("Budget: " + budget + ", Total: " + total + ", Remaining: " + remaining);

        ui.showRemaining(budget, total, remaining);
    }

    /**
     * Indicates that this command does not exit the application.
     *
     * @return false since this is not an exit command
     */
    @Override
    public boolean isExit() {
        return false;
    }
}