// @@author jainsaksham2006
package seedu.duke.command;

import java.util.logging.Logger;

import seedu.duke.ExpenseList;
import seedu.duke.Ui;

/**
 * Displays all available commands and their usage to the user.
 */
public class HelpCommand extends Command {

    private static final Logger logger = Logger.getLogger(HelpCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    /**
     * Executes the help command.
     * Displays a list of all supported commands through the UI.
     *
     * @param expenses the expense list (unused in this command)
     * @param ui the UI used to display output
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        logger.info("Executing HelpCommand");

        ui.showHelp();
    }

    /**
     * Indicates that this command does not terminate the application.
     *
     * @return false since this is not an exit command
     */
    @Override
    public boolean isExit() {
        return false;
    }
}
