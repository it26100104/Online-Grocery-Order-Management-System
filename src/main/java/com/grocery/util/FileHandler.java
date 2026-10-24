package com.grocery.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * FileHandler manages all persistent text-file operations.
 * Storage location: <user.home>/GroceryData
 * Ensures UTF-8 encoding, skips blank lines, synchronized operations,
 * and safe atomic replacement via temporary files.
 */
// ENCAPSULATION: Centralizes and protects direct filesystem access behind safe public static methods
public class FileHandler {

    // Persistent directory outside the project root so rebuilds never wipe data
    private static final String DATA_DIR = System.getProperty("user.home") + File.separator + "GroceryData";

    static {
        ensureDataDirectoryExists();
    }

    /**
     * Ensures that the GroceryData directory exists on the system.
     */
    public static synchronized void ensureDataDirectoryExists() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                System.out.println("[FileHandler] Created data storage directory: " + DATA_DIR);
            }
        }
    }

    /**
     * Resolves the full path of a data file.
     */
    public static File getFile(String fileName) {
        return new File(DATA_DIR, fileName);
    }

    /**
     * Returns the base directory path string.
     */
    public static String getDataDirectory() {
        return DATA_DIR;
    }

    /**
     * Checks if a file exists and is not empty.
     */
    public static synchronized boolean fileExistsAndNotEmpty(String fileName) {
        File file = getFile(fileName);
        return file.exists() && file.length() > 0;
    }

    /**
     * Reads all non-blank lines from the given file in UTF-8 encoding.
     * Returns an empty list if the file does not exist yet.
     */
    public static synchronized List<String> readAll(String fileName) {
        ensureDataDirectoryExists();
        List<String> lines = new ArrayList<>();
        File file = getFile(fileName);

        if (!file.exists()) {
            return lines;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    lines.add(trimmed);
                }
            }
        } catch (IOException e) {
            System.err.println("[FileHandler] Error reading " + fileName + ": " + e.getMessage());
        }
        return lines;
    }

    /**
     * Appends a single line to the given file in UTF-8 encoding.
     */
    public static synchronized void append(String fileName, String line) {
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        ensureDataDirectoryExists();
        File file = getFile(fileName);

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
            writer.write(line.trim());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("[FileHandler] Error appending to " + fileName + ": " + e.getMessage());
        }
    }

    /**
     * Overwrites the file with the provided lines safely using a .tmp file.
     * The temp file is written completely and then moved to replace the target file.
     */
    public static synchronized void writeAll(String fileName, List<String> lines) {
        ensureDataDirectoryExists();
        File targetFile = getFile(fileName);
        File tempFile = new File(DATA_DIR, fileName + ".tmp");

        try {
            // 1. Write everything to temporary file
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(tempFile, false), StandardCharsets.UTF_8))) {
                if (lines != null) {
                    for (String line : lines) {
                        if (line != null && !line.trim().isEmpty()) {
                            writer.write(line.trim());
                            writer.newLine();
                        }
                    }
                }
                writer.flush();
            }

            // 2. Replace target file with temp file
            Path tempPath = tempFile.toPath();
            Path targetPath = targetFile.toPath();

            try {
                Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException atomicEx) {
                // Fallback to standard replace if atomic move is unsupported by the OS filesystem
                Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            System.err.println("[FileHandler] Error writing to " + fileName + ": " + e.getMessage());
            if (tempFile.exists()) {
                boolean deleted = tempFile.delete();
                if (!deleted) {
                    tempFile.deleteOnExit();
                }
            }
        }
    }
}
