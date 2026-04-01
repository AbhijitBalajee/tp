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

On startup, SpendTrack loads your saved data before accepting commands. If the save file is missing, the app starts with an empty list silently. Malformed lines in the save file are skipped with a warning — the rest of your data is still loaded.

You do not need to run any save or load command. It happens automatically.

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

## FAQ

**Q**: How do I transfer my data to another computer?

**A**: Copy the `data/spendtrack.txt` file to the same location on the other computer. SpendTrack loads this file automatically on startup.

**Q**: What date formats are accepted?

**A**: `YYYY-MM-DD` (e.g. `2026-03-22`), `DD-MM-YYYY` (e.g. `22-03-2026`), `today`, and `yesterday`.

**Q**: What happens if I type a category in lowercase?

**A**: It is automatically capitalised. `food` becomes `Food`, `public transport` becomes `Public Transport`.

## Command Summary

| Action | Format | Alias |
|--------|--------|-------|
| Add expense | `add d/DESC a/AMT c/CAT [date/DATE]` | `a` |
| Delete expense | `delete INDEX` | `d` |
| List expenses | `list` | `l` |
| Filter by date | `filter from/DATE to/DATE` | — |
| Find by index | `find INDEX` | — |
| Summary | `summary` | `s` |
| Total | `total` | — |
| Budget | `budget AMOUNT` | `b` |
| Remaining | `remaining` | — |
| Help | `help` | `h` |
| Exit | `bye` | — |
