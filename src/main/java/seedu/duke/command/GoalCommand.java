package seedu.duke.command;

import java.util.logging.Logger;

import seedu.duke.ExpenseList;
import seedu.duke.Ui;

// @@author pranavjana
/**
 * Handles savings goal commands: setting a goal and viewing goal status.
 */
public class GoalCommand extends Command {

    private static final Logger logger = Logger.getLogger(GoalCommand.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    private final boolean isStatusRequest;
    private final double goalAmount;

    /**
     * Constructs a GoalCommand to display the current goal status.
     */
    public GoalCommand() {
        this.isStatusRequest = true;
        this.goalAmount = 0.0;
    }

    /**
     * Constructs a GoalCommand to set a new savings goal.
     *
     * @param goalAmount the savings goal amount (must be positive)
     */
    public GoalCommand(double goalAmount) {
        assert goalAmount > 0 : "Goal amount must be positive";
        this.isStatusRequest = false;
        this.goalAmount = goalAmount;
    }

    /**
     * Executes the goal command: either sets the goal or displays status.
     *
     * @param expenses the expense list containing goal and spending data
     * @param ui the UI for displaying output
     */
    @Override
    public void execute(ExpenseList expenses, Ui ui) {
        assert expenses != null : "ExpenseList should not be null";
        assert ui != null : "Ui should not be null";

        if (isStatusRequest) {
            showGoalStatus(expenses);
        } else {
            setGoal(expenses);
        }
    }

    private void setGoal(ExpenseList expenses) {
        expenses.setGoal(goalAmount);
        System.out.println("____________________________________________________________");
        System.out.printf(" Savings goal set to: $%.2f%n", goalAmount);
        System.out.println("____________________________________________________________");
        logger.info("Savings goal set to: " + goalAmount);
    }

    private void showGoalStatus(ExpenseList expenses) {
        if (!expenses.hasGoal()) {
            System.out.println("____________________________________________________________");
            System.out.println(" No savings goal set.");
            System.out.println("____________________________________________________________");
            logger.info("Goal status requested but no goal set.");
            return;
        }

        double goal = expenses.getGoal();
        double spent = expenses.getTotal();
        double saved = goal - spent;

        System.out.println("____________________________________________________________");
        System.out.println(" ===== Savings Goal =====");
        System.out.printf(" Goal    : $%.2f%n", goal);
        System.out.printf(" Spent   : $%.2f%n", spent);

        if (saved >= 0) {
            int percentage = (int) Math.round((saved / goal) * 100);
            System.out.printf(" Saved   : $%.2f (%d%% of goal reached)%n", saved, percentage);
        } else {
            System.out.printf(" Goal not reached. Over by $%.2f.%n", Math.abs(saved));
        }

        System.out.println(" ========================");
        System.out.println("____________________________________________________________");
        logger.info(String.format("Goal status: goal=%.2f, spent=%.2f, saved=%.2f",
                goal, spent, saved));
    }

    @Override
    public boolean mutatesData() {
        return !isStatusRequest;
    }
}
