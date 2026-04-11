package seedu.duke;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import seedu.duke.command.SortCommand;
import seedu.duke.command.AddCommand;
import seedu.duke.command.DeleteCommand;
import seedu.duke.command.EditCommand;
import seedu.duke.command.FilterCommand;
import seedu.duke.command.FindCommand;
import seedu.duke.command.TotalCommand;
import seedu.duke.command.ListCommand;
import seedu.duke.command.BudgetCommand;
import seedu.duke.command.BudgetResetCommand;
import seedu.duke.command.BudgetHistoryCommand;
import seedu.duke.command.RemainingCommand;
import seedu.duke.command.SummaryCommand;
import seedu.duke.command.SearchCommand;
import seedu.duke.command.HelpCommand;
import seedu.duke.command.Command;


import seedu.duke.command.GoalCommand;
import seedu.duke.command.ClearCommand;
import seedu.duke.command.ExportCommand;
import seedu.duke.command.UndoCommand;
import seedu.duke.command.TopCommand;
import seedu.duke.command.LastCommand;
import seedu.duke.command.ReportCommand;
import seedu.duke.command.MonthCommand;

/**
 * Parses user input into commands.
 */
public class Parser {

    private static final Logger logger = Logger.getLogger(Parser.class.getName());
    private static final String TOKEN_SPLIT_REGEX = " (?=(?:d/|a/(?:[^a-zA-Z]|NaN|Infinity|-Infinity)|c/|date/|recurring/))";
    // @@author AfshalG
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
    // @@author

    /**
     * Parses the user input and returns the corresponding command.
     * Uses a null UndoManager, so undo command is unavailable.
     *
     * @param input the raw user input string
     * @return the parsed Command
     * @throws SpendTrackException if input is invalid
     */
    public static Command parse(String input) throws SpendTrackException {
        return parse(input, null);
    }

