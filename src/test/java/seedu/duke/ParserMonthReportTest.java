// @@author AfshalG
package seedu.duke;

import org.junit.jupiter.api.Test;

import seedu.duke.command.Command;
import seedu.duke.command.MonthCommand;
import seedu.duke.command.ReportCommand;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParserMonthReportTest {

    @Test
    void parse_monthValidYearMonth_succeeds() throws SpendTrackException {
        Command cmd = Parser.parse("month 2026-03");
        assertInstanceOf(MonthCommand.class, cmd);
    }

    @Test
    void parse_reportValidYearMonth_succeeds() throws SpendTrackException {
        Command cmd = Parser.parse("report 2026-03");
        assertInstanceOf(ReportCommand.class, cmd);
    }

    @Test
    void parse_monthMonth13_throwsException() {
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("month 2026-13"));
        assertTrue(e.getMessage().toLowerCase().contains("month")
                        || e.getMessage().toLowerCase().contains("invalid"),
                "Error should mention invalid month, got: " + e.getMessage());
    }

    @Test
    void parse_monthMonth00_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("month 2026-00"));
    }

    @Test
    void parse_monthWrongOrder_throwsException() {
        // DD-MM or MM-YYYY style — not YYYY-MM
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("month 03-2026"));
        assertTrue(e.getMessage().toLowerCase().contains("yyyy-mm")
                        || e.getMessage().toLowerCase().contains("invalid")
                        || e.getMessage().toLowerCase().contains("format"),
                "Error should mention format/YYYY-MM, got: " + e.getMessage());
    }

    @Test
    void parse_monthSingleDigitMonth_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("month 2026-3"));
    }

    @Test
    void parse_monthGarbage_throwsException() {
        // Already works in v2.0, kept as regression guard
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("month abc"));
    }

    @Test
    void parse_reportMonth13_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("report 2026-13"));
    }

    @Test
    void parse_reportMonth00_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("report 2026-00"));
    }

    @Test
    void parse_reportWrongOrder_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("report 03-2026"));
    }

    @Test
    void parse_reportGarbage_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("report abc"));
    }
}
