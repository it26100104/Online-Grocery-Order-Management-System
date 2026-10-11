package com.grocery.servlet.auth;

import com.grocery.util.ActivityLogger;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for signing out active users and invalidating sessions.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(false);
        if (session != null) {
            String userId = (String) session.getAttribute("userId");
            if (userId != null) {
                ActivityLogger.log(userId, "LOGOUT", "User signed out");
            }
            session.invalidate();
        }

        HttpSession newSession = req.getSession(true);
        newSession.setAttribute("flashMessage", "You have been logged out successfully.");
        newSession.setAttribute("flashType", "success");

        res.sendRedirect(req.getContextPath() + "/login");
    }
}
