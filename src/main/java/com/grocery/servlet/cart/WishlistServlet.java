package com.grocery.servlet.cart;

import com.grocery.dao.CartDAO;
import com.grocery.model.WishlistItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Customer Wishlist management (/wishlist).
 */
@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {

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

        List<WishlistItem> wishlist = cartDAO.getWishlistByUser(userId);
        req.setAttribute("wishlist", wishlist);
        req.getRequestDispatcher("/WEB-INF/views/cart/wishlist.jsp").forward(req, res);
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
            boolean added = cartDAO.addToWishlist(userId, productId);
            if (added) {
                session.setAttribute("flashMessage", "Item saved to your wishlist.");
                session.setAttribute("flashType", "success");
            }
            res.sendRedirect(req.getContextPath() + "/wishlist");

        } else if ("remove".equalsIgnoreCase(action)) {
            String wishId = req.getParameter("wishId");
            cartDAO.removeFromWishlist(wishId);
            session.setAttribute("flashMessage", "Item removed from wishlist.");
            session.setAttribute("flashType", "info");
            res.sendRedirect(req.getContextPath() + "/wishlist");

        } else if ("moveToCart".equalsIgnoreCase(action)) {
            String wishId = req.getParameter("wishId");
            boolean moved = cartDAO.moveToCart(wishId, userId);
            if (moved) {
                session.setAttribute("flashMessage", "Item moved to your cart!");
                session.setAttribute("flashType", "success");
                res.sendRedirect(req.getContextPath() + "/cart");
            } else {
                session.setAttribute("flashMessage", "Could not move item (out of stock).");
                session.setAttribute("flashType", "error");
                res.sendRedirect(req.getContextPath() + "/wishlist");
            }

        } else {
            res.sendRedirect(req.getContextPath() + "/wishlist");
        }
    }
}
