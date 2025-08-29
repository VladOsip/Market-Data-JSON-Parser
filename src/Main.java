import com.fasterxml.jackson.databind.ObjectMapper;

import parser.factory.*;
import parser.parser.*;
import parser.domain.*
;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        File inputDir = new File("inputs");
        if (!inputDir.exists()) {
            inputDir.mkdirs();
            System.out.println("📂 Created 'inputs' folder. Please put JSON files inside and rerun the program.");
            return;
        }

        File[] inputFiles = inputDir.listFiles((_, name) -> name.toLowerCase().endsWith(".json"));
        if (inputFiles == null || inputFiles.length == 0) {
            System.out.println("⚠️ No JSON files found in 'inputs'. Please add files and rerun.");
            return;
        }

        System.out.println("=== Available Input Files ===");
        for (int i = 0; i < inputFiles.length; i++) {
            System.out.println((i + 1) + ". " + inputFiles[i].getName());
        }

        System.out.print("\nEnter the file name or number to process: ");
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim();
        File selectedFile = null;

        // Try match by number
        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < inputFiles.length) {
                selectedFile = inputFiles[index];
            }
        } catch (NumberFormatException ignored) {
        }

        // Try match by name
        if (selectedFile == null) {
            for (File f : inputFiles) {
                if (f.getName().equalsIgnoreCase(choice)) {
                    selectedFile = f;
                    break;
                }
            }
        }

        if (selectedFile == null) {
            System.out.println("❌ Invalid selection. Exiting.");
            return;
        }

        try {
            String inputJson = Files.readString(selectedFile.toPath());
            MarketParser parser = MarketParserFactory.createDefaultParser();
            List<ConvertedMarket> result = parser.parseFromString(inputJson);

            // Prepare output directory
            File outputDir = new File("outputs");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // Generate timestamped output filename
            LocalDateTime now = LocalDateTime.now();
            String timestamp = now.format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss"));
            String baseName = selectedFile.getName().replaceFirst("[.][^.]+$", ""); // strip .json
            File outputFile = new File(outputDir, "Output_" + baseName + "_" + timestamp + ".json");

            // Write result
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, result);

            System.out.println("✅ Conversion complete. Output written to: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("❌ Failed to process file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
