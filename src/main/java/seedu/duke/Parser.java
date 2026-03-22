package seedu.duke;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import seedu.duke.command.AddCommand;
import seedu.duke.command.BudgetCommand;
import seedu.duke.command.Command;
import seedu.duke.command.DeleteCommand;
import seedu.duke.command.FindCommand;
import seedu.duke.command.HelpCommand;
import seedu.duke.command.ListCommand;
import seedu.duke.command.RemainingCommand;
import seedu.duke.command.SummaryCommand;
import seedu.duke.command.TotalCommand;

/**
 * Parses user input into commands.
 */
public class Parser {

    private static final Logger logger = Logger.getLogger(Parser.class.getName());
    private static final String TOKEN_SPLIT_REGEX = " (?=(?:d|a|c|date)/)";
    private static final Map<String, String> ALIASES = new HashMap<>();

    static {
        logger.setUseParentHandlers(false);
        ALIASES.put("a", "add");
        ALIASES.put("d", "delete");
        ALIASES.put("l", "list");
        ALIASES.put("s", "summary");
        ALIASES.put("b", "budget");
        ALIASES.put("h", "help");
    }

    /**
     * Parses the user input and returns the corresponding command.
     *
     * @param input the raw user input string
     * @return the parsed Command
     * @throws SpendTrackException if input is invalid
     */
    public static Command parse(String input) throws SpendTrackException {
        assert input != null : "Input to parser should not be null";

        String trimmed = input.trim();
        String[] parts = trimmed.split(" ", 2);
        String commandWord = parts[0].toLowerCase();
        commandWord = ALIASES.getOrDefault(commandWord, commandWord);

        logger.info("Parsing command: " + commandWord);

        switch (commandWord) {
        case "add":
            return parseAddCommand(parts.length > 1 ? parts[1] : "");
        case "delete":
            try {
                return new DeleteCommand(Integer.parseInt(parts[1].trim()));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new SpendTrackException("delete requires a number. Usage: delete <index>");
            }
        case "find":
            try {
                return new FindCommand(Integer.parseInt(parts[1].trim()));
            } catch (NumberFormatException e) {
                throw new SpendTrackException("Index must be a whole number. Usage: find <index>");
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new SpendTrackException("find requires an index. Usage: find <index>");
            }
        case "total":
            return new TotalCommand();
        case "list":
            return new ListCommand();
        case "budget":
            return parseBudgetCommand(parts.length > 1 ? parts[1] : "");
        case "remaining":
            return new RemainingCommand();
        case "summary":
            return new SummaryCommand();
        case "help":
            return new HelpCommand();
        case "bye":
            return new ExitCommand();
        default:
            logger.warning("Unknown command: " + commandWord);
            return new UnknownCommand();
        }
    }

    private static AddCommand parseAddCommand(String args) throws SpendTrackException {
        String description = "";
        double amount = 0.0;
        String category = "Uncategorised";
        LocalDate date = LocalDate.now();

        String[] tokens = args.split(TOKEN_SPLIT_REGEX);
        for (String token : tokens) {
            token = token.trim();
            if (token.startsWith("date/")) {
                date = parseDate(token.substring(5).trim());
            } else if (token.startsWith("d/")) {
                description = token.substring(2).trim();
            } else if (token.startsWith("a/")) {
                amount = Double.parseDouble(token.substring(2).trim());
            } else if (token.startsWith("c/")) {
                category = token.substring(2).trim();
            }
        }

        return new AddCommand(description, amount, category, date);
    }

    private static LocalDate parseDate(String dateString) throws SpendTrackException {
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            throw new SpendTrackException("Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    private static Command parseBudgetCommand(String args) throws SpendTrackException {
        try {
            double amount = Double.parseDouble(args.trim());
            return new BudgetCommand(amount);
        } catch (NumberFormatException e) {
            throw new SpendTrackException("budget requires a number. Usage: budget <amount>");
        }
    }
}
