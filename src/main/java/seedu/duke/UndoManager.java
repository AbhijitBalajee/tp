package seedu.duke;

import java.util.ArrayList;
import java.util.logging.Logger;

// @@author pranavjana
/**
 * Manages undo functionality by storing a snapshot of the expense list
 * before each mutating command. Supports single-level undo only.
 */
public class UndoManager {

    private static final Logger logger = Logger.getLogger(UndoManager.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    private ArrayList<Expense> snapshot;
    private double snapshotBudget;
    private boolean hasSnapshot;

    /**
     * Constructs an UndoManager with no stored snapshot.
     */
    public UndoManager() {
        this.snapshot = null;
        this.snapshotBudget = 0.0;
        this.hasSnapshot = false;
    }

    /**
     * Saves a deep copy of the current expense list and budget as a snapshot.
     * Overwrites any previously stored snapshot.
     *
     * @param expenses the expense list to snapshot
     */
    public void saveSnapshot(ExpenseList expenses) {
        assert expenses != null : "ExpenseList should not be null when saving snapshot";

        snapshot = deepCopyExpenses(expenses.getExpenses());
        snapshotBudget = expenses.getBudget();
        hasSnapshot = true;
        logger.info("Snapshot saved: " + snapshot.size() + " expenses, budget=" + snapshotBudget);
    }

    /**
     * Restores the expense list from the stored snapshot.
     * After restoring, the snapshot is consumed and cannot be used again.
     *
     * @param expenses the expense list to restore into
     * @return true if undo was successful, false if no snapshot available
     */
    public boolean undo(ExpenseList expenses) {
        assert expenses != null : "ExpenseList should not be null when undoing";

        if (!hasSnapshot) {
            logger.info("Undo attempted but no snapshot available.");
            return false;
        }

        expenses.restoreFrom(snapshot, snapshotBudget);
        hasSnapshot = false;
        snapshot = null;
        logger.info("Snapshot restored successfully.");
        return true;
    }

    /**
     * Returns true if a snapshot is available for undo.
     *
     * @return true if undo is possible
     */
    public boolean hasSnapshot() {
        return hasSnapshot;
    }

    private ArrayList<Expense> deepCopyExpenses(ArrayList<Expense> original) {
        assert original != null : "Original expense list should not be null";

        ArrayList<Expense> copy = new ArrayList<>();
        for (Expense expense : original) {
            copy.add(new Expense(
                    expense.getDescription(),
                    expense.getAmount(),
                    expense.getCategory(),
                    expense.getDate(),
                    expense.isRecurring()
            ));
        }
        return copy;
    }
}
