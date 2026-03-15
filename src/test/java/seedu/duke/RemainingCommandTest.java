package seedu.duke;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemainingCommandTest {

    @Test
    public void getRemaining_returnsCorrectValue() {
        double budget = 100.0;
        double expenses = 40.0;

        double result = RemainingCommand.getRemaining(budget, expenses);

        assertEquals(60.0, result);
    }

}