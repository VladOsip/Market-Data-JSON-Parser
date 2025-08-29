package test.integration;

import parser.domain.ConvertedMarket;
import parser.factory.MarketParserFactory;
import parser.parser.MarketParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class MarketParserIntegrationTest {

    private MarketParser parser;

    @BeforeEach
    void setUp() {
        parser = MarketParserFactory.createDefaultParser();
    }

    @Test
    @DisplayName("Should parse and convert complete market data")
    void shouldParseAndConvertCompleteMarketData() throws Exception {
        String jsonInput = """
        [
          {
            "name": "1x2",
            "event_id": "123456",
            "selections": [
              {"name": "Team A", "odds": 1.65},
              {"name": "draw", "odds": 3.2},
              {"name": "Team B", "odds": 2.6}
            ]
          },
          {
            "name": "Total",
            "event_id": "123456",
            "selections": [
              {"name": "over 2.5", "odds": 1.85},
              {"name": "under 2.5", "odds": 1.95}
            ]
          }
        ]
        """;

        List<ConvertedMarket> result = parser.parseFromString(jsonInput);

        assertEquals(2, result.size());

        // Verify 1x2 market
        // Verify 1x2 market
ConvertedMarket market1x2 = result.get(0);
assertEquals("123456_1", market1x2.getMarketUid());
assertEquals("1", market1x2.getMarketTypeId());
assertTrue(market1x2.getSpecifiers().isEmpty());
assertEquals(3, market1x2.getSelections().size());

// Verify Total market  
ConvertedMarket marketTotal = result.get(1);
assertEquals("123456_18_2.5", marketTotal.getMarketUid());
assertEquals("18", marketTotal.getMarketTypeId());
assertEquals("2.5", marketTotal.getSpecifiers().get("total"));
assertEquals(2, marketTotal.getSelections().size());
    }
}