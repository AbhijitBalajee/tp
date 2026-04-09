// @@author AfshalG
package seedu.duke;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateParserStrictTest {

    // Invalid dates in DD-MM-YYYY that should be rejected

    @Test
    void parse_feb29NonLeapYearSgFormat_throwsException() {
        // 2025 is not a leap year — Feb 29 doesn't exist
        assertThrows(SpendTrackException.class, () -> DateParser.parse("29-02-2025"));
    }

    @Test
    void parse_april31SgFormat_throwsException() {
        // April has only 30 days
        assertThrows(SpendTrackException.class, () -> DateParser.parse("31-04-2026"));
    }

    @Test
    void parse_june31SgFormat_throwsException() {
        // June has only 30 days
        assertThrows(SpendTrackException.class, () -> DateParser.parse("31-06-2026"));
    }

    @Test
    void parse_feb30SgFormat_throwsException() {
        // Feb never has 30 days
        assertThrows(SpendTrackException.class, () -> DateParser.parse("30-02-2026"));
    }

    @Test
    void parse_day32SgFormat_throwsException() {
        // No month has 32 days
        assertThrows(SpendTrackException.class, () -> DateParser.parse("32-01-2026"));
    }

    // Invalid dates in ISO format (already rejected, regression guard)

    @Test
    void parse_feb29NonLeapYearIsoFormat_throwsException() {
        assertThrows(SpendTrackException.class, () -> DateParser.parse("2025-02-29"));
    }

    // Valid dates must still work

    @Test
    void parse_validSgFormat_returnsCorrectDate() throws SpendTrackException {
        assertEquals(LocalDate.of(2026, 3, 22), DateParser.parse("22-03-2026"));
    }

    @Test
    void parse_feb29LeapYearSgFormat_returnsCorrectDate() throws SpendTrackException {
        // 2024 is a leap year
        assertEquals(LocalDate.of(2024, 2, 29), DateParser.parse("29-02-2024"));
    }

    @Test
    void parse_feb29CenturyLeapYearSgFormat_returnsCorrectDate() throws SpendTrackException {
        // 2000 is a leap year (divisible by 400)
        assertEquals(LocalDate.of(2000, 2, 29), DateParser.parse("29-02-2000"));
    }

    @Test
    void parse_feb28NonLeapYearSgFormat_returnsCorrectDate() throws SpendTrackException {
        assertEquals(LocalDate.of(2025, 2, 28), DateParser.parse("28-02-2025"));
    }

    @Test
    void parse_validIsoFormat_returnsCorrectDate() throws SpendTrackException {
        assertEquals(LocalDate.of(2026, 3, 22), DateParser.parse("2026-03-22"));
    }

    @Test
    void parse_today_returnsToday() throws SpendTrackException {
        assertEquals(LocalDate.now(), DateParser.parse("today"));
    }

    @Test
    void parse_yesterday_returnsYesterday() throws SpendTrackException {
        assertEquals(LocalDate.now().minusDays(1), DateParser.parse("yesterday"));
    }
}
