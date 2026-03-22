package seedu.duke;

import java.util.logging.Logger;

import seedu.duke.command.Command;

/**
 * Main application class for SpendTrack.
 * Owns the main loop: read → parse → execute → repeat.
 */
public class SpendTrack {

    private static final Logger logger = Logger.getLogger(SpendTrack.class.getName());
    private static final String DATA_FILE_PATH = "data/spendtrack.txt";

    static {
        logger.setUseParentHandlers(false);
    }

    private final Ui ui;
    private final ExpenseList expenses;
    private final Storage storage;

    public SpendTrack() {
        ui = new Ui();
        expenses = new ExpenseList();
        storage = new Storage(DATA_FILE_PATH);
    }

    /**
     * Starts the application.
     */
    public void run() {
        logger.info("SpendTrack application starting");
        storage.load(expenses);
        ui.showWelcome();
        boolean isRunning = true;
        while (isRunning) {
            String input = ui.readCommand();
            try {
                Command command = Parser.parse(input);
                command.execute(expenses, ui);
                if (command.mutatesData()) {
                    storage.save(expenses);
                }
                isRunning = !command.isExit();
            } catch (SpendTrackException e) {
                logger.warning("SpendTrackException caught: " + e.getMessage());
                ui.showError(e.getMessage());
            }
        }
        logger.info("SpendTrack application exiting");
    }

    public static void main(String[] args) {
        new SpendTrack().run();
    }
}
