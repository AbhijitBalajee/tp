package seedu.duke;

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
        System.out.println(" Type 'add d/<desc> a/<amount> c/<category>' to add an expense.");
        System.out.println(" Type 'list' to view all expenses.");
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

        System.out.printf("  %-3s  %-14s %-24s %s%n", "#", "Category", "Description", "Amount");
        System.out.println(" ---  -------------  --------------------  --------");

        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.getExpense(i);
            assert e != null : "Expense at index " + i + " should not be null";

            String category = (e.getCategory() == null || e.getCategory().isBlank())
                    ? "Uncategorised" : e.getCategory();
            String description = (e.getDescription() == null || e.getDescription().isBlank())
                    ? "(no description)" : e.getDescription();

            System.out.printf("  %-3s  %-14s %-24s $%.2f%n",
                    (i + 1) + ".",
                    "[" + category + "]",
                    description,
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
        System.out.println(LINE);
        System.out.printf(" Total expenses: $%.2f%n", total);
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
