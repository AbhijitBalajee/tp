# Abhijit Balajee - Project Portfolio Page

## Overview

SpendTrack is a CLI expense tracker for NUS students who prefer typing over clicking. Users can log, categorise, and analyse daily spending with short typed commands.

## Summary of Contributions

### Code contributed

[RepoSense link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=AbhijitBalajee&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=false&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=AbhijitBalajee&tabRepo=AY2526S2-CS2113-T11-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

### Enhancements implemented

- **List expenses** (v1.0): Implemented `ListCommand` which displays all recorded expenses. In v2.0, extended to support `list recurring` as a sub-command using a boolean flag, avoiding the need for a separate command class. Implemented dynamic column widths in `Ui.showExpenseList()` and `Ui.showRecurringList()` — column widths are calculated at display time based on the longest entry in each column, ensuring the table always aligns correctly regardless of category or description length.

- **Set monthly budget** (v1.0): Implemented `BudgetCommand` with full input validation — rejects zero, negative, amounts below one cent, and amounts exceeding $1,000,000 with clear error messages. Displays current total spent and remaining balance on confirmation. Shows an overspend warning if expenses already exceed the new budget. Budget is stored in `ExpenseList` alongside expenses.

- **Edit expense** (v2.0/v2.1): Implemented `EditCommand` which performs partial field updates — only fields provided by the user are changed, others remain unchanged. Uses `null` as a sentinel for unspecified fields. Parser detects duplicate flags (e.g. `d/Latte d/Coffee`) and throws an error. Supports editing description, amount, category, date, and recurring flag. Validates all fields at execute time with clear error messages. **(v2.1)** Overrode `mutatesData()` to return `true` so `SpendTrack` persists edits to disk after `edit` (previously missing, edits were lost across sessions). After a successful edit, `BudgetChecker.check()` runs so spending vs budget warnings and alerts match the behaviour after `add`.

- **Budget reset and history** (v2.0/v2.1): Implemented `BudgetResetCommand` which clears the current budget and guards against reset when no budget is set. Implemented `BudgetHistoryCommand` which displays all previously set budgets in reverse chronological order. Budget history is stored as `ArrayList<String>` of `date|amount` strings in `ExpenseList` and populated automatically by `setBudget()`. **(v2.1)** `resetBudget()` appends a `date|0.0` entry to history; `Ui.showBudgetHistory()` displays those entries as `RESET ($0.00)` so users can see when the active limit was cleared.

- **Undo with budget history** (v2.1): Extended `UndoManager.saveSnapshot()` to copy `ExpenseList` budget history alongside expenses and budget, and `ExpenseList.restoreFrom()` to restore that history when undo runs. Previously, undo restored the budget amount but left stale rows in the history log (e.g. after a second `budget` or `budget reset`). Added regression tests in `UndoCommandTest` for history length after undo.

- **Input validation hardening** (v2.0/v2.1): Audited all commands for missing or inconsistent validation. Added missing description and amount checks to `add`, empty input and non-numeric checks to `budget`, and non-integer index checks to `edit`. Validation is split between `Parser` (format checks) and commands (value/range checks at execute time) for defence-in-depth. **(v2.1)** Parser rejects non-finite `budget` amounts (`NaN`, `Infinity`), extra tokens after `list` / `budget reset` / `budget history`, and invalid `list` subcommands; explicit non-finite rejection for `add` amounts where enforced at parse time.

- **Bug fixes — PE dry run (v2.1)**: Fixed three bugs found during the practical exam: (1) `EditCommand` now overrides `mutatesData()` so edited expenses persist to disk across sessions, (2) `EditCommand` now calls `BudgetChecker.check()` after successful edits so warnings are consistent with `add`, and (3) `showFilteredExpenses()` now includes the `[R]` recurring tag in filtered output. Also fixed `list recurring extra` incorrectly falling through to the full list, corrected `budget reset extra` and `budget history extra` error messages, and rejected `NaN` / `Infinity` as invalid budget amounts.

- **Recurring expenses** (v2.0): Added `recurring/true|false` flag to the `add` command. The flag is stored on `Expense` as `boolean isRecurring`, defaulting to `false`. Recurring expenses display `[R]` in the list. `list recurring` filters on the fly at display time. The recurring flag is also editable via `edit INDEX recurring/false`.

- **Dynamic list alignment** (v2.0): Refactored `Ui.showExpenseList()`, `Ui.showRecurringList()`, and `Ui.showFilteredExpenses()` to use dynamic column widths and per-column separator lines, replacing fixed-width formatting that broke alignment for long categories like `[Entertainment]`.

- **Assertions and logging** (v1.0/v2.0/v2.1): Added assertions and `java.util.logging` to `ListCommand`, `BudgetCommand`, `BudgetResetCommand`, `BudgetHistoryCommand`, `EditCommand`, `Expense`, `ExpenseList`, and `Ui`. Extended snapshot logging in `UndoManager` (v2.1) when budget history was included in undo. Logger suppressed from console output using `setUseParentHandlers(false)`.

### Contributions to testing

- **`ListCommandTest`** (v1.0, v2.1): Tests for empty list, single expense, multiple expenses, `isExit()` for default and recurring-only mode (`ListCommand(true)`), and executing with expenses.
- **`BudgetCommandTest`** (v1.0): Tests for valid amount, negative amount, zero amount, exceeds max, and with existing expenses.
- **`EditCommandTest`** (v2.0): Tests for editing each field individually, editing all fields, unchanged fields staying the same, editing recurring flag, invalid indices (zero, negative, out of range), no fields provided, blank/empty description, zero/negative amount, and list size unchanged after edit.
- **`RecurringExpenseTest`** (v2.0): Tests for default recurring flag, setting recurring true, `[R]` tag in toString, `list recurring` execution, parsing `recurring/true` and `recurring/false`, invalid recurring value, and recurring count in list.
- **`InputValidationTest`** (v2.0): Parser-level tests for validation gaps addressed in the audit — missing/empty description, missing/zero/negative/non-numeric amount for `add`; missing/non-numeric index for `delete` and `edit`; empty/non-numeric for `budget`; `list` and `list recurring` cases; empty/whitespace input.
- **`InputValidationTest` (extended v2.1)**: Added parser-level tests for extra tokens after `list`, `budget reset`, and `budget history`; rejected `NaN` and `Infinity` budget amounts; and `list abc` as an invalid list option.
- **`UndoCommandTest`** (v2.1): Tests that undo restores budget history after a second `setBudget` and after `budget reset`, in addition to existing undo coverage.
- **`ExpenseListTest`** (v2.1): Updated `resetBudget_budgetHistoryPreserved()` to verify a reset appends a `date|0.0` entry and history behaviour stays consistent with the reset flow.

### Contributions to the UG

- List expenses section with dynamic column width explanation and expected output
- List recurring section with expected output
- Adding a recurring expense section
- Edit expense section with format, examples, expected output, and error cases; **(v2.1)** clarified that successful edits are persisted automatically and that budget warning/alert behaviour after `edit` matches `add`
- Setting a monthly budget section with examples, expected output, and error cases; **(v2.1)** documented `NaN` / `Infinity` and other parser-level budget errors
- Budget reset section
- Budget history section
- Updated command summary table to include edit, list recurring, budget reset, budget history
- Undo section updates (v2.1): documented all mutating commands that consume the undo slot (including `budget` / `b` and `budget reset`), and that undo restores **budget history** together with expenses and current budget
- Error cases for invalid `list` arguments and for extra tokens after `budget reset` / `budget history`
- Filter section: added note that recurring expenses show `[R]` tag in filtered results
- Edit section: added note that edits save automatically and a budget warning appears after editing
- Budget section: added `NaN` / `Infinity` error cases

### Contributions to the DG

- List Expenses feature section with design considerations (dynamic vs fixed widths, sub-command vs separate command)
- Budget feature section covering set/reset/history with design considerations (history storage, routing through one parser method)
- Input Validation Hardening section with table of changes and parse-time vs execute-time design consideration
- Recurring Expenses feature section with design considerations (flag storage, validation approach)
- Edit Expense feature section with design considerations (null sentinel, duplicate flag detection, recurring as editable field); **(v2.1)** updated narrative to include `mutatesData()` for persistence and post-edit `BudgetChecker` behaviour
- User stories for list, edit, budget reset/history, recurring expenses
- Manual testing sections for listing, editing, budget reset and history
- Undo feature documentation (v2.1): snapshot and `ExpenseList.restoreFrom()` described to include budget history alongside expenses and budget
- Input Validation Hardening table: fixed markdown so `list` and `budget` appear on separate rows (with `budget` validation details consolidated)
- Updated Edit Expense feature section to document `mutatesData()` override and `BudgetChecker.check()` call after edit
- Updated Input Validation Hardening table with validation rows for `list`, `budget reset`, `budget history`, and `NaN` / `Infinity`
- **UML notation fixes** across contributed sequence diagrams: standard UML conventions — `<<class>>` for static callers where applicable (`Parser`, `DateParser`, `BudgetChecker`), underlined instance participant names, return arrows to activation bars, and `==` fragment dividers instead of non-standard section headers
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
- Added Repotools `@@author` tags on implemented features (list, recurring, budget, parser, UI, and related tests) for RepoSense contribution tracking, consistent with team practice

### Community

Reviewed PRs: #31, #103, #110, #111, #112, #115, #124, #166, #170, #174, #234, #236, #263, #265
