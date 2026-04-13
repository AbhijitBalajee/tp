# Afshal Gulam - Project Portfolio Page

## Overview

SpendTrack is a CLI expense tracker for NUS students who prefer typing over clicking. Users can log, categorise, and analyse daily spending with short typed commands.

## Summary of Contributions

### Code contributed

[RepoSense link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=afsh&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=false&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=AfshalG&tabRepo=AY2526S2-CS2113-T11-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

### Enhancements implemented

- **Add expense command** (v1.0): Implemented the core `add d/DESC a/AMOUNT c/CATEGORY` command with regex-based flag parsing that supports flags in any order. Created the `Expense`, `ExpenseList`, `Command`, and `AddCommand` classes that form the foundation of the app.
- **Date tagging** (v2.0): Added an optional `date/` parameter to the add command. If omitted, defaults to today's date. This was a foundational change that unblocked filter-by-date, sort-by-date, and monthly report features for other team members.
- **Flexible date parsing — DateParser** (v2.0): Extracted a `DateParser` utility class supporting four input formats: `YYYY-MM-DD`, `DD-MM-YYYY`, `today`, and `yesterday`. Uses sequential try-catch fallthrough for simplicity and is reused by the `filter` and `edit` commands.
- **Category summary with enhanced statistics** (v2.0): Implemented the `summary` command that groups expenses by category and displays total, percentage, transaction count, average, and maximum per category, sorted descending by total.
- **Command aliases** (v2.0): Added single-letter aliases (`a`, `d`, `l`, `s`, `b`, `h`) resolved via a static map in `Parser` before command routing. Commands are also case-insensitive.
- **Category normalisation** (v2.0): Added `normalizeCategory()` in `Parser` to auto-capitalise category names (e.g. `food` becomes `Food`, `public transport` becomes `Public Transport`), preventing duplicate categories in the summary.
- **Assertions and logging** (v1.0): Added assertions and `java.util.logging` to `AddCommand`, `Parser`, `ExpenseList`, `RemainingCommand`, and `HelpCommand`. Enabled assertions in `build.gradle` with `-ea` flag (PR #31).
- **Wired missing commands into Parser** (v2.0): Added `top`, `last`, `report`, `month` to Parser switch statement — command classes existed but were never routed. Moved `MonthCommand.java` from test folder to main source. Deleted duplicate `ReportCommand.java` in wrong package.
- **Fixed output formatting** (v2.0): Fixed `search`, `sort`, `last`, `top`, `report`, `month` commands to use `ui.showMessage()` instead of `System.out.println()` for consistent Ui borders across all commands.
- **Updated help command** (v2.0): Rewrote `Ui.showHelp()` to list all 25+ commands with aliases and descriptions, replacing the outdated v1.0 help output.
- **Pre-PE-D input hardening** (v2.1): Fixed nine functionality bugs across parsing, date validation, and command input handling, each covered by a dedicated JUnit test class. Changes include: graceful EOF handling in `Ui.readCommand()` (no more Java stack trace when stdin closes); rejection of `NaN`/`Infinity` in `add`, `edit`, `budget`, and `goal` amounts; strict `YYYY-MM` format validation with 1–12 month range for `month` and `report`; alignment of the `clear` confirmation prompt with the actual undo behaviour; strict calendar date parsing for `DD-MM-YYYY` to reject impossible dates like `29-02-2025`; duplicate flag detection for `filter`; rejection of `|` in descriptions to prevent silent save file corruption; rejection of empty `search` keywords; minimum `$0.01` enforcement for `budget` to avoid sub-cent rounding.
- **PE-D bug fixes and input validation** (v2.1): Triaged 20 PE-D bug reports, assigned labels/milestones/owners for all issues. Fixed bugs assigned to me: misleading error for unrecognised flag prefixes like `da/today` ([#219](https://github.com/AY2526S2-CS2113-T11-1/tp/issues/219)); UG jar filename case mismatch ([#220](https://github.com/AY2526S2-CS2113-T11-1/tp/issues/220)); summary percentage column alignment ([#229](https://github.com/AY2526S2-CS2113-T11-1/tp/issues/229)). Also fixed: summary command silently accepting extra arguments; sub-cent amounts below `$0.01` accepted and displayed as `$0.00` in `add` and `edit`.
- **Case-insensitive flags** (v2.1): Made `TOKEN_SPLIT_REGEX` and all `startsWith` checks in `parseAddCommand` and `parseEditCommand` case-insensitive, so `D/`, `A/`, `C/`, `DATE/`, `RECURRING/` work the same as lowercase. Consistent with case-insensitive command words.
- **Flag-in-description validation** (v2.1): Added `validateDescriptionFlags()` to reject descriptions containing flag prefixes (`a/`, `d/`, `c/`, `date/`, `recurring/`) that would cause ambiguous parsing. Non-flag slashes like `I/O`, `w/`, `N/A`, `24/7` are accepted. Applied consistently to both `add` and `edit`.
- **Monthly budget fix** (v2.1): Fixed high-severity PE-D bug ([#195](https://github.com/AY2526S2-CS2113-T11-1/tp/issues/195)) where budget, remaining, and budget alerts compared against all-time spending instead of current month only. Added `getMonthlyTotal()` to `ExpenseList` and updated `BudgetChecker`, `BudgetCommand`, and `RemainingCommand` to use it.

### Contributions to the UG

- Add expense section with date formats, category normalisation, and examples
- Delete expense section with error cases
- Category summary section with column explanations
- Command aliases table with note on case-insensitive commands and flags
- FAQ and command summary table
- Fixed UG error messages to match actual app output (bye, delete, remaining, edit)
- Updated help expected output to match all v2.0 commands
- Documented new error cases: unrecognised tokens, flag-in-description, amount cap, sub-cent minimum
- Fixed command summary table `c/CAT` shown as required when it is optional
- Fixed UG jar filename from `spendtrack.jar` to `SpendTrack.jar`

### Contributions to the DG

- Add expense feature section with design considerations (flag parsing, validation, category normalisation)
- Flexible date parsing (DateParser) section with design considerations (format strategy, SRP extraction)
- Category summary feature section with design considerations (data structures, sorting)
- Command aliases section
- Architecture sequence diagram showing full runtime loop
- UML diagrams (3 types, 6 diagrams):
  - Sequence diagram: add command flow with alt/opt boxes for validation and optional date
  - Sequence diagram: DateParser internal format fallthrough with alt boxes
  - Sequence diagram: SummaryCommand execution with loop and alt boxes
  - Sequence diagram: Architecture runtime loop with loop and alt boxes
  - Class diagram: Parser, DateParser, AddCommand, SummaryCommand, Expense, ExpenseList
  - Object diagram: SummaryCommand state snapshot showing category maps
- Cleaned up DG: removed duplicate Remaining/Help/Search sections, fixed heading levels, removed orphaned content, removed duplicate manual testing sections
- Updated DG add format and parsing description for case-insensitive flags and flag-in-description validation
- Fixed object diagram: removed primitive `double` as standalone object, added links from SummaryCommand to Expense objects
- Updated user stories, glossary, and manual testing appendix


### Contributions to team-based tasks

- Set up the team GitHub organisation and repo (together with Ariff), configured issue tracker with CS2113 labels and milestones
- Created and managed v2.0 and v2.0b milestones with 40 issues across all team members
- Created the v2.0 sprint plan with dependency chain, implementation order, and line count tracker
- Wrote reference guides for the team: DG assignments, coding standards, defensiveness guide, git workflow
- Reviewed PRs from teammates
- Fixed build-breaking issues from teammates' PRs (PRs #23, #24, #27, #28)
- Enabled assertions in `build.gradle` for the entire team (PR #31)
- Fixed critical release-blocking bugs: wired 4 missing commands into Parser, fixed output formatting across 6 commands, updated help and expected test output
- Triaged all 20 PE-D bug reports: assigned owners, labels (severity + type), and v2.1 milestone to every issue
- Removed misplaced `docs/diagrams/images/` folder created by a teammate
- Fixed high-severity PE-D bug #195 (budget compared against all-time total instead of current month)
- Performed v2.1 final submission: created GitHub release, generated PDFs, uploaded to Canvas

### Community

- Reviewed PRs: [#15](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/15), [#20](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/20), [#21](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/21), [#25](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/25), [#26](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/26), [#84](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/84), [#87](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/87), [#90](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/90), [#94](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/94), [#95](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/95), [#98](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/98), [#102](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/102), [#181](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/181), [#184](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/184), [#254](https://github.com/AY2526S2-CS2113-T11-1/tp/pull/254)
