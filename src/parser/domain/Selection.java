package parser.domain;

import java.util.Objects;

public class Selection {
    private final String name;
    private final double odds;

    public Selection(String name, double odds) {
        this.name = Objects.requireNonNull(name, "Selection name cannot be null");
        if (odds <= 0) {
            throw new IllegalArgumentException("Odds must be positive");
        }
        this.odds = odds;
    }

    public String getName() { return name; }
    public double getOdds() { return odds; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Selection selection = (Selection) o;
        return Double.compare(selection.odds, odds) == 0 &&
               Objects.equals(name, selection.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, odds);
    }
}