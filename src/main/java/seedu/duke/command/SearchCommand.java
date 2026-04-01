package seedu.duke.command;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;
import seedu.duke.SpendTrackException;

public class SearchCommand extends Command {

    private final String keyword;

    public SearchCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {

        int count = 0;
        System.out.println("Search results:");

        for (Expense e : expenses.getExpenses()) {

            if (e.getDescription().toLowerCase().contains(keyword.toLowerCase())) {

                count++;
                System.out.println(count + ". " + e);
            }
        }

        if (count == 0) {
            System.out.println("No matches found.");
        }
    }
}

