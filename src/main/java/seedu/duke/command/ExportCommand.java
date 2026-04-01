package seedu.duke.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Ui;

/**
 * Exports all expenses to a CSV file at data/spendtrack_export.csv.
 */
public class ExportCommand extends Command {

    private static final Logger logger = Logger.getLogger(ExportCommand.class.getName());
    private static final String EXPORT_DIR = "data";
    private static final String EXPORT_FILE_PATH = "data/spendtrack_export.csv";
    private static final String CSV_HEADER = "Description,Amount,Category,Date,Recurring";

    static {
        logger.setUseParentHandlers(false);
    }

    private final String filePath;

    /**
     * Constructs an ExportCommand with the default export file path.
     */
    public ExportCommand() {
        this.filePath = EXPORT_FILE_PATH;
    }

    /**
     * Constructs an ExportCommand with a custom file path (for testing).
     *
     * @param filePath the file path to export to
     */
    public ExportCommand(String filePath) {
        assert filePath != null && !filePath.isBlank() : "File path should not be null or blank";
        this.filePath = filePath;
    }

    /**
     * Executes the export command by writing all expenses to a CSV file.
     *
     * @param expenses the expense list to export
     * @param ui the UI for displaying output
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        if (expenses.size() == 0) {
            System.out.println("____________________________________________________________");
            System.out.println(" No expenses to export.");
            System.out.println("____________________________________________________________");
            logger.info("Export attempted with empty list.");
            return;
        }

        ensureDirectoryExists();

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(CSV_HEADER + System.lineSeparator());

            ArrayList<Expense> list = expenses.getExpenses();
            for (Expense expense : list) {
                writer.write(formatCsvRow(expense) + System.lineSeparator());
            }

            System.out.println("____________________________________________________________");
            System.out.println(" Expenses exported to " + filePath);
            System.out.println("____________________________________________________________");
            logger.info("Exported " + list.size() + " expenses to " + filePath);
        } catch (IOException e) {
            System.out.println("____________________________________________________________");
            System.out.println(" Error: Could not export expenses. " + e.getMessage());
            System.out.println("____________________________________________________________");
            logger.warning("Export failed: " + e.getMessage());
        }
    }

    private void ensureDirectoryExists() {
        File dir = new File(EXPORT_DIR);
        if (!dir.exists()) {
            boolean isCreated = dir.mkdirs();
            if (isCreated) {
                logger.info("Created export directory: " + EXPORT_DIR);
            }
        }
    }

    /**
     * Formats a single expense as a CSV row.
     * Descriptions containing commas are wrapped in double quotes.
     *
     * @param expense the expense to format
     * @return the CSV-formatted row
     */
    static String formatCsvRow(Expense expense) {
        assert expense != null : "Expense should not be null when formatting CSV row";

        String description = expense.getDescription();
        if (description.contains(",") || description.contains("\"")) {
            description = "\"" + description.replace("\"", "\"\"") + "\"";
        }

        return description + ","
                + String.format("%.2f", expense.getAmount()) + ","
                + expense.getCategory() + ","
                + expense.getDate() + ","
                + expense.isRecurring();
    }
}
