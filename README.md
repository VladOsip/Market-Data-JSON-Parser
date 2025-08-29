# Market JSON Converter

## Overview
This Java project parses raw JSON files containing market data, converts them into a standardized format, and outputs the results as pretty-printed JSON files. It is designed to handle multiple market types, extract key specifiers, generate deterministic UIDs, and ensure consistent data representation for downstream systems.

---

## Features
- Parses JSON input files from the `inputs` folder.
- Converts raw `Market` and `Selection` data into `ConvertedMarket` objects.
- Supports multiple market types:
  - 1x2
  - Total (Over/Under)
  - Handicap (Full-time, First Half, Second Half)
  - Both Teams to Score
- Automatically extracts relevant specifiers (e.g., `total`, `handicap`).
- Generates deterministic UIDs for markets and selections.
- Outputs results in `outputs` folder with timestamped filenames.
- Validates input data to ensure all required fields are present and odds are positive.
- Includes unit and integration tests for domain, services, and parsing logic.

---

## Requirements
- Java 17+ (or compatible)
- Jackson Databind for JSON processing
- JUnit 6 for testing

---

## How to Run

1. Place your JSON files in the `inputs` folder.
2. Run the program: java -cp "bin;lib/*" Main
3. Select the file to process (by number or filename).
4. Output will be written to the `outputs` folder with a timestamped filename.

---

## Testing

- Unit tests cover:
  - Domain validation (`Market`, `Selection`)
  - Market type resolution
  - UID generation
- Integration tests cover parsing and conversion of sample JSON input.

Run tests with JUnit, for example: java -jar lib/junit-platform-console-standalone-6.0.0-RC2.jar --class-path bin --scan-class-path

---

## Notes
- Input JSON root must be an array of market objects.
- The program creates `inputs` and `outputs` folders automatically if they don't exist.
- Invalid or incomplete data will throw an error, ensuring clean and consistent output.



