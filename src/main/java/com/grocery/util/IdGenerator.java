package com.grocery.util;

import java.util.List;

/**
 * IdGenerator generates sequential unique IDs with standard prefixes
 * (U001, P001, C001, W001, O001, PAY001, D001, LOG001).
 * It scans existing text files to determine the current highest numeric value.
 */
// ENCAPSULATION: Hides ID calculation and formatting logic behind a clean public API
public class IdGenerator {

    /**
     * Resolves default file name from ID prefix.
     */
    private static String getFileNameForPrefix(String prefix) {
        if (prefix == null) {
            return "reports.txt";
        }
        return switch (prefix.toUpperCase()) {
            case "U" -> "users.txt";
            case "P" -> "items.txt";
            case "C" -> "cart.txt";
            case "W" -> "wishlist.txt";
            case "O" -> "orders.txt";
            case "PAY", "M" -> "payments.txt";
            case "D" -> "deliveries.txt";
            case "LOG" -> "reports.txt";
            default -> "reports.txt";
        };
    }

    /**
     * Generates the next sequential ID for the given prefix using the default data file.
     * Example: getNextId("U") -> "U001", "U002", etc.
     */
    public static synchronized String getNextId(String prefix) {
        String fileName = getFileNameForPrefix(prefix);
        return getNextId(fileName, prefix);
    }

    /**
     * Generates the next sequential ID for a given prefix by reading the specified file.
     * Scans each line for IDs matching the prefix pattern (e.g. U001, PAY012)
     * and increments the highest number found.
     */
    public static synchronized String getNextId(String fileName, String prefix) {
        List<String> lines = FileHandler.readAll(fileName);
        int maxId = 0;

        String upperPrefix = prefix.toUpperCase();

        for (String line : lines) {
            String[] tokens = line.split("\\|");
            for (String token : tokens) {
                String trimmed = token.trim();
                if (trimmed.toUpperCase().startsWith(upperPrefix)) {
                    String numPart = trimmed.substring(upperPrefix.length());
                    try {
                        int num = Integer.parseInt(numPart);
                        if (num > maxId) {
                            maxId = num;
                        }
                    } catch (NumberFormatException ignored) {
                        // Ignore tokens that don't end in numeric digits
                    }
                }
            }
        }

        int nextNum = maxId + 1;
        return String.format("%s%03d", upperPrefix, nextNum);
    }
}
