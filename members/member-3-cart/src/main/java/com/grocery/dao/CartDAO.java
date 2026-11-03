package com.grocery.dao;

import com.grocery.model.CartItem;
import com.grocery.model.Product;
import com.grocery.model.WishlistItem;
import com.grocery.util.ActivityLogger;
import com.grocery.util.FileHandler;
import com.grocery.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Shopping Cart and Wishlist management.
 * Interacts with cart.txt and wishlist.txt using FileHandler.
 * Checks stock through ProductDAO before adding items.
 */
// ENCAPSULATION: Centralizes persistence for cart and wishlist operations
public class CartDAO {

    public static final double TAX_RATE = 0.08; // 8% sales tax constant
    private static final String CART_FILE = "cart.txt";
    private static final String WISHLIST_FILE = "wishlist.txt";

    private final ProductDAO productDAO = new ProductDAO();

    // ==========================================
    // CART OPERATIONS
    // ==========================================

    private CartItem parseCartLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] tokens = line.split("\\|");
        if (tokens.length < 6) {
            return null;
        }
        try {
            String cartItemId = tokens[0].trim();
            String userId = tokens[1].trim();
            String productId = tokens[2].trim();
            String productName = tokens[3].trim();
            double unitPrice = Double.parseDouble(tokens[4].trim());
            int quantity = Integer.parseInt(tokens[5].trim());
            return new CartItem(cartItemId, userId, productId, productName, unitPrice, quantity);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public synchronized List<CartItem> getAllCartItems() {
        List<String> lines = FileHandler.readAll(CART_FILE);
        List<CartItem> list = new ArrayList<>();
        for (String line : lines) {
            CartItem item = parseCartLine(line);
            if (item != null) {
                list.add(item);
            }
        }
        return list;
    }

    public synchronized List<CartItem> getCartByUser(String userId) {
        List<CartItem> result = new ArrayList<>();
        if (userId == null) return result;
        for (CartItem item : getAllCartItems()) {
            if (item.getUserId().equalsIgnoreCase(userId.trim())) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Adds an item to the shopping cart after validating warehouse stock.
     */
    public synchronized boolean addToCart(String userId, String productId, int quantity) {
        if (userId == null || productId == null || quantity <= 0) {
            return false;
        }

        Product product = productDAO.getById(productId);
        if (product == null) {
            return false;
        }

        List<CartItem> allItems = getAllCartItems();
        boolean found = false;
        List<String> updatedLines = new ArrayList<>();

        for (CartItem item : allItems) {
            if (item.getUserId().equalsIgnoreCase(userId) && item.getProductId().equalsIgnoreCase(productId)) {
                int newQty = item.getQuantity() + quantity;
                // Verify available inventory
                if (product.getStock() < newQty) {
                    return false;
                }
                item.setQuantity(newQty);
                found = true;
            }
            updatedLines.add(item.toDataLine());
        }

        if (found) {
            FileHandler.writeAll(CART_FILE, updatedLines);
            ActivityLogger.log(userId, "UPDATE_CART", "Incremented quantity for " + product.getName());
            return true;
        }

        // Check stock for new cart item
        if (product.getStock() < quantity) {
            return false;
        }

        String cartItemId = IdGenerator.getNextId("C");
        CartItem newItem = new CartItem(cartItemId, userId, productId, product.getName(), product.getPrice(), quantity);
        FileHandler.append(CART_FILE, newItem.toDataLine());
        ActivityLogger.log(userId, "ADD_TO_CART", "Added " + quantity + " units of " + product.getName());
        return true;
    }

    public synchronized boolean updateQuantity(String cartItemId, int quantity) {
        if (cartItemId == null || quantity <= 0) {
            return false;
        }

        List<CartItem> allItems = getAllCartItems();
        boolean found = false;
        List<String> updatedLines = new ArrayList<>();

        for (CartItem item : allItems) {
            if (item.getItemId().equalsIgnoreCase(cartItemId)) {
                Product p = productDAO.getById(item.getProductId());
                if (p != null && p.getStock() < quantity) {
                    return false; // Not enough stock
                }
                item.setQuantity(quantity);
                found = true;
            }
            updatedLines.add(item.toDataLine());
        }

        if (found) {
            FileHandler.writeAll(CART_FILE, updatedLines);
        }
        return found;
    }

    public synchronized boolean removeFromCart(String cartItemId) {
        if (cartItemId == null) return false;
        List<CartItem> allItems = getAllCartItems();
        boolean found = false;
        List<String> remainingLines = new ArrayList<>();

        for (CartItem item : allItems) {
            if (item.getItemId().equalsIgnoreCase(cartItemId.trim())) {
                found = true;
            } else {
                remainingLines.add(item.toDataLine());
            }
        }

        if (found) {
            FileHandler.writeAll(CART_FILE, remainingLines);
        }
        return found;
    }

    public synchronized void clearCart(String userId) {
        if (userId == null) return;
        List<CartItem> allItems = getAllCartItems();
        List<String> remainingLines = new ArrayList<>();

        for (CartItem item : allItems) {
            if (!item.getUserId().equalsIgnoreCase(userId.trim())) {
                remainingLines.add(item.toDataLine());
            }
        }
        FileHandler.writeAll(CART_FILE, remainingLines);
        ActivityLogger.log(userId, "CLEAR_CART", "Cleared shopping cart");
    }

    /**
     * Calculates subtotal, tax (8%), and grand total for a user's cart.
     * Returns double[] {subtotal, tax, total}
     */
    public synchronized double[] calculateTotals(String userId) {
        List<CartItem> userItems = getCartByUser(userId);
        double subtotal = 0.0;
        for (CartItem item : userItems) {
            // POLYMORPHISM: Invokes getLineTotal polymorphically
            subtotal += item.getLineTotal();
        }
        subtotal = Math.round(subtotal * 100.0) / 100.0;
        double tax = Math.round(subtotal * TAX_RATE * 100.0) / 100.0;
        double total = Math.round((subtotal + tax) * 100.0) / 100.0;
        return new double[]{subtotal, tax, total};
    }

    // ==========================================
    // WISHLIST OPERATIONS
    // ==========================================

    private WishlistItem parseWishlistLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] tokens = line.split("\\|");
        if (tokens.length < 5) {
            return null;
        }
        try {
            String wishId = tokens[0].trim();
            String userId = tokens[1].trim();
            String productId = tokens[2].trim();
            String productName = tokens[3].trim();
            double unitPrice = Double.parseDouble(tokens[4].trim());
            return new WishlistItem(wishId, userId, productId, productName, unitPrice);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public synchronized List<WishlistItem> getAllWishlistItems() {
        List<String> lines = FileHandler.readAll(WISHLIST_FILE);
        List<WishlistItem> list = new ArrayList<>();
        for (String line : lines) {
            WishlistItem item = parseWishlistLine(line);
            if (item != null) {
                list.add(item);
            }
        }
        return list;
    }

    public synchronized List<WishlistItem> getWishlistByUser(String userId) {
        List<WishlistItem> result = new ArrayList<>();
        if (userId == null) return result;
        for (WishlistItem item : getAllWishlistItems()) {
            if (item.getUserId().equalsIgnoreCase(userId.trim())) {
                result.add(item);
            }
        }
        return result;
    }

    public synchronized boolean addToWishlist(String userId, String productId) {
        if (userId == null || productId == null) return false;

        // Check if already in wishlist
        for (WishlistItem w : getWishlistByUser(userId)) {
            if (w.getProductId().equalsIgnoreCase(productId)) {
                return true; // already saved
            }
        }

        Product p = productDAO.getById(productId);
        if (p == null) return false;

        String wishId = IdGenerator.getNextId("W");
        WishlistItem item = new WishlistItem(wishId, userId, productId, p.getName(), p.getPrice());
        FileHandler.append(WISHLIST_FILE, item.toDataLine());
        ActivityLogger.log(userId, "ADD_TO_WISHLIST", "Saved " + p.getName() + " to wishlist");
        return true;
    }

    public synchronized boolean removeFromWishlist(String wishId) {
        if (wishId == null) return false;
        List<WishlistItem> all = getAllWishlistItems();
        boolean found = false;
        List<String> remaining = new ArrayList<>();

        for (WishlistItem w : all) {
            if (w.getItemId().equalsIgnoreCase(wishId.trim())) {
                found = true;
            } else {
                remaining.add(w.toDataLine());
            }
        }

        if (found) {
            FileHandler.writeAll(WISHLIST_FILE, remaining);
        }
        return found;
    }

    /**
     * Moves an item from wishlist to cart.
     */
    public synchronized boolean moveToCart(String wishId, String userId) {
        WishlistItem target = null;
        for (WishlistItem w : getWishlistByUser(userId)) {
            if (w.getItemId().equalsIgnoreCase(wishId)) {
                target = w;
                break;
            }
        }
        if (target == null) return false;

        boolean added = addToCart(userId, target.getProductId(), 1);
        if (added) {
            removeFromWishlist(wishId);
            ActivityLogger.log(userId, "MOVE_WISHLIST_TO_CART", "Moved " + target.getProductName() + " to cart");
            return true;
        }
        return false;
    }
}
