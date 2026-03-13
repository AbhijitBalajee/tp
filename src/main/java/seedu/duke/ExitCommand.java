package seedu.duke;

import seedu.duke.command.Command;

/**
 * Signals the application to exit.
 */
public class ExitCommand extends Command {

    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
