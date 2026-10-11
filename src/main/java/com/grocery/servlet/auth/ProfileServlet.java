package com.grocery.servlet.auth;

import com.grocery.dao.UserDAO;
import com.grocery.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for viewing and updating user profiles,
 * changing passwords, and deactivating accounts.
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = userDAO.getUserById(userId);
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/logout");
            return;
        }

        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/views/auth/profile.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        if ("updateProfile".equalsIgnoreCase(action)) {
            String fullName = req.getParameter("fullName");
            String phone = req.getParameter("phone");
            String address = req.getParameter("address");

            if (fullName == null || fullName.trim().isEmpty()) {
                session.setAttribute("flashMessage", "Full name cannot be empty.");
                session.setAttribute("flashType", "error");
            } else {
                boolean updated = userDAO.updateProfile(userId, fullName.trim(), phone, address);
                if (updated) {
                    session.setAttribute("fullName", fullName.trim());
                    session.setAttribute("flashMessage", "Profile details updated successfully.");
                    session.setAttribute("flashType", "success");
                } else {
                    session.setAttribute("flashMessage", "Failed to update profile.");
                    session.setAttribute("flashType", "error");
                }
            }
            res.sendRedirect(req.getContextPath() + "/profile");

        } else if ("changePassword".equalsIgnoreCase(action)) {
            String currentPassword = req.getParameter("currentPassword");
            String newPassword = req.getParameter("newPassword");
            String confirmPassword = req.getParameter("confirmPassword");

            if (currentPassword == null || newPassword == null || !newPassword.equals(confirmPassword)) {
                session.setAttribute("flashMessage", "New passwords do not match.");
                session.setAttribute("flashType", "error");
            } else if (newPassword.length() < 6) {
                session.setAttribute("flashMessage", "New password must be at least 6 characters.");
                session.setAttribute("flashType", "error");
            } else {
                boolean changed = userDAO.changePassword(userId, currentPassword, newPassword);
                if (changed) {
                    session.setAttribute("flashMessage", "Password changed successfully.");
                    session.setAttribute("flashType", "success");
                } else {
                    session.setAttribute("flashMessage", "Current password entered is incorrect.");
                    session.setAttribute("flashType", "error");
                }
            }
            res.sendRedirect(req.getContextPath() + "/profile");

        } else if ("deactivate".equalsIgnoreCase(action)) {
            boolean deactivated = userDAO.deactivateUser(userId);
            if (deactivated) {
                session.invalidate();
                HttpSession newSession = req.getSession(true);
                newSession.setAttribute("flashMessage", "Your account has been deactivated.");
                newSession.setAttribute("flashType", "info");
                res.sendRedirect(req.getContextPath() + "/login");
            } else {
                session.setAttribute("flashMessage", "Could not deactivate account.");
                session.setAttribute("flashType", "error");
                res.sendRedirect(req.getContextPath() + "/profile");
            }

        } else if ("deletePermanent".equalsIgnoreCase(action)) {
            boolean deleted = userDAO.permanentlyDeleteUser(userId);
            if (deleted) {
                session.invalidate();
                HttpSession newSession = req.getSession(true);
                newSession.setAttribute("flashMessage", "Your account has been permanently deleted.");
                newSession.setAttribute("flashType", "info");
                res.sendRedirect(req.getContextPath() + "/login");
            } else {
                session.setAttribute("flashMessage", "Could not permanently delete account.");
                session.setAttribute("flashType", "error");
                res.sendRedirect(req.getContextPath() + "/profile");
            }

        } else {
            res.sendRedirect(req.getContextPath() + "/profile");
        }
    }
}
