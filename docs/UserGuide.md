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
| Summary | `summary` | `s` |
| Help | `help` | `h` |
