package com.grocery.payment.servlet;

import com.grocery.payment.model.Order;
import com.grocery.payment.model.Payment;
import com.grocery.payment.service.DeliveryChargeCalculator;
import com.grocery.payment.service.OrderFileService;
import com.grocery.payment.service.PaymentFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** CREATE: handles the checkout form. */
@WebServlet("/placeOrder")
public class PlaceOrderServlet extends HttpServlet {

    private final OrderFileService service = new OrderFileService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String city = req.getParameter("city");
        String method = req.getParameter("paymentMethod");
        String cardHolder = req.getParameter("cardHolder");
        String cardNumber = req.getParameter("cardNumber");
        String subtotalText = req.getParameter("subtotal");

        // ----- validation -----
        String error = null;
        double subtotal = 0;
        try {
            subtotal = Double.parseDouble(subtotalText);
        } catch (Exception e) {
            error = "Invalid cart total.";
        }
        if (error == null && isEmpty(name)) error = "Full name is required.";
        else if (error == null && (phone == null || !phone.matches("0\\d{9}")))
            error = "Phone must be 10 digits, starting with 0 (example: 0771234567).";
        else if (error == null && isEmpty(address)) error = "Delivery address is required.";
        else if (error == null && isEmpty(city)) error = "City is required.";
        else if (error == null && "CARD".equals(method)) {
            if (isEmpty(cardHolder)) error = "Card holder name is required.";
            else if (cardNumber == null || !cardNumber.replace(" ", "").matches("\\d{16}"))
                error = "Card number must be 16 digits.";
        }

        if (error != null) {
            req.setAttribute("error", error);
            req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
            return;
        }

        // ----- calculate + save -----
        if (cardNumber != null) cardNumber = cardNumber.replace(" ", "");
        Payment payment = PaymentFactory.create(method, cardHolder, cardNumber); // polymorphism
        double delivery = DeliveryChargeCalculator.calculate(city, subtotal);
        double fee = payment.calculateFee(subtotal);

        String id = service.generateOrderId();
        Order order = new Order(id, name, phone, address, city, subtotal,
                delivery, payment.getMethod(), fee, "PLACED");
        service.createOrder(order);

        resp.sendRedirect(req.getContextPath() + "/order?id=" + id);
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
