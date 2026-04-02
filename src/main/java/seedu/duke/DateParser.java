// @@author AfshalG
package seedu.duke;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses date strings in multiple formats into LocalDate objects.
 * Supports ISO format (YYYY-MM-DD), Singapore format (DD-MM-YYYY),
 * and keywords like "today" and "yesterday".
 */
public class DateParser {

    private static final DateTimeFormatter SG_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Parses a date string into a LocalDate.
     * Accepted formats: YYYY-MM-DD, DD-MM-YYYY, "today", "yesterday".
     *
     * @param dateString the date string to parse
     * @return the parsed LocalDate
     * @throws SpendTrackException if the date string is not in a recognised format
     */
    public static LocalDate parse(String dateString) throws SpendTrackException {
        String trimmed = dateString.trim().toLowerCase();

        if (trimmed.equals("today")) {
            return LocalDate.now();
        }
        if (trimmed.equals("yesterday")) {
            return LocalDate.now().minusDays(1);
        }

        try {
            return LocalDate.parse(dateString.trim());
        } catch (DateTimeParseException e) {
            // fall through to try DD-MM-YYYY
        }

        try {
            return LocalDate.parse(dateString.trim(), SG_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new SpendTrackException(
                    "Invalid date format. Accepted: YYYY-MM-DD, DD-MM-YYYY, 'today', 'yesterday'.");
        }
    }
}
