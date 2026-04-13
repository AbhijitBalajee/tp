// @@author jainsaksham2006
package seedu.duke.command;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import java.util.ArrayList;

/**
 * Displays the last N expenses added to the expense list.
 */
public class LastCommand extends Command {

    private final int count;

    /**
     * Constructs a LastCommand with the given number of recent expenses to display.
     *
     * @param count number of recent expenses to show
     */
    public LastCommand(int count) {
        this.count = count;
    }

    /**
     * Executes the last command.
     * Retrieves the most recently added N expenses and displays them.
     *
     * @param expenses the expense list to retrieve from
     * @param ui the UI used to display output
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) {

        // Validate input count
        if (count <= 0) {
            ui.showError("Number must be greater than 0.");
            return;
        }

        int total = expenses.size();

        // Handle empty expense list
        if (total == 0) {
            ui.showMessage("No expenses recorded.");
            return;
        }

        // Determine starting index for last N expenses
        int start = Math.max(0, total - count);

        // Collect last N expenses in reverse order (most recent first)
        ArrayList<Expense> lastExpenses = new ArrayList<>();
        for (int i = total - 1; i >= start; i--) {
            lastExpenses.add(expenses.getExpense(i));
        }

        // Build output string
        StringBuilder sb = new StringBuilder();
        sb.append(" Showing last ").append(lastExpenses.size()).append(" expenses:\n");
        for (int i = 0; i < lastExpenses.size(); i++) {
            sb.append(" ").append(i + 1).append(". ").append(lastExpenses.get(i)).append("\n");
        }

        // Display result
        ui.showMessage(sb.toString().trim());
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
