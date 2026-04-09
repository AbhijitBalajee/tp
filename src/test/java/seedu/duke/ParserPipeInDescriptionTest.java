// @@author AfshalG
package seedu.duke;

import org.junit.jupiter.api.Test;

import seedu.duke.command.AddCommand;
import seedu.duke.command.Command;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParserPipeInDescriptionTest {

    @Test
    void parse_addDescriptionWithPipe_throwsException() {
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Alice|Bob shared dinner a/20 c/Food"));
        assertTrue(e.getMessage().contains("|"),
                "Error should mention the pipe character, got: " + e.getMessage());
    }

    @Test
    void parse_addDescriptionStartingWithPipe_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/|Leading a/5 c/Food"));
    }

    @Test
    void parse_addDescriptionEndingWithPipe_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Trailing| a/5 c/Food"));
    }

    @Test
    void parse_editDescriptionWithPipe_throwsException() {
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("edit 1 d/New|Desc"));
        assertTrue(e.getMessage().contains("|"),
                "Error should mention the pipe character, got: " + e.getMessage());
    }

    @Test
    void parse_addNormalDescription_succeeds() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/Food");
        assertInstanceOf(AddCommand.class, cmd);
    }

    @Test
    void parse_addDescriptionWithSlash_succeeds() throws SpendTrackException {
        // Forward slash is fine — only the pipe is reserved
        Command cmd = Parser.parse("add d/Coffee/Tea combo a/5 c/Food");
        assertInstanceOf(AddCommand.class, cmd);
    }
}
