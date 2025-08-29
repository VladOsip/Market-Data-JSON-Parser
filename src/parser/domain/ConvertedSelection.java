package parser.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ConvertedSelection {

    @JsonProperty("selection_uid")
    private final String selectionUid;

    @JsonProperty("selection_type_id")
    private final int selectionTypeId;

    @JsonProperty("decimal_odds")
    private final double decimalOdds;

    public ConvertedSelection(String selectionUid, int selectionTypeId, double decimalOdds) {
        this.selectionUid = Objects.requireNonNull(selectionUid, "selectionUid cannot be null");
        this.selectionTypeId = selectionTypeId;
        this.decimalOdds = decimalOdds;
    }

    public String getSelectionUid() {
        return selectionUid;
    }

    public int getSelectionTypeId() {
        return selectionTypeId;
    }

    public double getDecimalOdds() {
        return decimalOdds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConvertedSelection)) return false;
        ConvertedSelection that = (ConvertedSelection) o;
        return selectionTypeId == that.selectionTypeId &&
               Double.compare(that.decimalOdds, decimalOdds) == 0 &&
               Objects.equals(selectionUid, that.selectionUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(selectionUid, selectionTypeId, decimalOdds);
    }

    @Override
    public String toString() {
        return "ConvertedSelection{" +
               "selectionUid='" + selectionUid + '\'' +
               ", selectionTypeId=" + selectionTypeId +
               ", decimalOdds=" + decimalOdds +
               '}';
    }
}
