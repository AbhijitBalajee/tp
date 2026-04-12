// @@author AfshalG
package seedu.duke.command;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.ExpenseList;
import seedu.duke.Parser;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FlexibleDateTest {

    private ExpenseList expenses;
    private Ui ui;

    @BeforeEach
    void setUp() {
        expenses = new ExpenseList();
        ui = new Ui();
    }

    @Test
    void parser_dateToday_defaultsToToday() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/Food date/today");
        cmd.execute(expenses, ui);
        assertEquals(LocalDate.now(), expenses.getExpense(0).getDate());
    }

    @Test
    void parser_dateYesterday_defaultsToYesterday() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/Food date/yesterday");
        cmd.execute(expenses, ui);
        assertEquals(LocalDate.now().minusDays(1), expenses.getExpense(0).getDate());
    }

    @Test
    void parser_dateDdMmYyyy_parsesCorrectly() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/Food date/22-03-2026");
        cmd.execute(expenses, ui);
        assertEquals(LocalDate.of(2026, 3, 22), expenses.getExpense(0).getDate());
    }

    @Test
    void parser_dateIsoFormat_stillWorks() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/Food date/2026-03-22");
        cmd.execute(expenses, ui);
        assertEquals(LocalDate.of(2026, 3, 22), expenses.getExpense(0).getDate());
    }

    @Test
    void parser_dateTodayUppercase_parsesCorrectly() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/Food date/Today");
        cmd.execute(expenses, ui);
        assertEquals(LocalDate.now(), expenses.getExpense(0).getDate());
    }

    @Test
    void parser_dateYesterdayUppercase_parsesCorrectly() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/Food date/Yesterday");
        cmd.execute(expenses, ui);
        assertEquals(LocalDate.now().minusDays(1), expenses.getExpense(0).getDate());
    }

    @Test
    void parser_dateInvalidFormat_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Coffee a/3.50 c/Food date/March-22"));
    }

    @Test
    void parser_dateInvalidDdMmYyyy_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Coffee a/3.50 c/Food date/32-13-2026"));
    }

    @Test
    void parser_dateEmptyString_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Coffee a/3.50 c/Food date/"));
    }

    @Test
    void parser_dateLeapYear_parsesCorrectly() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/Food date/29-02-2024");
        cmd.execute(expenses, ui);
        assertEquals(LocalDate.of(2024, 2, 29), expenses.getExpense(0).getDate());
    }

    @Test
    void parser_dateNonLeapYear_rejectsInvalidFeb29() {
        // 2025 is not a leap year — Feb 29 must be rejected, not silently rounded
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/Coffee a/3.50 c/Food date/29-02-2025"));
    }

    @Test
    void parser_dateMonthEnd_parsesCorrectly() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/Food date/31-01-2026");
        cmd.execute(expenses, ui);
        assertEquals(LocalDate.of(2026, 1, 31), expenses.getExpense(0).getDate());
    }
}
