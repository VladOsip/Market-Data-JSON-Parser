package parser.service;

import parser.domain.Market;
import parser.domain.ConvertedMarket;

public interface MarketConverter {
    ConvertedMarket convert(Market market);
}