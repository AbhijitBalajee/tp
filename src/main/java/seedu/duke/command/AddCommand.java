package seedu.duke.command;

import java.time.LocalDate;
import java.util.logging.Logger;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

/**
 * Adds a new expense to the expense list.
 */
public class AddCommand extends Command {

    private static final Logger logger = Logger.getLogger(AddCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    private final String description;
    private final double amount;
    private final String category;
    private final LocalDate date;
    private final boolean isRecurring;

    /**
     * Constructs an AddCommand without recurring flag (defaults to false).
     *
     * @param description the description of the expense
     * @param amount the amount spent
     * @param category the category of the expense
     * @param date the date of the expense
     */
    public AddCommand(String description, double amount, String category, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.isRecurring = false;
    }

    /**
     * Constructs an AddCommand with all fields including recurring flag.
     *
     * @param description the description of the expense
     * @param amount the amount spent
     * @param category the category of the expense
     * @param date the date of the expense
     * @param isRecurring whether the expense is recurring
     */
    public AddCommand(String description, double amount, String category,
                      LocalDate date, boolean isRecurring) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.isRecurring = isRecurring;
    }

    /**
     * Executes the add command by creating and storing a new expense.
     *
     * @param expenses the expense list to add to
     * @param ui the UI for displaying output
     * @throws SpendTrackException if the expense cannot be added
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";
        assert amount > 0 : "Amount must be greater than 0";

        logger.info("Adding expense: " + description + ", amount: " + amount
                + ", category: " + category + ", date: " + date
                + ", recurring: " + isRecurring);
        Expense expense = new Expense(description, amount, category, date, isRecurring);
        expenses.addExpense(expense);
        ui.showAddSuccess(expense);
        logger.info("Expense added successfully. Total expenses: " + expenses.size());
    }

    @Override
    public boolean mutatesData() {
        return true;
    }
}
