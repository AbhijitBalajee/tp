// @@author AfshalG
package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.command.AddCommand;
import seedu.duke.command.SummaryCommand;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for PE-D fixes: #219, #229 (formatting tested manually),
 * summary extra args, flag-in-description validation, case-insensitive flags.
 */
public class PeDFixesTest {

    // ── #219: Wrong error for invalid prefix like da/today ───────────────────

    @Test
    void parse_addUnrecognisedPrefix_throwsMeaningfulError() {
        SpendTrackException ex = assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Coffee a/5 da/today"));
        assertTrue(ex.getMessage().toLowerCase().contains("unrecogni")
                || ex.getMessage().toLowerCase().contains("unknown")
                || ex.getMessage().toLowerCase().contains("da/"),
                "Error should mention unrecognised token, got: " + ex.getMessage());
    }

    @Test
    void parse_addUnrecognisedPrefixXyz_throwsMeaningfulError() {
        SpendTrackException ex = assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Coffee a/5 xyz/hello"));
        assertTrue(ex.getMessage().toLowerCase().contains("unrecogni")
                || ex.getMessage().toLowerCase().contains("unknown")
                || ex.getMessage().toLowerCase().contains("xyz/"),
                "Error should mention unrecognised token, got: " + ex.getMessage());
    }

    // ── Summary extra args rejection ─────────────────────────────────────────

    @Test
    void parse_summaryWithExtraArgs_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("summary abc"));
    }

    @Test
    void parse_summaryAliasWithExtraArgs_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("s s s s"));
    }

    @Test
    void parse_summaryNoArgs_returnsSummaryCommand() throws SpendTrackException {
        assertTrue(Parser.parse("summary") instanceof SummaryCommand);
    }

    // ── Flag-in-description validation (case-insensitive) ────────────────────

    @Test
    void parse_addDescriptionContainsLowercaseFlagA_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/buy a/c adapter a/50 c/Electronics"));
    }

    @Test
    void parse_addDescriptionContainsUppercaseFlagA_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/buy A/c adapter a/50 c/Electronics"));
    }

    @Test
    void parse_addDescriptionContainsSlashNonFlag_doesNotThrow() {
        // I/O is fine — I/ is not a flag prefix
        assertDoesNotThrow(() ->
                Parser.parse("add d/I/O cable a/10 c/Electronics"));
    }

    @Test
    void parse_addDescriptionContainsSlashW_doesNotThrow() {
        // w/ is not a flag prefix
        assertDoesNotThrow(() ->
                Parser.parse("add d/burger w/cheese a/10 c/Food"));
    }

    // ── Case-insensitive flags ───────────────────────────────────────────────

    @Test
    void parse_addUppercaseFlags_returnsAddCommand() throws SpendTrackException {
        assertTrue(Parser.parse("add D/Coffee A/5 C/Food") instanceof AddCommand);
    }

    @Test
    void parse_addMixedCaseFlags_returnsAddCommand() throws SpendTrackException {
        assertTrue(Parser.parse("add D/Latte a/3.50 C/Food") instanceof AddCommand);
    }

    @Test
    void parse_addUppercaseDateFlag_returnsAddCommand() throws SpendTrackException {
        assertTrue(Parser.parse("add d/Coffee a/5 c/Food DATE/today") instanceof AddCommand);
    }

    @Test
    void parse_addUppercaseRecurringFlag_returnsAddCommand() throws SpendTrackException {
        assertTrue(Parser.parse("add d/Coffee a/5 c/Food RECURRING/true") instanceof AddCommand);
    }

    @Test
    void parse_editUppercaseFlags_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("edit 1 D/Latte A/4.00 C/Food"));
    }
}
