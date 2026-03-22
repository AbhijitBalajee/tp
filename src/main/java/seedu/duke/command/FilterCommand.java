package seedu.duke.command;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Logger;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

/**
 * Filters and displays expenses within a given date range (inclusive).
 */
public class FilterCommand extends Command {

    private static final Logger logger = Logger.getLogger(FilterCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    private final LocalDate from;
    private final LocalDate to;

    /**
     * Constructs a FilterCommand with the given date range.
     *
     * @param from the start date (inclusive)
     * @param to the end date (inclusive)
     */
    public FilterCommand(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Executes the filter command by displaying expenses within the date range.
     *
     * @param expenses the expense list to filter
     * @param ui the UI for displaying output
     * @throws SpendTrackException if no expenses are found in the range
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";
        assert from != null : "From date should not be null";
        assert to != null : "To date should not be null";

        logger.info("Filtering expenses from " + from + " to " + to);

        ArrayList<Expense> filtered = new ArrayList<>();
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.getExpense(i);
            LocalDate date = e.getDate();
            if (date != null && !date.isBefore(from) && !date.isAfter(to)) {
                filtered.add(e);
            }
        }

        ui.showFilteredExpenses(filtered, from, to);
    }
}
