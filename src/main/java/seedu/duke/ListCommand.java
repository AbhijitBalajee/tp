package seedu.duke.command;

import seedu.duke.ExpenseList;
import seedu.duke.Ui;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Lists all recorded expenses.
 */
public class ListCommand extends Command {

    private static final Logger logger = Logger.getLogger(ListCommand.class.getName());

    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        logger.log(Level.INFO, "Executing list command with {0} expenses", expenses.size());

        ui.showExpenseList(expenses);
    }

    @Override
    public boolean isExit() {
        return false;
    }
}