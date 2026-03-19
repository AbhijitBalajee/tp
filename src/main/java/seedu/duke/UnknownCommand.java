package seedu.duke;

import java.util.logging.Logger;
import seedu.duke.command.Command;

/**
 * Represents an unrecognised command entered by the user.
 */
public class UnknownCommand extends Command {

    private static final Logger logger = Logger.getLogger(UnknownCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    /**
     * Executes the unknown command by displaying an error message.
     *
     * @param expenses the expense list (unused)
     * @param ui the UI for displaying output
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        assert ui != null : "Ui should not be null";
        logger.warning("Unknown command entered by user");
        ui.showError("Unknown command. Type 'help' for a list of commands.");
    }
}

