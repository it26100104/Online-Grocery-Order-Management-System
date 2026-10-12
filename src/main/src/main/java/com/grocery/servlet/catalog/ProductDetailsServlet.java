package com.grocery.servlet.catalog;

import com.grocery.dao.ProductDAO;
import com.grocery.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller for displaying full details of a specific grocery product.
 */
@WebServlet("/product-details")
public class ProductDetailsServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String id = req.getParameter("id");
        if (id == null || id.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/catalog");
            return;
        }

        Product product = productDAO.getById(id.trim());
        if (product == null) {
            req.getSession(true).setAttribute("flashMessage", "Product not found.");
            req.getSession().setAttribute("flashType", "error");
            res.sendRedirect(req.getContextPath() + "/catalog");
            return;
        }

        req.setAttribute("product", product);
        req.getRequestDispatcher("/WEB-INF/views/catalog/product-details.jsp").forward(req, res);
    }
}
