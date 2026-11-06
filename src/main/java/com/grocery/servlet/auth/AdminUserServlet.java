package com.grocery.servlet.auth;

import com.grocery.dao.UserDAO;
import com.grocery.model.AdminUser;
import com.grocery.model.User;
import com.grocery.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Admin User Management (/admin/users).
 * Lists all registered users, deactivates/reactivates accounts,
 * and creates new administrator credentials.
 */
@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        List<User> users = userDAO.getAllUsers();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/WEB-INF/views/auth/user-list.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(true);
        String action = req.getParameter("action");

        if ("deactivate".equalsIgnoreCase(action)) {
            String targetId = req.getParameter("userId");
            // Prevent administrator from deactivating themselves
            String currentAdminId = (String) session.getAttribute("userId");
            if (currentAdminId != null && currentAdminId.equalsIgnoreCase(targetId)) {
                session.setAttribute("flashMessage", "You cannot deactivate your own active session.");
                session.setAttribute("flashType", "error");
            } else {
                boolean done = userDAO.deactivateUser(targetId);
                if (done) {
                    session.setAttribute("flashMessage", "User account " + targetId + " deactivated.");
                    session.setAttribute("flashType", "success");
                } else {
                    session.setAttribute("flashMessage", "Could not deactivate user.");
                    session.setAttribute("flashType", "error");
                }
            }

        } else if ("reactivate".equalsIgnoreCase(action)) {
            String targetId = req.getParameter("userId");
            boolean done = userDAO.reactivateUser(targetId);
            if (done) {
                session.setAttribute("flashMessage", "User account " + targetId + " reactivated.");
                session.setAttribute("flashType", "success");
            } else {
                session.setAttribute("flashMessage", "Could not reactivate user.");
                session.setAttribute("flashType", "error");
            }

        } else if ("deletePermanent".equalsIgnoreCase(action)) {
            String targetId = req.getParameter("userId");
            String currentAdminId = (String) session.getAttribute("userId");
            if (currentAdminId != null && currentAdminId.equalsIgnoreCase(targetId)) {
                session.setAttribute("flashMessage", "You cannot permanently delete your own active administrator account.");
                session.setAttribute("flashType", "error");
            } else {
                boolean done = userDAO.permanentlyDeleteUser(targetId);
                if (done) {
                    session.setAttribute("flashMessage", "User account " + targetId + " permanently deleted.");
                    session.setAttribute("flashType", "success");
                } else {
                    session.setAttribute("flashMessage", "Could not permanently delete user account.");
                    session.setAttribute("flashType", "error");
                }
            }

        } else if ("createAdmin".equalsIgnoreCase(action)) {
            String fullName = req.getParameter("fullName");
            String email = req.getParameter("email");
            String phone = req.getParameter("phone");
            String address = req.getParameter("address");
            String password = req.getParameter("password");

            if (fullName == null || fullName.trim().isEmpty()
                    || email == null || email.trim().isEmpty()
                    || password == null || password.trim().isEmpty()) {
                session.setAttribute("flashMessage", "All fields are required to create an admin.");
                session.setAttribute("flashType", "error");
            } else if (userDAO.getUserByEmail(email.trim()) != null) {
                session.setAttribute("flashMessage", "An account with this email already exists.");
                session.setAttribute("flashType", "error");
            } else {
                String hash = PasswordUtil.hash(password.trim());
                AdminUser newAdmin = new AdminUser(
                        null,
                        fullName.trim(),
                        email.trim().toLowerCase(),
                        phone != null ? phone.trim() : "",
                        address != null ? address.trim() : "",
                        hash,
                        "ACTIVE"
                );
                boolean created = userDAO.createUser(newAdmin);
                if (created) {
                    session.setAttribute("flashMessage", "Admin account created successfully.");
                    session.setAttribute("flashType", "success");
                } else {
                    session.setAttribute("flashMessage", "Failed to create admin.");
                    session.setAttribute("flashType", "error");
                }
            }
        }

        // Post-Redirect-Get
        res.sendRedirect(req.getContextPath() + "/admin/users");
    }
}
