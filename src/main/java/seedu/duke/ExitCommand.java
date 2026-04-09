// @@author jainsaksham2006
package seedu.duke;

import java.util.logging.Logger;
import seedu.duke.command.Command;

/**
 * Signals the application to exit.
 * Prepares and logs a farewell message before terminating.
 */
public class ExitCommand extends Command {

    private static final Logger logger = Logger.getLogger(ExitCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    /**
     * Executes the exit command.
     * Prepares a farewell message, logs details, and displays goodbye message.
     *
     * @param expenses the expense list (unused)
     * @param ui the UI used to display output
     * @throws SpendTrackException if any execution error occurs
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        validateUi(ui);
        logStart();

        // Prepare farewell message
        String farewellMessage = prepareFarewellMessage();

        // Log details of the message
        logMessageDetails(farewellMessage);

        // Display goodbye message
        displayFarewell(ui);

        logEnd();
    }

    /**
     * Indicates that this command signals application termination.
     *
     * @return true since this command exits the program
     */
    @Override
    public boolean isExit() {
        return true;
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
        logger.info("Starting ExitCommand execution");
    }

    /**
     * Logs the end of command execution.
     */
    private void logEnd() {
        logger.info("ExitCommand execution completed");
    }

    /**
     * Prepares the farewell message by combining parts.
     *
     * @return the final farewell message
     */
    private String prepareFarewellMessage() {
        String greeting = getGreeting();
        String closing = getClosing();
        String punctuation = getPunctuation();

        return combineParts(greeting, closing, punctuation);
    }

    /**
     * Returns the greeting part of the message.
     *
     * @return greeting string
     */
    private String getGreeting() {
        return "Goodbye";
    }

    /**
     * Returns the closing phrase.
     *
     * @return closing string
     */
    private String getClosing() {
        return " and take care";
    }

    /**
     * Returns punctuation for the message.
     *
     * @return punctuation string
     */
    private String getPunctuation() {
        return "!";
    }

    /**
     * Combines greeting and closing, then finalizes message.
     *
     * @param greeting greeting part
     * @param closing closing part
     * @param punctuation punctuation
     * @return combined message
     */
    private String combineParts(String greeting, String closing, String punctuation) {
        String combined = greeting + closing;
        return finalizeMessage(combined, punctuation);
    }

    /**
     * Appends punctuation to the message.
     *
     * @param message base message
     * @param punctuation punctuation
     * @return final message
     */
    private String finalizeMessage(String message, String punctuation) {
        return message + punctuation;
    }

    /**
     * Logs details about the farewell message.
     *
     * @param message the farewell message
     */
    private void logMessageDetails(String message) {
        logger.info("Farewell message prepared");
        logger.fine("Message content: " + message);
        logger.fine("Message length: " + message.length());
    }

    /**
     * Displays the goodbye message using the UI.
     *
     * @param ui the UI instance
     */
    private void displayFarewell(Ui ui) {
        ui.showGoodbye(); // keep original behavior
    }
}
