package test.service;

import parser.service.*;
import parser.domain.Market;
import parser.domain.Selection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Map;

class DefaultMarketTypeResolverTest {

    private DefaultMarketTypeResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new DefaultMarketTypeResolver();
    }

    @Test
    @DisplayName("Should resolve market type IDs correctly")
    void shouldResolveMarketTypeIdsCorrectly() {
        assertEquals("1", resolver.resolveMarketTypeId("1x2"));
        assertEquals("18", resolver.resolveMarketTypeId("Total"));
        assertEquals("68", resolver.resolveMarketTypeId("1st half - total"));
        assertEquals("16", resolver.resolveMarketTypeId("Handicap"));
        assertEquals("66", resolver.resolveMarketTypeId("1st half - handicap"));
        assertEquals("88", resolver.resolveMarketTypeId("2nd half - handicap"));
        assertEquals("50", resolver.resolveMarketTypeId("Both teams to score"));
    }

    @Test
    @DisplayName("Should throw exception for unknown market type")
    void shouldThrowExceptionForUnknownMarketType() {
        assertThrows(IllegalArgumentException.class, 
            () -> resolver.resolveMarketTypeId("Unknown Market"));
    }

    @Test
    @DisplayName("Should extract total specifiers correctly")
    void shouldExtractTotalSpecifiersCorrectly() {
        Market market = new Market("Total", "123456", Arrays.asList(
            new Selection("over 2.5", 1.85),
            new Selection("under 2.5", 1.95)
        ));
        
        Map<String, String> specifiers = resolver.extractSpecifiers(market);
        assertEquals("2.5", specifiers.get("total"));
    }

    @Test
    @DisplayName("Should extract handicap specifiers correctly")
    void shouldExtractHandicapSpecifiersCorrectly() {
        Market market = new Market("Handicap", "123456", Arrays.asList(
            new Selection("Team A +1.5", 1.8),
            new Selection("Team B -1.5", 2.0)
        ));
        
        Map<String, String> specifiers = resolver.extractSpecifiers(market);
        assertEquals("1.5", specifiers.get("hcp"));
    }

    @Test
    @DisplayName("Should resolve 1x2 selection types correctly")
    void shouldResolve1x2SelectionTypesCorrectly() {
        assertEquals(1, resolver.resolveSelectionTypeId("1", "Team A", 0));
        assertEquals(2, resolver.resolveSelectionTypeId("1", "draw", 1));
        assertEquals(3, resolver.resolveSelectionTypeId("1", "Team B", 2));
    }

    @Test
    @DisplayName("Should resolve total selection types correctly")
    void shouldResolveTotalSelectionTypesCorrectly() {
        assertEquals(12, resolver.resolveSelectionTypeId("18", "over 2.5", 0));
        assertEquals(13, resolver.resolveSelectionTypeId("18", "under 2.5", 1));
    }

    @Test
    @DisplayName("Should resolve handicap selection types correctly")
    void shouldResolveHandicapSelectionTypesCorrectly() {
        assertEquals(1714, resolver.resolveSelectionTypeId("16", "Team A +1.5", 0));
        assertEquals(1715, resolver.resolveSelectionTypeId("16", "Team B -1.5", 1));
    }

    @Test
    @DisplayName("Should resolve both teams to score selection types correctly")
    void shouldResolveBothTeamsToScoreSelectionTypesCorrectly() {
        assertEquals(10, resolver.resolveSelectionTypeId("50", "Yes", 0));
        assertEquals(11, resolver.resolveSelectionTypeId("50", "No", 1));
    }
}