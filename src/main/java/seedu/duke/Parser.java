package seedu.duke;

import seedu.duke.command.AddCommand;
import seedu.duke.command.BudgetCommand;
import seedu.duke.command.Command;
import seedu.duke.command.DeleteCommand;
import seedu.duke.command.ListCommand;
import seedu.duke.command.TotalCommand;

/**
 * Parses user input into commands.
 */
public class Parser {

    /**
     * Parses the user input and returns the corresponding command.
     *
     * @param input the raw user input string
     * @return the parsed Command
     * @throws SpendTrackException if input is invalid
     */
    public static Command parse(String input) throws SpendTrackException {
        String trimmed = input.trim();
        String[] parts = trimmed.split(" ", 2);
        String commandWord = parts[0].toLowerCase();

        switch (commandWord) {
        case "add":
            return parseAddCommand(parts.length > 1 ? parts[1] : "");
        case "delete":
            try {
                return new DeleteCommand(Integer.parseInt(parts[1].trim()));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new SpendTrackException("delete requires a number. Usage: delete <index>");
            }
        case "total":
            return new TotalCommand();
        case "list":
            return new ListCommand();
        case "budget":
            return parseBudgetCommand(parts.length > 1 ? parts[1] : "");
        case "bye":
            return new ExitCommand();
        default:
            return new UnknownCommand();
        }
    }

    private static AddCommand parseAddCommand(String args) {
        String description = "";
        double amount = 0.0;
        String category = "Uncategorised";

        String[] tokens = args.split(" (?=[dac]/)");
        for (String token : tokens) {
            token = token.trim();
            if (token.startsWith("d/")) {
                description = token.substring(2).trim();
            } else if (token.startsWith("a/")) {
                amount = Double.parseDouble(token.substring(2).trim());
            } else if (token.startsWith("c/")) {
                category = token.substring(2).trim();
            }
        }

        return new AddCommand(description, amount, category);
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
