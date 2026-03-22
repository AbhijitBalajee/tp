package seedu.duke.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

/**
 * Sets the monthly budget limit for SpendTrack.
 */
public class BudgetCommand extends Command {

    private static final Logger logger = Logger.getLogger(BudgetCommand.class.getName());
    private static final double MAX_BUDGET = 1_000_000.00;

    static {
        logger.setUseParentHandlers(false);
    }

    private final double budgetAmount;

    /**
     * Constructs a BudgetCommand with the given budget amount.
     *
     * @param budgetAmount the monthly budget limit to set
     */
    public BudgetCommand(double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    /**
     * Executes the budget command, validating and setting the monthly budget.
     *
     * @param expenses the current expense list
     * @param ui       the UI handler
     * @throws SpendTrackException if the budget amount is invalid
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        validateBudget(budgetAmount);
        expenses.setBudget(budgetAmount);
        logger.log(Level.INFO, "Budget set to {0}", budgetAmount);

        double totalSpent = expenses.getTotal();
        ui.showBudgetSet(budgetAmount, totalSpent);

        if (totalSpent > budgetAmount) {
            ui.showBudgetExceeded(budgetAmount, totalSpent);
        }
    }

    /**
     * Validates the budget amount.
     *
     * @param amount the budget amount to validate
     * @throws SpendTrackException if the amount is invalid
     */
    private void validateBudget(double amount) throws SpendTrackException {
        if (amount <= 0) {
            throw new SpendTrackException("Budget must be greater than $0.00.");
        }
        if (amount > MAX_BUDGET) {
            throw new SpendTrackException("Budget cannot exceed $1,000,000.00.");
        }
    }

    @Override
    public boolean mutatesData() {
        return true;
    }
}
