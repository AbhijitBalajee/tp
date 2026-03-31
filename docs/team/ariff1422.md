# Ariff - Project Portfolio Page

## Overview

SpendTrack is a CLI expense tracker for NUS students who prefer typing over clicking. Users can log, categorise, and analyse daily spending with short typed commands.

## Summary of Contributions

### Code contributed

[RepoSense link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=ariff1422&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=false&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=Ariff1422&tabRepo=AY2526S2-CS2113-T11-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

### Enhancements implemented

- **UI and main application loop** (v1.0): Set up `Ui.java` and the main `SpendTrack` command loop. All user-facing input/output methods are centralised in `Ui`, keeping command classes free of `System.out` calls.
- **Delete expense command** (v1.0): Implemented `DeleteCommand` with 1-based index validation. Throws a `SpendTrackException` with a clear range message for out-of-bounds indices.
- **Save and load to file — Storage** (v2.0): Created the `Storage` class that persists the expense list, budget, and budget history to `data/spendtrack.txt` using a pipe-delimited format with section markers. Save is triggered automatically after every mutating command. Load is called on startup; malformed lines are skipped with a warning and the rest of the data is preserved.
- **Filter expenses by date range** (v2.0): Implemented `FilterCommand` which filters expenses inclusively between two dates. Depends on Afshal's date tagging. Validates that `from` is not after `to` and displays results in the same table format as `list`.
- **Find expense by index** (v2.0): Implemented `FindCommand` which displays full details of a single expense at a given 1-based index in a labelled detail view.
- **Startup reminder** (v2.0): After loading from file, if the list is non-empty, the most recently added expense is shown before the first prompt via `Ui.showLastExpense()`. Helps users avoid duplicate entries.
- **Assertions and logging** (v1.0): Added assertions and `java.util.logging` to `DeleteCommand`, `SpendTrack`, `Storage`, `FilterCommand`, `FindCommand`, and `Ui`.

### Contributions to testing

- **`DeleteCommandTest`** (v1.0): Tests for valid deletion, correct item removed, last index, out-of-range, and zero index.
- **`ExpenseTest`** (v2.0): Tests for both constructors, all getters/setters, recurring flag defaults, and `toString` formatting.
- **`ExpenseListTest`** (v2.0): Tests for add/get/set/delete, size tracking, `getTotal`, budget set/reset/directly, `hasBudget`, and budget history ordering.
- **`StorageTest`** (v2.0): Full round-trip save/load tests for expenses, budget, and budget history. Also covers missing file, malformed lines (bad amount, bad date, wrong column count), zero/malformed budget value, and field-level precision.
- **`DateParserTest`** (v2.0): Tests for ISO format, Singapore DD-MM-YYYY format, `today`/`yesterday` keywords (case-insensitive), whitespace tolerance, and invalid inputs including empty string, wrong separator, invalid month/day.
- **`FilterCommandTest`** (v2.0): Tests for date range filtering, no matches, list immutability, `isExit`/`mutatesData`, and parser-level validation (missing dates, reversed range, invalid format).
- **`FindCommandTest`** (v2.0): Tests for valid/invalid/negative/zero indices, empty list, list immutability, `isExit`/`mutatesData`, and parser-level validation.
- **`StartupReminderTest`** (v2.0): Tests for `showLastExpense` output content (description, amount, category, date), empty list logic, and recurring expense display.
- **`InputValidationTest` (extended)** (v2.0): Added parser-level tests for `filter`, `find`, `list`, alias resolution, `bye`, and unknown commands.

### Contributions to the UG

- Auto-save and load section explaining the file format, startup behaviour, and data transfer
- Startup reminder section with example output
- Filter command section with format, date format notes, examples, and error cases
- Find command section with format, examples, and error cases
- Expanded command summary table with all commands

### Contributions to the DG

- Storage implementation section (save/load design, file format, section markers)
- User stories for save/load, filter, find, and startup reminder features

### Contributions to team-based tasks

- Set up the team GitHub organisation and repo (together with Afshal)
- Maintained issue tracker: created and assigned issues for v1.0 and v2.0 milestones
- Reviewed PRs from all teammates throughout v1.0 and v2.0

### Community

Reviewed PRs: #3, #5, #14, #17, #18, #20, #21, #22, #23, #24, #27, #28, #29, #32, #34, #35, #36, #78, #79, #80, #81, #86, #90, #92, #93, #94, #95, #96, #97
