// @@author jainsaksham2006
package seedu.duke.command;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates a monthly spending report.
 * Displays total expenses and a breakdown by category for a given month.
 */
public class ReportCommand extends Command {

    private final String monthInput;

    /**
     * Constructs a ReportCommand with the specified month input.
     *
     * @param monthInput the target month in format YYYY-MM
     */
    public ReportCommand(String monthInput) {
        this.monthInput = monthInput;
    }

    /**
     * Executes the report command.
     * Parses the input month, filters expenses for that month,
     * calculates total spending and category-wise breakdown,
     * and displays the result.
     *
     * @param expenses the expense list to process
     * @param ui the UI used to display output
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        try {
            // Split input into year and month
            String[] parts = monthInput.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);

            double total = 0.0;

            // Map to store total spending per category
            Map<String, Double> categoryTotals = new HashMap<>();

            // Iterate through all expenses
            for (int i = 0; i < expenses.size(); i++) {
                Expense e = expenses.getExpense(i);
                LocalDate date = e.getDate();

                // Check if expense belongs to the specified month and year
                if (date != null &&
                        date.getYear() == year &&
                        date.getMonthValue() == month) {

                    total += e.getAmount();

                    // Aggregate amount by category
                    String category = e.getCategory();
                    categoryTotals.put(category,
                            categoryTotals.getOrDefault(category, 0.0) + e.getAmount());
                }
            }

            // Handle case where no expenses match
            if (total == 0.0) {
                ui.showMessage("No expenses found for " + monthInput);
                return;
            }

            // Build output string
            StringBuilder sb = new StringBuilder();
            sb.append(" Monthly Report for ").append(monthInput).append(":\n");
            sb.append(" Total spent: $").append(String.format("%.2f", total)).append("\n");
            sb.append(" Breakdown by category:\n");

            categoryTotals.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .forEach(entry -> sb.append("  - ").append(entry.getKey()).append(": $")
                            .append(String.format("%.2f", entry.getValue())).append("\n"));

            // Display result
            ui.showMessage(sb.toString().trim());

        } catch (Exception e) {
            // Handle invalid input format
            ui.showError("Usage: report <YYYY-MM>");
        }
    }

    /**
     * Indicates that this command does not terminate the application.
     *
     * @return false since this is not an exit command
     */
    @Override
    public boolean isExit() {
        return false;
    }
}
