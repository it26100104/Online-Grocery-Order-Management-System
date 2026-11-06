package com.grocery.servlet.payment;

import com.grocery.dao.OrderDAO;
import com.grocery.dao.PaymentDAO;
import com.grocery.model.CardPayment;
import com.grocery.model.CashOnDeliveryPayment;
import com.grocery.model.Order;
import com.grocery.model.Payment;
import com.grocery.model.PaymentMethod;
import com.grocery.model.WalletPayment;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for Payment Execution (/payment).
 * Accepts COD, Credit/Debit Cards, or Digital Wallets and saves reusable methods.
 */
@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String orderId = req.getParameter("orderId");
        if (orderId == null) {
            res.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        Order order = orderDAO.getOrderById(orderId);
        if (order == null || !order.getUserId().equalsIgnoreCase(userId)) {
            res.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        List<PaymentMethod> savedMethods = paymentDAO.getMethodsByUser(userId);

        req.setAttribute("order", order);
        req.setAttribute("savedMethods", savedMethods);
        req.getRequestDispatcher("/WEB-INF/views/payment/payment.jsp").forward(req, res);
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

        String orderId = req.getParameter("orderId");
        String methodType = req.getParameter("methodType");
        String saveMethod = req.getParameter("saveMethod");

        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            res.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        String now = LocalDateTime.now().format(FORMATTER);
        Payment payment;

        // POLYMORPHISM: Instantiate appropriate Payment subclass
        if ("CARD".equalsIgnoreCase(methodType)) {
            CardPayment cardPayment = new CardPayment(null, orderId, userId, order.getTotal(), "UNPAID", now);
            String cardNumber = req.getParameter("cardNumber");
            String expiry = req.getParameter("expiry");
            String cvv = req.getParameter("cvv");
            cardPayment.setCardDetails(cardNumber, expiry, cvv);
            payment = cardPayment;

            if ("true".equalsIgnoreCase(saveMethod) && cardNumber != null && cardNumber.length() >= 4) {
                String cleanNum = cardNumber.replaceAll("\\s+", "");
                String last4 = cleanNum.substring(cleanNum.length() - 4);
                paymentDAO.saveMethod(userId, "CARD", "Debit Card (•••• " + last4 + ")", last4);
            }

        } else if ("WALLET".equalsIgnoreCase(methodType)) {
            WalletPayment walletPayment = new WalletPayment(null, orderId, userId, order.getTotal(), "UNPAID", now);
            String walletMobile = req.getParameter("walletMobile");
            walletPayment.setWalletMobile(walletMobile);
            payment = walletPayment;

            if ("true".equalsIgnoreCase(saveMethod) && walletMobile != null && walletMobile.length() >= 4) {
                String cleanPhone = walletMobile.replaceAll("[^0-9]", "");
                String last4 = cleanPhone.substring(cleanPhone.length() - 4);
                paymentDAO.saveMethod(userId, "WALLET", "Digital Wallet (•••• " + last4 + ")", last4);
            }

        } else {
            payment = new CashOnDeliveryPayment(null, orderId, userId, order.getTotal(), "UNPAID", now);
        }

        // POLYMORPHISM: Polymorphic payment processing
        boolean processed = payment.processPayment();

        if (processed) {
            paymentDAO.recordPayment(payment);
            if (payment.isPaid()) {
                orderDAO.updateStatus(orderId, "PROCESSING");
            }
            session.setAttribute("flashMessage", "Payment processed successfully!");
            session.setAttribute("flashType", "success");
            res.sendRedirect(req.getContextPath() + "/invoice?paymentId=" + payment.getPaymentId());
        } else {
            session.setAttribute("flashMessage", "Payment validation failed. Please check your credentials.");
            session.setAttribute("flashType", "error");
            res.sendRedirect(req.getContextPath() + "/payment?orderId=" + orderId);
        }
    }
}
