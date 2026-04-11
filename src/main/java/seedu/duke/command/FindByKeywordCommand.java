// @@author Ariff1422
package seedu.duke.command;

import java.util.ArrayList;
import java.util.logging.Logger;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

/**
 * Finds and displays all expenses whose description contains a given keyword (case-insensitive).
 * Usage: find d/KEYWORD
 */
public class FindByKeywordCommand extends Command {

    private static final Logger logger = Logger.getLogger(FindByKeywordCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    private final String keyword;

    /**
     * Constructs a FindByKeywordCommand for the given keyword.
     *
     * @param keyword the search keyword (case-insensitive, matched against description)
     */
    public FindByKeywordCommand(String keyword) {
        assert keyword != null && !keyword.isBlank() : "Keyword should not be null or blank";
        this.keyword = keyword;
    }

    /**
     * Executes the command by searching all expenses for descriptions containing the keyword.
     * Displays matching expenses in a table, showing their original list indices.
     *
     * @param expenses the expense list to search
     * @param ui       the UI for displaying results
     * @throws SpendTrackException unused — kept for interface compliance
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        logger.info("Searching expenses for keyword: " + keyword);

        ArrayList<Expense> matches = new ArrayList<>();
        ArrayList<int[]> matchIndices = new ArrayList<>();

        String lowerKeyword = keyword.toLowerCase();
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.getExpense(i);
            if (e.getDescription() != null
                    && e.getDescription().toLowerCase().contains(lowerKeyword)) {
                matches.add(e);
                matchIndices.add(new int[]{i + 1});
            }
        }

        logger.info("Found " + matches.size() + " match(es) for keyword: " + keyword);
        ui.showExpensesByKeyword(matchIndices, matches, keyword);
    }

    @Override
    public boolean mutatesData() {
        return false;
    }
}
// @@author
