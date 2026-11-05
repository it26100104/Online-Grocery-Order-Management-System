package com.grocery.servlet.catalog;

import com.grocery.dao.ProductDAO;
import com.grocery.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Customer Grocery Catalog browsing.
 * Supports keyword search, category filtering, and inventory status display.
 */
@WebServlet("/catalog")
public class CatalogServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String query = req.getParameter("search");
        String category = req.getParameter("category");

        List<Product> products = productDAO.searchAndFilter(query, category);
        List<String> categories = productDAO.getCategories();

        req.setAttribute("products", products);
        req.setAttribute("categories", categories);
        req.setAttribute("searchQuery", (query != null) ? query.trim() : "");
        req.setAttribute("selectedCategory", (category != null) ? category.trim() : "ALL");

        req.getRequestDispatcher("/WEB-INF/views/catalog/catalog.jsp").forward(req, res);
    }
}
