package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.event.Event;

public interface ReadOnlyCalendar {
    /**
     * Generates an unmodifiable view of the event list.
     *
     * @return unmodifiable view of event list.
     */
    ObservableList<Event> getEventList();
}
