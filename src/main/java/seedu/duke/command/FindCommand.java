package seedu.duke.command;

import java.util.logging.Logger;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

/**
 * Displays full details of a single expense by its 1-based index.
 */
public class FindCommand extends Command {

    private static final Logger logger = Logger.getLogger(FindCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    private final int index;

    /**
     * Constructs a FindCommand for the given 1-based index.
     *
     * @param index the 1-based position of the expense to find
     */
    public FindCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the find command by displaying details of the expense at the given index.
     *
     * @param expenses the expense list to search
     * @param ui the UI for displaying output
     * @throws SpendTrackException if the index is out of range or list is empty
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        logger.info("Finding expense at index: " + index);

        if (expenses.size() == 0) {
            throw new SpendTrackException("No expenses recorded yet.");
        }
        if (index < 1 || index > expenses.size()) {
            throw new SpendTrackException("Index " + index + " is out of range. "
                    + "There are " + expenses.size() + " expense(s).");
        }

        Expense expense = expenses.getExpense(index - 1);
        logger.info("Found expense: " + expense);
        ui.showExpenseDetail(index, expense);
    }
}
