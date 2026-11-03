package com.grocery.dao;

import com.grocery.model.Delivery;
import com.grocery.model.DeliveryReport;
import com.grocery.model.ExpressDelivery;
import com.grocery.model.Order;
import com.grocery.model.OrderItem;
import com.grocery.model.Payment;
import com.grocery.model.SalesReport;
import com.grocery.model.StandardDelivery;
import com.grocery.util.ActivityLogger;
import com.grocery.util.FileHandler;
import com.grocery.util.IdGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Access Object for Delivery dispatch and Sales Analytics.
 * Interacts with deliveries.txt using FileHandler.
 * Format: deliveryId|orderId|userId|type|address|agent|status|date|fee
 * Inter-module: when DELIVERED, calls OrderDAO.updateStatus(orderId, DELIVERED)
 */
// ENCAPSULATION: Centralizes logistics dispatch state and analytics aggregation
public class DeliveryDAO {

    private static final String FILE_NAME = "deliveries.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final OrderDAO orderDAO = new OrderDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    private Delivery parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] tokens = line.split("\\|");
        if (tokens.length < 9) {
            return null;
        }

        try {
            String deliveryId = tokens[0].trim();
            String orderId = tokens[1].trim();
            String userId = tokens[2].trim();
            String type = tokens[3].trim();
            String address = tokens[4].trim();
            String agent = tokens[5].trim();
            String status = tokens[6].trim();
            String date = tokens[7].trim();
            double fee = Double.parseDouble(tokens[8].trim());

            // POLYMORPHISM: Factory creation of concrete Delivery subclass
            if ("EXPRESS".equalsIgnoreCase(type)) {
                return new ExpressDelivery(deliveryId, orderId, userId, address, agent, status, date, fee);
            } else {
                return new StandardDelivery(deliveryId, orderId, userId, address, agent, status, date, fee);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public synchronized List<Delivery> getAllDeliveries() {
        List<String> lines = FileHandler.readAll(FILE_NAME);
        List<Delivery> list = new ArrayList<>();
        for (String line : lines) {
            Delivery d = parseLine(line);
            if (d != null) {
                list.add(d);
            }
        }
        return list;
    }

    public synchronized Delivery getDeliveryById(String deliveryId) {
        if (deliveryId == null) return null;
        for (Delivery d : getAllDeliveries()) {
            if (d.getDeliveryId().equalsIgnoreCase(deliveryId.trim())) {
                return d;
            }
        }
        return null;
    }

    public synchronized Delivery getDeliveryByOrderId(String orderId) {
        if (orderId == null) return null;
        for (Delivery d : getAllDeliveries()) {
            if (d.getOrderId().equalsIgnoreCase(orderId.trim())) {
                return d;
            }
        }
        return null;
    }

    public synchronized List<Delivery> getDeliveriesByUser(String userId) {
        List<Delivery> list = new ArrayList<>();
        if (userId == null) return list;
        for (Delivery d : getAllDeliveries()) {
            if (d.getUserId().equalsIgnoreCase(userId.trim())) {
                list.add(d);
            }
        }
        return list;
    }

    /**
     * Creates a new delivery record for an order.
     */
    public synchronized boolean createForOrder(String orderId, String userId, String type, String address) {
        if (orderId == null || userId == null || address == null) return false;

        // Check if delivery already exists for this order
        if (getDeliveryByOrderId(orderId) != null) {
            return true;
        }

        String deliveryId = IdGenerator.getNextId("D");
        String now = LocalDateTime.now().format(FORMATTER);

        // POLYMORPHISM: Instantiate subclass and calculate fee
        Delivery delivery;
        if ("EXPRESS".equalsIgnoreCase(type)) {
            delivery = new ExpressDelivery(deliveryId, orderId, userId, address, "Unassigned", "SCHEDULED", now, 0.0);
        } else {
            delivery = new StandardDelivery(deliveryId, orderId, userId, address, "Unassigned", "SCHEDULED", now, 0.0);
        }

        delivery.setFee(delivery.calculateFee());
        FileHandler.append(FILE_NAME, delivery.toDataLine());
        ActivityLogger.log(userId, "DISPATCH_SCHEDULED", "Delivery " + deliveryId + " scheduled for Order #" + orderId);
        return true;
    }

    /**
     * Updates delivery status.
     * Inter-module requirement: When status is DELIVERED, calls OrderDAO.updateStatus(orderId, DELIVERED).
     */
    public synchronized boolean updateStatus(String deliveryId, String newStatus) {
        if (deliveryId == null || newStatus == null) return false;

        List<Delivery> deliveries = getAllDeliveries();
        boolean found = false;
        String associatedOrderId = null;
        List<String> updatedLines = new ArrayList<>();

        for (Delivery d : deliveries) {
            if (d.getDeliveryId().equalsIgnoreCase(deliveryId.trim())) {
                d.setStatus(newStatus.trim().toUpperCase());
                associatedOrderId = d.getOrderId();
                found = true;
            }
            updatedLines.add(d.toDataLine());
        }

        if (found) {
            FileHandler.writeAll(FILE_NAME, updatedLines);
            ActivityLogger.log("SYSTEM", "DELIVERY_STATUS_UPDATE", "Delivery " + deliveryId + " updated to " + newStatus);

            // Cross-module update: Notify OrderDAO
            if ("DELIVERED".equalsIgnoreCase(newStatus) && associatedOrderId != null) {
                orderDAO.updateStatus(associatedOrderId, "DELIVERED");
            }
        }
        return found;
    }

    public synchronized boolean assignAgent(String deliveryId, String agent) {
        if (deliveryId == null || agent == null) return false;

        List<Delivery> deliveries = getAllDeliveries();
        boolean found = false;
        List<String> updatedLines = new ArrayList<>();

        for (Delivery d : deliveries) {
            if (d.getDeliveryId().equalsIgnoreCase(deliveryId.trim())) {
                d.setAgent(agent.trim());
                if ("SCHEDULED".equalsIgnoreCase(d.getStatus())) {
                    d.setStatus("DISPATCHED");
                }
                found = true;
            }
            updatedLines.add(d.toDataLine());
        }

        if (found) {
            FileHandler.writeAll(FILE_NAME, updatedLines);
            ActivityLogger.log("ADMIN", "ASSIGN_AGENT", "Assigned agent " + agent + " to Delivery " + deliveryId);
        }
        return found;
    }

    /**
     * Cancels delivery when an order is cancelled.
     */
    public synchronized boolean cancelByOrder(String orderId) {
        Delivery d = getDeliveryByOrderId(orderId);
        if (d == null) return false;
        return updateStatus(d.getDeliveryId(), "CANCELLED");
    }

    // ==========================================
    // ANALYTICS & REPORTING
    // ==========================================

    /**
     * Aggregates sales figures using PaymentDAO (PAID records only) and OrderDAO.
     */
    public synchronized SalesReport generateSalesReport() {
        List<Payment> paidRecords = paymentDAO.getPaidRecords();
        double totalRevenue = 0.0;
        for (Payment p : paidRecords) {
            totalRevenue += p.getAmount();
        }

        List<Order> allOrders = orderDAO.getAllOrders();
        Map<String, Integer> itemCounts = new HashMap<>();

        for (Order o : allOrders) {
            if (!"CANCELLED".equalsIgnoreCase(o.getStatus())) {
                for (OrderItem oi : o.getItems()) {
                    itemCounts.put(oi.getName(), itemCounts.getOrDefault(oi.getName(), 0) + oi.getQuantity());
                }
            }
        }

        // Sort by sales descending
        Map<String, Integer> topItems = itemCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return new SalesReport(totalRevenue, paidRecords.size(), topItems);
    }

    public synchronized DeliveryReport generateDeliveryReport() {
        List<Delivery> all = getAllDeliveries();
        int total = all.size();
        int delivered = 0;
        int inTransit = 0;
        int cancelled = 0;

        for (Delivery d : all) {
            String s = d.getStatus().toUpperCase();
            if ("DELIVERED".equals(s)) delivered++;
            else if ("CANCELLED".equals(s)) cancelled++;
            else inTransit++;
        }

        return new DeliveryReport(total, delivered, inTransit, cancelled);
    }
}
