# SpendTrack User Guide

## Introduction

SpendTrack is a command-line expense tracker for NUS students who prefer typing over clicking. It lets you log, categorise, and analyse daily spending with short typed commands.

## Quick Start

1. Ensure that you have Java 17 or above installed.
2. Download the latest `spendtrack.jar` from the [GitHub releases page](https://github.com/AY2526S2-CS2113-T11-1/tp/releases).
3. Open a terminal, navigate to the folder containing the JAR file.
4. Run: `java -jar spendtrack.jar`
5. You should see the welcome message. Start typing commands.

## Features

**Notes about command format:**

- Words in `UPPER_CASE` are parameters to be supplied by the user.
  e.g. in `add d/DESCRIPTION`, `DESCRIPTION` is a parameter like `d/Coffee`.
- Items in square brackets are optional.
  e.g. `add d/DESCRIPTION a/AMOUNT c/CATEGORY [date/DATE]` can be used with or without the `date/` parameter.
- Parameters can be in any order.
  e.g. `add d/Coffee a/3.50 c/Food` and `add c/Food a/3.50 d/Coffee` are both valid.
- Most commands have single-letter aliases shown in parentheses.

### Adding an expense: `add`

Adds a new expense with a description, amount, category, and optional date.

Format: `add d/DESCRIPTION a/AMOUNT c/CATEGORY [date/DATE]`

- `AMOUNT` must be a positive number.
- `CATEGORY` is automatically capitalised (e.g. `food` becomes `Food`, `public transport` becomes `Public Transport`).
- `DATE` accepts multiple formats:
  - `YYYY-MM-DD` (e.g. `date/2026-03-22`)
  - `DD-MM-YYYY` (e.g. `date/22-03-2026`)
  - `today` (e.g. `date/today`)
  - `yesterday` (e.g. `date/yesterday`)
- If `date/` is omitted, defaults to today's date.

Alias: `a`

Examples:

- `add d/Coffee a/3.50 c/Food` — adds with today's date, category `Food`
- `add d/Lunch a/12.00 c/food date/22-03-2026` — category normalised to `Food`, date parsed as 2026-03-22
- `add d/Grab a/15.00 c/public transport date/yesterday` — category becomes `Public Transport`
- `a d/Tea a/2.00 c/Food` — using alias

Expected output:
```
____________________________________________________________
 New expense added:
   [Food] Coffee - $3.50 (2026-03-29)
____________________________________________________________
```

### Deleting an expense: `delete`

Removes an expense from the list by its index. SpendTrack will ask you to confirm before deleting.

Format: `delete INDEX`

- `INDEX` is 1-based (same numbering as `list`).
- Use `list` first to find the index of the expense you want to delete.
- Type `yes` to confirm deletion, or anything else to cancel.

Alias: `d`

Examples:
- `delete 1` — prompts for confirmation, then removes the first expense
- `d 2` — using alias, removes the second expense

Expected output (confirmed):
```
____________________________________________________________
 About to delete:
   [Food] Coffee - $3.50 (2026-03-22)
 Are you sure? (yes/no)
 > yes
____________________________________________________________
____________________________________________________________
 Expense deleted:
   [Food] Coffee - $3.50 (2026-03-22)
____________________________________________________________
```

Expected output (cancelled):
```
____________________________________________________________
 About to delete:
   [Food] Coffee - $3.50 (2026-03-22)
 Are you sure? (yes/no)
 > no
____________________________________________________________
____________________________________________________________
 Delete cancelled.
____________________________________________________________
```

Error cases:
- `delete 0` or `delete 99` (out of range) → `Index 99 is out of range. There are 3 expense(s).`
- `delete abc` or `delete` (missing/invalid index) → `delete requires a number. Usage: delete <index>`

---

### Viewing total expenses: `total`

Displays the total sum of all recorded expenses.

Format: `total`

Example: `total`

Expected output:
```
____________________________________________________________
 Total expenses: $32.30
____________________________________________________________
```

If no expenses have been added:
```
____________________________________________________________
 Total expenses: $0.00
____________________________________________________________
```

---

### Viewing category summary: `summary`

Displays a spending breakdown grouped by category, sorted from highest to lowest spend. Shows total, percentage, transaction count, average, and maximum for each category.

Format: `summary`

Alias: `s`

Example: `summary` or `s`

Expected output:
```
____________________________________________________________
 ===== Spending Summary =====
 Food            : $15.50    (48%)  | 2 txns  | avg $7.75  | max $12.00
 Public Transport: $15.00    (46%)  | 1 txn  | avg $15.00  | max $15.00
 Transport       : $1.80     (6%)  | 1 txn  | avg $1.80  | max $1.80
 ----------------------------
 Total           : $32.30
____________________________________________________________
```

Each row shows:
- **Category name** and **total spent** in that category
- **Percentage** of overall spending
- **txns** — number of transactions (i.e. expenses) in that category
- **avg** — average amount per transaction
- **max** — largest single expense in that category

Notes:
- Categories are sorted from highest to lowest total.
- If there are no expenses, shows: `No expenses recorded yet.`

### Command aliases

Most commands have single-letter shortcuts for faster input:

| Alias | Full command |
|-------|-------------|
| `a` | `add` |
| `d` | `delete` |
| `l` | `list` |
| `s` | `summary` |
| `b` | `budget` |
| `h` | `help` |

Aliases work exactly like the full command. For example, `a d/Coffee a/3.50 c/Food` is identical to `add d/Coffee a/3.50 c/Food`.

Commands are case-insensitive: `ADD`, `Add`, and `add` all work.

### Saving and loading data: auto-save

SpendTrack automatically saves your expenses and budget to `data/spendtrack.txt` after every `add`, `delete`, and `edit`. The file is created automatically if it does not exist.

On startup, SpendTrack loads your saved data before accepting commands. If the save file is missing, the app starts with an empty list silently.

Each expense is validated on load — entries with a blank description, non-positive amount, amount exceeding $1,000,000, or a date before the year 2000 are skipped with a warning. The rest of your data is still loaded normally.

You do not need to run any save or load command. It happens automatically.

**File encryption**

The save file is encrypted using AES-128-CBC with a key derived from your machine (OS name and username). This means:
- The file is not human-readable and cannot be manually edited.
- If the file is tampered with, SpendTrack will detect this on startup, reject the file, and start fresh with a warning:
  ```
  Warning: save file could not be decrypted. It may have been tampered with or created on a different machine. Starting fresh.
  ```
- The save file is tied to your machine — it cannot be transferred to another computer.

**Startup reminder**

If you have existing expenses, SpendTrack shows your most recently added expense when it starts:

```
____________________________________________________________
 Welcome to SpendTrack!
 ...
____________________________________________________________
 Last recorded expense: Coffee | $3.50 | Food | 2026-03-22
```

This helps you avoid accidentally logging the same expense twice.

---

### Filtering expenses by date range: `filter`

Shows only expenses whose date falls within the given range (inclusive).

Format: `filter from/DATE to/DATE`

- `DATE` accepts the same formats as the `add` command: `YYYY-MM-DD`, `DD-MM-YYYY`, `today`, `yesterday`.
- The `from` date must not be after the `to` date.
- Filtering does not modify the expense list.

Examples:

- `filter from/2026-03-01 to/2026-03-31` — shows all expenses in March 2026
- `filter from/today to/today` — shows only today's expenses
- `filter from/2026-03-15 to/2026-03-22` — shows expenses between 15 and 22 March

Expected output:
```
____________________________________________________________
 Expenses from 2026-03-15 to 2026-03-22
____________________________________________________________
  #    Category     Description  Date          Amount
  ---  -----------  -----------  ----------    --------
  1.   [Food]       Coffee       2026-03-15    $3.50
  2.   [Transport]  Bus          2026-03-22    $1.80
____________________________________________________________
 Total entries: 2
____________________________________________________________
```

If no expenses fall in the range:
```
 No expenses found in the given date range.
```

Error cases:
- `filter from/2026-03-31 to/2026-03-01` → `Start date must be before end date.`
- Missing `from/` or `to/` → `Usage: filter from/YYYY-MM-DD to/YYYY-MM-DD`

---

### Viewing a single expense: `find`

Displays the full details of one expense by its index in the list.

Format: `find INDEX`

- `INDEX` is 1-based (same numbering as `list`).
- Use `list` first to find the index of the expense you want.

Examples:

- `find 3` — shows full details of expense #3

Expected output:
```
____________________________________________________________
 ===== Expense #3 =====
 Description : Grab to airport
 Amount      : $24.50
 Category    : Transport
 Date        : 2026-03-15
____________________________________________________________
```

Error cases:
- `find 0` or `find 99` (out of range) → `Index X is out of range. There are Y expense(s).`
- `find abc` → `Index must be a whole number. Usage: find <index>`
- `find` on empty list → `No expenses recorded yet.`

---

### Listing all expenses: `list`

Displays all recorded expenses in a formatted table with dynamic column widths.

Format: `list`

Alias: `l`

Example: `list` or `l`

Expected output:
```
____________________________________________________________
 Your Expenses
____________________________________________________________
  #    Category     Description  Date          Amount
  ---  -----------  -----------  ----------    --------
  1.   [Food]       Coffee       2026-03-22    $3.50
  2.   [Transport]  Bus          2026-03-22    $1.80
____________________________________________________________
 Total entries: 2
____________________________________________________________
```

If no expenses have been added:
```
 No expenses recorded yet.
```

---

### Listing recurring expenses: `list recurring`

Displays only expenses marked as recurring.

Format: `list recurring`

Expected output:
```
____________________________________________________________
 Recurring Expenses
____________________________________________________________
  #    Category         Description    Date          Amount
  ---  ---------------  -------------  ----------    --------
  1.   [Entertainment]  Netflix [R]    2026-03-22    $18.00
____________________________________________________________
 Total recurring: 1
____________________________________________________________
```

If no recurring expenses exist:
```
 No recurring expenses found.
```
- The sub-command is case-insensitive: `list RECURRING` and `list Recurring` both work.

---

### Adding a recurring expense

To mark an expense as recurring, add `recurring/true` to the `add` command.

Format: `add d/DESCRIPTION a/AMOUNT c/CATEGORY [date/DATE] [recurring/true|false]`

- `recurring/` accepts only `true` or `false`. Any other value shows an error.
- If omitted, defaults to `false`.
- Recurring expenses are shown with `[R]` in the list.

Examples:
- `add d/Netflix a/18.00 c/Entertainment recurring/true`
- `add d/Coffee a/3.50 c/Food` — non-recurring by default

---

### Editing an expense: `edit`

Updates one or more fields of an existing expense by its 1-based index. Only the fields you provide are changed — all other fields remain unchanged.

Format: `edit INDEX [d/DESCRIPTION] [a/AMOUNT] [c/CATEGORY] [date/DATE] [recurring/true|false]`

- `INDEX` is 1-based (same numbering as `list`).
- At least one field must be provided.
- Duplicate flags (e.g. `d/Latte d/Coffee`) are not allowed.

Examples:
- `edit 1 d/Latte` — updates description only
- `edit 2 a/6.00 c/Drinks` — updates amount and category
- `edit 1 recurring/false` — un-marks a recurring expense
- `edit 3 d/Netflix a/18.00 c/Entertainment date/2026-03-01 recurring/true` — updates all fields

Expected output:
```
____________________________________________________________
 Expense #1 updated:
   Before: [Food] Coffee - $3.50 (2026-03-22)
   After:  [Food] Latte - $3.50 (2026-03-22)
____________________________________________________________
```

Error cases:
- `edit 999 d/Test` — index out of range → error message with valid range
- `edit 1` — no fields provided → `No fields provided to edit. Usage: edit <index> [d/<desc>] [a/<amount>] [c/<category>] [date/<YYYY-MM-DD>] [recurring/true|false]`
- `edit 1 a/-5` — negative amount → `Amount must be greater than 0.`
- `edit 1 d/` — empty description → `Description cannot be empty. Please provide a valid description after d/`
- `edit abc d/Latte` — non-integer index → `Index must be a whole number.`

---

### Setting a monthly budget: `budget`

Sets your monthly spending limit.

Format: `budget AMOUNT`

- `AMOUNT` must be greater than 0 and not exceed $1,000,000.

Alias: `b`

Examples:
- `budget 500` — sets a $500 monthly budget
- `b 300` — using alias

Expected output:
```
____________________________________________________________
 Monthly budget set to: $500.00
 Current total spent:   $12.00
 Remaining budget:      $488.00
____________________________________________________________
```

If your total spending already exceeds the new budget:
```
____________________________________________________________
 WARNING: You have exceeded your budget by $50.00!
____________________________________________________________
```

Error cases:
- `budget -10` → `Budget must be greater than $0.00.`
- `budget abc` → `budget requires a number. Usage: budget <amount>`
- `budget` → `budget requires a number. Usage: budget <amount>`

---

### Resetting the budget: `budget reset`

Clears the currently set budget.

Format: `budget reset`

Expected output:
```
____________________________________________________________
 Budget has been reset successfully.
 No budget is currently set.
____________________________________________________________
```

Error cases:
- `budget reset` when no budget is set → `No budget to reset.`

---

### Viewing budget history: `budget history`

Displays all previously set budgets in reverse chronological order (most recent first). Budget history is preserved after `budget reset` — resetting only clears the active limit, not the log.

Format: `budget history`

Expected output:
```
____________________________________________________________
 ===== Budget History =====
 2026-03-27 : $300.00
 2026-03-22 : $500.00
 ==========================
____________________________________________________________
```

If no budget has ever been set:
```
 No budget history recorded.
```

---

### Budget alert on add

After each `add` command, SpendTrack automatically checks your spending against the budget. No command is needed — alerts appear inline after the expense is added.

- At **90% or above** of your budget, a warning is shown.
- When spending **exceeds** the budget, an alert is shown.
- No message is shown if no budget is set or spending is under 90%.

Example (budget is $100, total spent is now $95 after adding):
```
____________________________________________________________
 New expense added:
   [Food] Dinner - $15.00 (2026-03-29)
____________________________________________________________
 [WARNING] You are close to your monthly budget! ($95.00 / $100.00 used)
```

Example (budget is $100, total spent is now $120 after adding):
```
____________________________________________________________
 New expense added:
   [Food] Groceries - $40.00 (2026-03-29)
____________________________________________________________
 [ALERT] You have exceeded your monthly budget! ($120.00 spent, budget is $100.00)
```

---

### Clearing all expenses: `clear`

Removes all expenses from the list after a confirmation prompt.

Format: `clear`

- You must type `yes` (case-insensitive) to confirm. Any other input cancels.
- If the expense list is already empty, shows a message without prompting.

Example:
```
____________________________________________________________
 Are you sure you want to delete all expenses? This cannot be undone. (yes/no):
____________________________________________________________
> yes
____________________________________________________________
 All expenses cleared. (5 expense(s) removed)
____________________________________________________________
```

Cancelling:
```
> no
____________________________________________________________
 Clear cancelled.
____________________________________________________________
```

If already empty:
```
____________________________________________________________
 No expenses to clear.
____________________________________________________________
```

---

### Undoing the last command: `undo`

Restores the expense list to its state before the last mutating command (`add`, `delete`, `edit`, `clear`).

Format: `undo`

- Only single-level undo is supported. A second consecutive `undo` prints `Nothing to undo.`
- Non-mutating commands (`list`, `help`, `summary`, etc.) do not affect the undo history.
- Budget state is also restored.

Example:
```
> add d/Mistake a/999 c/Oops
____________________________________________________________
 New expense added:
   [Oops] Mistake - $999.00 (2026-03-29)
____________________________________________________________
> undo
____________________________________________________________
 Last command undone successfully.
____________________________________________________________
```

If nothing to undo:
```
____________________________________________________________
 Nothing to undo.
____________________________________________________________
```

---

### Exporting expenses to CSV: `export csv`

Writes all expenses to a CSV file at `data/spendtrack_export.csv`.

Format: `export csv`

- The file includes a header row: `Description,Amount,Category,Date,Recurring`.
- Descriptions containing commas are wrapped in double quotes.
- The `data/` directory is created automatically if it does not exist.

Example:
```
____________________________________________________________
 Expenses exported to data/spendtrack_export.csv
____________________________________________________________
```

Sample CSV output:
```
Description,Amount,Category,Date,Recurring
Coffee,3.50,Food,2026-03-22,false
"Lunch, with John",12.00,Food,2026-03-22,false
Netflix,18.00,Entertainment,2026-03-22,true
```

If no expenses:
```
____________________________________________________________
 No expenses to export.
____________________________________________________________
```

Error cases:
- `export` without `csv` → `Usage: export csv`

---

### Savings goal tracking: `goal`

Set a monthly savings target and track your progress toward it.

**Setting a goal:**

Format: `goal g/AMOUNT`

- `AMOUNT` must be greater than 0.
- The goal is saved and persists across restarts.

Example:
```
> goal g/200
____________________________________________________________
 Savings goal set to: $200.00
____________________________________________________________
```

**Viewing goal status:**

Format: `goal status`

Example (under goal):
```
> goal status
____________________________________________________________
 ===== Savings Goal =====
 Goal    : $200.00
 Spent   : $87.30
 Saved   : $112.70 (56% of goal reached)
 ========================
____________________________________________________________
```

Example (over goal):
```
____________________________________________________________
 ===== Savings Goal =====
 Goal    : $200.00
 Spent   : $250.00
 Goal not reached. Over by $50.00.
 ========================
____________________________________________________________
```

If no goal set:
```
____________________________________________________________
 No savings goal set.
____________________________________________________________
```

Error cases:
- `goal g/0` or `goal g/-50` → `Goal amount must be greater than 0.`
- `goal g/abc` → `Goal amount must be a number. Usage: goal g/<amount>`
- `goal` or `goal abc` → `Usage: goal g/<amount> or goal status`

### Generating a monthly report: `report`

Displays the total amount spent in a given month, along with a breakdown by category.

Format: `report YYYY-MM`

- `YYYY-MM` is the year and month you want the report for (e.g. `2026-03`).
- Only expenses with a recorded date in that month are included.

Examples:

- `report 2026-03` — shows the full spending report for March 2026

Expected output:
```
____________________________________________________________
 Monthly Report for 2026-03:
 Total spent: $32.30
 Breakdown by category:
  - Food: $15.50
  - Transport: $16.80
____________________________________________________________
```

If no expenses exist for that month:
```
____________________________________________________________
 No expenses found for 2026-03
____________________________________________________________
```

Error cases:
- `report 03-2026` or `report abc` → `Usage: report <YYYY-MM>`

---

### Listing expenses by month: `month`

Lists all individual expenses recorded in a specific month, one per line.

Format: `month YYYY-MM`

- `YYYY-MM` is the year and month (e.g. `2026-03`).
- Unlike `report`, this shows each expense individually rather than grouping by category.

Examples:

- `month 2026-03` — lists every expense recorded in March 2026

Expected output:
```
____________________________________________________________
 Expenses for 2026-03:
 1. [Food] Coffee - $3.50 (2026-03-15)
 2. [Transport] Bus - $1.80 (2026-03-22)
____________________________________________________________
```

If no expenses exist for that month:
```
____________________________________________________________
 No expenses found for 2026-03
____________________________________________________________
```

Error cases:
- `month abc` or `month 03-2026` → `Usage: month <YYYY-MM>`

---

### Viewing top N expenses: `top`

Displays the N most expensive expenses, sorted from highest to lowest amount.

Format: `top N`

- `N` must be a positive integer.
- If `N` is greater than the total number of expenses, all expenses are shown.

Examples:

- `top 3` — shows the 3 most expensive expenses recorded

Expected output:
```
____________________________________________________________
 Top 3 expenses:
 1. [Transport] Grab to airport - $24.50 (2026-03-15)
 2. [Entertainment] Netflix - $18.00 (2026-03-22)
 3. [Food] Dinner - $12.00 (2026-03-22)
____________________________________________________________
```

Error cases:
- `top 0` or `top -1` → `Number must be greater than 0.`
- `top` on an empty list → `No expenses recorded.`

---

### Viewing last N expenses: `last`

Displays the N most recently added expenses, in the order they were added.

Format: `last N`

- `N` must be a positive integer.
- If `N` exceeds the total number of expenses, all expenses are shown.

Examples:

- `last 3` — shows the last 3 expenses that were added

Expected output:
```
____________________________________________________________
 Showing last 3 expenses:
 1. [Food] Coffee - $3.50 (2026-03-22)
 2. [Transport] Bus - $1.80 (2026-03-22)
 3. [Entertainment] Netflix - $18.00 (2026-03-22)
____________________________________________________________
```

Error cases:
- `last 0` or `last -5` → `Number must be greater than 0.`
- `last` on an empty list → `No expenses recorded.`

---

### Searching expenses by keyword: `search`

Finds all expenses whose description contains the given keyword. The search is case-insensitive.

Format: `search KEYWORD`

- `KEYWORD` is matched against the description of each expense.
- Partial matches are supported (e.g. `search cof` will match `Coffee`).

Examples:

- `search coffee` — finds all expenses with coffee in the description

Expected output:
```
____________________________________________________________
 Search results:
 1. [Food] Coffee - $3.50 (2026-03-22)
____________________________________________________________
```

If no matches are found:
```
____________________________________________________________
 No matches found.
____________________________________________________________
```

---

### Sorting expenses by amount: `sort`

Displays all expenses sorted from highest to lowest amount. The original list order is not changed.

Format: `sort`

Examples:

- `sort` — displays all expenses in descending order of amount

Expected output:
```
____________________________________________________________
 Here are your expenses sorted by amount:
 1. [Transport] Grab to airport - $24.50 (2026-03-15)
 2. [Entertainment] Netflix - $18.00 (2026-03-22)
 3. [Food] Dinner - $12.00 (2026-03-22)
 4. [Food] Coffee - $3.50 (2026-03-22)
____________________________________________________________
```

If no expenses have been added:
```
____________________________________________________________
 No expenses recorded.
____________________________________________________________
```

---

### Checking remaining budget: `remaining`

Displays how much of your monthly budget is left after deducting all recorded expenses.

Format: `remaining`

- A budget must be set using the `budget` command before using this.
- The remaining balance is calculated as: budget - total expenses.

Example: `remaining`

Expected output:
```
____________________________________________________________
 Budget    : $500.00
 Spent     : $87.30
 Remaining : $412.70
____________________________________________________________
```

If no budget has been set:
```
____________________________________________________________
 No budget set. Use 'budget <amount>' to set one first.
____________________________________________________________
```

---

### Viewing all commands: `help`

Displays a list of all available commands and their formats.

Format: `help`

Alias: `h`

- No parameters are needed.
- Shows every command supported by SpendTrack.

Example: `help` or `h`

Expected output:
```
____________________________________________________________
 Here are the commands you can use:
  add (a) d/DESC a/AMT c/CAT [date/DATE]           -- add expense
  delete (d) INDEX                                 -- delete expense
  list (l)                                         -- list all
  list recurring                                   -- list recurring only
  summary (s)                                      -- category breakdown
  total                                            -- show total
  budget (b) AMOUNT                                -- set budget
  budget reset                                     -- reset budget
  budget history                                   -- view budget history
  remaining                                        -- show remaining
  edit INDEX [d/DESC] [a/AMT] [c/CAT] [date/DATE]  -- edit expense
  filter from/DATE to/DATE                         -- filter by date
  find INDEX                                       -- view expense details
  search KEYWORD                                   -- search by keyword
  sort                                             -- sort by amount
  top N                                            -- top N expenses
  last N                                           -- last N added
  report YYYY-MM                                   -- monthly report
  month YYYY-MM                                    -- list by month
  clear                                            -- clear all
  undo                                             -- undo last
  export csv                                       -- export to CSV
  goal g/AMOUNT                                    -- set savings goal
  goal status                                      -- check goal
  help (h)                                         -- show this help
  bye                                              -- exit
____________________________________________________________
```

---

### Exiting the application: `bye`

Exits SpendTrack and saves all data automatically.

Format: `bye`

- All expenses and budget data are saved before the application closes.
- No data will be lost when you exit.

Example: `bye`

Expected output:
```
____________________________________________________________
 Goodbye! Stay on budget!
____________________________________________________________
```

---


## FAQ

**Q**: How do I transfer my data to another computer?

**A**: The save file is encrypted with a machine-specific key, so it cannot be transferred directly to another computer. You will need to re-enter your expenses on the new machine.

**Q**: What date formats are accepted?

**A**: `YYYY-MM-DD` (e.g. `2026-03-22`), `DD-MM-YYYY` (e.g. `22-03-2026`), `today`, and `yesterday`.

**Q**: What happens if I type a category in lowercase?

**A**: It is automatically capitalised. `food` becomes `Food`, `public transport` becomes `Public Transport`.

## Command Summary

| Action            | Format | Alias |
|-------------------|--------|-------|
| Add expense       | `add d/DESC a/AMT c/CAT [date/DATE] [recurring/true\|false]` | `a` |
| Delete expense    | `delete INDEX` | `d` |
| Edit expense      | `edit INDEX [d/DESC] [a/AMT] [c/CAT] [date/DATE] [recurring/true\|false]` | — |
| List expenses     | `list` | `l` |
| List recurring    | `list recurring` | — |
| Filter by date    | `filter from/DATE to/DATE` | — |
| Find by index     | `find INDEX` | — |
| Summary           | `summary` | `s` |
| Total             | `total` | — |
| Set budget        | `budget AMOUNT` | `b` |
| Reset budget      | `budget reset` | — |
| Budget history    | `budget history` | — |
| Remaining         | `remaining` | — |
| Clear all         | `clear` | — |
| Undo              | `undo` | — |
| Export CSV        | `export csv` | — |
| Set goal          | `goal g/AMOUNT` | — |
| Goal status       | `goal status` | — |
| Monthly report    | `report YYYY-MM`  | — |
| List by month     | `month YYYY-MM`   | — |
| Top N expenses    | `top N`           | — |
| Last N expenses   | `last N`          | — |
| Search by keyword | `search KEYWORD`  | — |
| Sort by amount    | `sort`            | — |
| Help              | `help` | `h` |
| Exit              | `bye` | — |
