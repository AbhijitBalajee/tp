package seedu.duke;

import java.util.logging.Logger;

// @@author pranavjana
/**
 * Checks total spending against the monthly budget and returns an appropriate alert message.
 * Used after each add command to warn the user if they are approaching or exceeding their budget.
 */
public class BudgetChecker {

    private static final Logger logger = Logger.getLogger(BudgetChecker.class.getName());
    private static final double WARNING_THRESHOLD = 0.9;

    static {
        logger.setUseParentHandlers(false);
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private BudgetChecker() {
        // Utility class should not be instantiated
    }

    /**
     * Checks the current spending against the budget and prints a warning or alert if needed.
     * Does nothing if no budget has been set or spending is below 90% of the budget.
     *
     * @param expenses the expense list containing budget and expense data
     * @param ui the UI used to display messages
     */
    public static void check(ExpenseList expenses, Ui ui) {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        if (!expenses.hasBudget()) {
            logger.fine("No budget set, skipping budget check.");
            return;
        }

        // @@author AfshalG
        double totalSpent = expenses.getMonthlyTotal();
        // @@author
        double budget = expenses.getBudget();

        assert budget > 0 : "Budget should be positive when hasBudget() returns true";

        double usageRatio = totalSpent / budget;

        logger.info(String.format("Budget check: spent=%.2f, budget=%.2f, ratio=%.2f",
                totalSpent, budget, usageRatio));

        if (totalSpent > budget) {
            ui.showBudgetAlert(totalSpent, budget);
        } else if (usageRatio >= WARNING_THRESHOLD) {
            ui.showBudgetWarning(totalSpent, budget);
        }
    }
}
