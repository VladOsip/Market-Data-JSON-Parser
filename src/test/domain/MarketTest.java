package test.domain;

import parser.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Arrays;

class MarketTest {

    @Test
    @DisplayName("Should create market with valid data")
    void shouldCreateMarketWithValidData() {
        List<Selection> selections = Arrays.asList(
            new Selection("Team A", 1.5),
            new Selection("Team B", 2.5)
        );
        
        Market market = new Market("1x2", "123456", selections);
        
        assertEquals("1x2", market.getName());
        assertEquals("123456", market.getEventId());
        assertEquals(2, market.getSelections().size());
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowExceptionWhenNameIsNull() {
        List<Selection> selections = Arrays.asList(new Selection("Team A", 1.5));
        
        assertThrows(NullPointerException.class, 
            () -> new Market(null, "123456", selections));
    }

    @Test
    @DisplayName("Should throw exception when event ID is null")
    void shouldThrowExceptionWhenEventIdIsNull() {
        List<Selection> selections = Arrays.asList(new Selection("Team A", 1.5));
        
        assertThrows(NullPointerException.class, 
            () -> new Market("1x2", null, selections));
    }

    @Test
    @DisplayName("Should throw exception when selections is null")
    void shouldThrowExceptionWhenSelectionsIsNull() {
        assertThrows(NullPointerException.class, 
            () -> new Market("1x2", "123456", null));
    }
}