package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.command.HelpCommand;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class HelpCommandTest {

    @Test
    public void helpCommand_isNotExit() {
        HelpCommand cmd = new HelpCommand();
        assertFalse(cmd.isExit());
    }

    @Test
    public void helpCommand_executesWithoutError() {
        HelpCommand cmd = new HelpCommand();
        cmd.execute(new ExpenseList(), new Ui());
    }
}
