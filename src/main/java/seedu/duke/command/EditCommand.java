package seedu.duke.command;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

/**
 * Edits an existing expense entry by its 1-based index.
 * Only fields provided by the user are updated; others remain unchanged.
 */
public class EditCommand extends Command {

    private static final Logger logger = Logger.getLogger(EditCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    private final int index;
    private final String newDescription;
    private final Double newAmount;
    private final String newCategory;
    private final LocalDate newDate;

    /**
     * Constructs an EditCommand.
     *
     * @param index          the 1-based index of the expense to edit
     * @param newDescription the new description, or null to keep existing
     * @param newAmount      the new amount, or null to keep existing
     * @param newCategory    the new category, or null to keep existing
     * @param newDate        the new date, or null to keep existing
     */
    public EditCommand(int index, String newDescription, Double newAmount,
                       String newCategory, LocalDate newDate) {
        this.index = index;
        this.newDescription = newDescription;
        this.newAmount = newAmount;
        this.newCategory = newCategory;
        this.newDate = newDate;
    }

    /**
     * Executes the edit command, updating only the specified fields.
     *
     * @param expenses the current expense list
     * @param ui       the UI handler
     * @throws SpendTrackException if the index is out of range or no fields provided
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        if (index < 1 || index > expenses.size()) {
            throw new SpendTrackException("Index " + index + " is out of range. "
                    + "There are " + expenses.size() + " expense(s).");
        }

        if (newDescription == null && newAmount == null && newCategory == null && newDate == null) {
            throw new SpendTrackException("No fields provided to edit. "
                    + "Usage: edit <index> [d/<desc>] [a/<amount>] [c/<category>] [date/<YYYY-MM-DD>]");
        }

        if (newDescription != null && newDescription.isBlank()) {
            throw new SpendTrackException("Description cannot be empty.");
        }
        if (newAmount != null && newAmount <= 0) {
            throw new SpendTrackException("Amount must be a positive number.");
        }

        Expense old = expenses.getExpense(index - 1);

        String updatedDescription = (newDescription != null) ? newDescription : old.getDescription();
        double updatedAmount      = (newAmount != null) ? newAmount : old.getAmount();
        String updatedCategory    = (newCategory != null) ? newCategory : old.getCategory();
        LocalDate updatedDate     = (newDate != null) ? newDate : old.getDate();

        Expense updated = new Expense(updatedDescription, updatedAmount, updatedCategory, updatedDate);
        expenses.setExpense(index - 1, updated);

        logger.log(Level.INFO, "Expense at index {0} edited.", index);
        ui.showEditSuccess(index, old, updated);
    }
}