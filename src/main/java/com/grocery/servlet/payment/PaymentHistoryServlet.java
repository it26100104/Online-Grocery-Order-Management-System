package com.grocery.servlet.payment;

import com.grocery.dao.PaymentDAO;
import com.grocery.model.Payment;
import com.grocery.model.PaymentMethod;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Customer Payment History and Saved Payment Methods (/payment-history).
 */
@WebServlet("/payment-history")
public class PaymentHistoryServlet extends HttpServlet {

    private final PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Payment> transactions = paymentDAO.getTransactionsByUser(userId);
        List<PaymentMethod> savedMethods = paymentDAO.getMethodsByUser(userId);

        req.setAttribute("transactions", transactions);
        req.setAttribute("savedMethods", savedMethods);
        req.getRequestDispatcher("/WEB-INF/views/payment/payment-history.jsp").forward(req, res);
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

        if ("deleteMethod".equalsIgnoreCase(action)) {
            String methodId = req.getParameter("methodId");
            paymentDAO.deleteMethod(methodId);
            session.setAttribute("flashMessage", "Saved payment method removed.");
            session.setAttribute("flashType", "info");

        } else if ("requestRefund".equalsIgnoreCase(action)) {
            String paymentId = req.getParameter("paymentId");
            Payment p = paymentDAO.getTransactionById(paymentId);
            if (p != null && p.getUserId().equalsIgnoreCase(userId) && p.isPaid()) {
                paymentDAO.updateStatus(paymentId, "REFUND_REQUESTED");
                session.setAttribute("flashMessage", "Refund request submitted for transaction #" + paymentId + ".");
                session.setAttribute("flashType", "success");
            }
        }

        res.sendRedirect(req.getContextPath() + "/payment-history");
    }
}
