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

    /**
     * Constructs an Expense with the given description, amount, category, and date.
     *
     * @param description the description of the expense
     * @param amount the amount spent
     * @param category the category of the expense
     * @param date the date of the expense
     */
    public Expense(String description, double amount, String category, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    /**
     * Returns the description of this expense.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the amount of this expense.
     *
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the category of this expense.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the date of this expense.
     *
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns a formatted string representation of this expense.
     *
     * @return formatted expense string
     */
    @Override
    public String toString() {
        return String.format("[%s] %s - $%.2f (%s)", category, description, amount, date);
    }
}
