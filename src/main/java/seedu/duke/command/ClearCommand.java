package seedu.duke.command;

import java.util.logging.Logger;

import seedu.duke.ExpenseList;
import seedu.duke.Ui;

// @@author pranavjana
/**
 * Clears all expenses from the list after user confirmation.
 */
public class ClearCommand extends Command {

    private static final Logger logger = Logger.getLogger(ClearCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    /**
     * Executes the clear command by prompting for confirmation, then removing all expenses.
     *
     * @param expenses the expense list to clear
     * @param ui the UI for displaying output and reading confirmation
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        if (expenses.size() == 0) {
            ui.showMessage("No expenses to clear.");
            logger.info("Clear attempted on empty list.");
            return;
        }

        ui.showMessage("Are you sure you want to delete all expenses? "
                + "You can undo this with 'undo'. (yes/no):");
        String confirmation = ui.readCommand().trim();
        logger.info("Clear confirmation input: " + confirmation);

        if (confirmation.equalsIgnoreCase("yes")) {
            int count = expenses.size();
            expenses.clearAll();
            ui.showMessage("All expenses cleared. (" + count + " expense(s) removed)");
            logger.info("All " + count + " expenses cleared.");
        } else {
            ui.showMessage("Clear cancelled.");
            logger.info("Clear cancelled by user.");
        }
    }

    @Override
    public boolean mutatesData() {
        return true;
    }
}
