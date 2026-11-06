package com.grocery.servlet.payment;

import com.grocery.dao.OrderDAO;
import com.grocery.dao.PaymentDAO;
import com.grocery.dao.UserDAO;
import com.grocery.model.Order;
import com.grocery.model.Payment;
import com.grocery.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for viewing official payment invoices.
 */
@WebServlet("/invoice")
public class InvoiceServlet extends HttpServlet {

    private final PaymentDAO paymentDAO = new PaymentDAO();
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

        String paymentId = req.getParameter("paymentId");
        if (paymentId == null) {
            res.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        Payment payment = paymentDAO.getTransactionById(paymentId);
        if (payment == null) {
            req.getSession(true).setAttribute("flashMessage", "Invoice transaction not found.");
            req.getSession().setAttribute("flashType", "error");
            res.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        // Privacy check
        if (!"ADMIN".equalsIgnoreCase(role) && !payment.getUserId().equalsIgnoreCase(userId)) {
            res.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        Order order = orderDAO.getOrderById(payment.getOrderId());
        User customer = userDAO.getUserById(payment.getUserId());

        req.setAttribute("payment", payment);
        req.setAttribute("order", order);
        req.setAttribute("customer", customer);

        req.getRequestDispatcher("/WEB-INF/views/payment/invoice.jsp").forward(req, res);
    }
}
