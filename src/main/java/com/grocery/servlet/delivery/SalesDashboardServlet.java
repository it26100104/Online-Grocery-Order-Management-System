package com.grocery.servlet.delivery;

import com.grocery.dao.DeliveryDAO;
import com.grocery.model.DeliveryReport;
import com.grocery.model.SalesReport;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller for Sales & Analytics Reporting Dashboard (/admin/reports).
 */
@WebServlet("/admin/reports")
public class SalesDashboardServlet extends HttpServlet {

    private final DeliveryDAO deliveryDAO = new DeliveryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        SalesReport salesReport = deliveryDAO.generateSalesReport();
        DeliveryReport deliveryReport = deliveryDAO.generateDeliveryReport();

        req.setAttribute("salesReport", salesReport);
        req.setAttribute("deliveryReport", deliveryReport);

        req.getRequestDispatcher("/WEB-INF/views/delivery/sales-dashboard.jsp").forward(req, res);
    }
}
