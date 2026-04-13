# Ariff - Project Portfolio Page

## Overview

SpendTrack is a CLI expense tracker for NUS students who prefer typing over clicking. Users can log, categorise, and analyse daily spending with short typed commands.

## Summary of Contributions

### Code contributed

[RepoSense link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=ariff1422&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=false&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=Ariff1422&tabRepo=AY2526S2-CS2113-T11-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

### Enhancements implemented

- **UI and main application loop** (v1.0): Set up `Ui.java` and the main `SpendTrack` command loop. All user-facing input/output methods are centralised in `Ui`, keeping command classes free of `System.out` calls.
- **Delete expense command** (v1.0): Implemented `DeleteCommand` with 1-based index validation. Throws a `SpendTrackException` with a clear range message for out-of-bounds indices.
- **Delete confirmation** (v2.1): Extended `DeleteCommand` to prompt the user for `yes/no` confirmation before deleting. If the user does not type `yes`, the deletion is cancelled and the list is unchanged. Prevents accidental deletion of the wrong expense.
- **Save and load to file — Storage** (v2.0): Created the `Storage` class that persists the expense list, budget, and budget history to `data/spendtrack.txt` using a pipe-delimited format with section markers. Save is triggered automatically after every mutating command. Load is called on startup; malformed lines are skipped with a warning and the rest of the data is preserved.
- **CRC32 checksum integrity** (v2.1): Replaced AES encryption with a per-line CRC32 checksum appended to each saved record. On load, lines whose checksum does not match are skipped with a warning, protecting against file corruption and manual tampering while keeping the save file human-readable and portable across machines.
- **Load-time data validation** (v2.1): Added `validateExpense()` to `Storage` — each expense parsed from the save file is checked for blank description, non-positive amount, amount exceeding $1,000,000, and dates before year 2000. Invalid entries are skipped with a warning while the rest of the data loads normally.
- **Filter expenses by date range** (v2.0): Implemented `FilterCommand` which filters expenses inclusively between two dates. Validates that `from` is not after `to` and displays results in the same table format as `list`. Rejects duplicate or unknown flags and pipe characters.
- **Filter by category** (v2.1): Extended `FilterCommand` to accept an optional `c/CATEGORY` flag. When provided, only expenses matching the category (case-insensitive) are shown. The header updates to display the active category filter.
- **Find expense by index** (v2.0): Implemented `FindCommand` which displays full details of a single expense at a given 1-based index in a labelled detail view. Rejects trailing garbage tokens.
- **Find expense by keyword** (v2.1): Implemented `FindByKeywordCommand` for `find d/KEYWORD` mode. Scans all expenses for descriptions containing the keyword (case-insensitive) and displays results in a table with original list indices, enabling easy chaining into `delete` or `find INDEX`.
- **Startup reminder** (v2.0): After loading from file, if the list is non-empty, the most recently added expense is shown before the first prompt via `Ui.showLastExpense()`. Helps users avoid duplicate entries.
- **Input validation hardening** (v2.1): Added parse-time validation for `add` and `edit` — amount is checked to be finite and positive before the command object is created, category and description reject the pipe character `|` to prevent save file corruption, and duplicate flags (e.g. two `d/` in one command) throw an error.
- **Recurring flag persistence** (v2.1): Fixed `Storage` to save and load the `recurring` field of each expense. Before this fix, recurring expenses lost their `[R]` tag on restart.
- **ASCII logo and UI polish** (v2.1): Added a SpendTrack ASCII logo to the welcome banner, improved error hint messages (contextual `Hint:` lines based on error type), and standardised the help command column alignment.
- **Assertions and logging** (v1.0): Added assertions and `java.util.logging` to `DeleteCommand`, `SpendTrack`, `Storage`, `FilterCommand`, `FindCommand`, and `Ui`.

### Contributions to testing

- **`DeleteCommandTest`** (v1.0, updated v2.1): Tests for valid deletion, correct item removed, last index, out-of-range, and zero index. Updated in v2.1 to simulate `yes/no` confirmation input via `ByteArrayInputStream`. Added tests for cancel with `no` and cancel with invalid response.
- **`ExpenseTest`** (v2.0): Tests for both constructors, all getters/setters, recurring flag defaults, and `toString` formatting.
- **`ExpenseListTest`** (v2.0): Tests for add/get/set/delete, size tracking, `getTotal`, budget set/reset/directly, `hasBudget`, and budget history ordering.
- **`StorageTest`** (v2.0, updated v2.1): Full round-trip save/load tests for expenses, budget, and budget history. Also covers missing file, zero/malformed budget value, and field-level precision. Updated in v2.1 with CRC32 tamper-detection tests: tampered checksum causes line to be skipped, valid file loads fully.
- **`DateParserTest`** (v2.0): Tests for ISO format, Singapore DD-MM-YYYY format, `today`/`yesterday` keywords (case-insensitive), whitespace tolerance, and invalid inputs including empty string, wrong separator, invalid month/day.
- **`FilterCommandTest`** (v2.0, extended v2.1): Tests for date range filtering, no matches, list immutability, `isExit`/`mutatesData`, and parser-level validation (missing dates, reversed range, invalid format). Extended in v2.1 with tests for `c/` category filter, duplicate `c/`, empty `c/`, pipe in category, and garbage token rejection.
- **`FindCommandTest`** (v2.0, extended v2.1): Tests for valid/invalid/negative/zero indices, empty list, list immutability, `isExit`/`mutatesData`, and parser-level validation. Extended in v2.1 with tests for `find d/KEYWORD` mode including case-insensitive match, no match, partial match, trailing garbage rejection, and `mutatesData` returning false.
- **`StartupReminderTest`** (v2.0): Tests for `showLastExpense` output content (description, amount, category, date), empty list logic, and recurring expense display.
- **`InputValidationTest` (extended)** (v2.0): Added parser-level tests for `filter`, `find`, `list`, alias resolution, `bye`, and unknown commands.

### Contributions to the UG

- Auto-save and load section explaining the file format, startup behaviour, data transfer, and load-time validation
- Delete command section updated with confirmation prompt, expected outputs for confirmed and cancelled deletion
- CRC32 checksum section explaining tamper detection and portability
- Updated FAQ: data transfer answer updated to reflect plain-text save with checksum integrity
- Startup reminder section with example output
- Filter command section with format, date format notes, `c/CATEGORY` optional flag, examples, and error cases
- Find command section covering both index mode and `find d/KEYWORD` keyword mode, with examples and error cases
- Help command expected output updated to reflect aligned column format and all current commands
- Expanded command summary table with all commands

### Contributions to the DG

- Architecture section — overview of the command-driven loop and component responsibilities
- Delete expense feature section with design considerations (1-based indexing, parse vs execute validation, delete confirmation)
- Storage implementation section (save/load design, file format, section markers, CRC32 checksum design, design considerations)
- Filter and Find feature section updated with `c/CATEGORY` filter, `find d/KEYWORD` keyword mode, `FindByKeywordCommand` design, and design considerations (list immutability, index validation, pipe rejection, find d/ vs search)
- User stories for save/load, filter, find, and startup reminder features
- Fixed UML sequence diagram notation across all diagrams to follow PlantUML conventions (`<<class>>` for static utility classes, `<u>...</u>` underline for instance participants, `create` for object instantiation, activation bar routing so return arrows do not cross active bars)
- UML diagrams:
    - Sequence diagram: full SpendTrack runtime architecture (`ArchitectureSequence`)
    - Sequence diagram: delete command flow with index validation and auto-save (`DeleteCommandSequence`)
    - Sequence diagram: Storage startup load flow including startup reminder (`StorageLoadSequence`)
    - Class diagram: Storage, ExpenseList, Expense, SpendTrack relationships (`StorageClassDiagram`)
    - Sequence diagram: filter (with category) and find (index + keyword) execution flows (`FilterFindSequence`)


### Contributions to team-based tasks

- Set up the team GitHub organisation and repo (together with Afshal)
- Maintained issue tracker: created and assigned issues for v1.0 and v2.0 milestones
- Reviewed PRs from all teammates throughout v1.0 and v2.0
- Recorded v2.1 demo video
- Performed v2.1 final checks before submission

### Community

Reviewed and approved PRs: [#3](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/3), [#5](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/5), [#14](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/14), [#17](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/17), [#18](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/18), [#20](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/20), [#21](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/21), [#22](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/22), [#23](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/23), [#24](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/24), [#27](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/27), [#28](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/28), [#29](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/29), [#32](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/32), [#34](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/34), [#35](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/35), [#36](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/36), [#77](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/77), [#78](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/78), [#79](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/79), [#80](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/80), [#81](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/81), [#86](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/86), [#90](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/90), [#92](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/92), [#93](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/93), [#94](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/94), [#95](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/95), [#96](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/96), [#97](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/97), [#113](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/113), [#117](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/117), [#118](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/118), [#119](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/119), [#120](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/120), [#121](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/121), [#123](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/123), [#126](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/126), [#127](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/127), [#128](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/128), [#131](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/131), [#132](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/132), [#133](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/133), [#135](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/135), [#137](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/137), [#138](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/138), [#139](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/139), [#140](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/140), [#141](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/141), [#142](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/142), [#143](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/143), [#144](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/144), [#159](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/159), [#161](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/161), [#162](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/162), [#172](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/172), [#185](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/185), [#186](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/186), [#235](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/235), [#238](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/238), [#239](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/239), [#240](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/240), [#244](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/244), [#245](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/245), [#246](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/246), [#247](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/247), [#248](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/248), [#249](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/249), [#250](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/250), [#259](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/259), [#260](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/260), [#261](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/261), [#262](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/262)
