package com.grocery.payment.servlet;

import com.grocery.payment.service.OrderFileService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** DELETE: cancel (remove) an order. */
@WebServlet("/deleteOrder")
public class DeleteOrderServlet extends HttpServlet {

    private final OrderFileService service = new OrderFileService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        service.deleteOrder(String.valueOf(req.getParameter("id")));
        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}
