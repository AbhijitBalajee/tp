package seedu.duke.command;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

/**
 * Adds a new expense to the expense list.
 */
public class AddCommand extends Command {

    private String description;
    private double amount;
    private String category;

    /**
     * Constructs an AddCommand with the given expense details.
     *
     * @param description the description of the expense
     * @param amount the amount spent
     * @param category the category of the expense
     */
    public AddCommand(String description, double amount, String category) {
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    /**
     * Executes the add command by creating and storing a new expense.
     *
     * @param expenses the expense list to add to
     * @param ui the UI for displaying output
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        Expense expense = new Expense(description, amount, category);
        expenses.addExpense(expense);
        ui.showAddSuccess(expense);
    }
}
