package seedu.duke;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;

/**
 * Manages the list of expenses.
 */
public class ExpenseList {
    private static final Logger logger = Logger.getLogger(ExpenseList.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    private ArrayList<Expense> expenses;
    private double budget;
    private ArrayList<String> budgetHistory;

    /**
     * Constructs an empty ExpenseList with no budget set.
     */
    public ExpenseList() {
        this.expenses = new ArrayList<>();
        this.budget = 0.0;
        this.budgetHistory = new ArrayList<>();
    }

    /**
     * Adds an expense to the list.
     *
     * @param expense the expense to add
     */
    public void addExpense(Expense expense) {
        assert expense != null : "Expense to add should not be null";
        expenses.add(expense);
        logger.info("Expense added: " + expense);
    }

    /**
     * Returns the expense at the given index.
     *
     * @param index the zero-based index
     * @return the expense at that index
     */
    public Expense getExpense(int index) {
        assert index >= 0 && index < expenses.size() : "Index out of bounds: " + index;
        return expenses.get(index);
    }

    /**
     * Replaces the expense at the given zero-based index.
     *
     * @param index   the zero-based index
     * @param expense the new expense to set
     */
    public void setExpense(int index, Expense expense) {
        assert index >= 0 && index < expenses.size() : "Set index out of bounds: " + index;
        assert expense != null : "Replacement expense should not be null";
        expenses.set(index, expense);
        logger.info("Expense at index " + index + " replaced: " + expense);
    }

    /**
     * Returns the number of expenses in the list.
     *
     * @return the size of the list
     */
    public int size() {
        return expenses.size();
    }

    /**
     * Removes and returns the expense at the given zero-based index.
     *
     * @param index the zero-based index
     * @return the removed expense
     */
    public Expense deleteExpense(int index) {
        assert index >= 0 && index < expenses.size() : "Delete index out of bounds: " + index;
        Expense removed = expenses.remove(index);
        logger.info("Expense deleted: " + removed);
        return removed;
    }

    /**
     * Returns the full list of expenses.
     *
     * @return the list of expenses
     */
    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Returns the total sum of all expense amounts.
     *
     * @return the total amount
     */
    public double getTotal() {
        assert expenses != null : "Internal expense list should not be null";

        double total = 0;
        for (Expense expense : expenses) {
            assert expense != null : "Expense entries should not be null";
            total += expense.getAmount();
        }
        logger.log(Level.FINE, "Computed total {0} from {1} expenses", new Object[]{total, expenses.size()});
        return total;
    }

    /**
     * Sets the monthly budget limit.
     *
     * @param budget the budget amount to set
     */
    public void setBudget(double budget) {
        assert budget > 0 : "Budget must be positive";
        this.budget = budget;
        this.budgetHistory.add(LocalDate.now() + "|" + budget);
        logger.info("Budget set to: " + budget);
    }

    /**
     * Returns the current monthly budget.
     *
     * @return the budget amount, or 0.0 if not set
     */
    public double getBudget() {
        return budget;
    }

    /**
     * Returns true if a budget has been set.
     *
     * @return true if budget is greater than 0
     */
    public boolean hasBudget() {
        return budget > 0.0;
    }

    /**
     * Resets the monthly budget to zero.
     */
    public void resetBudget() {
        this.budget = 0.0;
        logger.info("Budget reset to 0.0");
    }
  
    /**
     * Returns the budget history list.
     *
     * @return list of budget history entries as "date|amount" strings
     */
    public ArrayList<String> getBudgetHistory() {
        return budgetHistory;
    }

    /**
     * Adds a raw budget history entry directly (used by Storage when loading).
     *
     * @param entry the history entry string in format "date|amount"
     */
    public void addBudgetHistory(String entry) {
        assert entry != null && !entry.isBlank() : "Budget history entry should not be null or blank";
        budgetHistory.add(entry);
        logger.info("Budget history entry added: " + entry);
    }

    /**
     * Sets the budget directly without recording history (used by Storage on load).
     *
     * @param budget the budget amount to restore
     */
    public void setBudgetDirectly(double budget) {
        assert budget > 0 : "Budget must be positive";
        this.budget = budget;
        logger.info("Budget restored to: " + budget);
    }
}
