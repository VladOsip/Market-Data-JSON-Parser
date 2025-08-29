package parser.service;

import java.util.Map;
import java.util.stream.Collectors;

public class DefaultUidGenerator implements UidGenerator {

    @Override
public String generateMarketUid(String eventId, String marketTypeId, Map<String, String> specifiers) {
    StringBuilder uid = new StringBuilder()
        .append(eventId)
        .append("_")
        .append(marketTypeId);

    // Handle specifiers in a specific order to ensure consistent UIDs
    if (specifiers.containsKey("hcp")) {
        uid.append("_").append(specifiers.get("hcp"));
    } else if (specifiers.containsKey("total")) {
        uid.append("_").append(specifiers.get("total"));
    }
    
    // Handle any other specifiers in alphabetical order for consistency
    specifiers.entrySet().stream()
        .filter(entry -> !entry.getKey().equals("hcp") && !entry.getKey().equals("total"))
        .sorted(Map.Entry.comparingByKey())
        .forEach(entry -> uid.append("_").append(entry.getValue()));

    return uid.toString();
}

    @Override
    public String generateSelectionUid(String marketUid, int selectionTypeId) {
        return marketUid + "_" + selectionTypeId;
    }
}