package com.grocery.servlet.delivery;

import com.grocery.util.ActivityLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller for viewing system audit trails and clearing old logs (/admin/activity-logs).
 */
@WebServlet("/admin/activity-logs")
public class ActivityLogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        List<String> rawLogs = ActivityLogger.getAllLogs();
        List<LogEntry> logs = new ArrayList<>();

        for (String line : rawLogs) {
            String[] tokens = line.split("\\|");
            if (tokens.length >= 5) {
                logs.add(new LogEntry(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]));
            }
        }

        // Most recent events first
        Collections.reverse(logs);
        req.setAttribute("logs", logs);
        req.getRequestDispatcher("/WEB-INF/views/delivery/activity-log.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(true);
        String action = req.getParameter("action");

        if ("clear".equalsIgnoreCase(action)) {
            ActivityLogger.clearLogs();
            session.setAttribute("flashMessage", "Activity audit log cleared.");
            session.setAttribute("flashType", "info");
        }

        res.sendRedirect(req.getContextPath() + "/admin/activity-logs");
    }

    public static class LogEntry {
        private final String logId;
        private final String timestamp;
        private final String userId;
        private final String action;
        private final String details;

        public LogEntry(String logId, String timestamp, String userId, String action, String details) {
            this.logId = logId;
            this.timestamp = timestamp;
            this.userId = userId;
            this.action = action;
            this.details = details;
        }

        public String getLogId() { return logId; }
        public String getTimestamp() { return timestamp; }
        public String getUserId() { return userId; }
        public String getAction() { return action; }
        public String getDetails() { return details; }
    }
}
