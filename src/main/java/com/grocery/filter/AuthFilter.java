package com.grocery.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * AuthFilter controls access across the application:
 * - Public: login, register, home/index, and static assets (CSS, JS, images)
 * - Admin-only: all URLs under /admin/*
 * - Protected: all customer actions require an active session
 * Session attributes: userId, role (CUSTOMER | ADMIN), fullName.
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Ensure UTF-8 character encoding on all requests and responses
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");

        String contextPath = req.getContextPath();
        String uri = req.getRequestURI();
        String path = uri.substring(contextPath.length());

        HttpSession session = req.getSession(false);

        // Flash message handling for Post-Redirect-Get pattern
        if (session != null) {
            Object flashMsg = session.getAttribute("flashMessage");
            if (flashMsg != null) {
                req.setAttribute("flashMessage", flashMsg);
                req.setAttribute("flashType", session.getAttribute("flashType"));
                session.removeAttribute("flashMessage");
                session.removeAttribute("flashType");
            }
        }

        // Allow static resources
        if (isStaticResource(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Allow public pages and authentication servlets
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Check if user is logged in
        boolean loggedIn = (session != null && session.getAttribute("userId") != null);
        String role = loggedIn ? (String) session.getAttribute("role") : null;

        // Admin-only route guard
        if (path.startsWith("/admin/") || path.equals("/admin")) {
            if (!loggedIn) {
                setFlashMessage(req.getSession(true), "Please sign in with an admin account.", "error");
                res.sendRedirect(contextPath + "/login");
                return;
            }
            if (!"ADMIN".equalsIgnoreCase(role)) {
                setFlashMessage(session, "Access denied. Admin privileges required.", "error");
                res.sendRedirect(contextPath + "/index.jsp");
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        // General protected routes for logged-in users
        if (!loggedIn) {
            setFlashMessage(req.getSession(true), "Please sign in to continue.", "error");
            res.sendRedirect(contextPath + "/login");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isStaticResource(String path) {
        return path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".png")
                || path.endsWith(".jpg")
                || path.endsWith(".jpeg")
                || path.endsWith(".svg")
                || path.endsWith(".ico");
    }

    private boolean isPublicPath(String path) {
        return path.equals("/")
                || path.equals("/index.jsp")
                || path.equals("/login")
                || path.equals("/register")
                || path.equals("/logout")
                || path.equals("/catalog")
                || path.equals("/product-details");
    }

    private void setFlashMessage(HttpSession session, String message, String type) {
        if (session != null) {
            session.setAttribute("flashMessage", message);
            session.setAttribute("flashType", type);
        }
    }

    @Override
    public void destroy() {
        // Clean up filter resources if necessary
    }
}
