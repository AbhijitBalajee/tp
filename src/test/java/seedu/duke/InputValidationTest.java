package seedu.duke;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
