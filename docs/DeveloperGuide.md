# Developer Guide

## Acknowledgements

No external libraries or reused code beyond the Java standard library.

This project follows the structure and concepts taught in CS2113, including the Command pattern and separation of concerns between Parser, Command, and UI components. No external third-party libraries were used beyond standard Java libraries.

---

## Design & Implementation

### Architecture

SpendTrack follows a simple command-driven architecture. The main loop in `SpendTrack.run()` repeatedly reads input, parses it into a `Command`, executes the command, and optionally saves the result to disk.

The key components are:

| Component | Responsibility |
|-----------|---------------|
| `SpendTrack` | Main loop: read → parse → execute → save → repeat |
| `Parser` | Converts raw input string into a `Command` object |
| `Command` (abstract) | Defines the `execute()`, `isExit()`, and `mutatesData()` contract |
| `ExpenseList` | In-memory store of all expenses and budget |
| `Storage` | Persists `ExpenseList` to and from disk |
| `Ui` | All user-facing input and output |

The following sequence diagram shows the full runtime flow of SpendTrack, from launch through the command loop:

![Architecture sequence diagram](images/ArchitectureSequence.png)

Every command follows the same pattern: `Parser.parse()` creates a command object, `command.execute()` reads or mutates `ExpenseList` and calls `Ui` to display results, and if `command.mutatesData()` returns `true`, `Storage.save()` is called automatically. This means no command class ever handles I/O or file persistence directly.

### Delete Expense Feature

The delete feature removes an expense from the list by its 1-based index:

```
delete INDEX
```

#### How it works

1. The user enters `delete 2`.
2. `Parser.parse()` attempts to parse the second token as an integer. If it is missing or non-numeric, a `SpendTrackException` is thrown immediately.
3. A `DeleteCommand(2)` is created and returned.
4. `DeleteCommand.execute()` checks that the index is within bounds (1 to `expenseList.size()` inclusive). If not, a `SpendTrackException` is thrown with the valid range.
5. The expense at `index - 1` (zero-based) is removed from `ExpenseList` via `deleteExpense()`.
6. `Ui.showDeleteSuccess()` displays the deleted expense to confirm the action.
7. Because `mutatesData()` returns `true`, `SpendTrack` calls `Storage.save()` after execution.

The following sequence diagram shows the full delete flow:

![Sequence diagram for delete command](images/DeleteCommandSequence.png)

#### Design considerations

**Aspect: 1-based vs 0-based indexing**

- **Current approach:** User-facing indices are 1-based. `DeleteCommand` converts to 0-based internally by subtracting 1 before calling `ExpenseList.deleteExpense()`.
    - Pros: Matches the numbering shown in `list` output, which is more natural for non-technical users.
    - Cons: Requires a consistent conversion at every index-based command (`delete`, `find`, `edit`).

- **Alternative:** Use 0-based indices throughout.
    - Pros: No conversion needed.
    - Cons: Confusing for users who see item `1.` in the list but must type `delete 0`.

1-based indexing was chosen for usability. The conversion is trivial and centralised within each command's `execute()` method.

**Aspect: Where to validate the index**

Index range validation happens in `DeleteCommand.execute()`, not in `Parser`. The parser only checks that the input is a valid integer. This is because the list size is not known at parse time — `ExpenseList` is only available during execution.

### Add Expense Feature

The add expense feature allows users to record a new expense with a description, amount, category, and optional date using the command:

```
add d/DESCRIPTION a/AMOUNT c/CATEGORY [date/DATE]
```

The `date/` parameter accepts multiple formats: `YYYY-MM-DD`, `DD-MM-YYYY`, `today`, or `yesterday`. If omitted, the expense is tagged with today's date. Categories are automatically normalised to title case (e.g., `food` becomes `Food`, `public transport` becomes `Public Transport`).

#### How it works

The add mechanism follows the Command pattern used throughout SpendTrack. The following steps describe how an add command is processed:

