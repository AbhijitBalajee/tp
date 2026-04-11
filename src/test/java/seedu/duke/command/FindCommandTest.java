package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.Expense;
import seedu.duke.ExpenseList;
import seedu.duke.Parser;
import seedu.duke.SpendTrackException;
import seedu.duke.Ui;
import seedu.duke.command.FindByKeywordCommand;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// @@author Ariff1422
class FindCommandTest {

    private static final LocalDate DATE = LocalDate.of(2026, 3, 22);

    private ExpenseList expenses;
    private Ui ui;

    @BeforeEach
    void setUp() {
        expenses = new ExpenseList();
        ui = new Ui();
        expenses.addExpense(new Expense("Coffee", 4.50, "Food", DATE));
        expenses.addExpense(new Expense("Bus", 1.80, "Transport", DATE));
        expenses.addExpense(new Expense("Textbook", 35.00, "Education", DATE));
    }

    // --- Valid indices ---

    @Test
    void execute_firstIndex_doesNotThrow() {
        assertDoesNotThrow(() -> new FindCommand(1).execute(expenses, ui));
    }

    @Test
    void execute_lastIndex_doesNotThrow() {
        assertDoesNotThrow(() -> new FindCommand(3).execute(expenses, ui));
    }

    @Test
    void execute_middleIndex_doesNotThrow() {
        assertDoesNotThrow(() -> new FindCommand(2).execute(expenses, ui));
    }

    // --- Out-of-range indices ---

    @Test
    void execute_indexZero_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> new FindCommand(0).execute(expenses, ui));
    }

    @Test
    void execute_indexTooLarge_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> new FindCommand(99).execute(expenses, ui));
    }

    @Test
    void execute_negativeIndex_throwsException() {
        assertThrows(SpendTrackException.class,
                () -> new FindCommand(-1).execute(expenses, ui));
    }

    // --- Empty list ---

    @Test
    void execute_emptyList_throwsException() {
        ExpenseList empty = new ExpenseList();
        assertThrows(SpendTrackException.class,
                () -> new FindCommand(1).execute(empty, ui));
    }

    // --- Does not mutate ---

    @Test
    void execute_doesNotMutateList() throws SpendTrackException {
        new FindCommand(2).execute(expenses, ui);
        assertTrue(expenses.size() == 3);
    }

    // --- isExit / mutatesData ---

    @Test
    void isExit_returnsFalse() {
        assertTrue(!new FindCommand(1).isExit());
    }

    @Test
    void mutatesData_returnsFalse() {
        assertTrue(!new FindCommand(1).mutatesData());
    }

    // --- Parser integration ---

    @Test
    void parser_validIndex_returnsFindCommand() throws SpendTrackException {
        Command cmd = Parser.parse("find 2");
        assertTrue(cmd instanceof FindCommand);
    }

    @Test
    void parser_nonNumericIndex_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("find abc"));
    }

    @Test
    void parser_missingIndex_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("find"));
    }

    @Test
    void parser_floatIndex_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("find 1.5"));
    }

    // @@author Ariff1422

    // --- Reject garbage after index ---

    @Test
    void parser_indexWithTrailingGarbage_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("find 1 extra"));
    }

    @Test
    void parser_indexWithTwoTokens_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("find 1 2"));
    }

    // --- find d/KEYWORD mode ---

    @Test
    void parser_keywordMode_returnsFindByKeywordCommand() throws SpendTrackException {
        Command cmd = Parser.parse("find d/coffee");
        assertInstanceOf(FindByKeywordCommand.class, cmd);
    }

    @Test
    void parser_emptyKeyword_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("find d/"));
    }

    @Test
    void parser_pipeInKeyword_throwsException() {
        assertThrows(SpendTrackException.class, () -> Parser.parse("find d/coffee|hack"));
    }

    @Test
    void execute_keyword_matchesDescription() throws SpendTrackException {
        FindByKeywordCommand cmd = new FindByKeywordCommand("coffee");
        assertDoesNotThrow(() -> cmd.execute(expenses, ui));
        assertEquals(3, expenses.size()); // original list unchanged
    }

    @Test
    void execute_keyword_caseInsensitive_matchesDescription() throws SpendTrackException {
        FindByKeywordCommand cmd = new FindByKeywordCommand("COFFEE");
        assertDoesNotThrow(() -> cmd.execute(expenses, ui));
    }

    @Test
    void execute_keyword_noMatch_doesNotThrow() {
        FindByKeywordCommand cmd = new FindByKeywordCommand("xyz_no_match");
        assertDoesNotThrow(() -> cmd.execute(expenses, ui));
    }

    @Test
    void execute_keyword_partialMatch_doesNotThrow() {
        // "Bus" contains "bu"
        FindByKeywordCommand cmd = new FindByKeywordCommand("bu");
        assertDoesNotThrow(() -> cmd.execute(expenses, ui));
    }

    @Test
    void execute_keyword_doesNotMutateList() throws SpendTrackException {
        new FindByKeywordCommand("coffee").execute(expenses, ui);
        assertTrue(expenses.size() == 3);
    }

    @Test
    void keyword_mutatesData_returnsFalse() {
        assertTrue(!new FindByKeywordCommand("coffee").mutatesData());
    }

    // @@author
}
// @@author
