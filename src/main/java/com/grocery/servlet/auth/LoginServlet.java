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
 * Controller for user sign-in authentication.
 * Handles credential verification, session creation, and polymorphic redirection.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            String role = (String) session.getAttribute("role");
            if ("ADMIN".equalsIgnoreCase(role)) {
                res.sendRedirect(req.getContextPath() + "/admin/products");
            } else {
                res.sendRedirect(req.getContextPath() + "/catalog");
            }
            return;
        }

        req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            req.getSession(true).setAttribute("flashMessage", "Please enter both email and password.");
            req.getSession().setAttribute("flashType", "error");
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = userDAO.validateLogin(email.trim(), password.trim());

        if (user != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("role", user.getRole());
            session.setAttribute("fullName", user.getFullName());

            session.setAttribute("flashMessage", "Welcome back, " + user.getFullName() + "!");
            session.setAttribute("flashType", "success");

            // POLYMORPHISM: Destination URL determined dynamically by User subclass (Customer vs AdminUser)
            res.sendRedirect(req.getContextPath() + user.getDashboardUrl());
        } else {
            HttpSession session = req.getSession(true);
            session.setAttribute("flashMessage", "Invalid email, password, or account is deactivated.");
            session.setAttribute("flashType", "error");
            res.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
