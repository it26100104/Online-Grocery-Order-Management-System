package com.grocery.dao;

import com.grocery.model.BulkOrder;
import com.grocery.model.CartItem;
import com.grocery.model.Order;
import com.grocery.model.OrderItem;
import com.grocery.model.RegularOrder;
import com.grocery.util.ActivityLogger;
import com.grocery.util.FileHandler;
import com.grocery.util.IdGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Orders.
 * Interacts strictly with orders.txt using FileHandler.
 * Format: orderId|userId|orderType|items|subtotal|tax|total|address|status|date
 */
// ENCAPSULATION: Encapsulates all order lifecycle operations and state transitions
public class OrderDAO {

    private static final String FILE_NAME = "orders.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ProductDAO productDAO = new ProductDAO();
    private final CartDAO cartDAO = new CartDAO();

    private Order parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] tokens = line.split("\\|");
        if (tokens.length < 10) {
            return null;
        }

        try {
            String orderId = tokens[0].trim();
            String userId = tokens[1].trim();
            String orderType = tokens[2].trim();
            String itemsRaw = tokens[3].trim();
            double subtotal = Double.parseDouble(tokens[4].trim());
            double tax = Double.parseDouble(tokens[5].trim());
            double total = Double.parseDouble(tokens[6].trim());
            String address = tokens[7].trim();
            String status = tokens[8].trim();
            String date = tokens[9].trim();

            List<OrderItem> items = new ArrayList<>();
            String[] itemTokens = itemsRaw.split(";");
            for (String it : itemTokens) {
                OrderItem oi = OrderItem.parse(it);
                if (oi != null) {
                    items.add(oi);
                }
            }

            // POLYMORPHISM: Dynamic instantiation based on order type token
            if ("BULK".equalsIgnoreCase(orderType)) {
                return new BulkOrder(orderId, userId, items, subtotal, tax, total, address, status, date);
            } else {
                return new RegularOrder(orderId, userId, items, subtotal, tax, total, address, status, date);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public synchronized List<Order> getAllOrders() {
        List<String> lines = FileHandler.readAll(FILE_NAME);
        List<Order> list = new ArrayList<>();
        for (String line : lines) {
            Order o = parseLine(line);
            if (o != null) {
                list.add(o);
            }
        }
        return list;
    }

    public synchronized Order getOrderById(String orderId) {
        if (orderId == null) return null;
        for (Order o : getAllOrders()) {
            if (o.getOrderId().equalsIgnoreCase(orderId.trim())) {
                return o;
            }
        }
        return null;
    }

    public synchronized List<Order> getOrdersByUser(String userId) {
        List<Order> list = new ArrayList<>();
        if (userId == null) return list;
        for (Order o : getAllOrders()) {
            if (o.getUserId().equalsIgnoreCase(userId.trim())) {
                list.add(o);
            }
        }
        return list;
    }

    /**
     * Places a new order from items currently in the user's cart:
     * 1. Reads cart items
     * 2. Checks and reduces warehouse stock
     * 3. Calculates totals polymorphically
     * 4. Clears cart
     * 5. Saves PENDING order to orders.txt
     */
    public synchronized Order placeOrder(String userId, String address, String orderType) {
        return placeOrder(userId, address, orderType, null);
    }

    /**
     * Places a new order from items currently in the user's cart with optional coupon discount:
     * 1. Reads cart items
     * 2. Checks and reduces warehouse stock
     * 3. Calculates totals polymorphically
     * 4. Applies promo coupon discount (e.g. FRESH10, AURA20, SAVE15)
     * 5. Clears cart
     * 6. Saves PENDING order to orders.txt
     */
    public synchronized Order placeOrder(String userId, String address, String orderType, String couponCode) {
        List<CartItem> cartItems = cartDAO.getCartByUser(userId);
        if (cartItems.isEmpty()) {
            return null;
        }

        // Verify all items are in stock
        for (CartItem ci : cartItems) {
            if (!productDAO.isInStock(ci.getProductId(), ci.getQuantity())) {
                return null;
            }
        }

        // Deduct inventory
        List<OrderItem> orderItems = new ArrayList<>();
        double subtotal = 0.0;
        for (CartItem ci : cartItems) {
            productDAO.reduceStock(ci.getProductId(), ci.getQuantity());
            OrderItem oi = new OrderItem(ci.getProductId(), ci.getProductName(), ci.getUnitPrice(), ci.getQuantity());
            orderItems.add(oi);
            subtotal += oi.getLineTotal();
        }

        subtotal = Math.round(subtotal * 100.0) / 100.0;
        double tax = Math.round(subtotal * CartDAO.TAX_RATE * 100.0) / 100.0;
        String newOrderId = IdGenerator.getNextId("O");
        String now = LocalDateTime.now().format(DATE_FORMATTER);

        // Calculate coupon discount if provided
        double couponDiscount = 0.0;
        if (couponCode != null && !couponCode.trim().isEmpty()) {
            String code = couponCode.trim().toUpperCase();
            if ("FRESH10".equals(code)) {
                couponDiscount = Math.round(subtotal * 0.10 * 100.0) / 100.0;
            } else if ("AURA20".equals(code)) {
                couponDiscount = Math.round(subtotal * 0.20 * 100.0) / 100.0;
            } else if ("SAVE15".equals(code)) {
                couponDiscount = Math.round(subtotal * 0.15 * 100.0) / 100.0;
            } else if ("WELCOME100".equals(code) || "FIRSTBUY".equals(code)) {
                couponDiscount = Math.min(100.0, subtotal);
            }
        }

        // POLYMORPHISM: Instantiate appropriate subclass
        Order order;
        if ("BULK".equalsIgnoreCase(orderType)) {
            order = new BulkOrder(newOrderId, userId, orderItems, subtotal, tax, 0.0, address, "PENDING", now);
        } else {
            order = new RegularOrder(newOrderId, userId, orderItems, subtotal, tax, 0.0, address, "PENDING", now);
        }

        // POLYMORPHISM: Invoke calculateTotal and apply coupon discount
        double calculatedTotal = order.calculateTotal();
        double finalTotal = Math.max(0.0, Math.round((calculatedTotal - couponDiscount) * 100.0) / 100.0);
        order.setTotal(finalTotal);

        // Save order and clear cart
        FileHandler.append(FILE_NAME, order.toDataLine());
        cartDAO.clearCart(userId);
        
        String couponLog = (couponDiscount > 0) ? " (Coupon " + couponCode + " applied: -Rs. " + couponDiscount + ")" : "";
        ActivityLogger.log(userId, "PLACE_ORDER", "Order placed: " + newOrderId + " (" + order.getOrderType() + ", Total: Rs. " + order.getTotal() + ")" + couponLog);

        return order;
    }

    /**
     * Admin moves order through valid progression stages:
     * PENDING -> PROCESSING -> SHIPPED -> DELIVERED
     */
    public synchronized boolean updateStatus(String orderId, String newStatus) {
        if (orderId == null || newStatus == null) return false;

        List<Order> orders = getAllOrders();
        boolean found = false;
        List<String> lines = new ArrayList<>();

        for (Order o : orders) {
            if (o.getOrderId().equalsIgnoreCase(orderId.trim())) {
                // Validation of valid progression
                o.setStatus(newStatus.trim().toUpperCase());
                found = true;
            }
            lines.add(o.toDataLine());
        }

        if (found) {
            FileHandler.writeAll(FILE_NAME, lines);
            ActivityLogger.log("ADMIN", "UPDATE_ORDER_STATUS", "Order " + orderId + " changed to " + newStatus);
        }
        return found;
    }

    /**
     * Cancels an order ONLY if it is still PENDING.
     * Restores inventory back to items.txt.
     */
    public synchronized boolean cancelOrder(String orderId, String userId) {
        Order order = getOrderById(orderId);
        if (order == null) return false;

        // Security check: Only owner or admin can cancel
        if (userId != null && !userId.equalsIgnoreCase(order.getUserId()) && !userId.equalsIgnoreCase("ADMIN")) {
            return false;
        }

        // Rule: Only PENDING orders can be cancelled
        if (!order.isPending()) {
            return false;
        }

        // 1. Restore product stock
        for (OrderItem item : order.getItems()) {
            productDAO.restoreStock(item.getProductId(), item.getQuantity());
        }

        // 2. Set status to CANCELLED (Delete rule: orders become CANCELLED)
        boolean updated = updateStatus(orderId, "CANCELLED");
        if (updated) {
            ActivityLogger.log(userId, "CANCEL_ORDER", "Order " + orderId + " cancelled and stock restored");
        }
        return updated;
    }
}
