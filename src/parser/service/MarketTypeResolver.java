package parser.service;

import parser.domain.Market;
import java.util.Map;

public interface MarketTypeResolver {
    String resolveMarketTypeId(String marketName);
    Map<String, String> extractSpecifiers(Market market);
    int resolveSelectionTypeId(String marketTypeId, String selectionName, int selectionIndex);
}