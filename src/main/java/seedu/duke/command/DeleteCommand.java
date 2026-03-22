package seedu.duke.command;

import java.util.logging.Logger;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

/**
 * Deletes an expense from the expense list by its 1-based index.
 */
public class DeleteCommand extends Command {

    private static final Logger logger = Logger.getLogger(DeleteCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    private final int index;

    /**
     * Constructs a DeleteCommand for the given 1-based index.
     *
     * @param index the 1-based position of the expense to delete
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the delete command by removing the expense at the given index.
     *
     * @param expenses the expense list to delete from
     * @param ui the UI for displaying output
     * @throws SpendTrackException if the index is out of range
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        logger.info("Attempting to delete expense at index: " + index);
        if (index < 1 || index > expenses.size()) {
            logger.warning("Delete failed: index " + index + " out of range");
            throw new SpendTrackException("Index " + index + " is out of range. "
                    + "There are " + expenses.size() + " expense(s).");
        }
        Expense removed = expenses.deleteExpense(index - 1);
        logger.info("Expense deleted: " + removed);
        ui.showDeleteSuccess(removed);
    }

    @Override
    public boolean mutatesData() {
        return true;
    }
}
