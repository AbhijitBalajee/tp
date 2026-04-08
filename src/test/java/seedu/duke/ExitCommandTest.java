package seedu.duke;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ExitCommandTest {

    @Test
    public void execute_exit_noCrash() {
        ExpenseList list = new ExpenseList();
        Ui ui = new Ui();

        ExitCommand cmd = new ExitCommand();

        assertDoesNotThrow(() -> cmd.execute(list, ui));
    }
}
