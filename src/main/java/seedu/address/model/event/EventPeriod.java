package seedu.address.model.event;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a period in time when an event will occur.
 */
public class EventPeriod {
    public static final String MESSAGE_CONSTRAINTS = "The start date and time and end date and time should "
            + "be in the format 'yyyy-MM-dd HH:mm' where:\n"
            + "    -'yyyy' is the year.\n"
            + "    -'MM' is the month.\n"
            + "    -'dd' is the day.\n"
            + "    -'HH:mm' is the time in 24-hour format.";
    private static final DateTimeFormatter DATE_TIME_STRING_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm");

    private final LocalDateTime start;
    private final LocalDateTime end;

    /**
     * Constructs an EventPeriod object with the given start and end date/time strings.
     *
     * @param startString The string representation of the start date and time.
     * @param endString The string representation of the end date and time.
     */
    public EventPeriod(String startString, String endString) {
        this(LocalDateTime.parse(startString, DATE_TIME_STRING_FORMATTER),
                LocalDateTime.parse(endString, DATE_TIME_STRING_FORMATTER));
    }

    private EventPeriod(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Creates and returns a new EventPeriod object with minimum date/time values.
     *
     * @return A new EventPeriod object with minimum date/time values.
     */
    public static EventPeriod createNonConflictingPeriod() {
        return new EventPeriod(LocalDateTime.MIN, LocalDateTime.MIN);
    }

    /**
     * Checks if the given start and end date/time strings form a valid period.
     *
     * @param startString The string representation of the start date and time.
     * @param endString The string representation of the end date and time.
     * @return True if the period is valid, false otherwise.
     */
    public static boolean isValidPeriod(String startString, String endString) {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        try {
            startDateTime = LocalDateTime.parse(startString, DATE_TIME_STRING_FORMATTER);
            endDateTime = LocalDateTime.parse(endString, DATE_TIME_STRING_FORMATTER);
        } catch (DateTimeParseException invalidFormatException) {
            return false;
        }
        return startDateTime.isBefore(endDateTime);
    }

    /**
     * Checks if this EventPeriod overlaps with another EventPeriod.
     *
     * @param other The EventPeriod to check for overlap with.
     * @return True if there is an overlap, false otherwise.
     */
    public boolean isOverlapping(EventPeriod other) {
        requireNonNull(other);

        return this.start.isBefore(other.end) && other.start.isBefore(this.end);
    }

    /**
     * Compares this EventPeriod with another EventPeriod.
     *
     * @param other The EventPeriod to compare with.
     * @return 1 if this EventPeriod is after the other, -1 if it's before, 0 if they are the same.
     */
    public int compareTo(EventPeriod other) {
        if (this.start.isBefore(other.start)) {
            return 1;
        }
        return -1;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EventPeriod)) {
            return false;
        }

        EventPeriod otherEventPeriod = (EventPeriod) other;
        return otherEventPeriod.start.equals(this.start) && otherEventPeriod.end.equals(this.end);
    }

    @Override
    public String toString() {
        return "start: "
                + this.start.format(DATE_TIME_STRING_FORMATTER)
                + "; end: "
                + this.end.format(DATE_TIME_STRING_FORMATTER);
    }
}
