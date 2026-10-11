package com.grocery.servlet.auth;

import com.grocery.dao.UserDAO;
import com.grocery.model.Customer;
import com.grocery.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller for registering new customer accounts.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        // Basic server-side validation
        if (fullName == null || fullName.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            req.getSession(true).setAttribute("flashMessage", "Please fill in all required fields.");
            req.getSession().setAttribute("flashType", "error");
            res.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        if (!password.equals(confirmPassword)) {
            req.getSession(true).setAttribute("flashMessage", "Passwords do not match.");
            req.getSession().setAttribute("flashType", "error");
            res.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        if (password.length() < 6) {
            req.getSession(true).setAttribute("flashMessage", "Password must be at least 6 characters.");
            req.getSession().setAttribute("flashType", "error");
            res.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        if (userDAO.getUserByEmail(email.trim()) != null) {
            req.getSession(true).setAttribute("flashMessage", "An account with this email already exists.");
            req.getSession().setAttribute("flashType", "error");
            res.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        // Hashing password securely (never plain text)
        String passwordHash = PasswordUtil.hash(password.trim());

        // Create new Customer instance (Polymorphism & Inheritance)
        Customer customer = new Customer(
                null,
                fullName.trim(),
                email.trim().toLowerCase(),
                phone != null ? phone.trim() : "",
                address != null ? address.trim() : "",
                passwordHash,
                "ACTIVE"
        );

        boolean success = userDAO.createUser(customer);

        if (success) {
            req.getSession(true).setAttribute("flashMessage", "Account created successfully! Please sign in.");
            req.getSession().setAttribute("flashType", "success");
            // Post-Redirect-Get pattern
            res.sendRedirect(req.getContextPath() + "/login");
        } else {
            req.getSession(true).setAttribute("flashMessage", "Registration failed. Please try again.");
            req.getSession().setAttribute("flashType", "error");
            res.sendRedirect(req.getContextPath() + "/register");
        }
    }
}
