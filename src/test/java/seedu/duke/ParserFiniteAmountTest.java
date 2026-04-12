// @@author AfshalG
package seedu.duke;

import org.junit.jupiter.api.Test;

import seedu.duke.command.AddCommand;
import seedu.duke.command.BudgetCommand;
import seedu.duke.command.Command;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParserFiniteAmountTest {

    @Test
    void parse_addNaNAmount_throwsException() {
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/T a/NaN c/Food"));
        assertTrue(e.getMessage().toLowerCase().contains("finite")
                        || e.getMessage().toLowerCase().contains("valid number")
                        || e.getMessage().toLowerCase().contains("must be a number"),
                "Error should reject NaN, got: " + e.getMessage());
    }

    @Test
    void parse_addInfinityAmount_throwsException() {
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/T a/Infinity c/Food"));
        assertTrue(e.getMessage().toLowerCase().contains("finite")
                        || e.getMessage().toLowerCase().contains("valid number")
                        || e.getMessage().toLowerCase().contains("must be a number"),
                "Error should reject Infinity, got: " + e.getMessage());
    }

    @Test
    void parse_addNegativeInfinityAmount_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("add d/T a/-Infinity c/Food"));
    }

    @Test
    void parse_editNaNAmount_throwsException() {
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("edit 1 a/NaN"));
        assertTrue(e.getMessage().toLowerCase().contains("finite")
                        || e.getMessage().toLowerCase().contains("valid number")
                        || e.getMessage().toLowerCase().contains("must be a number"),
                "Error should reject NaN in edit, got: " + e.getMessage());
    }

    @Test
    void parse_budgetNaN_throwsException() {
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("budget NaN"));
        assertTrue(e.getMessage().toLowerCase().contains("finite")
                        || e.getMessage().toLowerCase().contains("number"),
                "Error should reject NaN budget, got: " + e.getMessage());
    }

    @Test
    void parse_budgetInfinity_throwsException() {
        assertThrows(SpendTrackException.class, () ->
                Parser.parse("budget Infinity"));
    }

    @Test
    void parse_goalNaN_throwsException() {
        SpendTrackException e = assertThrows(SpendTrackException.class, () ->
                Parser.parse("goal g/NaN"));
        assertTrue(e.getMessage().toLowerCase().contains("finite")
                        || e.getMessage().toLowerCase().contains("number"),
                "Error should reject NaN goal, got: " + e.getMessage());
    }

    @Test
    void parse_addNormalAmount_succeeds() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/Food");
        assertInstanceOf(AddCommand.class, cmd);
    }

    @Test
    void parse_budgetNormalAmount_succeeds() throws SpendTrackException {
        Command cmd = Parser.parse("budget 500");
        assertInstanceOf(BudgetCommand.class, cmd);
    }
}
