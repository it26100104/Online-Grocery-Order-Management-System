package com.grocery.payment.servlet;

import com.grocery.payment.model.Order;
import com.grocery.payment.service.OrderFileService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** READ: list all orders, with optional search by order ID or customer name. */
@WebServlet("/orders")
public class OrderListServlet extends HttpServlet {

    private final OrderFileService service = new OrderFileService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String q = req.getParameter("q");
        List<Order> result = new ArrayList<>();
        for (Order o : service.readAll()) {
            if (q == null || q.trim().isEmpty()
                    || o.getOrderId().equalsIgnoreCase(q.trim())
                    || o.getCustomerName().toLowerCase().contains(q.trim().toLowerCase())) {
                result.add(o);
            }
        }
        req.setAttribute("orders", result);
        req.setAttribute("q", q);
        req.getRequestDispatcher("/orders.jsp").forward(req, resp);
    }
}
