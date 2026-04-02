package seedu.duke.command;


import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

public class SearchCommand extends Command {

    private final String keyword;

    public SearchCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(ExpenseList expenses, Ui ui) throws SpendTrackException {

        int count = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(" Search results:\n");

        for (Expense e : expenses.getExpenses()) {
            if (e.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                count++;
                sb.append(" ").append(count).append(". ").append(e).append("\n");
            }
        }

        if (count == 0) {
            sb.append(" No matches found.\n");
        }

        ui.showMessage(sb.toString().trim());
    }
}
