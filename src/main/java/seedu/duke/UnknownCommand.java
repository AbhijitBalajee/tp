package seedu.duke;

import java.util.logging.Logger;
import seedu.duke.command.Command;

/**
 * Represents an unrecognised command entered by the user.
 */
public class UnknownCommand extends Command {

    private static final Logger logger = Logger.getLogger(UnknownCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        validateUi(ui);
        logStart();

        String message = buildFullErrorMessage();

        logMessageDetails(message);
        showMessage(ui, message);

        logEnd();
    }

    private void validateUi(Ui ui) {
        assert ui != null : "Ui should not be null";
    }

    private void logStart() {
        logger.info("Starting execution of UnknownCommand");
    }

    private void logEnd() {
        logger.info("Finished execution of UnknownCommand");
    }

    private String buildFullErrorMessage() {
        String header = buildHeader();
        String suggestion = buildSuggestion();
        String formatting = buildFormatting();

        return assembleMessage(header, suggestion, formatting);
    }

    private String buildHeader() {
        return "Unknown command.";
    }

    private String buildSuggestion() {
        return " Try typing 'help' to see available commands.";
    }

    private String buildFormatting() {
        return "";
    }

    private String assembleMessage(String header, String suggestion, String formatting) {
        String combined = header + suggestion;
        return applyFormatting(combined, formatting);
    }

    private String applyFormatting(String message, String formatting) {
        return formatting + message;
    }

    private void logMessageDetails(String message) {
        logger.warning("Unknown command encountered");
        logger.fine("Generated message: " + message);
        logger.fine("Message length: " + message.length());
    }

    private void showMessage(Ui ui, String message) {
        ui.showError(message);
    }
}

