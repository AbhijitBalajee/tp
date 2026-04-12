// @@author AfshalG
package seedu.duke;

import org.junit.jupiter.api.Test;

import seedu.duke.command.Command;
import seedu.duke.command.FilterCommand;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParserFilterDuplicateTest {

    @Test
    void parse_duplicateFrom_throwsException() {
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("filter from/2026-03-01 from/2026-03-05 to/2026-03-31"));
        assertTrue(e.getMessage().contains("from/"),
                "Error should mention from/, got: " + e.getMessage());
    }

    @Test
    void parse_duplicateTo_throwsException() {
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("filter from/2026-03-01 to/2026-03-05 to/2026-03-31"));
        assertTrue(e.getMessage().contains("to/"),
                "Error should mention to/, got: " + e.getMessage());
    }

    @Test
    void parse_tripleDuplicateFrom_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("filter from/2026-03-01 from/2026-03-05 from/2026-03-10 to/2026-03-31"));
    }

    @Test
    void parse_noDuplicates_succeeds() throws SpendTrackException {
        Command cmd = Parser.parse("filter from/2026-03-01 to/2026-03-31");
        assertInstanceOf(FilterCommand.class, cmd);
    }
}
