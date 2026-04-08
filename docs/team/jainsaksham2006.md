# Saksham Jain - Project Portfolio Page

## Overview

SpendTrack is a CLI expense tracker for NUS students who prefer typing over clicking. Users can log, categorise, and analyse daily spending with short typed commands.

## Summary of Contributions

### Code contributed

[RepoSense link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=jainsaksham2006&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=jainsaksham2006&tabRepo=AY2526S2-CS2113-T11-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

---

### Enhancements implemented

- **Remaining budget feature** (v1.0): Implemented `RemainingCommand` to compute remaining balance dynamically as `budget - total`. Includes validation using `ExpenseList.hasBudget()` and throws `SpendTrackException` when no budget is set, ensuring correctness over misleading defaults.
- **Help command** (v1.0): Implemented `HelpCommand` to display all available commands. Designed as a stateless command with alias support (`h`). Help content is centralised in `Ui.showHelp()` to decouple command logic from presentation.
- **Search feature** (v2.0): Implemented `SearchCommand` using case-insensitive substring matching via `toLowerCase().contains()`. Iterates through `ExpenseList` and dynamically builds output using `StringBuilder`. Handles no-match scenarios gracefully.
- **Sort feature** (v2.0): Implemented `SortCommand` to display expenses sorted by amount using `Comparator.comparingDouble(...).reversed()`. Operates on a copied list to preserve insertion order and prevent side effects on index-based commands.
- **Top N expenses feature** (v2.0): Implemented `TopCommand` to display highest N expenses. Includes validation for invalid input (`count <= 0`), empty lists, and oversized requests using `Math.min(count, list.size())`.
- **Last N expenses feature** (v2.0): Implemented `LastCommand` to display most recently added expenses based on insertion order. Uses index slicing (`Math.max(0, total - count)`) instead of sorting to reflect actual user activity.
- **Monthly report feature** (v2.0): Implemented `ReportCommand` to aggregate expenses by category using `HashMap<String, Double>`. Computes total spending and category breakdown for a given month using date filtering.
- **Month listing feature** (v2.0): Implemented `MonthCommand` to list individual expenses for a given month. Designed separately from report logic to maintain Single Responsibility Principle.
- **Unknown command handling** (v2.0): Refactored `UnknownCommand` with modular message construction (`buildHeader`, `buildSuggestion`) and integrated logging using `java.util.logging.Logger`.
- **Exit command refactoring** (v2.0): Refactored `ExitCommand` with modular message construction and logging. Maintains original behaviour while improving code structure and testability.

---

### Contributions to testing

- **`SortCommandTest`** (v2.0): Added test to verify that the sort command executes without crashing when handling multiple expenses.
- **`ReportCommandTest`** (v2.0): Added test to ensure the report command executes correctly for a valid month input.
- **`HelpCommandTest`** (v1.0): Tested execution of help command to confirm that it displays command information without errors.
- **`RemainingCommandTest`** (v1.0): Implemented functional test to verify correct remaining budget calculation.

- Focused on validating **command execution stability** using `assertDoesNotThrow()` to ensure robustness.
- Covered basic scenarios such as handling valid inputs and ensuring commands do not crash during execution.

### Contributions to the UG

- Search command section with keyword-based filtering and usage examples
- Sort command section explaining sorting by amount and expected output order
- Report command section describing monthly report format and category breakdown
- Remaining budget section explaining how remaining balance is calculated
- Help command section listing all available commands and usage formats
- Last command section showing how to retrieve the most recent n expenses
- Top command section describing retrieval of top n expenses by amount
- Updated command summary table to include all newly added commands
- Ensured consistency in command format and examples across all sections for better usability

### Contributions to the DG

- Search feature section with design considerations (simple keyword matching vs extensibility)
- Sort feature section with design considerations (sorting on a copy vs mutating original list)
- Report feature section with design considerations (use of HashMap for category aggregation and total computation)
- Remaining budget feature section with design considerations (static computation vs persistent state)
- Last command section with design considerations (index-based retrieval and boundary handling)
- Top command section with design considerations (sorting strategy and selecting top n elements)
- Help command section describing command dispatch and interaction with UI

- UML diagrams:
    - Sequence diagram: search command execution flow
    - Sequence diagram: report command aggregation logic
    - Sequence diagram: top/last command execution flow
    - Class diagram: Command hierarchy and interaction with ExpenseList and Ui

- Explained trade-offs between simplicity, performance, and correctness across implemented features
- Ensured alignment with overall architecture and maintained separation of concerns across commands

### Contributions to team-based tasks

- Reviewed PRs from teammates and provided feedback on code structure, validation logic, and formatting consistency.
- Assisted in debugging CI failures (Checkstyle, IO tests, formatting issues) and helped teammates resolve build-breaking problems.
- Coordinated with team members to maintain consistent command design and avoid merge conflicts during feature integration.

---

### Community

- Participated in PR reviews and debugging discussions
- Suggested improvements in command design and validation handling
