package seedu.duke;

import java.time.LocalDate;

/**
 * Represents an expense entry with a description, amount, category, and date.
 */
public class Expense {

    private String description;
    private double amount;
    private String category;
    private LocalDate date;
    private boolean isRecurring;

    /**
     * Constructs an Expense with the given details. Recurring defaults to false.
     *
     * @param description the description of the expense
     * @param amount the amount spent
     * @param category the category of the expense
     * @param date the date of the expense
     */
    public Expense(String description, double amount, String category, LocalDate date) {
        assert description != null : "Description should not be null";
        assert amount > 0 : "Amount must be greater than 0";
        assert category != null : "Category should not be null";
        assert date != null : "Date should not be null";
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.isRecurring = false;
    }

    /**
     * Constructs an Expense with all fields including recurring flag.
     *
     * @param description the description of the expense
     * @param amount the amount spent
     * @param category the category of the expense
     * @param date the date of the expense
     * @param isRecurring whether the expense is recurring
     */
    public Expense(String description, double amount, String category,
                   LocalDate date, boolean isRecurring) {
        assert description != null : "Description should not be null";
        assert amount > 0 : "Amount must be greater than 0";
        assert category != null : "Category should not be null";
        assert date != null : "Date should not be null";
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.isRecurring = isRecurring;
    }

    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }
    public boolean isRecurring() { return isRecurring; }

    public void setDescription(String description) { this.description = description; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setRecurring(boolean isRecurring) { this.isRecurring = isRecurring; }

    /**
     * Returns a formatted string representation of this expense.
     *
     * @return formatted expense string
     */
    @Override
    public String toString() {
        String recurringTag = isRecurring ? " [R]" : "";
        return String.format("[%s] %s - $%.2f (%s)%s",
                category, description, amount, date, recurringTag);
    }
}
