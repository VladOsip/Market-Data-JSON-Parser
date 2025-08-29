package parser.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConvertedMarket {

    @JsonProperty("market_uid")
    private final String marketUid;

    @JsonProperty("market_type_id")
    private final String marketTypeId;

    @JsonProperty("specifiers")
    private final Map<String, String> specifiers;

    @JsonProperty("selections")
    private final List<ConvertedSelection> selections;

    public ConvertedMarket(String marketUid,
                           String marketTypeId,
                           Map<String, String> specifiers,
                           List<ConvertedSelection> selections) {
        this.marketUid = Objects.requireNonNull(marketUid, "marketUid cannot be null");
        this.marketTypeId = Objects.requireNonNull(marketTypeId, "marketTypeId cannot be null");
        this.specifiers = Objects.requireNonNull(specifiers, "specifiers cannot be null");
        this.selections = Objects.requireNonNull(selections, "selections cannot be null");
    }

    public String getMarketUid() {
        return marketUid;
    }

    public String getMarketTypeId() {
        return marketTypeId;
    }

    public Map<String, String> getSpecifiers() {
        return specifiers;
    }

    public List<ConvertedSelection> getSelections() {
        return selections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConvertedMarket)) return false;
        ConvertedMarket that = (ConvertedMarket) o;
        return Objects.equals(marketUid, that.marketUid) &&
               Objects.equals(marketTypeId, that.marketTypeId) &&
               Objects.equals(specifiers, that.specifiers) &&
               Objects.equals(selections, that.selections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(marketUid, marketTypeId, specifiers, selections);
    }

    @Override
    public String toString() {
        return "ConvertedMarket{" +
               "marketUid='" + marketUid + '\'' +
               ", marketTypeId='" + marketTypeId + '\'' +
               ", specifiers=" + specifiers +
               ", selections=" + selections +
               '}';
    }
}
