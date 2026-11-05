package com.grocery.servlet.payment;

import com.grocery.dao.PaymentDAO;
import com.grocery.model.Payment;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Administrator Payment & Revenue Auditing (/admin/payments).
 * Approves refunds and manages transaction states.
 */
@WebServlet("/admin/payments")
public class AdminPaymentServlet extends HttpServlet {

    private final PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        List<Payment> transactions = paymentDAO.getAllTransactions();
        req.setAttribute("transactions", transactions);
        req.getRequestDispatcher("/WEB-INF/views/payment/admin-payment-list.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(true);
        String action = req.getParameter("action");
        String paymentId = req.getParameter("paymentId");

        if ("approveRefund".equalsIgnoreCase(action)) {
            boolean updated = paymentDAO.updateStatus(paymentId, "REFUNDED");
            if (updated) {
                session.setAttribute("flashMessage", "Refund approved for transaction #" + paymentId + ".");
                session.setAttribute("flashType", "success");
            } else {
                session.setAttribute("flashMessage", "Failed to approve refund.");
                session.setAttribute("flashType", "error");
            }

        } else if ("void".equalsIgnoreCase(action)) {
            boolean updated = paymentDAO.updateStatus(paymentId, "VOID");
            if (updated) {
                session.setAttribute("flashMessage", "Transaction #" + paymentId + " marked as VOID.");
                session.setAttribute("flashType", "info");
            }
        }

        res.sendRedirect(req.getContextPath() + "/admin/payments");
    }
}
