package seedu.duke.command;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Shows the top N most expensive expenses.
 */
public class TopCommand extends Command {

    private final int count;

    public TopCommand(int count) {
        this.count = count;
    }

    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        if (count <= 0) {
            ui.showError("Number must be greater than 0.");
            return;
        }

        if (expenses.size() == 0) {
            ui.showMessage("No expenses recorded.");
            return;
        }

        ArrayList<Expense> list = new ArrayList<>();
        for (int i = 0; i < expenses.size(); i++) {
            list.add(expenses.getExpense(i));
        }

        // sort descending by amount
        list.sort(Comparator.comparingDouble(Expense::getAmount).reversed());

        int limit = Math.min(count, list.size());

        ui.showMessage("Top " + limit + " expenses:");
        for (int i = 0; i < limit; i++) {
            ui.showMessage((i + 1) + ". " + list.get(i));
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
