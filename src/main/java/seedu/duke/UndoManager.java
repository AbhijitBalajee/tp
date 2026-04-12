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
    // @@author AbhijitBalajee
    private ArrayList<String> snapshotBudgetHistory;
    // @@author
    private boolean hasSnapshot;

    private ArrayList<Expense> backupSnapshot;
    private double backupBudget;
    private ArrayList<String> backupBudgetHistory;
    private boolean backupHasSnapshot;
    private boolean hasBackup;

    /**
     * Constructs an UndoManager with no stored snapshot.
     */
    public UndoManager() {
        this.snapshot = null;
        this.snapshotBudget = 0.0;
        // @@author AbhijitBalajee
        this.snapshotBudgetHistory = null;
        // @@author
        this.hasSnapshot = false;
        this.hasBackup = false;
    }

    /**
     * Saves a deep copy of the current expense list, budget, and budget history as a snapshot.
     * Overwrites any previously stored snapshot.
     *
     * @param expenses the expense list to snapshot
     */
    public void saveSnapshot(ExpenseList expenses) {
        assert expenses != null : "ExpenseList should not be null when saving snapshot";

        snapshot = deepCopyExpenses(expenses.getExpenses());
        snapshotBudget = expenses.getBudget();
        // @@author AbhijitBalajee
        snapshotBudgetHistory = new ArrayList<>(expenses.getBudgetHistory());
        logger.info("Snapshot saved: " + snapshot.size() + " expenses, budget=" + snapshotBudget
                + ", budgetHistoryEntries=" + snapshotBudgetHistory.size());
        // @@author
        hasSnapshot = true;
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

        // @@author AbhijitBalajee
        expenses.restoreFrom(snapshot, snapshotBudget, snapshotBudgetHistory);
        // @@author
        hasSnapshot = false;
        snapshot = null;
        // @@author AbhijitBalajee
        snapshotBudgetHistory = null;
        // @@author
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

    /**
     * Backs up the current undo state so it can be restored if a command fails
     * or does not actually mutate data.
     */
    public void backupState() {
        backupSnapshot = snapshot;
        backupBudget = snapshotBudget;
        backupBudgetHistory = snapshotBudgetHistory;
        backupHasSnapshot = hasSnapshot;
        hasBackup = true;
    }

    /**
     * Restores the undo state from the backup taken by {@link #backupState()}.
     * Used when a command fails or does not actually mutate data.
     */
    public void rollbackState() {
        if (!hasBackup) {
            return;
        }
        snapshot = backupSnapshot;
        snapshotBudget = backupBudget;
        snapshotBudgetHistory = backupBudgetHistory;
        hasSnapshot = backupHasSnapshot;
        hasBackup = false;
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
