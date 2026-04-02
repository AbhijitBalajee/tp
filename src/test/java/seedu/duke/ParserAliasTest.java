// @@author AfshalG
package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.command.AddCommand;
import seedu.duke.command.Command;
import seedu.duke.command.HelpCommand;
import seedu.duke.command.ListCommand;
import seedu.duke.command.SummaryCommand;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ParserAliasTest {

    @Test
    void parse_aliasA_returnsAddCommand() throws SpendTrackException {
        Command cmd = Parser.parse("a d/Coffee a/3.50 c/Food");
        assertInstanceOf(AddCommand.class, cmd);
    }

    @Test
    void parse_aliasL_returnsListCommand() throws SpendTrackException {
        Command cmd = Parser.parse("l");
        assertInstanceOf(ListCommand.class, cmd);
    }

    @Test
    void parse_aliasS_returnsSummaryCommand() throws SpendTrackException {
        Command cmd = Parser.parse("s");
        assertInstanceOf(SummaryCommand.class, cmd);
    }

    @Test
    void parse_aliasH_returnsHelpCommand() throws SpendTrackException {
        Command cmd = Parser.parse("h");
        assertInstanceOf(HelpCommand.class, cmd);
    }

    @Test
    void parse_deleteAlias_returnsDeleteCommand() throws SpendTrackException {
        Command cmd = Parser.parse("d 1");
        assertInstanceOf(seedu.duke.command.DeleteCommand.class, cmd);
    }

    @Test
    void parse_budgetAlias_returnsBudgetCommand() throws SpendTrackException {
        Command cmd = Parser.parse("b 500");
        assertInstanceOf(seedu.duke.command.BudgetCommand.class, cmd);
    }

    @Test
    void parse_unknownAlias_returnsUnknownCommand() throws SpendTrackException {
        Command cmd = Parser.parse("z");
        assertInstanceOf(UnknownCommand.class, cmd);
    }

    @Test
    void parse_fullCommandStillWorks_returnsCorrectCommand() throws SpendTrackException {
        Command cmd = Parser.parse("summary");
        assertInstanceOf(SummaryCommand.class, cmd);
    }

    @Test
    void parse_uppercaseAliasA_returnsAddCommand() throws SpendTrackException {
        Command cmd = Parser.parse("A d/Coffee a/3.50 c/Food");
        assertInstanceOf(AddCommand.class, cmd);
    }

    @Test
    void parse_uppercaseAliasL_returnsListCommand() throws SpendTrackException {
        Command cmd = Parser.parse("L");
        assertInstanceOf(ListCommand.class, cmd);
    }

    @Test
    void parse_uppercaseFullCommand_returnsCorrectCommand() throws SpendTrackException {
        Command cmd = Parser.parse("SUMMARY");
        assertInstanceOf(SummaryCommand.class, cmd);
    }
}
