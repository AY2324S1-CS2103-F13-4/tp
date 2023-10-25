package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Represents an event in the calendar
 */
public class Event {
    private EventDescription description;
    private EventPeriod eventPeriod;
    private Optional<Event> parentEvent;

    /**
     * Constructs an event with a given description occurring over the given time period.
     *
     * @param description The description of the event.
     * @param eventPeriod The period of time when the event occurs.
     */
    public Event(EventDescription description, EventPeriod eventPeriod) {
        requireAllNonNull(description, eventPeriod);

        this.description = description;
        this.eventPeriod = eventPeriod;
        this.parentEvent = Optional.<Event>empty();
    }

    /**
     * Constructs an event with a given description occurring over a given time period with a parent Event.
     *
     * @param description The description of the event.
     * @param eventPeriod The period of the time when the event occurs.
     * @param parent parent Event object this Event object was dervied from.
     */
    private Event(EventDescription description, EventPeriod eventPeriod, Event parent) {
        requireAllNonNull(description, eventPeriod, parent);

        this.description = description;
        this.eventPeriod = eventPeriod;
        this.parentEvent = Optional.<Event>of(parent);
    }

    /**
     * Creates and returns a new Event object that is guaranteed to not conflict with any other event.
     *
     * @return A new non-conflicting Event object.
     */
    public static Event createNonConflictingEvent() {
        return new Event(EventDescription.createUnusedDescription(), EventPeriod.createNonConflictingPeriod());
    }

    /**
     * Gets the description of the event.
     *
     * @return The description of the event.
     */
    public EventDescription getDescription() {
        return this.description;
    }

    /**
     * Gets the time period during which the event occurs.
     *
     * @return The time period of the event.
     */
    public EventPeriod getEventPeriod() {
        return this.eventPeriod;
    }

    /**
     * Checks if this event conflicts with another event.
     *
     * @param other The other event to check for conflicts with.
     * @return True if there is a conflict, false otherwise.
     */
    public boolean isConflicting(Event other) {
        requireNonNull(other);

        return eventPeriod.isOverlapping(other.eventPeriod);
    }

    /**
     * Changes the description of the event.
     *
     * @param description The new description for the event.
     */
    public void changeDescription(EventDescription description) {
        requireNonNull(description);

        this.description = description;
    }

    /**
     * Changes the time period during which the event occurs.
     *
     * @param eventPeriod The new time period for the event.
     */
    public void changeEventPeriod(EventPeriod eventPeriod) {
        requireNonNull(eventPeriod);

        this.eventPeriod = eventPeriod;
    }

    /**
     * Get the dates the event spans, stored in a list.
     *
     * @return list of the dates the event spans.
     */
    public List<LocalDate> getEventDays() {
        return this.eventPeriod.getDates();
    }

    /**
     * Checks if the {@code @LocalDateTime} is during the event.
     *
     * @param dateTime the specified {@code @LocalDateTime}.
     * @return true if the time is during the event, false otherwise.
     */
    public boolean isDuring(LocalDateTime dateTime) {
        requireNonNull(dateTime);

        return this.eventPeriod.isWithin(dateTime);
    }

    /**
     * Bounds the {@code @Event} such that the {@code @EventPeriod} of the formatted event happens within
     * the specified {@code @LocalDate}.
     *
     * @param date the specified {@code @LocalDate}.
     * @return new {@code @Event} with adjusted {@code @EventPeriod}.
     */
    public Event boundEventByDate(LocalDate date) {
        requireNonNull(date);
        if (hasParent()) {
            return this;
        }
        return new Event(description, eventPeriod.boundPeriodByDate(date), this);
    }

    /**
     * Get the parent Event object of this Event object.
     *
     * @return the parent Event object. If there is no parent, returns itself.
     */
    public Event getParentEvent() {
        return parentEvent.orElse(this);
    }

    /**
     * Checks if this Event object has a parent Event.
     *
     * @return true if it has a parent Event object, false otherwise.
     */
    public boolean hasParent() {
        return parentEvent.isPresent();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Event)) {
            return false;
        }

        Event otherEvent = (Event) other;
        return (otherEvent.description.equals(this.description) &&
                (otherEvent.eventPeriod.equals(this.eventPeriod) ||
                        otherEvent.eventPeriod.isContinuous(this.eventPeriod)));
    }
}
