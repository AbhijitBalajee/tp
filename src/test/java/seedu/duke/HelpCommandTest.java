package seedu.duke;

import org.junit.jupiter.api.Test;

public class HelpCommandTest {

    @Test
    public void execute_runsWithoutError() {
        HelpCommand help = new HelpCommand();
        help.execute();
    }
}