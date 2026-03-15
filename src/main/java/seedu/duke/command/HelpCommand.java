package seedu.duke.command;

import seedu.duke.ExpenseList;
import seedu.duke.Ui;

/**
 * Displays all available commands to the user.
 */
public class HelpCommand extends Command {

    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        ui.showHelp();
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
