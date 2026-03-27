package seedu.duke.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

/**
 * Resets the monthly budget to zero.
 */
public class BudgetResetCommand extends Command {

    private static final Logger logger = Logger.getLogger(BudgetResetCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    /**
     * Executes the budget reset command.
     *
     * @param expenses the current expense list
     * @param ui       the UI handler
     * @throws SpendTrackException if no budget has been set
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        if (!expenses.hasBudget()) {
            logger.warning("Budget reset attempted with no budget set");
            throw new SpendTrackException("No budget to reset.");
        }

        expenses.resetBudget();
        logger.log(Level.INFO, "Budget has been reset");
        ui.showBudgetReset();
    }

    @Override
    public boolean mutatesData() {
        return true;
    }
}
