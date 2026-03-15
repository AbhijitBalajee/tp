package seedu.duke;

/**
 * Represents an expense entry with a description, amount, and category.
 */
public class Expense {

    private String description;
    private double amount;
    private String category;

    /**
     * Constructs an Expense with the given description, amount, and category.
     *
     * @param description the description of the expense
     * @param amount the amount spent
     * @param category the category of the expense
     */
    public Expense(String description, double amount, String category) {
        this.description = description;
        this.amount = amount;
        this.category = category;
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
     * Returns a formatted string representation of this expense.
     *
     * @return formatted expense string
     */
    @Override
    public String toString() {
        return String.format("[%s] %s - $%.2f", category, description, amount);
    }
}
