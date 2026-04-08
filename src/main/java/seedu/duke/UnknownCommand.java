// @@author jainsaksham2006
package seedu.duke;

import java.util.logging.Logger;
import seedu.duke.command.Command;

/**
 * Represents an unrecognised command entered by the user.
 * Displays an error message with a helpful suggestion.
 */
public class UnknownCommand extends Command {

    private static final Logger logger = Logger.getLogger(UnknownCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    /**
     * Executes the unknown command.
     * Builds and displays an error message indicating the command is invalid.
     *
     * @param expenses the expense list (unused)
     * @param ui the UI used to display output
     * @throws SpendTrackException if any execution error occurs
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        validateUi(ui);
        logStart();

        // Build the full error message
        String message = buildFullErrorMessage();

        // Log details and display message
        logMessageDetails(message);
        showMessage(ui, message);

        logEnd();
    }

    /**
     * Validates that the UI is not null.
     *
     * @param ui the UI instance
     */
    private void validateUi(Ui ui) {
        assert ui != null : "Ui should not be null";
    }

    /**
     * Logs the start of command execution.
     */
    private void logStart() {
        logger.info("Starting execution of UnknownCommand");
    }

    /**
     * Logs the end of command execution.
     */
    private void logEnd() {
        logger.info("Finished execution of UnknownCommand");
    }

    /**
     * Builds the complete error message by combining parts.
     *
     * @return formatted error message
     */
    private String buildFullErrorMessage() {
        String header = buildHeader();
        String suggestion = buildSuggestion();
        String formatting = buildFormatting();

        return assembleMessage(header, suggestion, formatting);
    }

    /**
     * Returns the main error header.
     *
     * @return error header string
     */
    private String buildHeader() {
        return "Unknown command.";
    }

    /**
     * Returns a helpful suggestion for the user.
     *
     * @return suggestion string
     */
    private String buildSuggestion() {
        return " Try typing 'help' to see available commands.";
    }

    /**
     * Returns formatting string (currently empty).
     *
     * @return formatting string
     */
    private String buildFormatting() {
        return "";
    }

    /**
     * Combines header and suggestion into a single message.
     *
     * @param header main message
     * @param suggestion additional suggestion
     * @param formatting formatting prefix
     * @return combined message
     */
    private String assembleMessage(String header, String suggestion, String formatting) {
        String combined = header + suggestion;
        return applyFormatting(combined, formatting);
    }

    /**
     * Applies formatting to the message.
     *
     * @param message the base message
     * @param formatting formatting prefix
     * @return formatted message
     */
    private String applyFormatting(String message, String formatting) {
        return formatting + message;
    }

    /**
     * Logs details about the generated message.
     *
     * @param message the message to log
     */
    private void logMessageDetails(String message) {
        logger.warning("Unknown command encountered");
        logger.fine("Generated message: " + message);
        logger.fine("Message length: " + message.length());
    }

    /**
     * Displays the error message through the UI.
     *
     * @param ui the UI instance
     * @param message the message to display
     */
    private void showMessage(Ui ui, String message) {
        ui.showError(message);
    }
}

