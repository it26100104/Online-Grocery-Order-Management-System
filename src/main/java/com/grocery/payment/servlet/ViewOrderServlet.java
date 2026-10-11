package com.grocery.payment.servlet;

import com.grocery.payment.model.Order;
import com.grocery.payment.service.OrderFileService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** READ: order confirmation page for one order. */
@WebServlet("/order")
public class ViewOrderServlet extends HttpServlet {

    private final OrderFileService service = new OrderFileService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String id = req.getParameter("id");
        Order order = (id == null) ? null : service.findById(id);
        if (order == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }
        req.setAttribute("order", order);
        req.getRequestDispatcher("/confirmation.jsp").forward(req, resp);
    }
}
