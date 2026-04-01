// @@author Ariff1422
package seedu.duke;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Handles saving and loading of expenses and budget to/from a file.
 */
public class Storage {

    private static final Logger logger = Logger.getLogger(Storage.class.getName());
    private static final String EXPENSES_MARKER = "---EXPENSES---";
    private static final String BUDGET_MARKER = "---BUDGET---";
    private static final String BUDGET_HISTORY_MARKER = "---BUDGET-HISTORY---";
    private static final String GOAL_MARKER = "---GOAL---";

    static {
        logger.setUseParentHandlers(false);
    }

    private final String filePath;

    /**
     * Constructs a Storage instance for the given file path.
     *
     * @param filePath the path to the save file
     */
    public Storage(String filePath) {
        assert filePath != null && !filePath.isBlank() : "File path should not be null or blank";
        this.filePath = filePath;
    }

    /**
     * Saves all expenses and budget to disk.
     * Creates the data directory if it does not exist.
     * Prints a warning and continues if the write fails.
     *
     * @param expenses the expense list to save
     */
    public void save(ExpenseList expenses) {
        assert expenses != null : "ExpenseList to save should not be null";

        File file = new File(filePath);
        file.getParentFile().mkdirs();

        try (FileWriter fw = new FileWriter(file)) {
            fw.write(EXPENSES_MARKER + "\n");
            for (int i = 0; i < expenses.size(); i++) {
                Expense e = expenses.getExpense(i);
                fw.write(e.getDescription() + "|"
                        + e.getAmount() + "|"
                        + e.getCategory() + "|"
                        + e.getDate() + "\n");
            }
            fw.write(BUDGET_MARKER + "\n");
            fw.write(expenses.getBudget() + "\n");
            fw.write(BUDGET_HISTORY_MARKER + "\n");
            for (String entry : expenses.getBudgetHistory()) {
                fw.write(entry + "\n");
            }
            // @@author pranavjana
            fw.write(GOAL_MARKER + "\n");
            fw.write(expenses.getGoal() + "\n");
            // @@author
            logger.info("Saved " + expenses.size() + " expenses to " + filePath);
        } catch (IOException e) {
            System.out.println("Warning: could not save data. " + e.getMessage());
            logger.warning("Save failed: " + e.getMessage());
        }
    }

    /**
     * Loads expenses and budget from disk into the given expense list.
     * Missing file starts with an empty list silently.
     * Malformed lines are skipped with a warning.
     *
     * @param expenses the expense list to populate
     */
    public void load(ExpenseList expenses) {
        assert expenses != null : "ExpenseList to load into should not be null";

        File file = new File(filePath);
        if (!file.exists()) {
            logger.info("No save file found at " + filePath + ". Starting fresh.");
            return;
        }

        try (Scanner sc = new Scanner(file)) {
            boolean readingBudget = false;
            boolean readingHistory = false;
            boolean readingGoal = false;
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.equals(EXPENSES_MARKER)) {
                    readingBudget = false;
                    readingHistory = false;
                    readingGoal = false;
                    continue;
                }
                if (line.equals(BUDGET_MARKER)) {
                    readingBudget = true;
                    readingHistory = false;
                    readingGoal = false;
                    continue;
                }
                if (line.equals(BUDGET_HISTORY_MARKER)) {
                    readingBudget = false;
                    readingHistory = true;
                    readingGoal = false;
                    continue;
                }
                // @@author pranavjana
                if (line.equals(GOAL_MARKER)) {
                    readingBudget = false;
                    readingHistory = false;
                    readingGoal = true;
                    continue;
                }
                if (readingGoal) {
                    try {
                        double goal = Double.parseDouble(line);
                        if (goal > 0) {
                            expenses.setGoal(goal);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Warning: could not parse goal value: " + line);
                    }
                    readingGoal = false;
                    continue;
                }
                // @@author
                if (readingBudget) {
                    try {
                        double budget = Double.parseDouble(line);
                        if (budget > 0) {
                            expenses.setBudgetDirectly(budget);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Warning: could not parse budget value: " + line);
                    }
                    readingBudget = false;
                    continue;
                }
                if (readingHistory) {
                    if (!line.isBlank()) {
                        expenses.addBudgetHistory(line);
                    }
                    continue;
                }
                parseLine(line, expenses);
            }
            logger.info("Loaded " + expenses.size() + " expenses from " + filePath);
        } catch (IOException e) {
            System.out.println("Warning: could not load data. " + e.getMessage());
            logger.warning("Load failed: " + e.getMessage());
        }
    }

    private void parseLine(String line, ExpenseList expenses) {
        String[] parts = line.split("\\|");
        if (parts.length != 4) {
            System.out.println("Warning: skipping malformed line: " + line);
            return;
        }
        try {
            String description = parts[0];
            double amount = Double.parseDouble(parts[1]);
            String category = parts[2];
            LocalDate date = LocalDate.parse(parts[3]);
            expenses.addExpense(new Expense(description, amount, category, date));
        } catch (Exception e) {
            System.out.println("Warning: skipping malformed line: " + line);
            logger.warning("Failed to parse line: " + line);
        }
    }
}
// @@author
