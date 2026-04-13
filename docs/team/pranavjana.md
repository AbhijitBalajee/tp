# Pranav Janakiraman - Project Portfolio Page

## Overview

SpendTrack is a CLI expense tracker for NUS students who prefer typing over clicking. Users can log, categorise, and analyse daily spending with short typed commands.

## Summary of Contributions

### Code contributed

[RepoSense link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=pranavjana&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=false&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=pranavjana&tabRepo=AY2526S2-CS2113-T11-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

### Enhancements implemented

- **Total expenses command** (v1.0): Implemented `TotalCommand` to display the sum of all recorded expenses. Added assertions and logging. Added defensive checks for null and empty list edge cases (PR #36).
- **Budget alert** (v2.0): Created `BudgetChecker` utility class that automatically checks spending against the budget after every `add` command. Shows a warning at 90% usage and an alert when exceeded. No message if no budget is set or spending is under 90%.
- **Clear all expenses** (v2.0): Implemented `ClearCommand` with a confirmation prompt. User must type `yes` to confirm; any other input cancels. Empty list shows a message without prompting.
- **Undo command** (v2.0): Implemented `UndoManager` and `UndoCommand` for single-level undo. Saves a deep copy snapshot of the expense list before each mutating command. Restores expenses and budget state on undo. Wired into `SpendTrack` main loop and `Parser`.
- **Export to CSV** (v2.0): Implemented `ExportCommand` that writes all expenses to `data/spendtrack_export.csv`. Handles CSV quoting for descriptions containing commas or double quotes. Creates the data directory automatically if missing.
- **Savings goal** (v2.0): Implemented `GoalCommand` for setting a savings target (`goal g/AMOUNT`) and viewing progress (`goal status`). Shows percentage saved or over-by amount. Goal persisted via Storage.
- **PE-D bug fixes** (v2.1): Fixed seven PE-D bugs across undo, input validation, and documentation. Undo stack corruption (#199, #202, #230): added backup/rollback to `UndoManager` so failed commands and cancelled `clear` no longer overwrite the undo snapshot; `ClearCommand` now tracks whether it actually cleared via a `didClear()` flag; main loop rolls back undo state on `SpendTrackException`. Input validation (#212, #213): reject extra parameters on `total`, `clear`, and `undo` commands; `ClearCommand` now rejects non-yes/no confirmation input with a clear error message. UG documentation (#214, #222): updated `clear` command example to match actual app output.
- **UML diagram fixes** (v2.1): Fixed three UML issues across diagrams for owned features (#145, #146, #155). Added `<<class>>` stereotype to static classes (`Parser`, `BudgetChecker`) in sequence diagrams. Removed double-counted associations in `UndoClassDiagram` where relationships were shown as both attributes and lines. Added missing activation bars and return arrows in `BudgetAlertSequence` and `UndoSequence`.

### Contributions to the UG

- Total expenses section
- Budget alert section explaining automatic warning/alert behaviour
- Clear command section with confirmation flow and edge cases
- Undo command section with single-level undo explanation
- Export CSV section with file format and quoting details
- Savings goal section with set and status sub-commands
- Updated command summary table with all new commands
- Fixed clear command example output to match actual app behaviour (#214, #222)

### Contributions to the DG

- Total expenses feature section with design consideration (on-demand vs cached total)
- Budget alert feature section with design considerations (SRP extraction, threshold constant)
- Clear command section with design considerations (confirmation safety, empty list handling)
- Undo feature section with design considerations (snapshot depth, deep vs shallow copy, excluding undo from snapshot)
- Export CSV section with design considerations (CSV quoting strategy, testability)
- Savings goal section with design considerations (single class for set/status, persistence)
- UML diagrams:
  - Sequence diagram: budget alert flow after add command (`BudgetAlertSequence`)
  - Sequence diagram: clear command with confirmation prompt (`ClearCommandSequence`)
  - Sequence diagram: undo flow showing snapshot save and restore (`UndoSequence`)
  - Sequence diagram: export CSV file writing flow (`ExportCommandSequence`)
  - Sequence diagram: goal set and status flows (`GoalCommandSequence`)
  - Class diagram: UndoManager, UndoCommand, SpendTrack, ExpenseList, Expense relationships (`UndoClassDiagram`)
- Added user stories for budget alert, clear, undo, export, and savings goal
- Fixed UML diagrams: added `<<class>>` stereotypes, removed double-counted associations, added missing activation bars and return arrows (#145, #146, #155)

### Contributions to team-based tasks

- Fixed compilation error blocking CI for the team (PR #23)
- Reviewed PRs from teammates
- Added `@@author` tags across all contributed files for RepoSense attribution

### Community

- Reviewed PRs: #86, #103, #113, #116, #129, #130, #132
