package seedu.duke;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import seedu.duke.command.AddCommand;
import seedu.duke.command.BudgetCommand;
import seedu.duke.command.Command;
import seedu.duke.command.DeleteCommand;
import seedu.duke.command.FilterCommand;
import seedu.duke.command.FindCommand;
import seedu.duke.command.HelpCommand;
import seedu.duke.command.ListCommand;
import seedu.duke.command.RemainingCommand;
import seedu.duke.command.SummaryCommand;
import seedu.duke.command.TotalCommand;
import seedu.duke.command.EditCommand;
import seedu.duke.command.BudgetResetCommand;

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
        case "edit":
            return parseEditCommand(parts.length > 1 ? parts[1] : "");
        case "filter":
            return parseFilterCommand(parts.length > 1 ? parts[1] : "");
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
                date = DateParser.parse(token.substring(5).trim());
            } else if (token.startsWith("d/")) {
                description = token.substring(2).trim();
                if (description.isEmpty()) {
                    throw new SpendTrackException("Description cannot be empty. "
                            + "Please provide a valid description after d/");
                }
            } else if (token.startsWith("a/")) {
                try {
                    amount = Double.parseDouble(token.substring(2).trim());
                } catch (NumberFormatException e) {
                    throw new SpendTrackException("Amount must be a number. Usage: a/<amount>");
                }
                if (amount <= 0) {
                    throw new SpendTrackException("Amount must be a positive number. Usage: a/<amount>");
                }
            } else if (token.startsWith("c/")) {
                category = normalizeCategory(token.substring(2).trim());
            }
        }

        if (description.isEmpty()) {
            throw new SpendTrackException("Description is required. Usage: add d/<desc> a/<amount> c/<category>");
        }
        if (amount == 0.0) {
            throw new SpendTrackException("Amount is required and must be greater than 0. Usage: a/<amount>");
        }

        return new AddCommand(description, amount, category, date);
    }

    private static Command parseEditCommand(String args) throws SpendTrackException {
        String[] parts = args.trim().split(" ", 2);

        int index;
        try {
            index = Integer.parseInt(parts[0].trim());
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new SpendTrackException("Index must be a whole number.");
        }

        String remaining = parts.length > 1 ? parts[1].trim() : "";
        String newDescription = null;
        Double newAmount = null;
        String newCategory = null;
        LocalDate newDate = null;

        // Duplicate tracking
        boolean seenDescription = false;
        boolean seenAmount = false;
        boolean seenCategory = false;
        boolean seenDate = false;

        String[] tokens = remaining.split(TOKEN_SPLIT_REGEX);
        for (String token : tokens) {
            token = token.trim();
            if (token.startsWith("date/")) {
                if (seenDate) {
                    throw new SpendTrackException("Duplicate 'date/' detected. "
                            + "Please provide only one date.");
                }
                seenDate = true;
                newDate = DateParser.parse(token.substring(5).trim());

            } else if (token.startsWith("d/")) {
                if (seenDescription) {
                    throw new SpendTrackException("Duplicate 'd/' detected. "
                            + "Please provide only one description.");
                }
                seenDescription = true;
                newDescription = token.substring(2).trim();
                if (newDescription.isEmpty()) {
                    throw new SpendTrackException("Description cannot be empty. "
                            + "Please provide a valid description after d/");
                }

            } else if (token.startsWith("a/")) {
                if (seenAmount) {
                    throw new SpendTrackException("Duplicate 'a/' detected. "
                            + "Please provide only one amount.");
                }
                seenAmount = true;
                try {
                    newAmount = Double.parseDouble(token.substring(2).trim());
                } catch (NumberFormatException e) {
                    throw new SpendTrackException("Amount must be a number. Usage: a/<amount>");
                }
                if (newAmount <= 0) {
                    throw new SpendTrackException("Amount must be greater than 0.");
                }

            } else if (token.startsWith("c/")) {
                if (seenCategory) {
                    throw new SpendTrackException("Duplicate 'c/' detected. "
                            + "Please provide only one category.");
                }
                seenCategory = true;
                newCategory = normalizeCategory(token.substring(2).trim());
            }
        }

        return new EditCommand(index, newDescription, newAmount, newCategory, newDate);
    }

    private static String normalizeCategory(String category) {
        if (category.isEmpty()) {
            return "Uncategorised";
        }
        String[] words = category.trim().split("\\s+");
        StringBuilder normalized = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                normalized.append(' ');
            }
            normalized.append(Character.toUpperCase(words[i].charAt(0)));
            if (words[i].length() > 1) {
                normalized.append(words[i].substring(1).toLowerCase());
            }
        }
        return normalized.toString();
    }

    private static Command parseFilterCommand(String args) throws SpendTrackException {
        LocalDate from = null;
        LocalDate to = null;

        String[] tokens = args.split(" ");
        for (String token : tokens) {
            token = token.trim();
            if (token.startsWith("from/")) {
                from = DateParser.parse(token.substring(5).trim());
            } else if (token.startsWith("to/")) {
                to = DateParser.parse(token.substring(3).trim());
            }
        }

        if (from == null || to == null) {
            throw new SpendTrackException("Usage: filter from/YYYY-MM-DD to/YYYY-MM-DD");
        }
        if (from.isAfter(to)) {
            throw new SpendTrackException("Start date must be before end date.");
        }

        return new FilterCommand(from, to);
    }

    private static Command parseBudgetCommand(String args) throws SpendTrackException {
        if (args.trim().equalsIgnoreCase("reset")) {
            return new BudgetResetCommand();
        }
        if (args.trim().equalsIgnoreCase("history")) {
            return new BudgetHistoryCommand();
        }
        if (args.trim().isEmpty()) {
            throw new SpendTrackException("budget requires a number. Usage: budget <amount>");
        }
        try {
            double amount = Double.parseDouble(args.trim());
            return new BudgetCommand(amount);
        } catch (NumberFormatException e) {
            throw new SpendTrackException("budget requires a number. Usage: budget <amount>");
        }
    }
}
