package parser.domain;

import java.util.List;
import java.util.Objects;

public class Market {
    private final String name;
    private final String eventId;
    private final List<Selection> selections;

    public Market(String name, String eventId, List<Selection> selections) {
        this.name = Objects.requireNonNull(name, "Market name cannot be null");
        this.eventId = Objects.requireNonNull(eventId, "Event ID cannot be null");
        this.selections = Objects.requireNonNull(selections, "Selections cannot be null");
    }

    public String getName() { return name; }
    public String getEventId() { return eventId; }
    public List<Selection> getSelections() { return selections; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Market market = (Market) o;
        return Objects.equals(name, market.name) &&
               Objects.equals(eventId, market.eventId) &&
               Objects.equals(selections, market.selections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, eventId, selections);
    }
}