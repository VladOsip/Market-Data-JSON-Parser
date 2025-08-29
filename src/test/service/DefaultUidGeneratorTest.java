package test.service;

import parser.service.DefaultUidGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

class DefaultUidGeneratorTest {

    private DefaultUidGenerator uidGenerator;

    @BeforeEach
    void setUp() {
        uidGenerator = new DefaultUidGenerator();
    }

    @Test
    @DisplayName("Should generate market UID without specifiers")
    void shouldGenerateMarketUidWithoutSpecifiers() {
        Map<String, String> specifiers = new HashMap<>();
        
        String uid = uidGenerator.generateMarketUid("123456", "1", specifiers);
        
        assertEquals("123456_1", uid);
    }

    @Test
    @DisplayName("Should generate market UID with specifiers")
    void shouldGenerateMarketUidWithSpecifiers() {
        Map<String, String> specifiers = new HashMap<>();
        specifiers.put("total", "2.5");
        
        String uid = uidGenerator.generateMarketUid("123456", "18", specifiers);
        
        assertEquals("123456_18_2.5", uid);
    }

    @Test
    @DisplayName("Should generate selection UID correctly")
    void shouldGenerateSelectionUidCorrectly() {
        String uid = uidGenerator.generateSelectionUid("123456_18_2.5", 12);
        
        assertEquals("123456_18_2.5_12", uid);
    }
}