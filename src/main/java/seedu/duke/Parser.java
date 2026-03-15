package seedu.duke;

import seedu.duke.command.AddCommand;
import seedu.duke.command.Command;
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
     */
    public static Command parse(String input) {
        String trimmed = input.trim();
        String[] parts = trimmed.split(" ", 2);
        String commandWord = parts[0].toLowerCase();

        switch (commandWord) {
        case "add":
            return parseAddCommand(parts.length > 1 ? parts[1] : "");
        case "total":
            return new TotalCommand();
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
}
