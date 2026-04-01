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

        System.out.println("Here are your expenses sorted by amount:");

        int index = 1;
        for (Expense e : sorted) {
            System.out.println(index + ". " + e.toString());
            index++;
        }
    }
}
