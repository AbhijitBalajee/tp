// @@author jainsaksham2006
package seedu.duke.command;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Lists expenses filtered by a specific month.
 * Displays all expenses that match the given year and month.
 */
public class MonthCommand extends Command {

    private final String monthInput;

    /**
     * Constructs a MonthCommand with the specified month input.
     *
     * @param monthInput the target month in format YYYY-MM
     */
    public MonthCommand(String monthInput) {
        this.monthInput = monthInput;
    }

    /**
     * Executes the month command.
     * Parses the input month, filters expenses for that month,
     * and displays the matching results.
     *
     * @param expenses the expense list to filter
     * @param ui the UI used to display output
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        ArrayList<Expense> result = new ArrayList<>();

        try {
            // Parse input into year and month (YYYY-MM)
            String[] parts = monthInput.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);

            // Iterate through all expenses
            for (int i = 0; i < expenses.size(); i++) {
                Expense e = expenses.getExpense(i);
                LocalDate date = e.getDate();

                // Check if expense matches the given month and year
                if (date != null &&
                        date.getYear() == year &&
                        date.getMonthValue() == month) {
                    result.add(e);
                }
            }

            // Handle case where no expenses match
            if (result.isEmpty()) {
                ui.showMessage("No expenses found for " + monthInput);
                return;
            }

            // Build output string
            StringBuilder sb = new StringBuilder();
            sb.append(" Expenses for ").append(monthInput).append(":\n");

            for (int i = 0; i < result.size(); i++) {
                sb.append(" ").append(i + 1).append(". ").append(result.get(i)).append("\n");
            }

            // Display result
            ui.showMessage(sb.toString().trim());

        } catch (Exception e) {
            // Handle invalid input format
            ui.showError("Usage: month <YYYY-MM>");
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
