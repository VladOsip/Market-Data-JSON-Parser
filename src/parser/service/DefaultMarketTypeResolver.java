package parser.service;

import parser.domain.Market;
import parser.domain.Selection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultMarketTypeResolver implements MarketTypeResolver {
    
    // Market Type IDs
    private static final String MARKET_TYPE_1X2 = "1";
    private static final String MARKET_TYPE_TOTAL = "18";
    private static final String MARKET_TYPE_FIRST_HALF_TOTAL = "68";
    private static final String MARKET_TYPE_HANDICAP = "16";
    private static final String MARKET_TYPE_FIRST_HALF_HANDICAP = "66";
    private static final String MARKET_TYPE_SECOND_HALF_HANDICAP = "88";
    private static final String MARKET_TYPE_BOTH_TEAMS_TO_SCORE = "50";

    // Selection Type IDs for 1x2 market
    private static final int SELECTION_TYPE_TEAM_A = 1;
    private static final int SELECTION_TYPE_DRAW = 2;
    private static final int SELECTION_TYPE_TEAM_B = 3;

    // Selection Type IDs for Total markets
    private static final int SELECTION_TYPE_OVER = 12;
    private static final int SELECTION_TYPE_UNDER = 13;

    // Selection Type IDs for Handicap markets
    private static final int SELECTION_TYPE_HANDICAP_TEAM_A = 1714;
    private static final int SELECTION_TYPE_HANDICAP_TEAM_B = 1715;

    // Selection Type IDs for Both Teams to Score market
    private static final int SELECTION_TYPE_YES = 10;
    private static final int SELECTION_TYPE_NO = 11;

    // Selection index positions for fallback mapping
    private static final int FIRST_SELECTION_INDEX = 0;
    private static final int SECOND_SELECTION_INDEX = 1;

    // Specifier keys
    private static final String TOTAL_SPECIFIER_KEY = "total";
    private static final String HANDICAP_SPECIFIER_KEY = "hcp";

    // Selection name keywords
    private static final String DRAW_KEYWORD = "draw";
    private static final String TEAM_A_KEYWORD = "team a";
    private static final String TEAM_B_KEYWORD = "team b";
    private static final String OVER_KEYWORD = "over";
    private static final String UNDER_KEYWORD = "under";
    private static final String YES_KEYWORD = "yes";

    private static final Map<String, String> MARKET_TYPE_MAPPING = Map.of(
        "1x2", MARKET_TYPE_1X2,
        "Total", MARKET_TYPE_TOTAL,
        "1st half - total", MARKET_TYPE_FIRST_HALF_TOTAL,
        "Handicap", MARKET_TYPE_HANDICAP,
        "1st half - handicap", MARKET_TYPE_FIRST_HALF_HANDICAP,
        "2nd half - handicap", MARKET_TYPE_SECOND_HALF_HANDICAP,
        "Both teams to score", MARKET_TYPE_BOTH_TEAMS_TO_SCORE
    );

    private static final Pattern TOTAL_PATTERN = Pattern.compile("(over|under)\\s+(\\d+(?:\\.\\d+)?)");
    private static final Pattern HANDICAP_PATTERN = Pattern.compile("Team [AB] ([+-]\\d+(?:\\.\\d+)?)");

    @Override
    public String resolveMarketTypeId(String marketName) {
        String typeId = MARKET_TYPE_MAPPING.get(marketName);
        if (typeId == null) {
            throw new IllegalArgumentException("Unknown market type: " + marketName);
        }
        return typeId;
    }

    @Override
    public Map<String, String> extractSpecifiers(Market market) {
        Map<String, String> specifiers = new HashMap<>();
        String marketTypeId = resolveMarketTypeId(market.getName());

        switch (marketTypeId) {
            case MARKET_TYPE_TOTAL:
            case MARKET_TYPE_FIRST_HALF_TOTAL:
                extractTotalSpecifier(market, specifiers);
                break;
            case MARKET_TYPE_HANDICAP:
            case MARKET_TYPE_FIRST_HALF_HANDICAP:
            case MARKET_TYPE_SECOND_HALF_HANDICAP:
                extractHandicapSpecifier(market, specifiers);
                break;
            // Market types 1x2 and Both teams to score have no specifiers
        }

        return specifiers;
    }

    private void extractTotalSpecifier(Market market, Map<String, String> specifiers) {
        for (Selection selection : market.getSelections()) {
            Matcher matcher = TOTAL_PATTERN.matcher(selection.getName().toLowerCase());
            if (matcher.find()) {
                specifiers.put(TOTAL_SPECIFIER_KEY, matcher.group(2));
                return;
            }
        }
        throw new IllegalArgumentException("No total value found in market: " + market.getName());
    }

    private void extractHandicapSpecifier(Market market, Map<String, String> specifiers) {
        for (Selection selection : market.getSelections()) {
            Matcher matcher = HANDICAP_PATTERN.matcher(selection.getName());
            if (matcher.find()) {
                String handicap = matcher.group(1).replace("+", "");
                specifiers.put(HANDICAP_SPECIFIER_KEY, handicap);
                return;
            }
        }
        throw new IllegalArgumentException("No handicap value found in market: " + market.getName());
    }

    @Override
    public int resolveSelectionTypeId(String marketTypeId, String selectionName, int selectionIndex) {
        switch (marketTypeId) {
            case MARKET_TYPE_1X2:
                return resolve1x2SelectionType(selectionName, selectionIndex);
            case MARKET_TYPE_TOTAL:
            case MARKET_TYPE_FIRST_HALF_TOTAL:
                return resolveTotalSelectionType(selectionName);
            case MARKET_TYPE_HANDICAP:
            case MARKET_TYPE_FIRST_HALF_HANDICAP:
            case MARKET_TYPE_SECOND_HALF_HANDICAP:
                return resolveHandicapSelectionType(selectionName, selectionIndex);
            case MARKET_TYPE_BOTH_TEAMS_TO_SCORE:
                return resolveBothTeamsToScoreSelectionType(selectionName);
            default:
                throw new IllegalArgumentException("Unknown market type ID: " + marketTypeId);
        }
    }

    private int resolve1x2SelectionType(String selectionName, int selectionIndex) {
        String lowerName = selectionName.toLowerCase();
        if (lowerName.equals(DRAW_KEYWORD)) {
            return SELECTION_TYPE_DRAW;
        } else if (lowerName.contains(TEAM_A_KEYWORD)) {
            return SELECTION_TYPE_TEAM_A;
        } else if (lowerName.contains(TEAM_B_KEYWORD)) {
            return SELECTION_TYPE_TEAM_B;
        } else {
            // Fallback to index-based mapping
            return selectionIndex == FIRST_SELECTION_INDEX ? SELECTION_TYPE_TEAM_A : 
                   (selectionIndex == SECOND_SELECTION_INDEX ? SELECTION_TYPE_DRAW : SELECTION_TYPE_TEAM_B);
        }
    }

    private int resolveTotalSelectionType(String selectionName) {
        return selectionName.toLowerCase().startsWith(OVER_KEYWORD) ? 
               SELECTION_TYPE_OVER : SELECTION_TYPE_UNDER;
    }

    private int resolveHandicapSelectionType(String selectionName, int selectionIndex) {
        return selectionName.toLowerCase().contains(TEAM_A_KEYWORD) ? 
               SELECTION_TYPE_HANDICAP_TEAM_A : SELECTION_TYPE_HANDICAP_TEAM_B;
    }

    private int resolveBothTeamsToScoreSelectionType(String selectionName) {
        return selectionName.equalsIgnoreCase(YES_KEYWORD) ? 
               SELECTION_TYPE_YES : SELECTION_TYPE_NO;
    }
}