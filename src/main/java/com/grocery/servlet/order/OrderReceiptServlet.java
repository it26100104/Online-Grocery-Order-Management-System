package com.grocery.servlet.order;

import com.grocery.dao.OrderDAO;
import com.grocery.dao.UserDAO;
import com.grocery.model.Order;
import com.grocery.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for viewing official order receipts and invoices.
 */
@WebServlet("/order-receipt")
public class OrderReceiptServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        if (userId == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String orderId = req.getParameter("id");
        if (orderId == null || orderId.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        Order order = orderDAO.getOrderById(orderId.trim());
        if (order == null) {
            req.getSession(true).setAttribute("flashMessage", "Order not found.");
            req.getSession().setAttribute("flashType", "error");
            res.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        // Privacy guard: customer can only view their own receipts
        if (!"ADMIN".equalsIgnoreCase(role) && !order.getUserId().equalsIgnoreCase(userId)) {
            res.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        User customer = userDAO.getUserById(order.getUserId());

        req.setAttribute("order", order);
        req.setAttribute("customer", customer);
        req.getRequestDispatcher("/WEB-INF/views/order/order-receipt.jsp").forward(req, res);
    }
}
