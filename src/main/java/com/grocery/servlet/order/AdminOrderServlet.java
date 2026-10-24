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
 * Controller for Administrator Order Lifecycle Management (/admin/orders).
 * Transitions status: PENDING -> PROCESSING -> SHIPPED -> DELIVERED, or CANCELLED.
 */
@WebServlet("/admin/orders")
public class AdminOrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        List<Order> orders = orderDAO.getAllOrders();
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/WEB-INF/views/order/admin-order-list.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(true);
        String action = req.getParameter("action");
        String orderId = req.getParameter("orderId");

        if ("updateStatus".equalsIgnoreCase(action)) {
            String newStatus = req.getParameter("status");
            boolean updated = orderDAO.updateStatus(orderId, newStatus);
            if (updated) {
                session.setAttribute("flashMessage", "Order #" + orderId + " status updated to " + newStatus + ".");
                session.setAttribute("flashType", "success");
            } else {
                session.setAttribute("flashMessage", "Failed to update order status.");
                session.setAttribute("flashType", "error");
            }

        } else if ("cancel".equalsIgnoreCase(action)) {
            boolean cancelled = orderDAO.cancelOrder(orderId, "ADMIN");
            if (cancelled) {
                session.setAttribute("flashMessage", "Order #" + orderId + " cancelled and stock restored.");
                session.setAttribute("flashType", "success");
            } else {
                session.setAttribute("flashMessage", "Could not cancel order.");
                session.setAttribute("flashType", "error");
            }
        }

        // Post-Redirect-Get
        res.sendRedirect(req.getContextPath() + "/admin/orders");
    }
}
