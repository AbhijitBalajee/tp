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

        StringBuilder sb = new StringBuilder();
        sb.append(" Top ").append(limit).append(" expenses:\n");
        for (int i = 0; i < limit; i++) {
            sb.append(" ").append(i + 1).append(". ").append(list.get(i)).append("\n");
        }

        ui.showMessage(sb.toString().trim());
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
