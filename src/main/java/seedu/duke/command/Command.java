package seedu.duke.command;

import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

/**
 * Represents an executable command.
 */
public abstract class Command {

    /**
     * Executes the command with the given expense list and UI.
     *
     * @param expenses the expense list to operate on
     * @param ui the UI for displaying output
     */
    public abstract void execute(ExpenseList expenses, Ui ui) throws SpendTrackException;

    /**
     * Returns true if this command signals the application to exit.
     *
     * @return false by default
     */
    public boolean isExit() {
        return false;
    }
}
