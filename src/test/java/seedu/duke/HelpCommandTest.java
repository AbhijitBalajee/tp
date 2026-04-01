package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.command.HelpCommand;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class HelpCommandTest {

    @Test
    public void execute_help_noCrash() {
        ExpenseList list = new ExpenseList();
        Ui ui = new Ui();

        HelpCommand cmd = new HelpCommand();

        assertDoesNotThrow(() -> cmd.execute(list, ui));
    }
}
