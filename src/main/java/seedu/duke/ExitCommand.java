package seedu.duke;

import java.util.logging.Logger;
import seedu.duke.command.Command;

/**
 * Signals the application to exit.
 */
public class ExitCommand extends Command {

    private static final Logger logger = Logger.getLogger(ExitCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        validateUi(ui);
        logStart();

        String farewellMessage = prepareFarewellMessage();

        logMessageDetails(farewellMessage);
        displayFarewell(ui);

        logEnd();
    }

    @Override
    public boolean isExit() {
        return true;
    }

    private void validateUi(Ui ui) {
        assert ui != null : "Ui should not be null";
    }

    private void logStart() {
        logger.info("Starting ExitCommand execution");
    }

    private void logEnd() {
        logger.info("ExitCommand execution completed");
    }

    private String prepareFarewellMessage() {
        String greeting = getGreeting();
        String closing = getClosing();
        String punctuation = getPunctuation();

        return combineParts(greeting, closing, punctuation);
    }

    private String getGreeting() {
        return "Goodbye";
    }

    private String getClosing() {
        return " and take care";
    }

    private String getPunctuation() {
        return "!";
    }

    private String combineParts(String greeting, String closing, String punctuation) {
        String combined = greeting + closing;
        return finalizeMessage(combined, punctuation);
    }

    private String finalizeMessage(String message, String punctuation) {
        return message + punctuation;
    }

    private void logMessageDetails(String message) {
        logger.info("Farewell message prepared");
        logger.fine("Message content: " + message);
        logger.fine("Message length: " + message.length());
    }

    private void displayFarewell(Ui ui) {
        ui.showGoodbye(); // keep original behavior
    }
}
