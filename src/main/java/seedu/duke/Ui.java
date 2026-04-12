package seedu.duke;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;


/**
 * Handles all user-facing input and output for SpendTrack.
 */
public class Ui {

    private static final Logger logger = Logger.getLogger(Ui.class.getName());
    private static final String LINE = "____________________________________________________________";
    // @@author Ariff1422
    private static final String LOGO =
            "  ____                       _ _____               _\n"
            + " / ___| _ __   ___ _ __   __| |_   _| __ __ _  ___| | __\n"
            + " \\___ \\| '_ \\ / _ \\ '_ \\ / _` | | || '__/ _` |/ __| |/ /\n"
            + "  ___) | |_) |  __/ | | | (_| | | || | | (_| | (__|   <\n"
            + " |____/| .__/ \\___|_| |_|\\__,_| |_||_|  \\__,_|\\___|_|\\_\\\n"
            + "       |_|";
    private static final String LOGO_BLOCK =
            " \u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2557\u2588\u2588\u2588\u2588\u2588\u2588\u2557"
            + " \u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2557\u2588\u2588\u2588\u2557   \u2588\u2588\u2557"
            + "\u2588\u2588\u2588\u2588\u2588\u2588\u2557 \u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2557"
            + "\u2588\u2588\u2588\u2588\u2588\u2588\u2557  \u2588\u2588\u2588\u2588\u2588\u2557 "
            + " \u2588\u2588\u2588\u2588\u2588\u2588\u2557\u2588\u2588\u2557  \u2588\u2588\u2557\n"
            + " \u2588\u2588\u2554\u2550\u2550\u2550\u2550\u255d\u2588\u2588\u2554\u2550\u2550\u2588\u2588\u2557"
            + "\u2588\u2588\u2554\u2550\u2550\u2550\u2550\u255d\u2588\u2588\u2588\u2588\u2557  \u2588\u2588\u2551"
            + "\u2588\u2588\u2554\u2550\u2550\u2588\u2588\u2557\u255a\u2550\u2550\u2588\u2588\u2554\u2550\u2550\u255d"
            + "\u2588\u2588\u2554\u2550\u2550\u2588\u2588\u2557\u2588\u2588\u2554\u2550\u2550\u2588\u2588\u2557"
            + "\u2588\u2588\u2554\u2550\u2550\u2550\u2550\u255d\u2588\u2588\u2551 \u2588\u2588\u2554\u255d\n"
            + " \u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2557\u2588\u2588\u2588\u2588\u2588\u2588\u2554\u255d"
            + "\u2588\u2588\u2588\u2588\u2588\u2557  \u2588\u2588\u2554\u2588\u2588\u2557 \u2588\u2588\u2551"
            + "\u2588\u2588\u2551  \u2588\u2588\u2551   \u2588\u2588\u2551   "
            + "\u2588\u2588\u2588\u2588\u2588\u2588\u2554\u255d\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2551"
            + "\u2588\u2588\u2551     \u2588\u2588\u2588\u2588\u2588\u2554\u255d \n"
            + " \u255a\u2550\u2550\u2550\u2550\u2588\u2588\u2551\u2588\u2588\u2554\u2550\u2550\u2550\u255d "
            + "\u2588\u2588\u2554\u2550\u2550\u255d  \u2588\u2588\u2551\u255a\u2588\u2588\u2557\u2588\u2588\u2551"
            + "\u2588\u2588\u2551  \u2588\u2588\u2551   \u2588\u2588\u2551   "
            + "\u2588\u2588\u2554\u2550\u2550\u2588\u2588\u2557\u2588\u2588\u2554\u2550\u2550\u2588\u2588\u2551"
            + "\u2588\u2588\u2551     \u2588\u2588\u2554\u2550\u2588\u2588\u2557 \n"
            + " \u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2551\u2588\u2588\u2551     "
            + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2557\u2588\u2588\u2551 \u255a\u2588\u2588\u2588\u2588\u2551"
            + "\u2588\u2588\u2588\u2588\u2588\u2588\u2554\u255d   \u2588\u2588\u2551   "
            + "\u2588\u2588\u2551  \u2588\u2588\u2551\u2588\u2588\u2551  \u2588\u2588\u2551"
            + "\u255a\u2588\u2588\u2588\u2588\u2588\u2588\u2557\u2588\u2588\u2551  \u2588\u2588\u2557\n"
            + " \u255a\u2550\u2550\u2550\u2550\u2550\u2550\u255d\u255a\u2550\u255d     "
            + "\u255a\u2550\u2550\u2550\u2550\u2550\u2550\u255d\u255a\u2550\u255d  \u255a\u2550\u2550\u2550\u255d"
            + "\u255a\u2550\u2550\u2550\u2550\u2550\u255d    \u255a\u2550\u255d   "
            + "\u255a\u2550\u255d  \u255a\u2550\u255d\u255a\u2550\u255d  \u255a\u2550\u255d"
            + " \u255a\u2550\u2550\u2550\u2550\u2550\u255d\u255a\u2550\u255d  \u255a\u2550\u255d";
    // @@author
    private final Scanner scanner;

