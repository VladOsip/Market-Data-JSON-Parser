package parser.service;

import parser.domain.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultMarketConverter implements MarketConverter {
    
    private final MarketTypeResolver marketTypeResolver;
    private final UidGenerator uidGenerator;

    public DefaultMarketConverter(MarketTypeResolver marketTypeResolver, UidGenerator uidGenerator) {
        this.marketTypeResolver = marketTypeResolver;
        this.uidGenerator = uidGenerator;
    }

    @Override
    public ConvertedMarket convert(Market market) {
        String marketTypeId = marketTypeResolver.resolveMarketTypeId(market.getName());
        Map<String, String> specifiers = marketTypeResolver.extractSpecifiers(market);
        String marketUid = uidGenerator.generateMarketUid(market.getEventId(), marketTypeId, specifiers);

        List<ConvertedSelection> convertedSelections = new ArrayList<>();
        for (int i = 0; i < market.getSelections().size(); i++) {
            Selection selection = market.getSelections().get(i);
            int selectionTypeId = marketTypeResolver.resolveSelectionTypeId(marketTypeId, selection.getName(), i);
            String selectionUid = uidGenerator.generateSelectionUid(marketUid, selectionTypeId);
            
            convertedSelections.add(new ConvertedSelection(selectionUid, selectionTypeId, selection.getOdds()));
        }

        return new ConvertedMarket(marketUid, marketTypeId, specifiers, convertedSelections);
    }
}