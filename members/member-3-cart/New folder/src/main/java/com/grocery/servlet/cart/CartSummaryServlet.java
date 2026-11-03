package com.grocery.servlet.cart;

import com.grocery.dao.CartDAO;
import com.grocery.dao.UserDAO;
import com.grocery.model.CartItem;
import com.grocery.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for reviewing cart totals and customer delivery information
 * before finalizing an order.
 */
@WebServlet("/cart-summary")
public class CartSummaryServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();
    private final UserDAO userDAO = new UserDAO();

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
        if (cartItems.isEmpty()) {
            req.getSession(true).setAttribute("flashMessage", "Your cart is empty. Please add items first.");
            req.getSession().setAttribute("flashType", "info");
            res.sendRedirect(req.getContextPath() + "/catalog");
            return;
        }

        double[] totals = cartDAO.calculateTotals(userId);
        User user = userDAO.getUserById(userId);

        req.setAttribute("cartItems", cartItems);
        req.setAttribute("subtotal", totals[0]);
        req.setAttribute("tax", totals[1]);
        req.setAttribute("total", totals[2]);
        req.setAttribute("user", user);

        req.getRequestDispatcher("/WEB-INF/views/cart/cart-summary.jsp").forward(req, res);
    }
}
