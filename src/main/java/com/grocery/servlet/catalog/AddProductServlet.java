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
 * Controller for adding new products to the grocery inventory.
 */
@WebServlet("/admin/products/add")
public class AddProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setAttribute("categories", productDAO.getCategories());
        req.getRequestDispatcher("/WEB-INF/views/catalog/add-product.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(true);

        String name = req.getParameter("name");
        
        // Category selection or custom input
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

        if (name == null || name.trim().isEmpty()
                || category == null || category.trim().isEmpty()
                || priceStr == null || stockStr == null
                || type == null || extra == null || extra.trim().isEmpty()) {
            session.setAttribute("flashMessage", "Please fill in all required fields.");
            session.setAttribute("flashType", "error");
            res.sendRedirect(req.getContextPath() + "/admin/products/add");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr.trim());
            int stock = Integer.parseInt(stockStr.trim());

            if (price < 0 || stock < 0) {
                session.setAttribute("flashMessage", "Price and stock cannot be negative numbers.");
                session.setAttribute("flashType", "error");
                res.sendRedirect(req.getContextPath() + "/admin/products/add");
                return;
            }

            // POLYMORPHISM: Instantiate appropriate Product subclass based on type selection with imageUrl
            Product product;
            if ("PERISHABLE".equalsIgnoreCase(type.trim())) {
                product = new PerishableProduct(null, name.trim(), category.trim(), price, stock, description, extra.trim(), imageUrl);
            } else {
                product = new NonPerishableProduct(null, name.trim(), category.trim(), price, stock, description, extra.trim(), imageUrl);
            }

            boolean added = productDAO.addProduct(product);
            if (added) {
                session.setAttribute("flashMessage", "Product added successfully!");
                session.setAttribute("flashType", "success");
                res.sendRedirect(req.getContextPath() + "/admin/products");
            } else {
                session.setAttribute("flashMessage", "Failed to add product.");
                session.setAttribute("flashType", "error");
                res.sendRedirect(req.getContextPath() + "/admin/products/add");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("flashMessage", "Invalid price or stock numeric format.");
            session.setAttribute("flashType", "error");
            res.sendRedirect(req.getContextPath() + "/admin/products/add");
        }
    }
}