    static {
        logger.setUseParentHandlers(false);
    }

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    // @@author AfshalG
    /**
     * Package-private constructor for tests, allowing a custom input stream.
     *
     * @param in the input stream to read commands from
     */
    Ui(InputStream in) {
        this.scanner = new Scanner(in);
    }
    // @@author

    // @@author Ariff1422
    /**
     * Displays the welcome message on startup with ASCII logo.
     */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(LOGO);
        System.out.println(LINE);
        System.out.println(" Welcome to SpendTrack! Your personal CLI expense tracker.");
        System.out.println(" Type 'help' to see all available commands.");
        System.out.println(LINE);
    }
    // @@author

    /**
     * Displays the goodbye message on exit.
     */
    public void showGoodbye() {
        System.out.println(LINE);
        System.out.println(" Goodbye! Stay on budget!");
        System.out.println(LINE);
    }

    // @@author Ariff1422
    /**
     * Reads the next line of user input, wrapped with input separator bars.
     * If stdin reaches EOF (e.g. Ctrl+D or piped input ending without 'bye'),
     * returns "bye" so the main loop can exit gracefully instead of crashing.
     *
     * @return the user's input string, or "bye" on EOF
     */
    public String readCommand() {
        System.out.print(" > ");
        // @@author AfshalG
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            logger.info("EOF reached on stdin — treating as 'bye' for graceful exit");
            return "bye";
        }
        // @@author
    }

    /**
     * Prompts the user to confirm deletion of the given expense.
     * Returns true if the user types "yes" (case-insensitive), false otherwise.
     *
     * @param expense the expense to be deleted
     * @return true if user confirms, false if user cancels
     */
    public boolean confirmDelete(Expense expense) {
        System.out.println(LINE);
        System.out.println(" About to delete:");
        System.out.println("   " + expense);
        System.out.println(" Are you sure? (yes/no)");
        System.out.print(" > ");
        String response = scanner.nextLine().trim();
        System.out.println(LINE);
        return response.equalsIgnoreCase("yes");
    }
    // @@author

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
     * Displays a success message after an expense is edited.
     *
     * @param index   the 1-based index of the edited expense
     * @param old     the original expense before editing
     * @param updated the updated expense after editing
     */
    // @@author AbhijitBalajee
    public void showEditSuccess(int index, Expense old, Expense updated) {
        assert old != null : "Old expense should not be null";
        assert updated != null : "Updated expense should not be null";
        System.out.println(LINE);
        System.out.println(" Expense #" + index + " updated:");
        System.out.println("   Before: " + old);
        System.out.println("   After:  " + updated);
        System.out.println(LINE);
    }
    // @@author

    /**
     * Displays all expenses in a formatted table.
     *
     * @param expenses the list of expenses to display
     */
    // @@author AbhijitBalajee
    public void showExpenseList(ExpenseList expenses) {
        assert expenses != null : "ExpenseList passed to showExpenseList should not be null";

        System.out.println(LINE);
        System.out.println(" Your Expenses");
        System.out.println(LINE);

        if (expenses.size() == 0) {
            System.out.println(" No expenses recorded yet.");
            // @@author Ariff1422
            System.out.println(" Hint: Use 'add d/DESCRIPTION a/AMOUNT c/CATEGORY' to add one.");
            // @@author
            System.out.println(LINE);
            return;
        }

        int catWidth = "Category".length();
        int descWidth = "Description".length();

        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.getExpense(i);
            String category = (e.getCategory() == null || e.getCategory().isBlank())
                    ? "Uncategorised" : e.getCategory();
            String description = (e.getDescription() == null || e.getDescription().isBlank())
                    ? "(no description)" : e.getDescription();
            String recurringTag = e.isRecurring() ? " [R]" : "";

            catWidth = Math.max(catWidth, category.length() + 2);
            descWidth = Math.max(descWidth, description.length() + recurringTag.length());
        }

        String headerFormat = "  %-3s  %-" + catWidth + "s  %-" + descWidth + "s  %-12s  %s%n";
        String rowFormat    = "  %-3s  %-" + catWidth + "s  %-" + descWidth + "s  %-12s  $%.2f%n";

        System.out.printf(headerFormat, "#", "Category", "Description", "Date", "Amount");
        System.out.printf("  %-3s  %-" + catWidth + "s  %-" + descWidth + "s  %-12s  %s%n",
                "---",
                "-".repeat(catWidth),
                "-".repeat(descWidth),
                "----------",
                "--------");

        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.getExpense(i);
            assert e != null : "Expense at index " + i + " should not be null";

            String category = (e.getCategory() == null || e.getCategory().isBlank())
                    ? "Uncategorised" : e.getCategory();
            String description = (e.getDescription() == null || e.getDescription().isBlank())
                    ? "(no description)" : e.getDescription();
            String recurringTag = e.isRecurring() ? " [R]" : "";
            String date = (e.getDate() != null) ? e.getDate().toString() : "-";

            System.out.printf(rowFormat,
                    (i + 1) + ".",
                    "[" + category + "]",
                    description + recurringTag,
                    date,
                    e.getAmount());
        }

        System.out.println(LINE);
        System.out.println(" Total entries: " + expenses.size());
        System.out.println(LINE);
    }
    // @@author

    /**
     * Displays only recurring expenses.
     *
     * @param expenses the full expense list to filter from
     */
    // @@author AbhijitBalajee
    public void showRecurringList(ExpenseList expenses) {
        assert expenses != null : "ExpenseList should not be null";

        System.out.println(LINE);
        System.out.println(" Recurring Expenses");
        System.out.println(LINE);

        ArrayList<Expense> recurring = new ArrayList<>();
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.getExpense(i).isRecurring()) {
                recurring.add(expenses.getExpense(i));
            }
        }

        if (recurring.isEmpty()) {
            System.out.println(" No recurring expenses found.");
            System.out.println(LINE);
            System.out.println(" Total recurring: 0");
            System.out.println(LINE);
            return;
        }

        int catWidth = "Category".length();
        int descWidth = "Description".length();

        for (Expense e : recurring) {
            String category = (e.getCategory() == null || e.getCategory().isBlank())
                    ? "Uncategorised" : e.getCategory();
            String description = (e.getDescription() == null || e.getDescription().isBlank())
                    ? "(no description)" : e.getDescription();
            catWidth = Math.max(catWidth, category.length() + 2);
            descWidth = Math.max(descWidth, description.length() + " [R]".length());
        }

        String headerFormat = "  %-3s  %-" + catWidth + "s  %-" + descWidth + "s  %-12s  %s%n";
        String rowFormat    = "  %-3s  %-" + catWidth + "s  %-" + descWidth + "s  %-12s  $%.2f%n";

        System.out.printf(headerFormat, "#", "Category", "Description", "Date", "Amount");
        System.out.printf("  %-3s  %-" + catWidth + "s  %-" + descWidth + "s  %-12s  %s%n",
                "---",
                "-".repeat(catWidth),
                "-".repeat(descWidth),
                "----------",
                "--------");

        for (int i = 0; i < recurring.size(); i++) {
            Expense e = recurring.get(i);
            String category = (e.getCategory() == null || e.getCategory().isBlank())
                    ? "Uncategorised" : e.getCategory();
            String description = (e.getDescription() == null || e.getDescription().isBlank())
                    ? "(no description)" : e.getDescription();
            String date = (e.getDate() != null) ? e.getDate().toString() : "-";

            System.out.printf(rowFormat,
                    (i + 1) + ".",
                    "[" + category + "]",
                    description + " [R]",
                    date,
                    e.getAmount());
        }

        System.out.println(LINE);
        System.out.println(" Total recurring: " + recurring.size());
        System.out.println(LINE);
    }
    // @@author

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
    // @@author AbhijitBalajee
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
     * Displays confirmation after budget is reset.
     */
    public void showBudgetReset() {
        System.out.println(LINE);
        System.out.println(" Budget has been reset successfully.");
        System.out.println(" No budget is currently set.");
        System.out.println(LINE);
    }

    /**
     * Displays the budget history in reverse chronological order.
     *
     * @param history the list of budget history entries as "date|amount" strings
     */
    public void showBudgetHistory(ArrayList<String> history) {
        assert history != null : "Budget history list should not be null";

        System.out.println(LINE);
        System.out.println(" Budget History");
        System.out.println(LINE);

        if (history.isEmpty()) {
            System.out.println(" No budget history recorded.");
            System.out.println(LINE);
            return;
        }

        for (int i = history.size() - 1; i >= 0; i--) {
            String entry = history.get(i);
            String[] parts = entry.split("\\|");
            if (parts.length == 2) {
                try {
                    double amount = Double.parseDouble(parts[1]);
                    if (amount < 0) {
                        logger.warning("Skipping invalid budget history entry: " + entry);
                        continue;
                    }
                    if (amount == 0.0) {
                        System.out.printf(" %s : RESET ($0.00)%n", parts[0]);
                    } else {
                        System.out.printf(" %s : $%.2f%n", parts[0], amount);
                    }
                } catch (NumberFormatException e) {
                    logger.warning("Malformed budget history entry skipped: " + entry);
                }
            }
        }

        System.out.println(LINE);
    }
    // @@author

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
        System.out.println(" Available Commands");
        System.out.println(LINE);
        String format = "  %-48s -- %s";
        System.out.println(String.format(format,
                "add (a) d/DESC a/AMT c/CAT [date/DATE] [recurring/true]", "add expense"));
        System.out.println(String.format(format,
                "delete (d) INDEX", "delete expense"));
        System.out.println(String.format(format,
                "list (l)", "list all"));
        System.out.println(String.format(format,
                "list recurring", "list recurring only"));
        System.out.println(String.format(format,
                "summary (s)", "category breakdown"));
        System.out.println(String.format(format,
                "total", "show total"));
        System.out.println(String.format(format,
                "budget (b) AMOUNT", "set budget"));
        System.out.println(String.format(format,
                "budget reset", "reset budget"));
        System.out.println(String.format(format,
                "budget history", "view budget history"));
        System.out.println(String.format(format,
                "remaining", "show remaining"));
        System.out.println(String.format(format,
                "edit INDEX [d/DESC] [a/AMT] [c/CAT] [date/DATE] [recurring/true|false]",
                "edit expense"));
        System.out.println(String.format(format,
                "filter from/DATE to/DATE", "filter by date"));
        System.out.println(String.format(format,
                "find INDEX", "view expense details"));
        System.out.println(String.format(format,
                "search KEYWORD", "search by keyword"));
        System.out.println(String.format(format,
                "sort", "sort by amount"));
        System.out.println(String.format(format,
                "top N", "top N expenses"));
        System.out.println(String.format(format,
                "last N", "last N added"));
        System.out.println(String.format(format,
                "report YYYY-MM", "monthly report"));
        System.out.println(String.format(format,
                "month YYYY-MM", "list by month"));
        System.out.println(String.format(format,
                "clear", "clear all"));
        System.out.println(String.format(format,
                "undo", "undo last"));
        System.out.println(String.format(format,
                "export csv", "export to CSV"));
        System.out.println(String.format(format,
                "goal g/AMOUNT", "set savings goal"));
        System.out.println(String.format(format,
                "goal status", "check goal"));
        System.out.println(String.format(format,
                "help (h)", "show this help"));
        System.out.println(String.format(format,
                "bye", "exit"));
        System.out.println(LINE);
    }

    /**
     * Displays a spending summary grouped by category.
     *
     * @param sortedCategories the categories sorted by total descending
     * @param grandTotal the total of all expenses
     */
    // @@author AfshalG
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

    public void showEnhancedSummary(ArrayList<Map.Entry<String, Double>> sortedCategories,
            Map<String, Integer> categoryCounts,
            Map<String, Double> categoryMax,
            double grandTotal) {
        System.out.println(LINE);
        System.out.println(" ===== Spending Summary =====");

        for (Map.Entry<String, Double> entry : sortedCategories) {
            String category = entry.getKey();
            double amount = entry.getValue();
            int count = categoryCounts.getOrDefault(category, 0);
            double max = categoryMax.getOrDefault(category, 0.0);
            double avg = (count > 0) ? amount / count : 0.0;
            int percentage = (int) Math.round(amount / grandTotal * 100);
            String txnLabel = (count == 1) ? "txn" : "txns";
            System.out.printf(" %-16s: $%-8.2f (%3d%%)  | %d %s  | avg $%.2f  | max $%.2f%n",
                    category, amount, percentage, count, txnLabel, avg, max);
        }

        System.out.println(" ----------------------------");
        System.out.printf(" %-16s: $%.2f%n", "Total", grandTotal);
        System.out.println(LINE);
    }
    // @@author

    // @@author Ariff1422
    /**
     * Displays full details of a single expense.
     *
     * @param index the 1-based index of the expense
     * @param expense the expense to display
     */
    public void showExpenseDetail(int index, Expense expense) {
        assert expense != null : "Expense passed to showExpenseDetail should not be null";
        System.out.println(LINE);
        System.out.println(" Expense #" + index + " Details");
        System.out.println(LINE);
        System.out.printf(" Description : %s%n", expense.getDescription());
        System.out.printf(" Amount      : $%.2f%n", expense.getAmount());
        System.out.printf(" Category    : %s%n", expense.getCategory());
        System.out.printf(" Date        : %s%n", expense.getDate());
        // @@author Ariff1422
        System.out.printf(" Recurring   : %s%n", expense.isRecurring() ? "Yes" : "No");
        // @@author
        System.out.println(LINE);
    }
    // @@author

    /**
     * Displays expenses filtered by date range.
     *
     * @param filtered the list of matching expenses
     * @param from the start date of the filter range
     * @param to the end date of the filter range
     */
    public void showFilteredExpenses(ArrayList<Expense> filtered, LocalDate from, LocalDate to) {
        showFilteredExpenses(filtered, from, to, null);
    }

    // @@author Ariff1422
    /**
     * Displays expenses filtered by date range and optional category.
     *
     * @param filtered the list of matching expenses
     * @param from     the start date of the filter range
     * @param to       the end date of the filter range
     * @param category the category filter applied, or null if none
     */
    public void showFilteredExpenses(ArrayList<Expense> filtered, LocalDate from, LocalDate to, String category) {
        assert filtered != null : "Filtered list should not be null";

        System.out.println(LINE);
        if (category != null) {
            System.out.println(" Expenses from " + from + " to " + to + " [" + category + "]");
        } else {
            System.out.println(" Expenses from " + from + " to " + to);
        }
        System.out.println(LINE);

        if (filtered.isEmpty()) {
            System.out.println(" No expenses found in the given date range"
                    + (category != null ? " for category '" + category + "'" : "") + ".");
            System.out.println(" Hint: Use 'filter from/YYYY-MM-DD to/YYYY-MM-DD [cat/CATEGORY]'"
                    + " to adjust the range.");
            System.out.println(LINE);
            return;
        }

        int catWidth = "Category".length();
        int descWidth = "Description".length();

        for (Expense e : filtered) {
            String expCat = (e.getCategory() == null || e.getCategory().isBlank())
                    ? "Uncategorised" : e.getCategory();
            String expDesc = (e.getDescription() == null || e.getDescription().isBlank())
                    ? "(no description)" : e.getDescription();
            // @@author AbhijitBalajee
            String recurringTag = e.isRecurring() ? " [R]" : "";
            // @@author
            catWidth = Math.max(catWidth, expCat.length() + 2);
            descWidth = Math.max(descWidth, expDesc.length() + recurringTag.length());
        }

        String headerFormat = "  %-3s  %-" + catWidth + "s  %-" + descWidth + "s  %-12s  %s%n";
        String rowFormat    = "  %-3s  %-" + catWidth + "s  %-" + descWidth + "s  %-12s  $%.2f%n";

        System.out.printf(headerFormat, "#", "Category", "Description", "Date", "Amount");
        System.out.printf("  %-3s  %-" + catWidth + "s  %-" + descWidth + "s  %-12s  %s%n",
                "---",
                "-".repeat(catWidth),
                "-".repeat(descWidth),
                "----------",
                "--------");

        for (int i = 0; i < filtered.size(); i++) {
            Expense e = filtered.get(i);
            String expCat = (e.getCategory() == null || e.getCategory().isBlank())
                    ? "Uncategorised" : e.getCategory();
            String expDesc = (e.getDescription() == null || e.getDescription().isBlank())
                    ? "(no description)" : e.getDescription();
            // @@author AbhijitBalajee
            String recurringTag = e.isRecurring() ? " [R]" : "";
            // @@author
            String date = (e.getDate() != null) ? e.getDate().toString() : "-";

            System.out.printf(rowFormat,
                    (i + 1) + ".",
                    "[" + expCat + "]",
                    expDesc + recurringTag,
                    date,
                    e.getAmount());
        }

        System.out.println(LINE);
        System.out.println(" Total entries: " + filtered.size());
        System.out.println(LINE);
    }
    // @@author

    // @@author Ariff1422
    /**
     * Displays all expenses whose description contains the given keyword (case-insensitive).
     *
     * @param matches the list of matching expenses with their original 1-based indices
     * @param keyword the keyword that was searched
     */
    public void showExpensesByKeyword(ArrayList<int[]> matchIndices,
            ArrayList<Expense> matches, String keyword) {
        assert matches != null : "Matches list should not be null";
        assert keyword != null : "Keyword should not be null";

        System.out.println(LINE);
        System.out.println(" Search results for: \"" + keyword + "\"");
        System.out.println(LINE);

        if (matches.isEmpty()) {
            System.out.println(" No expenses found matching '" + keyword + "'.");
            System.out.println(" Hint: Use 'find d/<keyword>' to search by description.");
            System.out.println(LINE);
            return;
        }

        int catWidth = "Category".length();
        int descWidth = "Description".length();

        for (Expense e : matches) {
            String category = (e.getCategory() == null || e.getCategory().isBlank())
                    ? "Uncategorised" : e.getCategory();
            String description = (e.getDescription() == null || e.getDescription().isBlank())
                    ? "(no description)" : e.getDescription();
            String recurringTag = e.isRecurring() ? " [R]" : "";
            catWidth = Math.max(catWidth, category.length() + 2);
            descWidth = Math.max(descWidth, description.length() + recurringTag.length());
        }

        String headerFormat = "  %-3s  %-" + catWidth + "s  %-" + descWidth + "s  %-12s  %s%n";
        String rowFormat    = "  %-3s  %-" + catWidth + "s  %-" + descWidth + "s  %-12s  $%.2f%n";

        System.out.printf(headerFormat, "#", "Category", "Description", "Date", "Amount");
        System.out.printf("  %-3s  %-" + catWidth + "s  %-" + descWidth + "s  %-12s  %s%n",
                "---",
                "-".repeat(catWidth),
                "-".repeat(descWidth),
                "----------",
                "--------");

        for (int i = 0; i < matches.size(); i++) {
            Expense e = matches.get(i);
            int originalIndex = matchIndices.get(i)[0];
            String category = (e.getCategory() == null || e.getCategory().isBlank())
                    ? "Uncategorised" : e.getCategory();
            String description = (e.getDescription() == null || e.getDescription().isBlank())
                    ? "(no description)" : e.getDescription();
            String recurringTag = e.isRecurring() ? " [R]" : "";
            String date = (e.getDate() != null) ? e.getDate().toString() : "-";

            System.out.printf(rowFormat,
                    originalIndex + ".",
                    "[" + category + "]",
                    description + recurringTag,
                    date,
                    e.getAmount());
        }

        System.out.println(LINE);
        System.out.println(" Total entries: " + matches.size());
        System.out.println(LINE);
    }

    /**
     * Displays the most recently recorded expense as a startup reminder.
     *
     * @param expense the last expense in the list
     */
    public void showLastExpense(Expense expense) {
        assert expense != null : "Expense passed to showLastExpense should not be null";
        System.out.println(LINE);
        System.out.println(" Last recorded expense:");
        System.out.printf("   %s | $%.2f | %s | %s%n",
                expense.getDescription(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getDate());
        System.out.println(LINE);
    }
    // @@author

    // @@author pranavjana
    /**
     * Displays a general message to the user.
     *
     * @param message the message to display
     */
    public void showMessage(String message) {
        System.out.println(LINE);
        System.out.println(" " + message);
        System.out.println(LINE);
    }
    // @@author

    // @@author pranavjana
    /**
     * Displays a warning when spending is at or above 90% of the budget.
     *
     * @param totalSpent the current total spent
     * @param budget the monthly budget limit
     */
    public void showBudgetWarning(double totalSpent, double budget) {
        System.out.printf(" [WARNING] You are close to your monthly budget! ($%.2f / $%.2f used)%n",
                totalSpent, budget);
    }

    /**
     * Displays an alert when spending has exceeded the budget.
     *
     * @param totalSpent the current total spent
     * @param budget the monthly budget limit
     */
    public void showBudgetAlert(double totalSpent, double budget) {
        System.out.printf(" [ALERT] You have exceeded your monthly budget! ($%.2f spent, budget is $%.2f)%n",
                totalSpent, budget);
    }
    // @@author

    // @@author Ariff1422
    /**
     * Displays an error message with a hint where applicable.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println(LINE);
        System.out.println(" Error: " + message);
        if (message.contains("out of range")) {
            System.out.println(" Hint: Use 'list' to see valid indices.");
        } else if (message.contains("requires") || message.contains("Usage")) {
            System.out.println(" Hint: Type 'help' to see the correct command format.");
        }
        System.out.println(LINE);
    }
    // @@author
}
