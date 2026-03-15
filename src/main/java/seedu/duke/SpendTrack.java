package seedu.duke;

import seedu.duke.command.Command;

/**
 * Main application class for SpendTrack.
 * Owns the main loop: read → parse → execute → repeat.
 */
public class SpendTrack {

    private final Ui ui;
    private final ExpenseList expenses;

    public SpendTrack() {
        ui = new Ui();
        expenses = new ExpenseList();
    }

    /**
     * Starts the application.
     */
    public void run() {
        ui.showWelcome();
        boolean isRunning = true;
        while (isRunning) {
            String input = ui.readCommand();
            try {
                Command command = Parser.parse(input);
                command.execute(expenses, ui);
                isRunning = !command.isExit();
            } catch (SpendTrackException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new SpendTrack().run();
    }
}
