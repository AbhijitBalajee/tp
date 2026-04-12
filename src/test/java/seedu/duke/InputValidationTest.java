package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.command.DeleteCommand;
import seedu.duke.command.FilterCommand;
import seedu.duke.command.FindCommand;
import seedu.duke.command.ListCommand;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InputValidationTest {

    // ── Add command validation ────────────────────────────────────────────────

    @Test
    void parse_addMissingDescription_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add a/5.00 c/Food"));
    }

    @Test
    void parse_addEmptyDescription_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/ a/5.00 c/Food"));
    }

    @Test
    void parse_addMissingAmount_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Coffee c/Food"));
    }

    @Test
    void parse_addZeroAmount_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Coffee a/0 c/Food"));
    }

    @Test
    void parse_addNegativeAmount_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Coffee a/-5.00 c/Food"));
    }

    @Test
    void parse_addNonNumericAmount_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Coffee a/abc c/Food"));
    }

    @Test
    void parse_addValidInput_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("add d/Coffee a/5.00 c/Food"));
    }

    @Test
    void parse_addValidInputNoCategory_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("add d/Coffee a/5.00"));
    }

    // ── Delete command validation ─────────────────────────────────────────────

    @Test
    void parse_deleteNonNumericIndex_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("delete abc"));
    }

    @Test
    void parse_deleteMissingIndex_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("delete"));
    }

    @Test
    void parse_deleteValidIndex_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("delete 1"));
    }

    // ── Budget command validation ─────────────────────────────────────────────
    // @@author AbhijitBalajee

    @Test
    void parse_budgetNonNumeric_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("budget abc"));
    }

    @Test
    void parse_budgetEmpty_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("budget"));
    }

    @Test
    void parse_budgetValidAmount_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("budget 500"));
    }

    @Test
    void parse_budgetNaN_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("budget NaN"));
    }

    @Test
    void parse_budgetInfinity_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("budget Infinity"));
    }

    @Test
    void parse_budgetResetWithExtra_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("budget reset now"));
    }

    @Test
    void parse_budgetHistoryWithExtra_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("budget history all"));
    }

    // @@author

    // ── Edit command validation ───────────────────────────────────────────────

    @Test
    void parse_editNonNumericIndex_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("edit abc d/Latte"));
    }

    @Test
    void parse_editMissingIndex_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("edit"));
    }

    @Test
    void parse_editNegativeAmount_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("edit 1 a/-5.00"));
    }

    @Test
    void parse_editZeroAmount_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("edit 1 a/0"));
    }

    @Test
    void parse_editNonNumericAmount_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("edit 1 a/abc"));
    }

    @Test
    void parse_editEmptyDescription_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("edit 1 d/"));
    }

    @Test
    void parse_editValidInput_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("edit 1 d/Latte"));
    }

    // ── Null/empty input ──────────────────────────────────────────────────────

    @Test
    void parse_emptyInput_returnsUnknownCommand() {
        assertDoesNotThrow(() ->
                Parser.parse(""));
    }

    @Test
    void parse_whitespaceOnlyInput_returnsUnknownCommand() {
        assertDoesNotThrow(() ->
                Parser.parse("   "));
    }

    // @@author Ariff1422

    // ── Filter command validation ─────────────────────────────────────────────

    @Test
    void parse_filterValid_returnsFilterCommand() throws SpendTrackException {
        assertTrue(Parser.parse("filter from/2026-01-01 to/2026-03-31") instanceof FilterCommand);
    }

    @Test
    void parse_filterSameDates_returnsFilterCommand() throws SpendTrackException {
        assertTrue(Parser.parse("filter from/2026-03-22 to/2026-03-22") instanceof FilterCommand);
    }

    @Test
    void parse_filterFromAfterTo_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> Parser.parse("filter from/2026-12-31 to/2026-01-01"));
    }

    @Test
    void parse_filterMissingBothArgs_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("filter"));
    }

    @Test
    void parse_filterOnlyFrom_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> Parser.parse("filter from/2026-01-01"));
    }

    @Test
    void parse_filterOnlyTo_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> Parser.parse("filter to/2026-03-31"));
    }

    @Test
    void parse_filterInvalidDateFormat_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> Parser.parse("filter from/notadate to/alsonotadate"));
    }

    // ── Find command validation ───────────────────────────────────────────────

    @Test
    void parse_findValid_returnsFindCommand() throws SpendTrackException {
        assertTrue(Parser.parse("find 1") instanceof FindCommand);
    }

    @Test
    void parse_findMissingIndex_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("find"));
    }

    @Test
    void parse_findNonNumeric_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("find coffee"));
    }

    @Test
    void parse_findFloat_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("find 2.5"));
    }

    // ── List command validation ───────────────────────────────────────────────
    // @@author AbhijitBalajee

    @Test
    void parse_listPlain_returnsListCommand() throws SpendTrackException {
        assertTrue(Parser.parse("list") instanceof ListCommand);
    }

    @Test
    void parse_listRecurring_returnsListCommand() throws SpendTrackException {
        assertTrue(Parser.parse("list recurring") instanceof ListCommand);
    }

    @Test
    void parse_listAliasL_returnsListCommand() throws SpendTrackException {
        assertTrue(Parser.parse("l") instanceof ListCommand);
    }

    @Test
    void parse_listInvalidSubcommand_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("list foo"));
    }

    // @@author

    // ── Alias resolution ─────────────────────────────────────────────────────

    @Test
    void parse_aliasD_returnsDeleteCommand() throws SpendTrackException {
        assertTrue(Parser.parse("d 2") instanceof DeleteCommand);
    }

    @Test
    void parse_bye_returnsExitCommand() throws SpendTrackException {
        assertTrue(Parser.parse("bye") instanceof ExitCommand);
    }

    @Test
    void parse_bye_isExitTrue() throws SpendTrackException {
        assertTrue(Parser.parse("bye").isExit());
    }

    @Test
    void parse_unknownCommand_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("foobar"));
    }

    // ── Add: amount cap, date before 2000, pipe in category ──────────────────

    @Test
    void parse_addAmountOverMax_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Thing a/1000001 c/Food"));
    }

    @Test
    void parse_addAmountAtMax_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("add d/Thing a/1000000 c/Food"));
    }

    @Test
    void parse_addDateBefore2000_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Thing a/5 c/Food date/1999-12-31"));
    }

    @Test
    void parse_addPipeInCategory_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Thing a/5 c/Food|hack"));
    }

    // ── Edit: amount cap, date before 2000, pipe in category, empty category ──

    @Test
    void parse_editAmountOverMax_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("edit 1 a/1000001"));
    }

    @Test
    void parse_editDateBefore2000_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("edit 1 date/1999-01-01"));
    }

    @Test
    void parse_editPipeInCategory_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("edit 1 c/Food|hack"));
    }

    @Test
    void parse_editEmptyCategory_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("edit 1 c/"));
    }

    // @@author
}
