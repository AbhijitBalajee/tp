package seedu.duke.command;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

/**
 * Deletes an expense from the expense list by its 1-based index.
 */
public class DeleteCommand extends Command {

    private int index;

    /**
     * Constructs a DeleteCommand for the given 1-based index.
     *
     * @param index the 1-based position of the expense to delete
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        if (index < 1 || index > expenses.size()) {
            throw new SpendTrackException("Index " + index + " is out of range. "
                    + "There are " + expenses.size() + " expense(s).");
        }
        Expense removed = expenses.deleteExpense(index - 1);
        ui.showDeleteSuccess(removed);
    }
}
