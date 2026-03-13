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
