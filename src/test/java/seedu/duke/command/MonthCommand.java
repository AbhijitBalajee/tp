package seedu.duke.command;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Lists expenses filtered by a specific month.
 */
public class MonthCommand extends Command {

    private final String monthInput;

    public MonthCommand(String monthInput) {
        this.monthInput = monthInput;
    }

    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        ArrayList<Expense> result = new ArrayList<>();

        try {
            // parse YYYY-MM
            String[] parts = monthInput.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);

            for (int i = 0; i < expenses.size(); i++) {
                Expense e = expenses.getExpense(i);
                LocalDate date = e.getDate();

                if (date != null &&
                        date.getYear() == year &&
                        date.getMonthValue() == month) {
                    result.add(e);
                }
            }

            if (result.isEmpty()) {
                ui.showMessage("No expenses found for " + monthInput);
                return;
            }

            ui.showMessage("Expenses for " + monthInput + ":");
            for (int i = 0; i < result.size(); i++) {
                ui.showMessage((i + 1) + ". " + result.get(i));
            }

        } catch (Exception e) {
            ui.showError("Usage: month <YYYY-MM>");
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
