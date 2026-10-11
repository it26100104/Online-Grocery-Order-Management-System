package com.grocery.payment.servlet;

import com.grocery.payment.model.Order;
import com.grocery.payment.service.OrderFileService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** UPDATE: change the delivery details of an existing order. */
@WebServlet("/updateOrder")
public class UpdateOrderServlet extends HttpServlet {

    private final OrderFileService service = new OrderFileService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Order order = service.findById(String.valueOf(req.getParameter("id")));
        if (order == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }
        req.setAttribute("order", order);
        req.getRequestDispatcher("/edit-order.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String id = req.getParameter("orderId");
        String name = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String city = req.getParameter("city");

        boolean bad = name == null || name.trim().isEmpty()
                || address == null || address.trim().isEmpty()
                || city == null || city.trim().isEmpty()
                || phone == null || !phone.matches("0\\d{9}");
        if (bad) {
            req.setAttribute("error", "Please fill all fields. Phone must be 10 digits starting with 0.");
            req.setAttribute("order", service.findById(String.valueOf(id)));
            req.getRequestDispatcher("/edit-order.jsp").forward(req, resp);
            return;
        }

        service.updateDelivery(id, name, phone, address, city);
        resp.sendRedirect(req.getContextPath() + "/order?id=" + id);
    }
}
