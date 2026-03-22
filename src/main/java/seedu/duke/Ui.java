package seedu.duke;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;


/**
 * Handles all user-facing input and output for SpendTrack.
 */
public class Ui {

    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message on startup.
     */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(" Welcome to SpendTrack!");
        System.out.println(" Type 'add d/<desc> a/<amount> c/<category> [date/<YYYY-MM-DD>]' to add an expense.");
        System.out.println(" Type 'list' to view all expenses.");
        System.out.println(" Type 'delete <index>' to delete an expense.");
        System.out.println(" Type 'total' to view total expenses.");
        System.out.println(" Type 'budget <amount>' to set your monthly budget.");
        System.out.println(" Type 'bye' to exit.");
        System.out.println(LINE);
    }

    /**
     * Displays the goodbye message on exit.
     */
    public void showGoodbye() {
        System.out.println(LINE);
        System.out.println(" Goodbye! Stay on budget!");
        System.out.println(LINE);
    }

    /**
     * Reads the next line of user input.
     *
     * @return the user's input string
     */
    public String readCommand() {
        System.out.print("> ");
        return scanner.nextLine();
    }

    /**
     * Displays a success message after an expense is added.
     *
     * @param expense the expense that was added
     */
    public void showAddSuccess(Expense expense) {
        System.out.println(LINE);
        System.out.println(" New expense added:");
        System.out.println("   " + expense);
        System.out.println(LINE);
    }

    /**
     * Displays a success message after an expense is deleted.
     *
     * @param expense the expense that was removed
     */
    public void showDeleteSuccess(Expense expense) {
        System.out.println(LINE);
        System.out.println(" Expense deleted:");
        System.out.println("   " + expense);
        System.out.println(LINE);
    }

    /**
     * Displays all expenses in a formatted table.
     *
     * @param expenses the list of expenses to display
     */
    public void showExpenseList(ExpenseList expenses) {
        assert expenses != null : "ExpenseList passed to showExpenseList should not be null";

        System.out.println(LINE);
        System.out.println(" Your Expenses");
        System.out.println(LINE);

        if (expenses.size() == 0) {
            System.out.println(" No expenses recorded yet.");
            System.out.println(LINE);
            return;
        }

        System.out.printf("  %-3s  %-14s %-20s %-12s %s%n",
                "#", "Category", "Description", "Date", "Amount");
        System.out.println(" ---  -------------  ------------------  ----------  --------");

        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.getExpense(i);
            assert e != null : "Expense at index " + i + " should not be null";

            String category = (e.getCategory() == null || e.getCategory().isBlank())
                    ? "Uncategorised" : e.getCategory();
            String description = (e.getDescription() == null || e.getDescription().isBlank())
                    ? "(no description)" : e.getDescription();
            String date = (e.getDate() != null) ? e.getDate().toString() : "-";

            System.out.printf("  %-3s  %-14s %-20s %-12s $%.2f%n",
                    (i + 1) + ".",
                    "[" + category + "]",
                    description,
                    date,
                    e.getAmount());
        }

        System.out.println(LINE);
        System.out.println(" Total entries: " + expenses.size());
        System.out.println(LINE);
    }

    /**
     * Displays the total sum of all expenses.
     *
     * @param total the total expense amount
     */
    public void showTotal(double total) {
        assert Double.isFinite(total) : "Total shown to the user should be a finite number";

        System.out.println(LINE);
        System.out.printf(" Total expenses: $%.2f%n", total);
        System.out.println(LINE);
    }

    /**
     * Displays confirmation after budget is set.
     *
     * @param budget     the budget amount set
     * @param totalSpent the current total spent
     */
    public void showBudgetSet(double budget, double totalSpent) {
        assert budget > 0 : "Budget should be positive when showing budget set message";
        System.out.println(LINE);
        System.out.printf(" Monthly budget set to: $%.2f%n", budget);
        System.out.printf(" Current total spent:   $%.2f%n", totalSpent);
        System.out.printf(" Remaining budget:      $%.2f%n", budget - totalSpent);
        System.out.println(LINE);
    }

    /**
     * Displays a warning when expenses have exceeded the budget.
     *
     * @param budget     the budget limit
     * @param totalSpent the current total spent
     */
    public void showBudgetExceeded(double budget, double totalSpent) {
        assert totalSpent > budget : "Should only show exceeded warning when total exceeds budget";
        System.out.println(LINE);
        System.out.printf(" WARNING: You have exceeded your budget by $%.2f!%n", totalSpent - budget);
        System.out.println(LINE);
    }

    /**
     * Displays the remaining balance against the set budget.
     *
     * @param budget     the monthly budget limit
     * @param totalSpent the total amount spent
     * @param remaining  the remaining balance
     */
    public void showRemaining(double budget, double totalSpent, double remaining) {
        System.out.println(LINE);
        System.out.printf(" Budget:         $%.2f%n", budget);
        System.out.printf(" Total spent:    $%.2f%n", totalSpent);
        System.out.printf(" Remaining:      $%.2f%n", remaining);
        if (remaining < 0) {
            System.out.printf(" WARNING: You are over budget by $%.2f!%n", Math.abs(remaining));
        }
        System.out.println(LINE);
    }

    /**
     * Displays all available commands.
     */
    public void showHelp() {
        System.out.println(LINE);
        System.out.println(" Available commands (alias in brackets):");
        System.out.println("  add (a)    d/<desc> a/<amt> c/<cat> [date/<YYYY-MM-DD>] -- add expense");
        System.out.println("  delete (d) <index>                                     -- delete expense");
        System.out.println("  list (l)                                               -- list all");
        System.out.println("  total                                                  -- show total");
        System.out.println("  summary (s)                                            -- category breakdown");
        System.out.println("  budget (b) <amount>                                    -- set budget");
        System.out.println("  remaining                                              -- show remaining");
        System.out.println("  help (h)                                               -- show this help");
        System.out.println("  bye                                                    -- exit");
        System.out.println(LINE);
    }

    /**
     * Displays a spending summary grouped by category.
     *
     * @param sortedCategories the categories sorted by total descending
     * @param grandTotal the total of all expenses
     */
    public void showSummary(ArrayList<Map.Entry<String, Double>> sortedCategories,
            double grandTotal) {
        System.out.println(LINE);
        System.out.println(" ===== Spending Summary =====");

        for (Map.Entry<String, Double> entry : sortedCategories) {
            String category = entry.getKey();
            double amount = entry.getValue();
            int percentage = (int) Math.round(amount / grandTotal * 100);
            System.out.printf(" %-16s: $%.2f  (%d%%)%n", category, amount, percentage);
        }

        System.out.println(" ----------------------------");
        System.out.printf(" %-16s: $%.2f%n", "Total", grandTotal);
        System.out.println(LINE);
    }

    /**
     * Displays full details of a single expense.
     *
     * @param index the 1-based index of the expense
     * @param expense the expense to display
     */
    public void showExpenseDetail(int index, Expense expense) {
        assert expense != null : "Expense passed to showExpenseDetail should not be null";
        System.out.println(LINE);
        System.out.println(" ===== Expense #" + index + " =====");
        System.out.printf(" Description : %s%n", expense.getDescription());
        System.out.printf(" Amount      : $%.2f%n", expense.getAmount());
        System.out.printf(" Category    : %s%n", expense.getCategory());
        System.out.printf(" Date        : %s%n", expense.getDate());
        System.out.println(LINE);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println(LINE);
        System.out.println(" Error: " + message);
        System.out.println(LINE);
    }
}