1. The user enters `add d/Coffee a/3.50 c/food date/22-03-2026`.
2. `SpendTrack.run()` passes the raw input to `Parser.parse()`.
3. `Parser.parse()` lowercases the command word and checks the `ALIASES` map (e.g., `a` resolves to `add`). It then delegates to `Parser.parseAddCommand()`.
4. `parseAddCommand()` uses a regex lookahead to split the arguments into tokens. Each token starts with a flag prefix (`d/`, `a/`, `c/`, `date/`, or `recurring/`), allowing the user to enter them in any order.
5. The `c/` value is passed through `normalizeCategory()`, which capitalises the first letter of each word.
6. The `date/` value is passed to `DateParser.parse()`, which tries multiple date formats in sequence (see [Flexible Date Parsing](#flexible-date-parsing-dateparser) below).
7. The extracted values are used to create a new `AddCommand` object.
8. `SpendTrack` calls `AddCommand.execute()`, which creates a new `Expense` and adds it to the `ExpenseList`.
9. `Ui.showAddSuccess()` displays a confirmation message to the user.

The following sequence diagram shows the full flow of the add command, including date parsing and category normalisation:

![Sequence diagram for add command](images/AddCommandSequence.png)

#### Design considerations

**Aspect: How to parse the flag-based input**

- **Alternative 1 (current):** Regex lookahead split.
    - Pros: Users can type flags in any order (`c/Food a/3.50 d/Coffee` also works). Handles descriptions with spaces naturally since the split only occurs before a flag prefix.
    - Cons: The regex is less readable than simple string splitting.

- **Alternative 2:** Split by space and manually iterate.
    - Pros: Simpler to understand.
    - Cons: Breaks if the description contains spaces (e.g., `d/Bus fare` would be split incorrectly).

Alternative 1 was chosen because supporting spaces in descriptions is essential for a natural user experience.

**Aspect: Where to validate input**

Validation is split across two layers:
- `Parser` validates the format (e.g., amount is a valid number, description is non-empty, date is a recognised format).
- `AddCommand` validates the values at runtime using assertions (e.g., amount is non-negative, parameters are not null).

This defence-in-depth approach ensures that even if one layer is bypassed during future refactoring, the other still catches invalid data.

**Aspect: Category normalisation**

- **Current approach:** `Parser.normalizeCategory()` capitalises the first letter of each word before creating the command.
    - Pros: Categories are consistent regardless of how the user types them. `food`, `FOOD`, and `fOoD` all become `Food`. This prevents duplicate categories in the summary.
    - Cons: Users cannot intentionally use unconventional casing (e.g., `WiFi`).

- **Alternative:** Normalise at display time only.
    - Pros: Preserves the user's original input.
    - Cons: `food` and `Food` would be treated as different categories in the summary, leading to fragmented breakdowns.

The current approach was chosen because consistent categories are more important for accurate spending analysis than preserving case.

#### Class structure

The following class diagram shows the relationships between the classes involved in the add and summary commands, including the v2.0 additions (`DateParser`, `SummaryCommand`, date field, alias map):

![Class diagram for add and summary features](images/AddCommandClass.png)

`Parser` resolves command aliases, creates `AddCommand` and `SummaryCommand` objects, and delegates date parsing to `DateParser`. `AddCommand` creates `Expense` objects and interacts with `ExpenseList` to store them. `SummaryCommand` reads from `ExpenseList` and displays grouped statistics via `Ui`. All concrete commands extend the abstract `Command` class.

### Flexible Date Parsing (DateParser)

The `DateParser` class provides flexible date input for the `add` command. It accepts four formats to accommodate different user preferences:

| Input | Interpretation |
|-------|---------------|
| `2026-03-22` | ISO format (YYYY-MM-DD) |
| `22-03-2026` | Singapore format (DD-MM-YYYY) |
| `today` | Current date (`LocalDate.now()`) |
| `yesterday` | Previous day (`LocalDate.now().minusDays(1)`) |

#### How it works

`DateParser.parse()` attempts each format in sequence, falling through to the next if parsing fails:

1. Trim the input and create a lowercased copy for keyword matching.
2. Check the lowercased copy for the keyword `today` — return `LocalDate.now()`.
3. Check the lowercased copy for the keyword `yesterday` — return `LocalDate.now().minusDays(1)`.
4. Try parsing the original trimmed string as ISO format (`YYYY-MM-DD`) using `LocalDate.parse()`. The original (not lowercased) string is used here because ISO date strings are case-sensitive.
5. If that fails, try parsing the original trimmed string as Singapore format (`DD-MM-YYYY`) using `DateTimeFormatter.ofPattern("dd-MM-yyyy")`.
6. If all formats fail, throw a `SpendTrackException` with a message listing the accepted formats.

The following sequence diagram shows the internal parsing flow:

![Sequence diagram for DateParser](images/DateParserSequence.png)

#### Design considerations

**Aspect: How to support multiple date formats**

- **Current approach:** Sequential try-catch fallthrough in a single `parse()` method.
    - Pros: Simple to understand and extend. Adding a new format requires only one additional try-catch block. The keyword checks (`today`, `yesterday`) short-circuit before any format parsing.
    - Cons: Relies on exceptions for control flow, which is generally discouraged.

- **Alternative:** Use a regex to detect the format first, then parse with the correct formatter.
    - Pros: Avoids exception-based control flow.
    - Cons: More complex regex logic, and the regex must be kept in sync with the actual parsing logic.

The current approach was chosen for its simplicity and because `DateTimeParseException` is a lightweight unchecked exception that does not carry significant performance cost for a CLI application.

**Aspect: Extracting DateParser as a separate class**

`DateParser` was extracted from `Parser` to follow the Single Responsibility Principle (SRP). `Parser` is responsible for command routing and flag extraction, while `DateParser` handles date format resolution. This separation also allows other commands (e.g., `filter`, `edit`) to reuse `DateParser` without duplicating parsing logic.

### Category Summary Feature

The category summary feature displays a spending breakdown grouped by category, showing totals, percentages, transaction counts, averages, and maximum amounts per category:

```
summary
```

The `summary` command can also be invoked using the alias `s`.

#### How it works

1. The user enters `summary` (or `s`).
2. `Parser.parse()` resolves the alias and creates a `SummaryCommand`.
3. `SummaryCommand.execute()` retrieves all expenses from `ExpenseList`.
4. If the list is empty, an error message is shown and the command returns.
5. For each expense, three `LinkedHashMap` structures are updated:
    - `categoryTotals` — running sum per category.
    - `categoryCounts` — transaction count per category.
    - `categoryMax` — largest single expense per category.
6. The grand total is computed alongside the per-category totals.
7. Categories are sorted in descending order by total amount.
8. `Ui.showEnhancedSummary()` displays the formatted table with percentage, count, average, and max for each category.

The following sequence diagram shows the summary command execution flow:

![Sequence diagram for summary command](images/SummaryCommandSequence.png)

#### Object diagram: summary state after processing

The following object diagram shows a snapshot of the internal state of `SummaryCommand` after processing three expenses (Coffee $3.50, Lunch $10.00 in Food; Bus $1.20 in Transport):

![Object diagram for summary command state](images/SummaryObjectDiagram.png)

The three maps (`categoryTotals`, `categoryCounts`, `categoryMax`) are built in a single pass over the expense list. The `grandTotal` is computed in the same loop. After sorting, the maps are passed to `Ui.showEnhancedSummary()` which computes the average and percentage for display.

#### Design considerations

**Aspect: Data structure for category grouping**

- **Current approach:** Three separate `LinkedHashMap` instances (totals, counts, max).
    - Pros: Simple and flat. Each map is independently readable and testable. The single-pass loop updates all three maps simultaneously.
    - Cons: Three maps must be kept in sync (all keyed by category name).

- **Alternative:** A custom `CategoryStats` class holding total, count, and max fields.
    - Pros: Encapsulates all statistics for a category in one object. Eliminates the risk of map key mismatches.
    - Cons: Introduces an additional class for a relatively simple computation. Over-engineering for the current scope.

The current approach was chosen because three maps are straightforward for the current feature scope. If more statistics are added in the future, refactoring to a `CategoryStats` class would be a natural next step.

**Aspect: Sorting order**

Categories are sorted in descending order by total spending, so the highest-spend category appears first. This matches the user's primary concern: identifying where they are overspending. The sort uses `Double.compare(b, a)` (reversed) to achieve descending order.

### Command Aliases

To support faster input for experienced users, single-letter aliases are defined for common commands:

| Alias | Full command |
|-------|-------------|
| `a` | `add` |
| `d` | `delete` |
| `l` | `list` |
| `s` | `summary` |
| `b` | `budget` |
| `h` | `help` |

#### How it works

Alias resolution occurs in `Parser.parse()` before the main command switch:

1. The input is split into command word and arguments.
2. The command word is lowercased (so `A`, `a`, and `Add` all work).
3. The `ALIASES` map is checked using `getOrDefault()`. If the command word is a known alias, it is replaced with the full command name.
4. The resolved command word proceeds through the normal switch-case routing.

The alias map is defined as a static `HashMap<String, String>` initialised in a static block, making it easy to add new aliases as new commands are introduced.

### Storage Feature

The storage feature allows SpendTrack to persist expense data and budget across sessions. Expenses are saved to `data/spendtrack.txt` automatically after every mutating command (`add`, `delete`, `edit`), and loaded back on startup.

#### How it works

**Saving:**

1. After every mutating command executes, `SpendTrack` calls `Storage.save(expenseList)`.
2. `Storage` opens `data/spendtrack.txt` using a `try-with-resources` block with a `FileWriter`.
3. Each expense is written as a pipe-delimited line: `DESCRIPTION|AMOUNT|CATEGORY|DATE|RECURRING`.
4. The budget is written at the end after a `---BUDGET---` separator line.
5. If the `data/` directory does not exist, it is created automatically before writing.
6. Any write failure prints a warning — the app does not crash.

**Loading:**

1. On startup, `SpendTrack` calls `Storage.load(expenseList)` before the main command loop.
2. `Storage` reads `data/spendtrack.txt` line by line using a `BufferedReader`.
3. Lines above `---BUDGET---` are parsed into `Expense` objects and added to the `ExpenseList`.
4. The line below `---BUDGET---` is parsed as the saved budget amount.
5. Malformed lines are skipped with a warning message.
6. If the file does not exist, the app starts silently with an empty list.

The following sequence diagram shows the startup load flow, including the startup reminder:

![Sequence diagram for storage load](images/StorageLoadSequence.png)

#### File format

```
DESCRIPTION|AMOUNT|CATEGORY|DATE|RECURRING
Coffee|3.50|Food|2026-03-22|false
Bus fare|1.80|Transport|2026-03-22|false
---BUDGET---
500.00
```

#### Design considerations

**Aspect: Where to trigger saves**

- **Current approach:** Every mutating command calls `Storage.save()` after executing.
    - Pros: Data is never lost even if the app crashes mid-session.
    - Cons: Slightly more I/O per command, but negligible for typical expense list sizes.

- **Alternative:** Save only on `bye` command.
    - Pros: Fewer writes.
    - Cons: Data loss if the app is closed unexpectedly.

**Aspect: File format**

- Pipe (`|`) delimiter was chosen over CSV because expense descriptions may contain commas.
- All file I/O is encapsulated inside `Storage` — no `FileWriter` or `BufferedReader` exists in command classes, keeping the separation of concerns clean.

#### Class structure

The following class diagram shows the relationships between `Storage`, `ExpenseList`, `Expense`, and `SpendTrack`:

![Class diagram for storage feature](images/StorageClassDiagram.png)

`SpendTrack` owns one `Storage` instance and one `ExpenseList`. On startup, `Storage.load()` populates the `ExpenseList` with `Expense` objects parsed from disk. After every mutating command, `SpendTrack` calls `Storage.save()` to persist the current state. All file I/O is contained within `Storage`, keeping command classes and `ExpenseList` free of I/O concerns.

### Filter and Find Features

#### Filter expenses by date range

The `filter` command allows users to view expenses within an inclusive date range:

```
filter from/DATE to/DATE
```

**How it works:**

1. The user enters `filter from/2026-03-01 to/2026-03-31`.
2. `Parser.parse()` delegates to `Parser.parseFilterCommand()`.
3. `parseFilterCommand()` extracts the `from/` and `to/` values and passes each to `DateParser.parse()`.
4. If either date is missing, or `from` is after `to`, a `SpendTrackException` is thrown.
5. A `FilterCommand(from, to)` is created and returned.
6. `FilterCommand.execute()` iterates over all expenses and collects those whose date falls within the range (inclusive).
7. `Ui.showFilteredExpenses()` displays the results in the same table format as `list`.
8. The original `ExpenseList` is not modified — filtering is display-only.

#### Find expense by index

The `find` command displays the full details of a single expense:

```
find INDEX
```

**How it works:**

1. The user enters `find 2`.
2. `Parser.parse()` parses the index as an integer and creates a `FindCommand(2)`.
3. `FindCommand.execute()` checks that the list is non-empty and the index is within bounds (1-based).
4. The expense at `index - 1` is retrieved from `ExpenseList`.
5. `Ui.showExpenseDetail()` displays a labelled detail view with all fields.

The following sequence diagram shows the execution flow for both `filter` and `find`:

![Sequence diagram for filter and find commands](images/FilterFindSequence.png)

#### Design considerations

**Aspect: Filter does not mutate the list**

`FilterCommand` builds a separate `ArrayList<Expense>` of matching expenses and passes it to `Ui.showFilteredExpenses()`. The underlying `ExpenseList` is never modified. This ensures that filtering is a safe, read-only operation that cannot accidentally corrupt data.

**Aspect: Index validation in FindCommand**

`FindCommand` validates the index at execute time rather than parse time. This follows the same pattern as `DeleteCommand` — the parser only checks that the index is a valid integer, while the command checks that it is within the current list bounds. This is necessary because the list size is not known at parse time.

### Edit Expense Feature

The edit expense feature allows users to update one or more fields of an existing expense by its 1-based index:
```
edit INDEX [d/DESCRIPTION] [a/AMOUNT] [c/CATEGORY] [date/YYYY-MM-DD] [recurring/true|false]
```

Only the fields provided are updated — all other fields remain unchanged.

#### How it works

1. The user enters `edit 1 d/Latte a/6.00 recurring/true`.
2. `SpendTrack.run()` passes the input to `Parser.parse()`.
3. `Parser.parse()` identifies the command word `edit` and delegates to `Parser.parseEditCommand()`.
4. `parseEditCommand()` extracts the index from the first token, then splits the remaining arguments using the same flag regex as `parseAddCommand()` to extract only the fields provided. Fields not provided are left as `null`. Duplicate flags throw a `SpendTrackException`.
5. A new `EditCommand` is created with the index and the parsed fields (`null` for unchanged fields).
6. `EditCommand.execute()` validates the index and fields, retrieves the existing `Expense` from `ExpenseList`, constructs an updated `Expense` by substituting `null` fields with the original expense's values, and replaces it via `ExpenseList.setExpense()`.
7. `Ui.showEditSuccess()` displays the before and after state of the expense.

The following sequence diagram illustrates the full flow of the edit command:

![Sequence diagram for edit command](images/EditCommandSequence.png)

#### Design considerations

**Aspect: How to handle partial updates**

- **Current approach:** Fields not provided by the user are passed as `null`. Inside `EditCommand.execute()`, `null` fields fall back to the original expense's values.
  - Pros: Clean separation — `Parser` handles input extraction, `EditCommand` handles the update logic. The command is immutable once constructed.
  - Cons: Using `null` as a sentinel requires null checks throughout `execute()`.

- **Alternative:** Pass the original `Expense` into the command and only override provided fields.
  - Pros: No null checks needed inside the command.
  - Cons: Tighter coupling between `Parser` and `ExpenseList`, since the parser would need to look up the existing expense at parse time rather than at execution time.

The current approach was chosen to keep `Parser` stateless and decoupled from `ExpenseList`.

**Aspect: Duplicate flag detection**

`parseEditCommand()` tracks which flags have been seen using boolean variables (`seenDescription`, `seenAmount`, etc.). If the same flag appears twice in a single edit command, a `SpendTrackException` is thrown immediately. This prevents ambiguous updates like `edit 1 d/Latte d/Coffee` where the intended value is unclear.

**Aspect: Recurring flag as an editable field**

The `recurring/` flag is editable via `edit INDEX recurring/false` to allow users to un-mark an expense. This follows the same `null`-sentinel pattern as other fields — if `recurring/` is not provided, the existing value is preserved. Only `true` or `false` are accepted; any other value throws a `SpendTrackException`.

---

### Remaining Command

The `remaining` command allows users to check how much budget is left after deducting expenses.

#### How it works

1. The user enters `remaining`.
2. `Parser.parse()` identifies the command and creates a `RemainingCommand` object.
3. `RemainingCommand.execute()` calls `ExpenseList.hasBudget()` to check if a budget has been set. If not, a `SpendTrackException` is thrown.
4. If a budget exists, the budget is retrieved using `ExpenseList.getBudget()` and the total expenses using `ExpenseList.getTotal()`.
5. The remaining balance is calculated as: budget - total.
6. `Ui.showRemaining()` displays the result to the user.
7. `mutatesData()` returns `false`, so no save is triggered.

The following sequence diagram illustrates the flow of the remaining command:

![Sequence diagram for remaining command](images/RemainingCommandSequence.png)

#### Design considerations

**Aspect: What to show when no budget is set**

- **Current approach:** Throw a `SpendTrackException` when no budget is set.
    - Pros: Clear and honest feedback — the user knows they must set a budget first. No risk of misleading the user with a `$0.00` figure.
    - Cons: Slightly more abrupt than showing a neutral message.

- **Alternative:** Return `$0.00` remaining when no budget is set.
    - Pros: Always shows a result without an error.
    - Cons: Misleading — `$0.00` remaining implies a budget of zero was set, which is not the case.

The exception approach was chosen because correctness and clarity matter more than always producing output.

**Aspect: Where to compute the remaining balance**

- **Current approach:** Calculated dynamically as budget - total on every call.
    - Pros: Always accurate regardless of how many expenses have been added or removed since the budget was set.
    - Cons: Requires calling both `getBudget()` and `getTotal()` each time.

- **Alternative:** Cache the remaining balance and update it after every mutating command.
    - Pros: O(1) lookup.
    - Cons: Risk of the cached value diverging from actual state if any mutating path forgets to update it.

The on-demand approach was chosen because correctness is more important than micro-optimisation for a student budget tracker.

---

### Help Command

The `help` command provides users with a list of all available commands and their usage.

#### How it works

1. The user enters `help` (or alias `h`).
2. `Parser.parse()` identifies the command and creates a `HelpCommand` object.
3. `HelpCommand.execute()` calls `Ui.showHelp()`, which displays a formatted list of all commands and their formats.
4. `mutatesData()` returns `false`, so no save is triggered.

#### Design considerations

**Aspect: How to generate help content**

- **Current approach:** Help content is hardcoded in `Ui.showHelp()`.
    - Pros: Simple to implement and maintain. No coupling between command classes and the help output.
    - Cons: Must be manually updated when new commands are added.

- **Alternative:** Dynamically generate help content by iterating over all registered command classes.
    - Pros: Help content is always in sync with available commands.
    - Cons: Adds significant complexity for v1.0. Requires each command class to expose metadata about itself.

The hardcoded approach was chosen for simplicity. The help text is short enough that manual maintenance is not a burden.

---

### Search Feature

The `search` command allows users to find expenses based on a keyword in the description.

#### How it works

1. The user enters `search KEYWORD`.
2. `Parser.parse()` creates a `SearchCommand` with the keyword string.
3. `SearchCommand.execute()` iterates through all expenses using `getExpenses()`.
4. For each expense, `.getDescription().toLowerCase().contains(keyword.toLowerCase())` is used for case-insensitive substring matching.
5. Matching expenses are printed with a running count.
6. If no matches are found, `No matches found.` is shown.

The following activity diagram shows the logic of the search feature:

![Activity diagram for search feature](images/SearchActivity.png)

#### Design considerations

**Aspect: Type of keyword matching**

- **Current approach:** Simple case-insensitive substring match.
    - Pros: Intuitive and fast. Partial matches work naturally (e.g. `cof` matches `Coffee`). No configuration needed.
    - Cons: Cannot handle typos or approximate matches.

- **Alternative:** Regex or fuzzy matching.
    - Pros: More powerful — handles typos and partial word boundaries.
    - Cons: Significantly more complex to implement and explain to users. Unexpected results if the user is unfamiliar with regex syntax.

Simple substring matching was chosen because it covers the most common use case and is easy to understand.

---

### Sort Feature

The `sort` command displays all expenses ordered from highest to lowest amount without modifying the underlying list.

#### How it works

1. The user enters `sort`.
2. `Parser.parse()` creates a `SortCommand`.
3. `SortCommand.execute()` copies all expenses into a new `ArrayList` via `getExpenses()`.
4. The copy is sorted in descending order using `Comparator.comparingDouble(Expense::getAmount).reversed()`.
5. Each expense is printed with a 1-based index.
6. `mutatesData()` returns `false`, so no save is triggered.

#### Design considerations

**Aspect: Whether to sort the original list or a copy**

- **Current approach:** Sort a copy of the list. The original list order is preserved.
    - Pros: Index-based commands like `delete` and `edit` still work correctly after `sort` since the underlying list is unchanged.
    - Cons: Requires creating a new `ArrayList` for every `sort` call.

- **Alternative:** Sort the original list in place.
    - Pros: No additional memory allocation.
    - Cons: Permanently changes the insertion order of expenses, breaking index-based commands for the rest of the session.

Sorting a copy was chosen to keep the application state consistent and predictable.

---

### Top N Expenses Feature

The `top N` command shows the N most expensive expenses.

#### How it works

1. The user enters `top N`.
2. `Parser.parse()` parses `N` as an integer and creates a `TopCommand(N)`.
3. `TopCommand.execute()` validates that `count > 0`. If not, an error is shown.
4. If the list is empty, `No expenses recorded.` is shown.
5. All expenses are copied into a local `ArrayList` and sorted in descending order using `Comparator.comparingDouble(Expense::getAmount).reversed()`.
6. The first `Math.min(count, list.size())` entries are displayed via `Ui.showMessage()`.
7. `mutatesData()` returns `false`, so no save is triggered.

#### Design considerations

**Aspect: Whether to sort the original list or a copy**

- **Current approach:** Sort a copy of the list.
    - Pros: Original insertion order is preserved. Index-based commands remain correct after `top` is used.
    - Cons: Slightly more memory usage per call.

- **Alternative:** Sort the original list in place.
    - Pros: Simpler code, no copy needed.
    - Cons: Permanently reorders expenses, corrupting the behaviour of `delete`, `edit`, and `find`.

Sorting a copy was chosen for the same reason as `SortCommand` — correctness of index-based commands takes priority.

**Aspect: Handling N greater than list size**

- **Current approach:** Use `Math.min(count, list.size())` to cap the display count.
    - Pros: Gracefully handles oversized requests without an error. Shows all available expenses instead.
    - Cons: The output header says `Top N expenses` but fewer than N may be shown — could be slightly confusing.

- **Alternative:** Throw an error if N exceeds the list size.
    - Pros: Strictly accurate output label.
    - Cons: Unnecessarily strict for a simple display command.

The capped approach was chosen for a better user experience.

---

### Last N Expenses Feature

The `last N` command shows the N most recently added expenses based on insertion order.

#### How it works

1. The user enters `last N`.
2. `Parser.parse()` creates a `LastCommand(N)`.
3. `LastCommand.execute()` validates that `count > 0`. If not, an error is shown.
4. If the list is empty, `No expenses recorded.` is shown.
5. The start index is calculated as `Math.max(0, total - count)`.
6. Expenses from that index to the end of the list are collected into a new `ArrayList`.
7. They are displayed with a 1-based index.
8. `mutatesData()` returns `false`, so no save is triggered.

#### Design considerations

**Aspect: How to determine the most recent expenses**

- **Current approach:** Use insertion order — take the last N entries from the `ArrayList`.
    - Pros: Simple and fast. No sorting needed. Reflects the order in which the user actually added expenses.
    - Cons: If expenses are added out of chronological order (e.g. backdated with `date/`), the last N by insertion may not be the last N by date.

- **Alternative:** Sort by date descending and take the first N.
    - Pros: Always shows the N most recent by date.
    - Cons: More expensive. Behaviour differs from what "last added" implies. Could confuse users who expect to see what they just typed.

Insertion order was chosen because `last` is intended to mean "last added", not "last by date".

---

### Monthly Report Feature

The `report` command generates a monthly spending summary grouped by category.

#### How it works

1. The user enters `report YYYY-MM`.
2. `Parser.parse()` creates a `ReportCommand` with the month string.
3. `ReportCommand.execute()` splits the input on `-` to extract year and month as integers.
4. All expenses are iterated. For each expense, `getYear()` and `getMonthValue()` are compared to the target month.
5. Matching expenses are accumulated into a running total and a `categoryTotals` `HashMap`.
6. If the total is zero, `No expenses found for YYYY-MM` is shown and the command returns.
7. The total and each category's subtotal are displayed via `Ui.showMessage()`.
8. `mutatesData()` returns `false`, so no save is triggered.

#### Design considerations

**Aspect: Data structure for category grouping**

- **Current approach:** A `HashMap<String, Double>` mapping category name to total.
    - Pros: Simple and efficient. No additional classes needed.
    - Cons: `HashMap` does not guarantee order, so category display order is non-deterministic.

- **Alternative:** Use a `LinkedHashMap` or sort entries before display.
    - Pros: Consistent, predictable output order.
    - Cons: Slightly more code. For a simple report, ordering may not matter much.

`HashMap` was chosen for simplicity. Sorting categories by total descending (like `summary`) is a natural future improvement.

**Aspect: Error handling for malformed input**

- **Current approach:** A broad `catch (Exception e)` catches any parsing failure and shows `Usage: report <YYYY-MM>`.
    - Pros: Simple and robust. Any malformed input (wrong format, missing parts, non-numeric) is caught cleanly.
    - Cons: Catches too broadly — unexpected errors may be silently swallowed.

- **Alternative:** Validate the format explicitly with a regex before parsing.
    - Pros: More precise error detection. Can give more specific error messages.
    - Cons: Adds complexity for a simple format check.

The broad catch was chosen for simplicity in v1.0.

---

### Month Listing Feature

The `month` command lists all individual expenses recorded in a specific month, one per line.

#### How it works

1. The user enters `month YYYY-MM`.
2. `Parser.parse()` creates a `MonthCommand` with the month string.
3. `MonthCommand.execute()` splits the input on `-` to extract year and month.
4. All expenses are iterated. Those whose date matches the target year and month are collected into an `ArrayList`.
5. If the result is empty, `No expenses found for YYYY-MM` is shown.
6. Otherwise, each matching expense is printed with a 1-based index via `Ui.showMessage()`.
7. `mutatesData()` returns `false`, so no save is triggered.

#### Design considerations

**Aspect: Overlap with ReportCommand**

- **Current approach:** `MonthCommand` and `ReportCommand` are separate classes with similar date-matching logic.
    - Pros: Each class has a single clear responsibility. `MonthCommand` lists individual entries; `ReportCommand` aggregates by category. Independent testability.
    - Cons: Some duplication of date-matching logic between the two classes.

- **Alternative:** Merge into a single command with a flag (e.g. `month YYYY-MM --report`).
    - Pros: Less duplication.
    - Cons: Violates Single Responsibility Principle. Makes the command harder to understand and test.

Separate classes were chosen to keep each command focused and independently testable. A shared date-matching utility method could be extracted in a future refactor.

---

### Unknown Command Handling

When the user enters an unrecognised command, the `Parser` returns an `UnknownCommand` object.

#### How it works

1. The user enters an unrecognised command word.
2. `Parser.parse()` falls through all switch cases and returns an `UnknownCommand`.
3. `UnknownCommand.execute()` asserts that `ui` is not null.
4. The event is logged using `java.util.logging.Logger` at warning level.
5. The error message `Unknown command. Try typing 'help' to see available commands.` is built and passed to `ui.showError()`.
6. `mutatesData()` returns `false`, so no save is triggered.

#### Design considerations

**Aspect: How to build the error message**

- **Current approach:** Message is assembled from small private methods (`buildHeader()`, `buildSuggestion()`).
    - Pros: Each part is independently testable. Easy to modify one part without touching others.
    - Cons: Extra methods for a simple string concatenation.

- **Alternative:** Return the full message as a single string literal.
    - Pros: Simpler and more direct.
    - Cons: Harder to test individual parts of the message in isolation.

The decomposed approach was chosen to keep each method focused and testable.

---

### Exit Command

When the user enters `bye`, the `Parser` returns an `ExitCommand`.

#### How it works

1. The user enters `bye`.
2. `Parser.parse()` creates an `ExitCommand`.
3. `ExitCommand.execute()` asserts that `ui` is not null.
4. The farewell message is prepared by combining `"Goodbye"`, `" and take care"`, and `"!"`.
5. The preparation is logged using `Logger`.
6. `ui.showGoodbye()` displays the farewell to the user.
7. `isExit()` returns `true`, which causes `SpendTrack.run()` to exit the main loop.

#### Design considerations

**Aspect: How to build the farewell message**

- **Current approach:** Message is assembled from small private methods (`getGreeting()`, `getClosing()`, `getPunctuation()`).
    - Pros: Each part is independently testable. Consistent with the decomposed style used in `UnknownCommand`.
    - Cons: Verbose for a simple string.

- **Alternative:** Return the full farewell as a single string literal.
    - Pros: Much simpler — one line instead of several methods.
    - Cons: Less granular testability.

The decomposed approach was chosen for consistency with the rest of the codebase and to keep each method focused.

---

### List Expenses Feature

The list feature displays all recorded expenses in a formatted table:
```
list
list recurring
```

The `list recurring` sub-command filters to show only expenses marked as recurring.

#### How it works

1. The user enters `list` or `list recurring`.
2. `Parser.parse()` checks if a second token `recurring` is present.
3. A `ListCommand(false)` or `ListCommand(true)` is created accordingly.
4. `ListCommand.execute()` delegates to either `Ui.showExpenseList()` or `Ui.showRecurringList()`.
5. Both methods calculate column widths dynamically by iterating over all expenses first, then print the table with the correct widths.

The following sequence diagram shows the list command execution flow:

![Sequence diagram for list command](images/ListCommandSequence.png)

#### Design considerations

**Aspect: Dynamic vs fixed column widths**

- **Current approach:** Column widths are calculated at display time by iterating over all expenses first.
  - Pros: Table always aligns correctly regardless of category or description length. `[Entertainment]` and `[Food]` both fit cleanly without overflow.
  - Cons: Requires two passes over the list — one to calculate widths, one to print rows.

- **Alternative:** Fixed column widths.
  - Pros: Simpler code, single pass.
  - Cons: Long categories like `[Entertainment]` overflow and misalign subsequent columns.

Dynamic widths were chosen because alignment correctness matters more than the minor cost of a second pass.

**Aspect: Recurring filter as sub-command vs separate command**

- **Current approach:** `list recurring` is handled as a sub-command of `list` using a boolean flag in `ListCommand`.
  - Pros: Intuitive — both are variants of listing. Single command class handles both cases.
  - Cons: Slight coupling between the list and recurring concerns.

- **Alternative:** A separate `ListRecurringCommand` class.
  - Pros: Cleaner separation of concerns.
  - Cons: Duplicates most of the list logic for a minor variation.

The sub-command approach was chosen for simplicity since the only difference is a filter applied before display.

---

### Budget Feature

The budget feature allows users to set a monthly spending limit, reset it, and view its history:
```
budget AMOUNT
budget reset
budget history
```

#### How it works — Set budget

1. The user enters `budget 500`.
2. `Parser.parseBudgetCommand()` checks for the keywords `reset` and `history` first, then parses the remaining argument as a double.
3. `BudgetCommand.execute()` calls `validateBudget()` — amount must be greater than 0 and not exceed $1,000,000. A `SpendTrackException` is thrown if invalid.
4. `ExpenseList.setBudget()` stores the amount and appends a `date|amount` entry to the budget history list.
5. `Ui.showBudgetSet()` displays the budget, current total spent, and remaining balance.
6. If total spent already exceeds the new budget, `Ui.showBudgetExceeded()` displays a warning.

#### How it works — Reset budget

1. The user enters `budget reset`.
2. `Parser.parseBudgetCommand()` matches the keyword `reset` and returns a `BudgetResetCommand`.
3. `BudgetResetCommand.execute()` checks that a budget is currently set — throws `SpendTrackException` if not.
4. `ExpenseList.resetBudget()` sets the budget field back to 0.0.
5. `Ui.showBudgetReset()` confirms the reset.

#### How it works — Budget history

1. The user enters `budget history`.
2. `Parser.parseBudgetCommand()` matches the keyword `history` and returns a `BudgetHistoryCommand`.
3. `BudgetHistoryCommand.execute()` retrieves the history list from `ExpenseList.getBudgetHistory()`.
4. `Ui.showBudgetHistory()` displays entries in reverse chronological order, skipping any malformed or zero-amount entries.

The following sequence diagram shows all three budget sub-command flows:

![Sequence diagram for budget command](images/BudgetCommandSequence.png)

The following class diagram shows the relationships between the budget-related classes:

![Class diagram for budget feature](images/BudgetClassDiagram.png)

#### Design considerations

**Aspect: Where to store budget history**

- **Current approach:** History stored as `ArrayList<String>` of `date|amount` strings inside `ExpenseList`.
  - Pros: Simple format, easy to serialise to the save file as plain text. No additional class needed.
  - Cons: History entries are raw strings — parsing is required at display time in `Ui`.

- **Alternative:** A dedicated `BudgetRecord` class with `LocalDate date` and `double amount` fields.
  - Pros: Type-safe. No string parsing at display time.
  - Cons: Adds an extra class and requires custom serialisation logic for `Storage`.

The current approach was chosen for simplicity. Refactoring to a `BudgetRecord` class is a natural next step if history features expand in later iterations.

**Aspect: Routing set/reset/history through one parser method**

All three budget sub-commands are routed through `parseBudgetCommand()`. The method checks for the keywords `reset` and `history` first using `equalsIgnoreCase()`, then falls through to numeric parsing. This means a single `budget` switch case handles all three variants cleanly without needing three separate switch cases.

---

### Input Validation Hardening

As part of v2.0, all commands were audited to ensure no user input can cause an unhandled Java exception. The goal was consistent, clear error messages at every entry point.

#### Changes made

| Command | Validation added |
|---------|-----------------|
| `add` | Missing `d/` throws error; missing `a/` throws error; zero/negative amount throws error; non-numeric amount throws error |
| `delete` | Non-integer index throws error; missing index throws error |
| `edit` | Non-integer index; empty/blank description; zero/negative amount; no fields provided; duplicate flags all throw errors |
| `budget` | Empty input throws error; non-numeric amount throws error |

#### Design considerations

**Aspect: Parse-time vs execute-time validation**

Validation is split across two layers intentionally:

- `Parser` validates **format** — is the input a valid number? Is the description non-empty? This is checked at parse time because it requires no knowledge of the current application state.
- Commands validate **value and range** — is the index within bounds? Is the amount positive? This is checked at execute time because it requires access to `ExpenseList`.

This defence-in-depth approach means that even if one layer is bypassed during future refactoring, the other still catches invalid data.

---

### Recurring Expenses Feature

The recurring feature allows users to mark expenses as recurring and filter for them:
```
add ... recurring/true
list recurring
edit INDEX recurring/false
```

#### How it works

1. The user adds `recurring/true` to any `add` command.
2. `Parser.parseAddCommand()` extracts the `recurring/` token using the same flag regex as other tokens, and validates it is either `true` or `false` — throws `SpendTrackException` otherwise.
3. `AddCommand` passes the flag to the `Expense` constructor. If omitted, `isRecurring` defaults to `false`.
4. `list` shows `[R]` next to recurring expenses in the description column.
5. `list recurring` passes `true` to `ListCommand`, which calls `Ui.showRecurringList()` to filter and display only recurring expenses.
6. `edit INDEX recurring/false` un-marks an expense using `EditCommand` with the `newRecurring` field set to `false`.

The following sequence diagram shows the recurring expense flow from add through to list:

![Sequence diagram for recurring expense](images/RecurringExpenseSequence.png)

#### Design considerations

**Aspect: Where to store the recurring flag**

The flag is stored directly on `Expense` as a `boolean isRecurring` field. This keeps it co-located with the other expense data and naturally flows through to `Storage` serialisation as a fifth pipe-delimited field (`DESCRIPTION|AMOUNT|CATEGORY|DATE|RECURRING`). No separate recurring list is maintained — `list recurring` filters on the fly at display time, ensuring the list is always consistent with the current state of expenses.

**Aspect: Validating the recurring/ value**

Only `true` or `false` are accepted — any other value throws a `SpendTrackException`. This is validated in `Parser` at parse time since it requires no state knowledge, consistent with the validation approach used for other flag values.

---

### Total Expenses Feature

The total feature displays the sum of all expense amounts:

```
total
```

#### How it works

1. The user enters `total`.
2. `Parser.parse()` creates a `TotalCommand`.
3. `TotalCommand.execute()` calls `ExpenseList.getTotal()`, which iterates over all expenses and sums their amounts.
4. `Ui.showTotal()` displays the formatted total to the user.
5. `mutatesData()` returns `false`, so no save is triggered.

#### Design considerations

**Aspect: Where to compute the total**

- **Current approach:** `ExpenseList.getTotal()` computes the total on demand by iterating over the list each time it is called.
    - Pros: Always accurate — no risk of the cached total diverging from the actual list after add, delete, or edit operations. Simple to implement and maintain.
    - Cons: O(n) per call. For very large lists, this could be slow.

- **Alternative:** Maintain a running total field in `ExpenseList`, updated on every add/delete/edit.
    - Pros: O(1) lookup.
    - Cons: Must be updated in every mutating method. Risk of inconsistency if a mutating path forgets to update the total. More error-prone during refactoring.

The on-demand approach was chosen because expense lists in a student budget tracker are small (typically under 1000 entries), making the O(n) cost negligible. Correctness is more important than micro-optimisation here.

---

### Budget Alert Feature

The budget alert feature automatically warns users when their spending approaches or exceeds their monthly budget. The check runs after every `add` command — no separate command is needed.

```
add d/Dinner a/50 c/Food
→ [WARNING] You are close to your monthly budget! ($95.00 / $100.00 used)
```

#### How it works

1. After `AddCommand.execute()` adds the expense and displays the success message, it calls `BudgetChecker.check(expenses, ui)`.
2. `BudgetChecker.check()` is a static utility method. It first checks if a budget has been set via `ExpenseList.hasBudget()`. If not, it returns silently.
3. It retrieves the total spent via `ExpenseList.getTotal()` and the budget via `ExpenseList.getBudget()`.
4. It computes the usage ratio (`totalSpent / budget`).
5. If `totalSpent > budget`, it calls `Ui.showBudgetAlert()` to display an exceeded alert.
6. Otherwise, if the usage ratio is at or above 0.9 (90%), it calls `Ui.showBudgetWarning()` to display a warning.
7. If spending is under 90%, no message is shown.

The following sequence diagram shows the budget alert flow after an add command:

![Sequence diagram for budget alert](images/BudgetAlertSequence.png)

#### Design considerations

**Aspect: Where to place the budget check logic**

- **Current approach:** A separate static `BudgetChecker` utility class called from `AddCommand.execute()`.
    - Pros: Follows the Single Responsibility Principle — `AddCommand` handles adding, `BudgetChecker` handles the budget check. Easy to test independently. Can be reused by other commands in the future if needed.
    - Cons: Adds an extra class for a relatively simple check.

- **Alternative:** Inline the check directly in `AddCommand.execute()`.
    - Pros: Fewer classes. All add logic in one place.
    - Cons: Violates SRP. Makes `AddCommand` harder to test in isolation.

The separate class was chosen because it keeps `AddCommand` focused on its primary responsibility and makes the budget check independently testable.

**Aspect: Threshold value**

The 90% warning threshold is defined as a named constant (`WARNING_THRESHOLD = 0.9`) in `BudgetChecker` to avoid magic numbers. The exact boundary at 100% uses `totalSpent > budget` (strict greater than) so that spending exactly at budget shows a warning rather than an alert — the user has not yet exceeded their limit.

---

### Clear All Expenses Feature

The clear feature removes all expenses from the list after a confirmation prompt:

```
clear
```

#### How it works

1. The user enters `clear`.
2. `Parser.parse()` creates a `ClearCommand`.
3. `ClearCommand.execute()` first checks if the expense list is empty. If so, it shows `No expenses to clear.` and returns without prompting.
4. If the list is non-empty, it shows a confirmation prompt via `Ui.showMessage()` asking `Are you sure...? (yes/no):`.
5. It reads the user's response via `Ui.readCommand()`.
6. If the response is `yes` (case-insensitive), it calls `ExpenseList.clearAll()` which clears the internal `ArrayList`, then shows a success message with the count of removed expenses.
7. Any other input cancels the operation with `Clear cancelled.`
8. Because `mutatesData()` returns `true`, `Storage.save()` is called after execution.

The following sequence diagram shows the clear command flow:

![Sequence diagram for clear command](images/ClearCommandSequence.png)

#### Design considerations

**Aspect: Confirmation before destructive action**

- **Current approach:** Require the user to type `yes` to confirm. Any other input cancels.
    - Pros: Prevents accidental data loss. The user must explicitly opt in. Case-insensitive matching (`YES`, `Yes`, `yes` all work) is forgiving.
    - Cons: Adds an extra step for the user.

- **Alternative:** Clear immediately without confirmation.
    - Pros: Faster for the user.
    - Cons: A single typo could delete all data with no recovery (unless undo is available).

Confirmation was chosen because clearing all expenses is irreversible and high-risk. The minor inconvenience of typing `yes` is worth the safety.

**Aspect: Empty list handling**

If the list is already empty, `ClearCommand` shows `No expenses to clear.` without prompting. This avoids confusing the user with a confirmation prompt when there is nothing to clear.

---

### Undo Feature

The undo feature restores the expense list to its state before the last mutating command:

```
undo
```

#### How it works

1. `SpendTrack` owns an `UndoManager` instance, created on startup.
2. Before every mutating command executes (except `undo` itself), `SpendTrack` calls `UndoManager.saveSnapshot(expenses)`.
3. `saveSnapshot()` creates a deep copy of all `Expense` objects in the list and stores the current budget value. This ensures the snapshot is independent of future mutations.
4. When the user enters `undo`, `Parser` creates an `UndoCommand` with a reference to the `UndoManager`.
5. `UndoCommand.execute()` calls `UndoManager.undo(expenses)`.
6. `undo()` checks if a snapshot exists. If not, it returns `false` and the command prints `Nothing to undo.`
7. If a snapshot exists, it calls `ExpenseList.restoreFrom()` which replaces the internal expense list and budget with the snapshot data. The snapshot is then consumed (set to `null`), preventing a second undo.
8. Because `mutatesData()` returns `true`, `Storage.save()` persists the restored state.

The following sequence diagram shows the undo flow:

![Sequence diagram for undo command](images/UndoSequence.png)

The following class diagram shows the relationships between the undo-related classes:

![Class diagram for undo feature](images/UndoClassDiagram.png)

#### Design considerations

**Aspect: Snapshot depth**

- **Current approach:** Single-level undo. Only one snapshot is stored at a time. A second consecutive `undo` prints `Nothing to undo.`
    - Pros: Simple implementation. Low memory usage. Predictable behaviour — the user always knows exactly one step can be undone.
    - Cons: Cannot undo multiple steps.

- **Alternative:** Multi-level undo using a stack of snapshots.
    - Pros: More flexible — users can undo several steps.
    - Cons: Higher memory usage (each snapshot is a full deep copy). More complex to implement and test. Risk of unbounded memory growth.

Single-level undo was chosen because it covers the most common use case (undoing a mistake immediately after making it) without the complexity of multi-level undo.

**Aspect: Deep copy vs shallow copy**

`UndoManager.deepCopyExpenses()` creates new `Expense` objects for each entry in the list. This is necessary because `Expense` fields are mutable (via setters used by `EditCommand`). A shallow copy would share object references, causing the snapshot to be corrupted when the original expenses are modified.

**Aspect: Excluding undo from the snapshot**

`SpendTrack` explicitly skips `saveSnapshot()` when the command is an `UndoCommand` (checked via `instanceof`). Without this, undoing would save the current (post-undo) state as the new snapshot, making it impossible to redo or detect "nothing to undo".

---

### Export CSV Feature

The export feature writes all expenses to a CSV file:

```
export csv
```

#### How it works

1. The user enters `export csv`.
2. `Parser.parse()` checks for the `csv` sub-command. If missing, a `SpendTrackException` is thrown with usage instructions.
3. An `ExportCommand` is created and returned.
4. `ExportCommand.execute()` checks if the expense list is empty. If so, it shows `No expenses to export.` and returns without creating a file.
5. `ensureDirectoryExists()` creates the `data/` directory if it does not exist.
6. A `FileWriter` is opened using `try-with-resources` for `data/spendtrack_export.csv`.
7. The CSV header row (`Description,Amount,Category,Date,Recurring`) is written first.
8. For each expense, `formatCsvRow()` formats the row. If the description contains a comma or double quote, it is wrapped in double quotes with internal quotes escaped (`"` → `""`).
9. On success, a confirmation message is shown. On `IOException`, a warning is printed and the app continues.

The following sequence diagram shows the export flow:

![Sequence diagram for export command](images/ExportCommandSequence.png)

#### Design considerations

**Aspect: CSV quoting strategy**

- **Current approach:** Only descriptions containing commas or double quotes are quoted. All other fields are written as-is.
    - Pros: Produces cleaner CSV output for the common case. Follows RFC 4180 conventions.
    - Cons: If a category ever contains a comma (unlikely with current normalisation), it would break the CSV.

- **Alternative:** Always quote all string fields.
    - Pros: Guaranteed safe regardless of content.
    - Cons: Noisier output — every row has unnecessary quotes.

The selective quoting approach was chosen because category normalisation prevents commas in categories, and amounts/dates/booleans never contain commas.

**Aspect: Testability**

`ExportCommand` accepts a custom file path via a second constructor, allowing tests to write to a temporary file rather than the production path. The `formatCsvRow()` method is package-private (`static`) so tests can verify CSV formatting independently of file I/O.

---

### Savings Goal Feature

The savings goal feature lets users set a monthly savings target and track progress:

```
goal g/AMOUNT
goal status
```

#### How it works

**Setting a goal:**

1. The user enters `goal g/200`.
2. `Parser.parseGoalCommand()` extracts the `g/` value, validates it is a positive number, and creates a `GoalCommand(200.0)`.
3. `GoalCommand.execute()` calls `ExpenseList.setGoal(200.0)` to store the goal.
4. A confirmation message is printed.
5. Because `mutatesData()` returns `true`, `Storage.save()` persists the goal.

**Viewing status:**

1. The user enters `goal status`.
2. `Parser.parseGoalCommand()` matches the keyword `status` and creates a `GoalCommand()` (status mode).
3. `GoalCommand.execute()` checks `ExpenseList.hasGoal()`. If no goal is set, it prints `No savings goal set.` and returns.
4. Otherwise, it retrieves the goal and total spent, computes `saved = goal - spent`.
5. If `saved >= 0`, it displays the saved amount and percentage. If `saved < 0`, it shows `Goal not reached. Over by $X.XX.`
6. `mutatesData()` returns `false` for status queries, so no save is triggered.

The following sequence diagram shows both the set and status flows:

![Sequence diagram for goal command](images/GoalCommandSequence.png)

#### Design considerations

**Aspect: Single command class for set and status**

- **Current approach:** `GoalCommand` handles both `goal g/AMOUNT` (set) and `goal status` (view) using a boolean `isStatusRequest` flag.
    - Pros: Keeps related functionality together. The `goal` command namespace is intuitive — `goal g/200` to set, `goal status` to view.
    - Cons: One class handles two concerns.

- **Alternative:** Separate `SetGoalCommand` and `GoalStatusCommand`.
    - Pros: Cleaner SRP.
    - Cons: Two classes for a simple feature. The `mutatesData()` distinction is already handled by the boolean flag.

The single-class approach was chosen for simplicity, following the same pattern as `BudgetCommand` which handles `budget AMOUNT`, `budget reset`, and `budget history` through parser routing.

**Aspect: Persistence**

The goal is stored as a `double goal` field in `ExpenseList` and persisted via `Storage` using a `---GOAL---` marker in the save file, following the same pattern as budget and budget history. This keeps all state in one place and requires no additional file.

## Product scope

### Target user profile

NUS students who want a fast, keyboard-driven way to track daily spending. The target user prefers typing commands over clicking through a GUI, is comfortable with a CLI, and wants to quickly log expenses on the go.

### Value proposition

SpendTrack helps students track expenses faster than a typical GUI app. Users can add, delete, list, and analyse expenses with short typed commands. Budget tracking and spending summaries help students stay within their means.

## User Stories

| Version | As a ... | I want to ... | So that I can ... |
|---------|----------|---------------|-------------------|
| v1.0 | new user | see usage instructions | refer to them when I forget how to use the application |
| v1.0 | student | add an expense with description, amount, and category | keep track of my spending |
| v1.0 | student | delete an expense by index | remove entries I added by mistake |
| v1.0 | student | list all my expenses | see everything I have spent |
| v1.0 | student | view the total of all expenses | know how much I have spent overall |
| v1.0 | student | set a monthly budget | control my spending |
| v1.0 | student | view my remaining balance | know how much I can still spend |
| v1.0 | student | see usage instructions | refer to them when I forget how to use the application |
| v1.0 | student | check my remaining budget | understand how much I can still spend |
| v1.0 | student | view help commands | know how to use the system easily |
| v2.0 | student | tag expenses with a date | log past purchases I forgot to record |
| v2.0 | student | enter dates in DD-MM-YYYY or use "today"/"yesterday" | log expenses quickly without remembering ISO format |
| v2.0 | student | view a category breakdown with statistics | see where I am overspending and by how much |
| v2.0 | fast typist | use short aliases for commands | log expenses quickly without typing full command names |
| v2.0 | student | have categories auto-normalised | avoid duplicate categories due to inconsistent casing |
| v2.0 | student | search expenses by keyword | find a specific purchase quickly |
| v2.0 | student | save and load expenses from file | keep my data between sessions |
| v2.0 | student | filter expenses by date range | analyse spending over specific periods |
| v2.0 | student | view full details of a single expense by index | inspect it without scrolling the entire list |
| v2.0 | forgetful user | see my last logged expense on startup | avoid logging duplicate entries |
| v2.0 | student | list only recurring expenses | identify habitual spending patterns |
| v2.0 | student | mark an expense as recurring | track regular purchases |
| v2.0 | student | edit any field of an existing expense | correct mistakes without deleting and re-adding |
| v2.0 | student | set and reset a monthly budget | control my spending flexibly |
| v2.0 | student | view budget history | track how my budget has changed over time |
| v2.0 | student | be warned when approaching my budget | avoid overspending before it is too late |
| v2.0 | student | clear all expenses at once | start fresh for a new month |
| v2.0 | student | undo my last action | recover from mistakes quickly |
| v2.0 | student | export expenses to CSV | open them in Excel or Google Sheets |
| v2.0 | student | set a savings goal and track progress | stay motivated to save money |
| v2.0 | student | search for expenses by keyword | locate specific expenses quickly |
| v2.0 | student | sort expenses by amount | see my biggest expenses at a glance |
| v2.0 | student | view the top N most expensive expenses | identify my largest spending items |
| v2.0 | student | view the last N expenses I added | quickly review my most recent spending |
| v2.0 | student | generate a monthly spending report | analyse my spending for a given month |
| v2.0 | student | list all expenses for a specific month | review individual transactions in a month |

## Non-Functional Requirements

1. Should work on any mainstream OS (Windows, macOS, Linux) with Java 17 installed.
2. Should respond to any command within 1 second.
3. A user with average typing speed should be able to log an expense faster than using a GUI app.
4. Data files should be human-readable plain text. 
5. The application should handle invalid inputs gracefully without crashing. 
6. The code should follow object-oriented design principles. 
7. Logging and assertions should be used for debugging and reliability.

## Glossary

* *Expense* - A single spending entry with a description, amount, category, and date.
* *Budget* - The total amount of money allocated for spending.
* *Remaining Balance* - The amount of money left after subtracting expenses from the budget.
* *Mutating command* - A command that changes the expense list (add, delete, edit).
* *Category normalisation* - Automatic capitalisation of the first letter of each word in a category name (e.g., `public transport` becomes `Public Transport`).
* *Alias* - A single-letter shortcut for a command (e.g., `a` for `add`, `s` for `summary`).
* *DateParser* - A utility class that parses date strings in multiple formats (ISO, Singapore, keywords).
* *Command* - A user input that triggers a specific action in the system.
* *Keyword* - A search term used to filter expenses by description.
* *Monthly Report* - A summary of all expenses in a given month grouped by category.
* *Insertion order* - The order in which expenses were added by the user, preserved by the underlying `ArrayList`

## Instructions for manual testing

### Launch

1. Ensure Java 17 is installed.
2. Download the latest `spendtrack.jar` from the GitHub releases page.
3. Open a terminal, navigate to the folder containing the JAR, and run: `java -jar spendtrack.jar`

### Adding an expense

1. Type `add d/Coffee a/3.50 c/Food` and press Enter.
2. Expected: confirmation message showing the added expense with today's date and category `Food`.
3. Type `add d/Lunch a/12.00 c/food date/22-03-2026` to test DD-MM-YYYY format and category normalisation.
4. Expected: date shown as `2026-03-22`, category shown as `Food` (capitalised).
5. Type `add d/Snack a/2.00 c/Food date/yesterday` to test keyword date input.
6. Expected: date shown as yesterday's date.
7. Type `add d/Fail a/3.00 c/Food date/` to test empty date.
8. Expected: error message listing accepted date formats.
9. Type `a d/Tea a/1.50 c/Food` to test the `a` alias.
10. Expected: same behaviour as `add`.

### Category summary

1. Add several expenses across different categories.
2. Type `summary` (or `s`) and press Enter.
3. Expected: categories listed in descending order by total, with percentage, transaction count, average, and max per category.
4. Type `summary` with no expenses in the list.
5. Expected: error message `No expenses recorded yet.`

### Deleting an expense

1. Type `list` to see current expenses and their indices.
2. Type `delete 1` to remove the first expense.
3. Expected: confirmation showing the deleted expense.
4. Type `delete 999` to test out-of-range index.
5. Expected: error message showing the valid range.

### Save and load

1. Add a few expenses and set a budget.
2. Type `bye` to exit.
3. Relaunch the app: `java -jar spendtrack.jar`
4. Expected: all expenses and budget restored. Last expense shown as a reminder on startup.
5. Open `data/spendtrack.txt` to verify the file format.

### Filtering by date range

1. Add expenses with explicit dates: `add d/Coffee a/3.50 c/Food date/2026-03-01`
2. Type `filter from/2026-03-01 to/2026-03-31`
3. Expected: only expenses within that range shown.
4. Type `filter from/2026-03-31 to/2026-03-01`
5. Expected: error message — start date must be before end date.

### Finding an expense by index

1. Type `list` to see current expenses.
2. Type `find 1` to view details of the first expense.
3. Expected: detailed view with all fields.
4. Type `find 999`
5. Expected: out-of-range error message.

### Setting a budget

1. Type `budget 500` to set a $500 budget.
2. Expected: confirmation showing budget, current total, and remaining.
3. Type `remaining` to verify the remaining balance.
4. Type `budget -10` to test negative amount.
5. Expected: error message.

### Viewing remaining budget
1. Set a budget using the budget command.
2. Add some expenses.
3. Enter `remaining`
4. Verify that the remaining balance is displayed correctly.

### Using the help command
1. Enter `help`
2. Verify that a list of commands and their usage is displayed.
3. Enter `h` to test the alias.
4. Expected: same output as `help`.

### Searching for expenses
1. Add several expenses with different descriptions.
2. Enter `search coffee`
3. Verify that only expenses containing "coffee" are shown (case-insensitive).
4. Enter `search zzz` for a keyword with no matches.
5. Expected: `No matches found.`

### Sorting expenses by amount
1. Add several expenses with different amounts.
2. Enter `sort`
3. Verify that expenses are displayed from highest to lowest amount.
4. Enter `list` and verify the original order is unchanged.

### Viewing top expenses
1. Add several expenses with different amounts.
2. Enter `top 3`
3. Verify the 3 most expensive expenses are shown in descending order.
4. Enter `top 0`
5. Expected: error message — number must be greater than 0.

### Viewing most recent expenses
1. Add several expenses.
2. Enter `last 3`
3. Verify the last 3 expenses added are shown in insertion order.
4. Enter `last 0`
5. Expected: error message — number must be greater than 0.

### Generating a monthly report
1. Add expenses with explicit dates: `add d/Coffee a/3.50 c/Food date/2026-03-22`
2. Enter `report 2026-03`
3. Verify total and category breakdown for March 2026 is shown.
4. Enter `report 2026-04` for a month with no expenses.
5. Expected: `No expenses found for 2026-04`
6. Enter `report abc`
7. Expected: `Usage: report <YYYY-MM>`

### Viewing expenses by month
1. Add expenses with explicit dates across different months.
2. Enter `month 2026-03`
3. Verify only expenses from March 2026 are listed individually.
4. Enter `month 2026-04` for a month with no expenses.
5. Expected: `No expenses found for 2026-04`

### Listing expenses

1. Add a few expenses then type `list`.
2. Expected: formatted table with dynamic column widths.
3. Type `add d/Netflix a/18.00 c/Entertainment recurring/true`.
4. Type `list recurring`.
5. Expected: only Netflix shown with `[R]` tag.

### Editing an expense

1. Type `list` to see current expenses.
2. Type `edit 1 d/Latte a/6.00` to edit description and amount.
3. Expected: before and after shown.
4. Type `edit 1 recurring/true` to mark as recurring.
5. Type `edit 999 d/Test` to test out-of-range.
6. Expected: error message.

### Budget reset and history

1. Type `budget 500` then `budget 300` to set two budgets.
2. Type `budget history` to view history.
3. Expected: entries in reverse chronological order.
4. Type `budget reset` to clear the budget.
5. Type `budget reset` again.
6. Expected: error — no budget to reset.

