package com.grocery.servlet.catalog;

import com.grocery.dao.ProductDAO;
import com.grocery.model.NonPerishableProduct;
import com.grocery.model.PerishableProduct;
import com.grocery.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for editing existing products in the inventory.
 */
@WebServlet("/admin/products/edit")
public class EditProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String id = req.getParameter("id");
        if (id == null || id.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/admin/products");
            return;
        }

        Product product = productDAO.getById(id.trim());
        if (product == null) {
            req.getSession(true).setAttribute("flashMessage", "Product not found.");
            req.getSession().setAttribute("flashType", "error");
            res.sendRedirect(req.getContextPath() + "/admin/products");
            return;
        }

        req.setAttribute("product", product);
        req.setAttribute("categories", productDAO.getCategories());
        req.getRequestDispatcher("/WEB-INF/views/catalog/edit-product.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(true);

        String productId = req.getParameter("productId");
        String name = req.getParameter("name");
        
        String category = req.getParameter("category");
        String categorySelect = req.getParameter("categorySelect");
        String customCategory = req.getParameter("customCategory");

        if (customCategory != null && !customCategory.trim().isEmpty()) {
            category = customCategory.trim();
        } else if (categorySelect != null && !categorySelect.trim().isEmpty() && !"__NEW__".equalsIgnoreCase(categorySelect)) {
            category = categorySelect.trim();
        }

        String priceStr = req.getParameter("price");
        String stockStr = req.getParameter("stock");
        String description = req.getParameter("description");
        String type = req.getParameter("type");
        String extra = req.getParameter("extra");
        String imageUrl = req.getParameter("imageUrl");

        if (productId == null || name == null || priceStr == null || stockStr == null) {
            session.setAttribute("flashMessage", "Please fill in all required fields.");
            session.setAttribute("flashType", "error");
            res.sendRedirect(req.getContextPath() + "/admin/products");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr.trim());
            int stock = Integer.parseInt(stockStr.trim());

            if (price < 0 || stock < 0) {
                session.setAttribute("flashMessage", "Price and stock cannot be negative.");
                session.setAttribute("flashType", "error");
                res.sendRedirect(req.getContextPath() + "/admin/products/edit?id=" + productId);
                return;
            }

            // POLYMORPHISM: Update using the appropriate subclass with imageUrl
            Product product;
            if ("PERISHABLE".equalsIgnoreCase(type)) {
                product = new PerishableProduct(productId, name.trim(), category.trim(), price, stock, description, extra, imageUrl);
            } else {
                product = new NonPerishableProduct(productId, name.trim(), category.trim(), price, stock, description, extra, imageUrl);
            }

            boolean updated = productDAO.updateProduct(product);
            if (updated) {
                session.setAttribute("flashMessage", "Product updated successfully!");
                session.setAttribute("flashType", "success");
                res.sendRedirect(req.getContextPath() + "/admin/products");
            } else {
                session.setAttribute("flashMessage", "Failed to update product.");
                session.setAttribute("flashType", "error");
                res.sendRedirect(req.getContextPath() + "/admin/products/edit?id=" + productId);
            }

        } catch (NumberFormatException e) {
            session.setAttribute("flashMessage", "Invalid price or stock numeric value.");
            session.setAttribute("flashType", "error");
            res.sendRedirect(req.getContextPath() + "/admin/products/edit?id=" + productId);
        }
    }
}
