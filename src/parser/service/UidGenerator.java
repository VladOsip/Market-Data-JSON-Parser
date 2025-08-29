package parser.service;

import java.util.Map;

public interface UidGenerator {
    String generateMarketUid(String eventId, String marketTypeId, Map<String, String> specifiers);
    String generateSelectionUid(String marketUid, int selectionTypeId);
}