    /**
     * Parses the user input and returns the corresponding command.
     *
     * @param input the raw user input string
     * @param undoManager the undo manager for creating undo commands
     * @return the parsed Command
     * @throws SpendTrackException if input is invalid
     */
    public static Command parse(String input, UndoManager undoManager) throws SpendTrackException {
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
            if (parts.length > 1) {
                String listArg = parts[1].trim().toLowerCase();
                if (listArg.equals("recurring")) {
                    return new ListCommand(true);
                }
                throw new SpendTrackException(
                        "Invalid list option. Usage: list OR list recurring");
            }
            return new ListCommand();
        case "budget":
            return parseBudgetCommand(parts.length > 1 ? parts[1] : "");
        case "remaining":
            return new RemainingCommand();
        // @@author pranavjana
        case "goal":
            return parseGoalCommand(parts.length > 1 ? parts[1] : "");
        case "clear":
            return new ClearCommand();
        case "export":
            if (parts.length > 1 && parts[1].trim().equalsIgnoreCase("csv")) {
                return new ExportCommand();
            }
            throw new SpendTrackException("Usage: export csv");
        // @@author
        case "undo":
            if (undoManager == null) {
                throw new SpendTrackException("Undo is not available.");
            }
            return new UndoCommand(undoManager);
        case "summary":
            return new SummaryCommand();
        case "search":
            return new SearchCommand(parts.length > 1 ? parts[1] : "");
        case "sort":
            return new SortCommand();
        case "top":
            try {
                return new TopCommand(Integer.parseInt(parts[1].trim()));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new SpendTrackException("Usage: top <number>");
            }
        case "last":
            try {
                return new LastCommand(Integer.parseInt(parts[1].trim()));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new SpendTrackException("Usage: last <number>");
            }
        case "report":
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                throw new SpendTrackException("Usage: report <YYYY-MM>");
            }
            return new ReportCommand(validateYearMonth(parts[1].trim(), "report"));
        case "month":
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                throw new SpendTrackException("Usage: month <YYYY-MM>");
            }
            return new MonthCommand(validateYearMonth(parts[1].trim(), "month"));
        case "help":
            return new HelpCommand();
        case "bye":
            return new ExitCommand();
        default:
            logger.warning("Unknown command: " + commandWord);
            return new UnknownCommand();
        }
    }

    // @@author AfshalG
    private static AddCommand parseAddCommand(String args) throws SpendTrackException {
        String description = "";
        double amount = 0.0;
        String category = "Uncategorised";
        LocalDate date = LocalDate.now();
        boolean isRecurring = false;

        boolean seenDescription = false;
        boolean seenAmount = false;
        boolean seenCategory = false;
        boolean seenDate = false;
        boolean seenRecurring = false;

        String[] tokens = args.split(TOKEN_SPLIT_REGEX);
        for (String token : tokens) {
            token = token.trim();
            if (token.startsWith("date/")) {
                if (seenDate) {
                    throw new SpendTrackException("Duplicate 'date/' detected. "
                            + "Please provide only one date.");
                }
                seenDate = true;
                date = DateParser.parse(token.substring(5).trim());
                // @@author Ariff1422
                if (date.getYear() < 2000) {
                    throw new SpendTrackException("Date must be year 2000 or later.");
                }
                // @@author
            } else if (token.startsWith("d/")) {
                if (seenDescription) {
                    throw new SpendTrackException("Duplicate 'd/' detected. "
                            + "Please provide only one description.");
                }
                seenDescription = true;
                description = token.substring(2).trim();
                if (description.isEmpty()) {
                    throw new SpendTrackException("Description cannot be empty. "
                            + "Please provide a valid description after d/");
                }
                if (description.contains("|")) {
                    throw new SpendTrackException("Description cannot contain '|' "
                            + "(reserved for save file format). Please use a different character.");
                }
            } else if (token.startsWith("a/")) {
                if (seenAmount) {
                    throw new SpendTrackException("Duplicate 'a/' detected. "
                            + "Please provide only one amount.");
                }
                seenAmount = true;
                String amountStr = token.substring(2).trim();

                // Explicitly reject NaN and Infinity before parseDouble
                if (amountStr.equalsIgnoreCase("nan")
                        || amountStr.equalsIgnoreCase("infinity")
                        || amountStr.equalsIgnoreCase("-infinity")) {
                    throw new SpendTrackException("Amount must be a finite number. Usage: a/<amount>");
                }

                try {
                    amount = Double.parseDouble(amountStr);
                } catch (NumberFormatException e) {
                    throw new SpendTrackException("Amount must be a number. Usage: a/<amount>");
                }
                if (!Double.isFinite(amount)) {
                    throw new SpendTrackException("Amount must be a finite number. Usage: a/<amount>");
                }
                if (amount <= 0) {
                    throw new SpendTrackException("Amount must be a positive number. Usage: a/<amount>");
                }
                // @@author Ariff1422
                if (amount > 1000000) {
                    throw new SpendTrackException("Amount must not exceed $1,000,000.");
                }
                // @@author
            } else if (token.startsWith("c/")) {
                if (seenCategory) {
                    throw new SpendTrackException("Duplicate 'c/' detected. "
                            + "Please provide only one category.");
                }
                seenCategory = true;
                category = normalizeCategory(token.substring(2).trim());
                // @@author Ariff1422
                if (category.contains("|")) {
                    throw new SpendTrackException("Category cannot contain '|' "
                            + "(reserved for save file format). Please use a different character.");
                }
                // @@author
            } else if (token.startsWith("recurring/")) {
                if (seenRecurring) {
                    throw new SpendTrackException("Duplicate 'recurring/' detected. "
                            + "Please provide only one recurring value.");
                }
                seenRecurring = true;
                String val = token.substring(10).trim().toLowerCase();
                if (!val.equals("true") && !val.equals("false")) {
                    throw new SpendTrackException("recurring/ must be 'true' or 'false'.");
                }
                isRecurring = val.equals("true");
            }
        }

        if (description.isEmpty()) {
            throw new SpendTrackException("Description is required. Usage: add d/<desc> a/<amount> c/<category>");
        }
        if (amount == 0.0) {
            throw new SpendTrackException("Amount is required and must be greater than 0. Usage: a/<amount>");
        }

        return new AddCommand(description, amount, category, date, isRecurring);
    }
    // @@author

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
        Boolean newRecurring = null;

        boolean seenDescription = false;
        boolean seenAmount = false;
        boolean seenCategory = false;
        boolean seenDate = false;
        boolean seenRecurring = false;

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
                // @@author Ariff1422
                if (newDate.getYear() < 2000) {
                    throw new SpendTrackException("Date must be year 2000 or later.");
                }
                // @@author

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
                // @@author AfshalG
                if (newDescription.contains("|")) {
                    throw new SpendTrackException("Description cannot contain '|' "
                            + "(reserved for save file format). Please use a different character.");
                }
                // @@author

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
                if (!Double.isFinite(newAmount)) {
                    throw new SpendTrackException("Amount must be a finite number. Usage: a/<amount>");
                }
                if (newAmount <= 0) {
                    throw new SpendTrackException("Amount must be greater than 0.");
                }
                // @@author Ariff1422
                if (newAmount > 1000000) {
                    throw new SpendTrackException("Amount must not exceed $1,000,000.");
                }
                // @@author

            } else if (token.startsWith("c/")) {
                if (seenCategory) {
                    throw new SpendTrackException("Duplicate 'c/' detected. "
                            + "Please provide only one category.");
                }
                seenCategory = true;
                newCategory = normalizeCategory(token.substring(2).trim());
                // @@author Ariff1422
                if (newCategory.contains("|")) {
                    throw new SpendTrackException("Category cannot contain '|' "
                            + "(reserved for save file format). Please use a different character.");
                }
                // @@author

            } else if (token.startsWith("recurring/")) {
                if (seenRecurring) {
                    throw new SpendTrackException("Duplicate 'recurring/' detected. "
                            + "Please provide only one recurring value.");
                }
                seenRecurring = true;
                String val = token.substring(10).trim().toLowerCase();
                if (!val.equals("true") && !val.equals("false")) {
                    throw new SpendTrackException("recurring/ must be 'true' or 'false'.");
                }
                newRecurring = val.equals("true");
            }
        }

        return new EditCommand(index, newDescription, newAmount, newCategory, newDate, newRecurring);
    }

    // @@author AfshalG
    /**
     * Validates that the given string is a valid YYYY-MM format with month 1-12.
     * Used by month and report commands for strict format enforcement.
     *
     * @param input the user-provided year-month string
     * @param commandName the calling command, used in the usage error message
     * @return the validated input (unchanged) if valid
     * @throws SpendTrackException if format is wrong or month is out of range
     */
    private static String validateYearMonth(String input, String commandName) throws SpendTrackException {
        if (!input.matches("\\d{4}-\\d{2}")) {
            throw new SpendTrackException(
                    "Invalid format. Usage: " + commandName + " <YYYY-MM> (e.g. 2026-03)");
        }
        int month;
        try {
            month = Integer.parseInt(input.substring(5));
        } catch (NumberFormatException e) {
            throw new SpendTrackException(
                    "Invalid format. Usage: " + commandName + " <YYYY-MM> (e.g. 2026-03)");
        }
        if (month < 1 || month > 12) {
            throw new SpendTrackException(
                    "Invalid month. Use YYYY-MM with month between 01 and 12.");
        }
        return input;
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
    // @@author

    private static Command parseFilterCommand(String args) throws SpendTrackException {
        LocalDate from = null;
        LocalDate to = null;
        // @@author AfshalG
        boolean seenFrom = false;
        boolean seenTo = false;
        // @@author

        String[] tokens = args.split(" ");
        for (String token : tokens) {
            token = token.trim();
            if (token.startsWith("from/")) {
                // @@author AfshalG
                if (seenFrom) {
                    throw new SpendTrackException("Duplicate 'from/' detected. "
                            + "Please provide only one start date.");
                }
                seenFrom = true;
                // @@author
                from = DateParser.parse(token.substring(5).trim());
            } else if (token.startsWith("to/")) {
                // @@author AfshalG
                if (seenTo) {
                    throw new SpendTrackException("Duplicate 'to/' detected. "
                            + "Please provide only one end date.");
                }
                seenTo = true;
                // @@author
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
        String trimmed = args.trim();
        if (trimmed.equalsIgnoreCase("reset")) {
            return new BudgetResetCommand();
        }
        if (trimmed.equalsIgnoreCase("history")) {
            return new BudgetHistoryCommand();
        }
        if (trimmed.isEmpty()) {
            throw new SpendTrackException(
                    "budget requires a number. Usage: budget <amount>");
        }
        if (trimmed.toLowerCase().startsWith("reset")) {
            throw new SpendTrackException("Usage: budget reset");
        }
        if (trimmed.toLowerCase().startsWith("history")) {
            throw new SpendTrackException("Usage: budget history");
        }
        try {
            double amount = Double.parseDouble(trimmed);
            if (!Double.isFinite(amount)) {
                throw new SpendTrackException(
                        "budget requires a number. Usage: budget <amount>");
            }
            return new BudgetCommand(amount);
        } catch (NumberFormatException e) {
            throw new SpendTrackException(
                    "budget requires a number. Usage: budget <amount>");
        }
    }

    // @@author pranavjana
    private static Command parseGoalCommand(String args) throws SpendTrackException {
        String trimmed = args.trim();
        if (trimmed.equalsIgnoreCase("status")) {
            return new GoalCommand();
        }
        if (!trimmed.startsWith("g/")) {
            throw new SpendTrackException("Usage: goal g/<amount> or goal status");
        }
        String valueStr = trimmed.substring(2).trim();
        try {
            double amount = Double.parseDouble(valueStr);
            if (!Double.isFinite(amount)) {
                throw new SpendTrackException("Goal amount must be a finite number. Usage: goal g/<amount>");
            }
            if (amount <= 0) {
                throw new SpendTrackException("Goal amount must be greater than 0.");
            }
            return new GoalCommand(amount);
        } catch (NumberFormatException e) {
            throw new SpendTrackException("Goal amount must be a number. Usage: goal g/<amount>");
        }
    }
}
