package seedu.duke.command;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

/**
 * Displays a spending breakdown grouped by category.
 */
public class SummaryCommand extends Command {

    private static final Logger logger = Logger.getLogger(SummaryCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    /**
     * Executes the summary command by grouping expenses by category
     * and displaying totals with percentages.
     *
     * @param expenses the expense list to summarise
     * @param ui the UI for displaying output
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        logger.info("Executing summary command");

        ArrayList<Expense> allExpenses = expenses.getExpenses();
        if (allExpenses.isEmpty()) {
            ui.showError("No expenses recorded yet.");
            return;
        }

        Map<String, Double> categoryTotals = new LinkedHashMap<>();
        double grandTotal = 0.0;

        for (Expense expense : allExpenses) {
            String category = expense.getCategory();
            double amount = expense.getAmount();
            categoryTotals.put(category,
                    categoryTotals.getOrDefault(category, 0.0) + amount);
            grandTotal += amount;
        }

        ArrayList<Map.Entry<String, Double>> sorted = new ArrayList<>(categoryTotals.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        ui.showSummary(sorted, grandTotal);
        logger.info("Summary displayed with " + sorted.size() + " categories");
    }
}
