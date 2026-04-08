// @@author jainsaksham2006
package seedu.duke.command;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Shows the top N most expensive expenses.
 * Displays expenses sorted in descending order by amount.
 */
public class TopCommand extends Command {

    private final int count;

    /**
     * Constructs a TopCommand with the given number of expenses to display.
     *
     * @param count number of top expenses to show
     */
    public TopCommand(int count) {
        this.count = count;
    }

    /**
     * Executes the top command.
     * Copies the expense list, sorts it by amount (highest first),
     * and displays the top N expenses.
     *
     * @param expenses the expense list to process
     * @param ui the UI used to display output
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) {

        // Validate input count
        if (count <= 0) {
            ui.showError("Number must be greater than 0.");
            return;
        }

        // Handle empty expense list
        if (expenses.size() == 0) {
            ui.showMessage("No expenses recorded.");
            return;
        }

        // Create a copy of expenses
        ArrayList<Expense> list = new ArrayList<>();
        for (int i = 0; i < expenses.size(); i++) {
            list.add(expenses.getExpense(i));
        }

        // Sort expenses in descending order by amount
        list.sort(Comparator.comparingDouble(Expense::getAmount).reversed());

        // Ensure limit does not exceed list size
        int limit = Math.min(count, list.size());

        // Build output string
        StringBuilder sb = new StringBuilder();
        sb.append(" Top ").append(limit).append(" expenses:\n");
        for (int i = 0; i < limit; i++) {
            sb.append(" ").append(i + 1).append(". ").append(list.get(i)).append("\n");
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
