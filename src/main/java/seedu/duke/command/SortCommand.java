// @@author jainsaksham2006
package seedu.duke.command;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Sorts and displays all expenses in descending order based on amount.
 * Does not modify the original expense list.
 */
public class SortCommand extends Command {

    /**
     * Executes the sort command.
     * Creates a copy of the expense list, sorts it by amount (highest first),
     * and displays the sorted result.
     *
     * @param expenses the expense list to sort
     * @param ui the UI used to display output
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) {

        // Create a copy of the original expense list to avoid modifying it
        List<Expense> sorted = new ArrayList<>(expenses.getExpenses());

        // Sort expenses by amount in descending order
        sorted.sort(Comparator.comparingDouble(Expense::getAmount).reversed());

        // Handle case where there are no expenses
        if (sorted.isEmpty()) {
            ui.showMessage("No expenses recorded.");
            return;
        }

        // Build the output string
        StringBuilder sb = new StringBuilder();
        sb.append(" Here are your expenses sorted by amount:\n");

        int index = 1;
        for (Expense e : sorted) {
            sb.append(" ").append(index).append(". ").append(e).append("\n");
            index++;
        }

        // Display the sorted list
        ui.showMessage(sb.toString().trim());
    }
}
