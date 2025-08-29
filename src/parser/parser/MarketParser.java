package parser.parser;

import parser.domain.*;
import parser.service.MarketConverter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MarketParser {
    
    private final ObjectMapper objectMapper;
    private final MarketConverter marketConverter;

    public MarketParser(MarketConverter marketConverter) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.marketConverter = marketConverter;
    }

    /**
     * Processes input file and writes converted output to specified file
     */
    public void processFile(String inputFilePath, String outputFilePath) throws IOException {
        validateInputFile(inputFilePath);
        createOutputDirectoryIfNeeded(outputFilePath);
        
        try (InputStream inputStream = Files.newInputStream(Paths.get(inputFilePath))) {
            List<ConvertedMarket> convertedMarkets = parseFromInputStream(inputStream);
            writeToFile(convertedMarkets, outputFilePath);
        }
    }

    /**
     * Processes input file and returns converted markets without writing to file
     */
    public List<ConvertedMarket> processFile(String inputFilePath) throws IOException {
        validateInputFile(inputFilePath);
        
        try (InputStream inputStream = Files.newInputStream(Paths.get(inputFilePath))) {
            return parseFromInputStream(inputStream);
        }
    }

    /**
     * Parses markets from InputStream and converts them
     */
    public List<ConvertedMarket> parseFromInputStream(InputStream inputStream) throws IOException {
        JsonNode rootNode = objectMapper.readTree(inputStream);
        List<Market> markets = parseMarketsFromJson(rootNode);
        return convertMarkets(markets);
    }

    /**
     * Parses markets from JSON string and converts them
     */
    public List<ConvertedMarket> parseFromString(String jsonString) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonString);
        List<Market> markets = parseMarketsFromJson(rootNode);
        return convertMarkets(markets);
    }

    /**
     * Writes converted markets to specified output file as JSON
     */
    public void writeToFile(List<ConvertedMarket> convertedMarkets, String outputFilePath) throws IOException {
        createOutputDirectoryIfNeeded(outputFilePath);
        
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            objectMapper.writeValue(writer, convertedMarkets);
        }
    }

    /**
     * Converts list of ConvertedMarket objects to JSON string
     */
    public String toJsonString(List<ConvertedMarket> convertedMarkets) throws IOException {
        return objectMapper.writeValueAsString(convertedMarkets);
    }

    private void validateInputFile(String inputFilePath) throws IOException {
        Path inputPath = Paths.get(inputFilePath);
        
        if (!Files.exists(inputPath)) {
            throw new FileNotFoundException("Input file does not exist: " + inputFilePath);
        }
        
        if (!Files.isReadable(inputPath)) {
            throw new IOException("Input file is not readable: " + inputFilePath);
        }
        
        if (Files.size(inputPath) == 0) {
            throw new IOException("Input file is empty: " + inputFilePath);
        }
    }

    private void createOutputDirectoryIfNeeded(String outputFilePath) throws IOException {
        Path outputPath = Paths.get(outputFilePath);
        Path parentDir = outputPath.getParent();
        
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }
    }

    private List<Market> parseMarketsFromJson(JsonNode rootNode) {
        List<Market> markets = new ArrayList<>();
        
        if (rootNode.isArray()) {
            for (JsonNode marketNode : rootNode) {
                markets.add(parseMarketFromNode(marketNode));
            }
        } else {
            throw new IllegalArgumentException("JSON root must be an array of markets");
        }
        
        return markets;
    }

    private Market parseMarketFromNode(JsonNode marketNode) {
        validateMarketNode(marketNode);
        
        String name = marketNode.get("name").asText();
        String eventId = marketNode.get("event_id").asText();
        
        List<Selection> selections = new ArrayList<>();
        JsonNode selectionsNode = marketNode.get("selections");
        
        if (selectionsNode.isArray()) {
            for (JsonNode selectionNode : selectionsNode) {
                validateSelectionNode(selectionNode);
                String selectionName = selectionNode.get("name").asText();
                double odds = selectionNode.get("odds").asDouble();
                selections.add(new Selection(selectionName, odds));
            }
        } else {
            throw new IllegalArgumentException("Market selections must be an array");
        }
        
        if (selections.isEmpty()) {
            throw new IllegalArgumentException("Market must have at least one selection");
        }
        
        return new Market(name, eventId, selections);
    }

    private void validateMarketNode(JsonNode marketNode) {
        if (marketNode.get("name") == null || marketNode.get("name").asText().trim().isEmpty()) {
            throw new IllegalArgumentException("Market name is required and cannot be empty");
        }
        
        if (marketNode.get("event_id") == null || marketNode.get("event_id").asText().trim().isEmpty()) {
            throw new IllegalArgumentException("Market event_id is required and cannot be empty");
        }
        
        if (marketNode.get("selections") == null) {
            throw new IllegalArgumentException("Market selections are required");
        }
    }

    private void validateSelectionNode(JsonNode selectionNode) {
        if (selectionNode.get("name") == null || selectionNode.get("name").asText().trim().isEmpty()) {
            throw new IllegalArgumentException("Selection name is required and cannot be empty");
        }
        
        if (selectionNode.get("odds") == null) {
            throw new IllegalArgumentException("Selection odds are required");
        }
        
        double odds = selectionNode.get("odds").asDouble();
        if (odds <= 0) {
            throw new IllegalArgumentException("Selection odds must be positive");
        }
    }

    private List<ConvertedMarket> convertMarkets(List<Market> markets) {
        List<ConvertedMarket> convertedMarkets = new ArrayList<>();
        for (Market market : markets) {
            try {
                convertedMarkets.add(marketConverter.convert(market));
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert market: " + market.getName() + " - " + e.getMessage(), e);
            }
        }
        return convertedMarkets;
    }
}