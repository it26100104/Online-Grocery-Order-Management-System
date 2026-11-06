package com.grocery.servlet.catalog;

import com.grocery.dao.ProductDAO;
import com.grocery.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Administrator Product Inventory Management.
 * Displays all products and processes product deletions.
 */
@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        List<Product> products = productDAO.getAllProducts();
        req.setAttribute("products", products);
        req.getRequestDispatcher("/WEB-INF/views/catalog/admin-product-list.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(true);
        String action = req.getParameter("action");
        String productId = req.getParameter("productId");

        if ("delete".equalsIgnoreCase(action)) {
            boolean deleted = productDAO.deleteProduct(productId);
            if (deleted) {
                session.setAttribute("flashMessage", "Product " + productId + " removed from catalog.");
                session.setAttribute("flashType", "success");
            } else {
                session.setAttribute("flashMessage", "Failed to delete product " + productId + ".");
                session.setAttribute("flashType", "error");
            }
        }

        // Post-Redirect-Get
        res.sendRedirect(req.getContextPath() + "/admin/products");
    }
}
