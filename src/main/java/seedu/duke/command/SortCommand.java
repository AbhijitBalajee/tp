package seedu.duke.command;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortCommand extends Command {

    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        List<Expense> sorted = new ArrayList<>(expenses.getExpenses());

        sorted.sort(Comparator.comparingDouble(Expense::getAmount).reversed());

        if (sorted.isEmpty()) {
            ui.showMessage("No expenses recorded.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(" Here are your expenses sorted by amount:\n");

        int index = 1;
        for (Expense e : sorted) {
            sb.append(" ").append(index).append(". ").append(e).append("\n");
            index++;
        }

        ui.showMessage(sb.toString().trim());
    }
}
