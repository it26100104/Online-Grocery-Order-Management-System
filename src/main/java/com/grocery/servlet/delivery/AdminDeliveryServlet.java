package com.grocery.servlet.delivery;

import com.grocery.dao.DeliveryDAO;
import com.grocery.model.Delivery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Logistics Delivery Scheduling & Dispatching (/admin/deliveries).
 */
@WebServlet("/admin/deliveries")
public class AdminDeliveryServlet extends HttpServlet {

    private final DeliveryDAO deliveryDAO = new DeliveryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        List<Delivery> deliveries = deliveryDAO.getAllDeliveries();
        req.setAttribute("deliveries", deliveries);
        req.getRequestDispatcher("/WEB-INF/views/delivery/delivery-schedule.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(true);
        String action = req.getParameter("action");
        String deliveryId = req.getParameter("deliveryId");

        if ("assignAgent".equalsIgnoreCase(action)) {
            String agent = req.getParameter("agent");
            boolean assigned = deliveryDAO.assignAgent(deliveryId, agent);
            if (assigned) {
                session.setAttribute("flashMessage", "Agent assigned successfully.");
                session.setAttribute("flashType", "success");
            }

        } else if ("updateStatus".equalsIgnoreCase(action)) {
            String status = req.getParameter("status");
            boolean updated = deliveryDAO.updateStatus(deliveryId, status);
            if (updated) {
                session.setAttribute("flashMessage", "Delivery status updated to " + status + ".");
                session.setAttribute("flashType", "success");
            }

        } else if ("schedule".equalsIgnoreCase(action)) {
            String orderId = req.getParameter("orderId");
            String userId = req.getParameter("userId");
            String type = req.getParameter("type");
            String address = req.getParameter("address");

            boolean scheduled = deliveryDAO.createForOrder(orderId, userId, type, address);
            if (scheduled) {
                session.setAttribute("flashMessage", "Delivery scheduled for Order #" + orderId + ".");
                session.setAttribute("flashType", "success");
            }
        }

        res.sendRedirect(req.getContextPath() + "/admin/deliveries");
    }
}
