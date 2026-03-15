package seedu.duke.command;

import java.util.logging.Logger;

import seedu.duke.ExpenseList;
import seedu.duke.Ui;

/**
 * Displays all available commands to the user.
 */
public class HelpCommand extends Command {

    private static final Logger logger = Logger.getLogger(HelpCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    /**
     * Executes the help command by displaying all available commands.
     *
     * @param expenses the expense list (unused)
     * @param ui the UI for displaying output
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        assert ui != null : "Ui should not be null";
        logger.info("Displaying help menu");
        ui.showHelp();
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
