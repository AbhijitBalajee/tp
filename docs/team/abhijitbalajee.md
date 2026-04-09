# Abhijit Balajee - Project Portfolio Page

## Overview

SpendTrack is a CLI expense tracker for NUS students who prefer typing over clicking. Users can log, categorise, and analyse daily spending with short typed commands.

## Summary of Contributions

### Code contributed

[RepoSense link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=AbhijitBalajee&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=false&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=AbhijitBalajee&tabRepo=AY2526S2-CS2113-T11-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

### Enhancements implemented

- **List expenses** (v1.0): Implemented `ListCommand` which displays all recorded expenses. In v2.0, extended to support `list recurring` as a sub-command using a boolean flag, avoiding the need for a separate command class. Implemented dynamic column widths in `Ui.showExpenseList()` and `Ui.showRecurringList()` — column widths are calculated at display time based on the longest entry in each column, ensuring the table always aligns correctly regardless of category or description length.

- **Set monthly budget** (v1.0): Implemented `BudgetCommand` with full input validation — rejects zero, negative, and amounts exceeding $1,000,000 with clear error messages. Displays current total spent and remaining balance on confirmation. Shows an overspend warning if expenses already exceed the new budget. Budget is stored in `ExpenseList` alongside expenses.

- **Edit expense** (v2.0): Implemented `EditCommand` which performs partial field updates — only fields provided by the user are changed, others remain unchanged. Uses `null` as a sentinel for unspecified fields. Parser detects duplicate flags (e.g. `d/Latte d/Coffee`) and throws an error. Supports editing description, amount, category, date, and recurring flag. Validates all fields at execute time with clear error messages.

- **Budget reset and history** (v2.0): Implemented `BudgetResetCommand` which clears the current budget and guards against reset when no budget is set. Implemented `BudgetHistoryCommand` which displays all previously set budgets in reverse chronological order. Budget history is stored as `ArrayList<String>` of `date|amount` strings in `ExpenseList` and populated automatically by `setBudget()`.

- **Input validation hardening** (v2.0): Audited all commands for missing or inconsistent validation. Added missing description and amount checks to `add`, empty input and non-numeric checks to `budget`, and non-integer index checks to `edit`. Validation is split between `Parser` (format checks) and commands (value/range checks at execute time) for defence-in-depth.

- **Recurring expenses** (v2.0): Added `recurring/true|false` flag to the `add` command. The flag is stored on `Expense` as `boolean isRecurring`, defaulting to `false`. Recurring expenses display `[R]` in the list. `list recurring` filters on the fly at display time. The recurring flag is also editable via `edit INDEX recurring/false`.

- **Dynamic list alignment** (v2.0): Refactored `Ui.showExpenseList()`, `Ui.showRecurringList()`, and `Ui.showFilteredExpenses()` to use dynamic column widths and per-column separator lines, replacing fixed-width formatting that broke alignment for long categories like `[Entertainment]`.

- **Assertions and logging** (v1.0/v2.0): Added assertions and `java.util.logging` to `ListCommand`, `BudgetCommand`, `BudgetResetCommand`, `BudgetHistoryCommand`, `EditCommand`, `Expense`, `ExpenseList`, and `Ui`. Logger suppressed from console output using `setUseParentHandlers(false)`.

### Contributions to testing

- **`ListCommandTest`** (v1.0): Tests for empty list, single expense, multiple expenses, and `isExit()`.
- **`BudgetCommandTest`** (v1.0): Tests for valid amount, negative amount, zero amount, exceeds max, and with existing expenses.
- **`EditCommandTest`** (v2.0): Tests for editing each field individually, editing all fields, unchanged fields staying the same, editing recurring flag, invalid indices (zero, negative, out of range), no fields provided, blank/empty description, zero/negative amount, and list size unchanged after edit.
- **`RecurringExpenseTest`** (v2.0): Tests for default recurring flag, setting recurring true, `[R]` tag in toString, `list recurring` execution, parsing `recurring/true` and `recurring/false`, invalid recurring value, and recurring count in list.
- **`InputValidationTest`** (v2.0): Parser-level tests for all validation added — missing/empty description, missing/zero/negative/non-numeric amount for `add`; missing/non-numeric index for `delete` and `edit`; empty/non-numeric for `budget`; and empty/whitespace input.

### Contributions to the UG

- List expenses section with dynamic column width explanation and expected output
- List recurring section with expected output
- Adding a recurring expense section
- Edit expense section with format, examples, expected output, and error cases
- Setting a monthly budget section with examples, expected output, and error cases
- Budget reset section
- Budget history section
- Delete expense section
- Total and remaining balance sections
- Updated command summary table with all commands including edit, list recurring, budget reset, budget history

### Contributions to the DG

- List Expenses feature section with design considerations (dynamic vs fixed widths, sub-command vs separate command)
- Budget feature section covering set/reset/history with design considerations (history storage, routing through one parser method)
- Input Validation Hardening section with table of changes and parse-time vs execute-time design consideration
- Recurring Expenses feature section with design considerations (flag storage, validation approach)
- Edit Expense feature section with design considerations (null sentinel, duplicate flag detection, recurring as editable field)
- User stories for list, edit, budget reset/history, recurring expenses
- Manual testing sections for listing, editing, budget reset and history
- UML diagrams:
    - Sequence diagram: `EditCommand` execution flow (`EditCommandSequence`)
    - Sequence diagram: Budget set/reset/history flows (`BudgetCommandSequence`)
    - Sequence diagram: `ListCommand` with recurring filter branch (`ListCommandSequence`)
    - Sequence diagram: Recurring expense add and list flow (`RecurringExpenseSequence`)
    - Class diagram: Budget-related classes — `BudgetCommand`, `BudgetResetCommand`, `BudgetHistoryCommand`, `ExpenseList`, `Ui` (`BudgetClassDiagram`)

### Contributions to team-based tasks

- Reviewed PRs from teammates throughout v1.0 and v2.0
- Coordinated with Ariff on `Ui.java` method additions for list and budget features to avoid merge conflicts
- Coordinated with Afshal on `Expense.java` constructor changes needed for the recurring flag

### Community

Reviewed PRs: #31, #103, #110, #111, #112, #115, #124, #166, #170, #174
