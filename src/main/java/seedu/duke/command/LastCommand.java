package seedu.duke.command;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import java.util.ArrayList;

public class LastCommand extends Command {

    private final int count;

    public LastCommand(int count) {
        this.count = count;
    }

    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        if (count <= 0) {
            ui.showError("Number must be greater than 0.");
            return;
        }

        int total = expenses.size();

        if (total == 0) {
            System.out.println("No expenses recorded.");
            return;
        }

        int start = Math.max(0, total - count);

        ArrayList<Expense> lastExpenses = new ArrayList<>();
        for (int i = start; i < total; i++) {
            lastExpenses.add(expenses.getExpense(i));
        }

        System.out.println("Showing last " + lastExpenses.size() + " expenses:");
        for (int i = 0; i < lastExpenses.size(); i++) {
            System.out.println((i + 1) + ". " + lastExpenses.get(i));
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
