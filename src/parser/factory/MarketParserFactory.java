package parser.factory;

import parser.parser.MarketParser;
import parser.service.*;

public class MarketParserFactory {
    
    public static MarketParser createDefaultParser() {
        MarketTypeResolver resolver = new DefaultMarketTypeResolver();
        UidGenerator uidGenerator = new DefaultUidGenerator();
        MarketConverter converter = new DefaultMarketConverter(resolver, uidGenerator);
        return new MarketParser(converter);
    }
    
    public static MarketParser createCustomParser(MarketTypeResolver resolver, 
                                                 UidGenerator uidGenerator) {
        MarketConverter converter = new DefaultMarketConverter(resolver, uidGenerator);
        return new MarketParser(converter);
    }
}