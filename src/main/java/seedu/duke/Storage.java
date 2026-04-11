// @@author Ariff1422
package seedu.duke;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.zip.CRC32;

/**
 * Handles saving and loading of expenses and budget to/from a plain-text file.
 * Each expense line includes a CRC32 checksum so that manually tampered lines
 * are detected and skipped individually, while the rest of the data loads normally.
 * The file remains human-readable and human-editable as required by course constraints.
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
     * Computes a CRC32 checksum of the given string.
     *
     * @param data the string to checksum
     * @return hex string of the CRC32 value
     */
    private String checksum(String data) {
        CRC32 crc = new CRC32();
        crc.update(data.getBytes(StandardCharsets.UTF_8));
        return Long.toHexString(crc.getValue());
    }

    /**
     * Saves all expenses and budget to disk as a plain-text pipe-delimited file.
     * Each expense line has a CRC32 checksum appended as the last field.
     * Creates the data directory if it does not exist.
     *
     * @param expenses the expense list to save
     */
    public void save(ExpenseList expenses) {
        assert expenses != null : "ExpenseList to save should not be null";

        File file = new File(filePath);
        file.getParentFile().mkdirs();

        StringBuilder sb = new StringBuilder();
        sb.append(EXPENSES_MARKER).append("\n");
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.getExpense(i);
            String data = e.getDescription() + "|"
                    + e.getAmount() + "|"
                    + e.getCategory() + "|"
                    + e.getDate() + "|"
                    + e.isRecurring();
            sb.append(data).append("|").append(checksum(data)).append("\n");
        }
        sb.append(BUDGET_MARKER).append("\n");
        sb.append(expenses.getBudget()).append("\n");
        sb.append(BUDGET_HISTORY_MARKER).append("\n");
        for (String entry : expenses.getBudgetHistory()) {
            sb.append(entry).append("\n");
        }
        // @@author pranavjana
        sb.append(GOAL_MARKER).append("\n");
        sb.append(expenses.getGoal()).append("\n");
        // @@author Ariff1422

        try (FileWriter fw = new FileWriter(file)) {
            fw.write(sb.toString());
            logger.info("Saved " + expenses.size() + " expenses to " + filePath);
        } catch (IOException e) {
            System.out.println("Warning: could not save data. " + e.getMessage());
            logger.warning("Save failed: " + e.getMessage());
        }
    }

    /**
     * Loads expenses and budget from the plain-text file into the given expense list.
     * If the file is missing, starts fresh silently.
     * Lines with invalid checksums are skipped with a warning; all other lines load normally.
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

        try (Scanner sc = new Scanner(file, StandardCharsets.UTF_8)) {
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
                // @@author Ariff1422
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
        // @@author Ariff1422
        if (parts.length != 6) {
            System.out.println("Warning: skipping malformed line: " + line);
            return;
        }
        try {
            String description = parts[0];
            double amount = Double.parseDouble(parts[1]);
            String category = parts[2];
            LocalDate date = LocalDate.parse(parts[3]);
            boolean recurring = Boolean.parseBoolean(parts[4]);

            String data = parts[0] + "|" + parts[1] + "|" + parts[2] + "|" + parts[3] + "|" + parts[4];
            String expected = checksum(data);
            if (!expected.equals(parts[5])) {
                System.out.println("Warning: skipping tampered line (checksum mismatch): " + description);
                logger.warning("Checksum mismatch for line: " + line);
                return;
            }

            if (!validateExpense(description, amount, date)) {
                return;
            }
            // @@author

            expenses.addExpense(new Expense(description, amount, category, date, recurring));
        } catch (Exception e) {
            System.out.println("Warning: skipping malformed line: " + line);
            logger.warning("Failed to parse line: " + line);
        }
    }

    // @@author Ariff1422
    /**
     * Validates an expense's fields after parsing from the save file.
     * Warns and returns false if any field is invalid, so the line is skipped.
     *
     * @param description the expense description
     * @param amount      the expense amount
     * @param date        the expense date
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateExpense(String description, double amount, LocalDate date) {
        if (description == null || description.isBlank()) {
            System.out.println("Warning: skipping expense with blank description.");
            logger.warning("Skipping expense with blank description.");
            return false;
        }
        if (amount <= 0) {
            System.out.println("Warning: skipping expense with non-positive amount: " + amount);
            logger.warning("Skipping expense with non-positive amount: " + amount);
            return false;
        }
        if (amount > 1000000) {
            System.out.println("Warning: skipping expense with unrealistic amount: " + amount);
            logger.warning("Skipping expense with unrealistic amount: " + amount);
            return false;
        }
        if (date != null && date.getYear() < 2000) {
            System.out.println("Warning: skipping expense with implausible date: " + date);
            logger.warning("Skipping expense with implausible date: " + date);
            return false;
        }
        return true;
    }
    // @@author
}
// @@author
