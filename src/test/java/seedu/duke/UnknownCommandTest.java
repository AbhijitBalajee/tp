package seedu.duke;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UnknownCommandTest {

    @Test
    public void execute_unknown_noCrash() {
        ExpenseList list = new ExpenseList();
        Ui ui = new Ui();

        UnknownCommand cmd = new UnknownCommand();

        assertDoesNotThrow(() -> cmd.execute(list, ui));
    }
}