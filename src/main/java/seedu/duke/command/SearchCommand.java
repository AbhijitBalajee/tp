// @@author jainsaksham2006
package seedu.duke.command;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

/**
 * Searches for expenses whose descriptions contain a given keyword.
 * Matching is case-insensitive.
 */
public class SearchCommand extends Command {

    private final String keyword;

    /**
     * Constructs a SearchCommand with the given keyword.
     *
     * @param keyword the keyword to search for in expense descriptions
     */
    public SearchCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Executes the search command.
     * Iterates through all expenses and displays those whose descriptions
     * contain the given keyword (case-insensitive).
     *
     * @param expenses the expense list to search
     * @param ui the UI used to display output
     * @throws SpendTrackException if any execution error occurs
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        // @@author AfshalG
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new SpendTrackException("Please provide a search keyword. Usage: search <keyword>");
        }
        // @@author

        int count = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(" Search results:\n");

        for (Expense e : expenses.getExpenses()) {
            if (e.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                count++;
                sb.append(" ").append(count).append(". ").append(e).append("\n");
            }
        }

        if (count == 0) {
            sb.append(" No matches found.\n");
        }

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
