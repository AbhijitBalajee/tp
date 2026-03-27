package seedu.duke.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.duke.ExpenseList;
import seedu.duke.Ui;

/**
 * Lists all recorded expenses, or only recurring expenses if specified.
 */
public class ListCommand extends Command {

    private static final Logger logger = Logger.getLogger(ListCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    private final boolean recurringOnly;

    /**
     * Constructs a ListCommand that lists all expenses.
     */
    public ListCommand() {
        this.recurringOnly = false;
    }

    /**
     * Constructs a ListCommand with optional recurring filter.
     *
     * @param recurringOnly if true, only recurring expenses are shown
     */
    public ListCommand(boolean recurringOnly) {
        this.recurringOnly = recurringOnly;
    }

    /**
     * Executes the list command.
     *
     * @param expenses the current expense list
     * @param ui the UI handler
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        if (recurringOnly) {
            logger.log(Level.INFO, "Listing recurring expenses only");
            ui.showRecurringList(expenses);
        } else {
            logger.log(Level.INFO, "Executing list command with {0} expenses", expenses.size());
            ui.showExpenseList(expenses);
        }
    }
}