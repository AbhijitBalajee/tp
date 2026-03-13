package seedu.duke;

import seedu.duke.command.Command;

/**
 * Represents an unrecognised command entered by the user.
 */
public class UnknownCommand extends Command {

    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        ui.showError("Unknown command. Type 'help' for a list of commands.");
    }
}
