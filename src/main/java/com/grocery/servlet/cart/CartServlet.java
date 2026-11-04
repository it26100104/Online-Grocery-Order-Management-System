package com.grocery.servlet.cart;

import com.grocery.dao.CartDAO;
import com.grocery.model.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Shopping Cart management (/cart).
 */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<CartItem> cartItems = cartDAO.getCartByUser(userId);
        double[] totals = cartDAO.calculateTotals(userId);

        req.setAttribute("cartItems", cartItems);
        req.setAttribute("subtotal", totals[0]);
        req.setAttribute("tax", totals[1]);
        req.setAttribute("total", totals[2]);

        req.getRequestDispatcher("/WEB-INF/views/cart/cart.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(true);
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        if ("add".equalsIgnoreCase(action)) {
            String productId = req.getParameter("productId");
            String qtyStr = req.getParameter("quantity");
            int qty = 1;
            try {
                if (qtyStr != null) qty = Integer.parseInt(qtyStr.trim());
            } catch (NumberFormatException ignored) {}

            boolean added = cartDAO.addToCart(userId, productId, qty);
            if (added) {
                session.setAttribute("flashMessage", "Item added to cart.");
                session.setAttribute("flashType", "success");
            } else {
                session.setAttribute("flashMessage", "Could not add item (insufficient stock).");
                session.setAttribute("flashType", "error");
            }
            res.sendRedirect(req.getContextPath() + "/cart");

        } else if ("update".equalsIgnoreCase(action)) {
            String cartItemId = req.getParameter("cartItemId");
            String qtyStr = req.getParameter("quantity");
            try {
                int qty = Integer.parseInt(qtyStr.trim());
                boolean updated = cartDAO.updateQuantity(cartItemId, qty);
                if (updated) {
                    session.setAttribute("flashMessage", "Quantity updated.");
                    session.setAttribute("flashType", "success");
                } else {
                    session.setAttribute("flashMessage", "Requested quantity exceeds available stock.");
                    session.setAttribute("flashType", "error");
                }
            } catch (Exception ignored) {}
            res.sendRedirect(req.getContextPath() + "/cart");

        } else if ("remove".equalsIgnoreCase(action)) {
            String cartItemId = req.getParameter("cartItemId");
            cartDAO.removeFromCart(cartItemId);
            session.setAttribute("flashMessage", "Item removed from cart.");
            session.setAttribute("flashType", "info");
            res.sendRedirect(req.getContextPath() + "/cart");

        } else if ("clear".equalsIgnoreCase(action)) {
            cartDAO.clearCart(userId);
            session.setAttribute("flashMessage", "Your shopping cart is now empty.");
            session.setAttribute("flashType", "info");
            res.sendRedirect(req.getContextPath() + "/cart");

        } else {
            res.sendRedirect(req.getContextPath() + "/cart");
        }
    }
}
