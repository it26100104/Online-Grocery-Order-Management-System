package com.grocery.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ActivityLogger records application events, user actions, and system transactions.
 * Appends records to reports.txt in the format:
 * logId|timestamp|userId|action|details
 */
// ENCAPSULATION: Encapsulates audit trail logging into reports.txt
public class ActivityLogger {

    private static final String FILE_NAME = "reports.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Logs an activity to reports.txt.
     *
     * @param userId  ID of user performing action (or "SYSTEM" / "ANONYMOUS")
     * @param action  Action title (e.g. USER_REGISTER, ORDER_PLACED, LOGIN)
     * @param details Additional description or parameters
     */
    public static synchronized void log(String userId, String action, String details) {
        String logId = IdGenerator.getNextId(FILE_NAME, "LOG");
        String timestamp = LocalDateTime.now().format(FORMATTER);

        String safeUserId = (userId == null || userId.trim().isEmpty()) ? "SYSTEM" : userId.replace("|", "/").trim();
        String safeAction = (action == null || action.trim().isEmpty()) ? "ACTION" : action.replace("|", "/").trim();
        String safeDetails = (details == null) ? "" : details.replace("|", "/").trim();

        String line = String.format("%s|%s|%s|%s|%s", logId, timestamp, safeUserId, safeAction, safeDetails);
        FileHandler.append(FILE_NAME, line);
    }

    /**
     * Reads all recorded log lines.
     */
    public static synchronized List<String> getAllLogs() {
        return FileHandler.readAll(FILE_NAME);
    }

    /**
     * Clears all logs from reports.txt.
     */
    public static synchronized void clearLogs() {
        FileHandler.writeAll(FILE_NAME, List.of());
    }
}
