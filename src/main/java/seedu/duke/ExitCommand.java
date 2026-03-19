package seedu.duke;

import java.util.logging.Logger;
import seedu.duke.command.Command;

/**
 * Signals the application to exit.
 */
public class ExitCommand extends Command {

    private static final Logger logger = Logger.getLogger(ExitCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    /**
     * Executes the exit command by displaying the goodbye message.
     *
     * @param expenses the expense list (unused)
     * @param ui the UI for displaying output
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        assert ui != null : "Ui should not be null";
        logger.info("Exit command executed — application shutting down");
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
