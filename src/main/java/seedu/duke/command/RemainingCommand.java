package seedu.duke.command;

import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

/**
 * Displays the remaining balance after deducting expenses from the budget.
 */
public class RemainingCommand extends Command {

    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        if (!expenses.hasBudget()) {
            throw new SpendTrackException("No budget set. Use 'budget <amount>' to set one first.");
        }
        double remaining = expenses.getBudget() - expenses.getTotal();
        ui.showRemaining(expenses.getBudget(), expenses.getTotal(), remaining);
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
