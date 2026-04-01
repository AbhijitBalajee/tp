package seedu.duke;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// @@author Ariff1422
class DateParserTest {

    // --- ISO format YYYY-MM-DD ---

    @Test
    void parse_isoFormat_returnsCorrectDate() throws SpendTrackException {
        LocalDate result = DateParser.parse("2026-03-22");
        assertEquals(LocalDate.of(2026, 3, 22), result);
    }

    @Test
    void parse_isoFormat_differentDate() throws SpendTrackException {
        LocalDate result = DateParser.parse("2025-12-31");
        assertEquals(LocalDate.of(2025, 12, 31), result);
    }

    // --- Singapore format DD-MM-YYYY ---

    @Test
    void parse_sgFormat_returnsCorrectDate() throws SpendTrackException {
        LocalDate result = DateParser.parse("22-03-2026");
        assertEquals(LocalDate.of(2026, 3, 22), result);
    }

    @Test
    void parse_sgFormat_differentDate() throws SpendTrackException {
        LocalDate result = DateParser.parse("01-01-2025");
        assertEquals(LocalDate.of(2025, 1, 1), result);
    }

    // --- Keywords ---

    @Test
    void parse_today_returnsTodaysDate() throws SpendTrackException {
        LocalDate result = DateParser.parse("today");
        assertEquals(LocalDate.now(), result);
    }

    @Test
    void parse_todayUppercase_returnsTodaysDate() throws SpendTrackException {
        LocalDate result = DateParser.parse("TODAY");
        assertEquals(LocalDate.now(), result);
    }

    @Test
    void parse_yesterday_returnsYesterdaysDate() throws SpendTrackException {
        LocalDate result = DateParser.parse("yesterday");
        assertEquals(LocalDate.now().minusDays(1), result);
    }

    @Test
    void parse_yesterdayMixedCase_returnsYesterdaysDate() throws SpendTrackException {
        LocalDate result = DateParser.parse("Yesterday");
        assertEquals(LocalDate.now().minusDays(1), result);
    }

    // --- Leading/trailing whitespace ---

    @Test
    void parse_isoWithLeadingSpace_returnsCorrectDate() throws SpendTrackException {
        LocalDate result = DateParser.parse("  2026-03-22");
        assertEquals(LocalDate.of(2026, 3, 22), result);
    }

    @Test
    void parse_isoWithTrailingSpace_returnsCorrectDate() throws SpendTrackException {
        LocalDate result = DateParser.parse("2026-03-22  ");
        assertEquals(LocalDate.of(2026, 3, 22), result);
    }

    // --- Invalid formats ---

    @Test
    void parse_completelyInvalid_throwsException() {
        assertThrows(SpendTrackException.class, () -> DateParser.parse("notadate"));
    }

    @Test
    void parse_wrongSeparator_throwsException() {
        assertThrows(SpendTrackException.class, () -> DateParser.parse("2026/03/22"));
    }

    @Test
    void parse_partialDate_throwsException() {
        assertThrows(SpendTrackException.class, () -> DateParser.parse("2026-03"));
    }

    @Test
    void parse_emptyString_throwsException() {
        assertThrows(SpendTrackException.class, () -> DateParser.parse(""));
    }

    @Test
    void parse_invalidMonth_throwsException() {
        assertThrows(SpendTrackException.class, () -> DateParser.parse("2026-13-01"));
    }

    @Test
    void parse_invalidDay_throwsException() {
        assertThrows(SpendTrackException.class, () -> DateParser.parse("2026-02-30"));
    }

    // --- Exception message content ---

    @Test
    void parse_invalid_exceptionMessageMentionsFormats() {
        SpendTrackException ex = assertThrows(SpendTrackException.class,
                () -> DateParser.parse("baddate"));
        assertTrue(ex.getMessage().contains("YYYY-MM-DD") || ex.getMessage().contains("Invalid date"));
    }
}
// @@author
