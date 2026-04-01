package seedu.duke.command;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates a monthly spending report.
 */
public class ReportCommand extends Command {

    private final String monthInput;

    public ReportCommand(String monthInput) {
        this.monthInput = monthInput;
    }

    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        try {
            String[] parts = monthInput.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);

            double total = 0.0;
            Map<String, Double> categoryTotals = new HashMap<>();

            for (int i = 0; i < expenses.size(); i++) {
                Expense e = expenses.getExpense(i);
                LocalDate date = e.getDate();

                if (date != null &&
                        date.getYear() == year &&
                        date.getMonthValue() == month) {

                    total += e.getAmount();

                    String category = e.getCategory();
                    categoryTotals.put(category,
                            categoryTotals.getOrDefault(category, 0.0) + e.getAmount());
                }
            }

            if (total == 0.0) {
                ui.showMessage("No expenses found for " + monthInput);
                return;
            }

            ui.showMessage("Monthly Report for " + monthInput + ":");
            ui.showMessage("Total spent: $" + String.format("%.2f", total));

            ui.showMessage("Breakdown by category:");
            for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
                ui.showMessage(" - " + entry.getKey() + ": $" +
                        String.format("%.2f", entry.getValue()));
            }

        } catch (Exception e) {
            ui.showError("Usage: report <YYYY-MM>");
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
