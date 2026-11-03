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
 * Controller for Customer Delivery Tracking (/deliveries).
 */
@WebServlet("/deliveries")
public class DeliveryTrackingServlet extends HttpServlet {

    private final DeliveryDAO deliveryDAO = new DeliveryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Delivery> deliveries = deliveryDAO.getDeliveriesByUser(userId);
        req.setAttribute("deliveries", deliveries);
        req.getRequestDispatcher("/WEB-INF/views/delivery/delivery-tracking.jsp").forward(req, res);
    }
}
