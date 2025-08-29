package test.domain;

import parser.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class SelectionTest {

    @Test
    @DisplayName("Should create selection with valid data")
    void shouldCreateSelectionWithValidData() {
        Selection selection = new Selection("Team A", 1.75);
        
        assertEquals("Team A", selection.getName());
        assertEquals(1.75, selection.getOdds(), 0.001);
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(NullPointerException.class, 
            () -> new Selection(null, 1.75));
    }

    @Test
    @DisplayName("Should throw exception when odds are zero")
    void shouldThrowExceptionWhenOddsAreZero() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Selection("Team A", 0));
    }

    @Test
    @DisplayName("Should throw exception when odds are negative")
    void shouldThrowExceptionWhenOddsAreNegative() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Selection("Team A", -1.5));
    }
}
