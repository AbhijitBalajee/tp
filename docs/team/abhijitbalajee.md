# Abhijit Balajee - Project Portfolio Page

## Overview

SpendTrack is a CLI expense tracker for NUS students who prefer typing over clicking. Users can log, categorise, and analyse daily spending with short typed commands.

## Summary of Contributions

### Code contributed

[RepoSense link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=AbhijitBalajee&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=false&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=AbhijitBalajee&tabRepo=AY2526S2-CS2113-T11-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

### Enhancements implemented

- **List expenses** (v1.0): Implemented `ListCommand` to display all expenses. In v2.0, extended it with `list recurring` and dynamic column widths in `Ui.showExpenseList()` / `Ui.showRecurringList()` for consistent alignment.

- **Set monthly budget** (v1.0): Implemented `BudgetCommand` with validation for invalid ranges (zero, negative, below one cent, over $1,000,000), plus clear feedback for spent, remaining, and overspend status. Budget is stored in `ExpenseList`.

- **Edit expense** (v2.0/v2.1): Implemented partial updates in `EditCommand` (using `null` sentinel), duplicate-flag detection, and execute-time validation. **(v2.1)** Overrode `mutatesData()` so edits persist across sessions, and called `BudgetChecker.check()` after edit to align warnings with `add`.

- **Budget reset and history** (v2.0/v2.1): Implemented `BudgetResetCommand` and `BudgetHistoryCommand`, with history stored as `date|amount` entries in `ExpenseList`. **(v2.1)** `resetBudget()` appends `date|0.0`, and `Ui.showBudgetHistory()` renders this as `RESET ($0.00)`.

- **Undo with budget history** (v2.1): Extended `UndoManager.saveSnapshot()` and `ExpenseList.restoreFrom()` to include budget history, so `undo` restores expenses, budget, and history consistently.

- **Input validation hardening** (v2.0/v2.1): Strengthened parse-time and execute-time checks across `add`, `edit`, `budget`, and `list`. **(v2.1)** Added parser rejection for non-finite values (`NaN`, `Infinity`), invalid `list` options, and extra tokens after `list`, `budget reset`, and `budget history`.

- **Bug fixes — PE dry run (v2.1)**: Fixed three PE bugs: `EditCommand` persistence via `mutatesData()`, post-edit budget checks via `BudgetChecker.check()`, and missing `[R]` tag in `showFilteredExpenses()`. Also fixed `list recurring extra` parser fallthrough, corrected `budget reset` / `budget history` extra-token error paths, and rejected `NaN` / `Infinity` budget amounts.

- **Recurring expenses** (v2.0): Added `recurring/true|false` for `add`, persisted as `boolean isRecurring` in `Expense` (default `false`), displayed with `[R]`, supported in `list recurring`, and editable via `edit INDEX recurring/false`.

- **Dynamic list alignment** (v2.0): Refactored `Ui.showExpenseList()`, `Ui.showRecurringList()`, and `Ui.showFilteredExpenses()` to use dynamic widths and separators, replacing fixed-width output that misaligned long entries.

- **Assertions and logging** (v1.0/v2.0/v2.1): Added assertions and `java.util.logging` across command/model/UI classes, extended `UndoManager` snapshot logging in v2.1 for budget-history-aware undo, and suppressed console noise via `setUseParentHandlers(false)`.

### Contributions to team-based tasks

- Reviewed PRs from teammates throughout v1.0 and v2.0
- Coordinated with Ariff on `Ui.java` method additions for list and budget features to avoid merge conflicts
- Coordinated with Afshal on `Expense.java` constructor changes needed for the recurring flag
- Added Repotools `@@author` tags on implemented features (list, recurring, budget, parser, UI, and related tests) for RepoSense contribution tracking, consistent with team practice

### Contributions to testing

- Added and extended test coverage for `ListCommandTest`, `BudgetCommandTest`, `EditCommandTest`, `RecurringExpenseTest`, `InputValidationTest`, `UndoCommandTest`, and `ExpenseListTest` across v1.0-v2.1.

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

### Community

Reviewed PRs: [#31](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/31), [#103](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/103), [#110](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/110), [#111](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/111), [#112](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/112), [#115](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/115), [#124](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/124), [#166](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/166), [#170](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/170), [#174](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/174), [#234](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/234), [#236](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/236), [#263](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/263), [#265](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/265), [#267](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/267), [#271](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/271)
