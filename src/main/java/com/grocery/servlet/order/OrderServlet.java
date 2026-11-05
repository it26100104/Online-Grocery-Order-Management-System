package com.grocery.servlet.order;

import com.grocery.dao.OrderDAO;
import com.grocery.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Customer Orders (/orders).
 * Handles placing orders from cart, viewing order history, and cancelling pending orders.
 */
@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Order> orders = orderDAO.getOrdersByUser(userId);
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/WEB-INF/views/order/order-history.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(true);
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        if ("placeOrder".equalsIgnoreCase(action)) {
            String address = req.getParameter("address");
            String orderType = req.getParameter("orderType");
            String couponCode = req.getParameter("couponCode");

            if (address == null || address.trim().isEmpty()) {
                session.setAttribute("flashMessage", "Please provide a delivery address.");
                session.setAttribute("flashType", "error");
                res.sendRedirect(req.getContextPath() + "/cart-summary");
                return;
            }

            Order placed = orderDAO.placeOrder(userId, address.trim(), orderType, couponCode);
            if (placed != null) {
                session.setAttribute("flashMessage", "Order #" + placed.getOrderId() + " placed successfully!");
                session.setAttribute("flashType", "success");
                res.sendRedirect(req.getContextPath() + "/order-receipt?id=" + placed.getOrderId());
            } else {
                session.setAttribute("flashMessage", "Could not place order (cart is empty or item out of stock).");
                session.setAttribute("flashType", "error");
                res.sendRedirect(req.getContextPath() + "/cart");
            }

        } else if ("cancel".equalsIgnoreCase(action)) {
            String orderId = req.getParameter("orderId");
            boolean cancelled = orderDAO.cancelOrder(orderId, userId);
            if (cancelled) {
                session.setAttribute("flashMessage", "Order #" + orderId + " has been cancelled and stock restored.");
                session.setAttribute("flashType", "success");
            } else {
                session.setAttribute("flashMessage", "Cannot cancel order (it is already processing or shipped).");
                session.setAttribute("flashType", "error");
            }
            res.sendRedirect(req.getContextPath() + "/orders");

        } else {
            res.sendRedirect(req.getContextPath() + "/orders");
        }
    }
}
