// @@author AfshalG
package seedu.duke;

import org.junit.jupiter.api.Test;

import seedu.duke.command.AddCommand;
import seedu.duke.command.Command;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParserAddDuplicateTest {

    @Test
    void parse_duplicateDescription_throwsException() {
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Coffee d/Tea a/5 c/Food"));
        assertTrue(e.getMessage().contains("d/"),
                "Error should mention d/, got: " + e.getMessage());
    }

    @Test
    void parse_duplicateAmount_throwsException() {
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Coffee a/3.00 a/4.50 c/Food"));
        assertTrue(e.getMessage().contains("a/"),
                "Error should mention a/, got: " + e.getMessage());
    }

    @Test
    void parse_duplicateCategory_throwsException() {
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Coffee a/3.50 c/Food c/Drinks"));
        assertTrue(e.getMessage().contains("c/"),
                "Error should mention c/, got: " + e.getMessage());
    }

    @Test
    void parse_duplicateDate_throwsException() {
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Coffee a/3.50 c/Food date/2026-03-10 date/2026-03-11"));
        assertTrue(e.getMessage().contains("date/"),
                "Error should mention date/, got: " + e.getMessage());
    }

    @Test
    void parse_duplicateRecurring_throwsException() {
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Gym a/50 c/Health recurring/true recurring/false"));
        assertTrue(e.getMessage().contains("recurring/"),
                "Error should mention recurring/, got: " + e.getMessage());
    }

    @Test
    void parse_tripleAmountDuplicate_throwsException() {
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Coffee a/3.00 a/4.00 a/4.50 c/Food"));
        assertTrue(e.getMessage().contains("a/"));
    }

    @Test
    void parse_duplicateSeparatedByOtherFlags_throwsException() {
        // Duplicates not adjacent — should still be detected
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Coffee a/3.50 c/Food d/Tea"));
        assertTrue(e.getMessage().contains("d/"));
    }

    @Test
    void parse_duplicateAliasForm_throwsException() {
        // Using alias 'a' for add should also trigger duplicate detection
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("a d/Coffee d/Tea a/5 c/Food"));
        assertTrue(e.getMessage().contains("d/"));
    }

    @Test
    void parse_noDuplicates_succeeds() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/Food");
        assertInstanceOf(AddCommand.class, cmd);
    }

    @Test
    void parse_noDuplicatesWithAllOptionalFlags_succeeds() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Gym a/50 c/Health date/2026-03-10 recurring/true");
        assertInstanceOf(AddCommand.class, cmd);
    }
}
