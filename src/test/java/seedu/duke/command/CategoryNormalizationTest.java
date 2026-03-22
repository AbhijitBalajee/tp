package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.ExpenseList;
import seedu.duke.Parser;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryNormalizationTest {

    private ExpenseList expenses;
    private Ui ui;

    @BeforeEach
    void setUp() {
        expenses = new ExpenseList();
        ui = new Ui();
    }

    @Test
    void parser_lowercaseCategory_capitalizedOnAdd() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/food");
        cmd.execute(expenses, ui);
        assertEquals("Food", expenses.getExpense(0).getCategory());
    }

    @Test
    void parser_uppercaseCategory_capitalizedOnAdd() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/FOOD");
        cmd.execute(expenses, ui);
        assertEquals("Food", expenses.getExpense(0).getCategory());
    }

    @Test
    void parser_mixedCaseCategory_capitalizedOnAdd() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/fOoD");
        cmd.execute(expenses, ui);
        assertEquals("Food", expenses.getExpense(0).getCategory());
    }

    @Test
    void parser_multiWordCategory_eachWordCapitalized() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Grab a/15.00 c/public transport");
        cmd.execute(expenses, ui);
        assertEquals("Public Transport", expenses.getExpense(0).getCategory());
    }

    @Test
    void parser_extraSpacesInCategory_trimmedAndCapitalized() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/  food  ");
        cmd.execute(expenses, ui);
        assertEquals("Food", expenses.getExpense(0).getCategory());
    }

    @Test
    void parser_alreadyCapitalized_unchanged() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50 c/Food");
        cmd.execute(expenses, ui);
        assertEquals("Food", expenses.getExpense(0).getCategory());
    }

    @Test
    void parser_noCategory_defaultUncategorised() throws SpendTrackException {
        Command cmd = Parser.parse("add d/Coffee a/3.50");
        cmd.execute(expenses, ui);
        assertEquals("Uncategorised", expenses.getExpense(0).getCategory());
    }
}
