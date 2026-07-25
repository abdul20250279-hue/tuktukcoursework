package fileparsing;

import datamodels.Part;
import datamodels.dealer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LegacyDataParser {
    private static final String[] MONTH_NAMES = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private static final DateTimeFormatter[] DATE_FORMATS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("d-MMM-yyyy"),
            DateTimeFormatter.ofPattern("MMM d, yyyy")
    };


    private final List<String> skippedLines = new ArrayList<>();

    public List<String> getSkippedLines() {
        return skippedLines;
    }

    public List<Part> loadInventory(Path file) throws IOException {
        List<Part> parts = new ArrayList<>();
        List<String> lines = Files.readAllLines(file);

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }
            try {
                Part part = parseInventoryLine(line);
                parts.add(part);
            } catch (Exception e) {
                skippedLines.add(line + "  (skipped: " + e.getMessage() + ")");
            }
        }
        return parts;
    }

    public Part parseInventoryLine(String line) {
        String[] fields = splitLine(line);

        if (fields.length < 6) {
            throw new IllegalArgumentException("not enough fields");
        }

        String code = fields[0].trim();
        String name = fields[1].trim();
        String supplier = fields[2].trim();
        double price = parsePrice(fields[3]);
        int quantity = parseQuantity(fields[4]);
        String category = normaliseCategory(fields[5]);

        LocalDate date = fields.length > 6 ? parseDate(fields[6]) : null;
        String image = fields.length > 7 ? fields[7].trim() : "";

        if (code.isEmpty() || name.isEmpty()) {
            throw new IllegalArgumentException("missing code or name");
        }
        return new Part(code, name, supplier, price, quantity, category, date, image);
    }

    public List<dealer> loadDealers(Path file) throws IOException {
        List<dealer> dealers = new ArrayList<>();
        List<String> lines = Files.readAllLines(file);

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }
            try {
                dealer dealer = parseDealerLine(line);
                dealers.add(dealer);
            } catch (Exception e) {
                skippedLines.add(line + "  (skipped: " + e.getMessage() + ")");
            }
        }
        return dealers;
    }

    public dealer parseDealerLine(String line) {
        String[] fields = splitLine(line);

        if (fields.length < 3) {
            throw new IllegalArgumentException("not enough fields");
        }

        String code = fields[0].trim();
        String name = fields[1].trim();
        String phone = "";
        String location = "";

        if (fields.length == 3) {
            location = fields[2].trim();
            phone = "N/A";
        } else if (fields.length >= 4) {
            phone = fields[2].trim();
            location = fields[3].trim();
        }

        if (code.isEmpty() || name.isEmpty()) {
            throw new IllegalArgumentException("missing code or name");
        }

        return new dealer(code, name, phone, location);
    }

    private String[] splitLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == ',' || c == '|' || c == ';') {
                tokens.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        tokens.add(current.toString().trim());

        List<String> fixedTokens = new ArrayList<>();
        int i = 0;
        while (i < tokens.size()) {
            String token = tokens.get(i);
            boolean isMonth = false;
            for (String month : MONTH_NAMES) {
                if (token.startsWith(month)) {
                    isMonth = true;
                    break;
                }
            }
            if (isMonth && i + 1 < tokens.size() && looksLikeYear(tokens.get(i + 1))) {
                fixedTokens.add(token + ", " + tokens.get(i + 1));
                i = i + 2;
            } else {
                fixedTokens.add(token);
                i = i + 1;
            }
        }
        return fixedTokens.toArray(new String[0]);
    }

    private boolean looksLikeMonthAndDay(String token) {
        for (String month : MONTH_NAMES) {
            if (token.startsWith(month)) {
                return true;
            }
        }
        return false;
    }

    private boolean looksLikeYear(String token) {
        if (token.length() != 4) {
            return false;
        }
        for (int i = 0; i < token.length(); i++) {
            if (!Character.isDigit(token.charAt(i))) {
                return false;
            }
        }
        return true;
    }



    private double parsePrice(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return 0.0;
        }
        String cleaned = raw.replaceAll("[^0-9.]", "");

        if (cleaned.isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private int parseQuantity(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return 0;
        }
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (Character.isDigit(c)) {
                number.append(c);
            }
        }
        if (number.length() == 0) {
            return 0;
        }
        return Integer.parseInt(number.toString());
    }
    private String normaliseCategory(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return "Uncategorised";
        }
        String trimmed = raw.trim().toLowerCase();
        String firstLetter = trimmed.substring(0, 1).toUpperCase();
        String rest = trimmed.substring(1);
        return firstLetter + rest;
    }


    private LocalDate parseDate(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return null;
        }
        String trimmed = raw.trim();
        for (DateTimeFormatter format : DATE_FORMATS) {
            try {
                return LocalDate.parse(trimmed, format);
            } catch (DateTimeParseException e) {
            }
        }
        return null;
    }
}
