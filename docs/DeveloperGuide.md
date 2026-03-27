# Developer Guide

## Acknowledgements

No external libraries or reused code beyond the Java standard library.

## Design & Implementation

### Add Expense Feature

The add expense feature allows users to record a new expense with a description, amount, and category using the command:

```
add d/DESCRIPTION a/AMOUNT c/CATEGORY
```

#### How it works

The add mechanism follows the Command pattern used throughout SpendTrack. The following steps describe how an add command is processed:

1. The user enters `add d/Coffee a/3.50 c/Food`.
2. `SpendTrack.run()` passes the raw input to `Parser.parse()`.
3. `Parser.parse()` identifies the command word `add` and delegates to `Parser.parseAddCommand()`.
4. `parseAddCommand()` uses a regex lookahead `" (?=[dac]/)"` to split the arguments into tokens. Each token starts with a flag prefix (`d/`, `a/`, or `c/`), allowing the user to enter them in any order.
5. The extracted values are used to create a new `AddCommand` object.
6. `SpendTrack` calls `AddCommand.execute()`, which creates a new `Expense` and adds it to the `ExpenseList`.
7. `Ui.showAddSuccess()` displays a confirmation message to the user.

The following sequence diagram shows the full flow of the add command:

![Sequence diagram for add command](images/AddCommandSequence.png)

#### Design considerations

**Aspect: How to parse the flag-based input**

- **Alternative 1 (current):** Regex lookahead split `" (?=[dac]/)"`.
    - Pros: Users can type flags in any order (`a/3.50 d/Coffee c/Food` also works). Handles descriptions with spaces naturally since the split only occurs before a flag prefix.
    - Cons: The regex is less readable than simple string splitting.

- **Alternative 2:** Split by space and manually iterate.
    - Pros: Simpler to understand.
    - Cons: Breaks if the description contains spaces (e.g., `d/Bus fare` would be split incorrectly).

Alternative 1 was chosen because supporting spaces in descriptions is essential for a natural user experience.

**Aspect: Where to validate input**

Validation is split across two layers:
- `Parser` validates the format (e.g., amount is a valid number).
- `AddCommand` validates the values at runtime using assertions (e.g., amount is non-negative, parameters are not null).

This defence-in-depth approach ensures that even if one layer is bypassed during future refactoring, the other still catches invalid data.

#### Class structure

The following class diagram shows the relationships between the classes involved in the add command:

![Class diagram for add command](images/AddCommandClass.png)

`Parser` creates `AddCommand` objects. `AddCommand` creates `Expense` objects and interacts with `ExpenseList` to store them. All concrete commands extend the abstract `Command` class, which defines the `execute()` method.

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

The following sequence diagram shows the startup load flow:

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

### Edit Expense Feature

The edit expense feature allows users to update one or more fields of an existing expense by its 1-based index:
```
edit INDEX [d/DESCRIPTION] [a/AMOUNT] [c/CATEGORY] [date/YYYY-MM-DD]
```

Only the fields provided are updated — all other fields remain unchanged.

#### How it works

1. The user enters `edit 1 d/Latte a/6.00`.
2. `SpendTrack.run()` passes the input to `Parser.parse()`.
3. `Parser.parse()` identifies the command word `edit` and delegates to `Parser.parseEditCommand()`.
4. `parseEditCommand()` extracts the index from the first token, then splits the remaining arguments using the same flag regex as `parseAddCommand()` to extract only the fields provided. Fields not provided are left as `null`.
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

### [Proposed] Date Tagging Extension

In v2.0, the add command will support an optional `date/` parameter:

```
add d/Coffee a/3.50 c/Food date/2026-03-20
```

If omitted, the expense will be tagged with today's date using `LocalDate.now()`. This change requires:
- Adding a `LocalDate date` field to the `Expense` class.
- Extending `Parser.parseAddCommand()` to extract the optional `date/` token.
- Updating `Ui.showExpenseList()` to display the date column.

The date field is foundational for several planned features including filtering by date range, sorting by date, listing by month, and monthly spending reports.

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
| v2.0 | student | tag expenses with a date | log past purchases I forgot to record |
| v2.0 | student | view a category breakdown | see where I am overspending |
| v2.0 | student | search expenses by keyword | find a specific purchase quickly |
| v2.0 | student | save and load expenses from file | keep my data between sessions |
| v2.0 | student | filter expenses by date range | analyse spending over specific periods |
| v2.0 | student | view full details of a single expense by index | inspect it without scrolling the entire list |
| v2.0 | forgetful user | see my last logged expense on startup | avoid logging duplicate entries |

## Non-Functional Requirements

1. Should work on any mainstream OS (Windows, macOS, Linux) with Java 17 installed.
2. Should respond to any command within 1 second.
3. A user with average typing speed should be able to log an expense faster than using a GUI app.
4. Data files should be human-readable plain text.

## Glossary

* *Expense* - A single spending entry with a description, amount, and category.
* *Budget* - A monthly spending limit set by the user.
* *Remaining balance* - The difference between the budget and total expenses.
* *Mutating command* - A command that changes the expense list (add, delete, edit).

## Instructions for manual testing

### Launch

1. Ensure Java 17 is installed.
2. Download the latest `spendtrack.jar` from the GitHub releases page.
3. Open a terminal, navigate to the folder containing the JAR, and run: `java -jar spendtrack.jar`

### Adding an expense

1. Type `add d/Coffee a/3.50 c/Food` and press Enter.
2. Expected: confirmation message showing the added expense.
3. Type `list` to verify the expense appears.

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
