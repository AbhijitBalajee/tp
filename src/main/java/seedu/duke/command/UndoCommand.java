package seedu.duke.command;

import java.util.logging.Logger;

import seedu.duke.ExpenseList;
import seedu.duke.Ui;
import seedu.duke.UndoManager;

// @@author pranavjana
/**
 * Undoes the last mutating command by restoring the previous expense list snapshot.
 */
public class UndoCommand extends Command {

    private static final Logger logger = Logger.getLogger(UndoCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    private final UndoManager undoManager;

    /**
     * Constructs an UndoCommand with the given UndoManager.
     *
     * @param undoManager the undo manager holding the snapshot
     */
    public UndoCommand(UndoManager undoManager) {
        assert undoManager != null : "UndoManager should not be null";
        this.undoManager = undoManager;
    }

    /**
     * Executes the undo by restoring the expense list from the last snapshot.
     *
     * @param expenses the expense list to restore
     * @param ui the UI for displaying output
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        boolean isUndone = undoManager.undo(expenses);
        if (isUndone) {
            System.out.println("____________________________________________________________");
            System.out.println(" Last command undone successfully.");
            System.out.println("____________________________________________________________");
            logger.info("Undo executed successfully.");
        } else {
            System.out.println("____________________________________________________________");
            System.out.println(" Nothing to undo.");
            System.out.println("____________________________________________________________");
            logger.info("Nothing to undo.");
        }
    }

    @Override
    public boolean mutatesData() {
        return true;
    }
}
