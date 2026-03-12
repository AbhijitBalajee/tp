package seedu.duke;

import java.util.ArrayList;

/**
 * Manages the list of expenses.
 */
public class ExpenseList {

    private ArrayList<Expense> expenses;

    /**
     * Constructs an empty ExpenseList.
     */
    public ExpenseList() {
        this.expenses = new ArrayList<>();
    }

    /**
     * Adds an expense to the list.
     *
     * @param expense the expense to add
     */
    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    /**
     * Returns the expense at the given index.
     *
     * @param index the zero-based index
     * @return the expense at that index
     */
    public Expense getExpense(int index) {
        return expenses.get(index);
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
     * Returns the full list of expenses.
     *
     * @return the list of expenses
     */
    public ArrayList<Expense> getExpenses() {
        return expenses;
    }
}
