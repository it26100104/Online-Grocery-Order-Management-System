package com.grocery.payment.service;

import com.grocery.payment.model.Order;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FILE HANDLING + CRUD.
 * Data is saved in:  <your user folder>/grocery-data/orders.txt
 * C = createOrder, R = readAll / findById, U = updateDelivery, D = deleteOrder
 */
public class OrderFileService {

    private final File file;

    public OrderFileService() {
        File folder = new File(System.getProperty("user.home"), "grocery-data");
        folder.mkdirs();
        file = new File(folder, "orders.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot create orders.txt", e);
        }
    }

    // ---------- CREATE ----------
    public synchronized void createOrder(Order order) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(order.toFileString());
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Could not save order", e);
        }
    }

    // ---------- READ ----------
    public synchronized List<Order> readAll() {
        List<Order> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    list.add(Order.fromFileString(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read orders", e);
        }
        return list;
    }

    public synchronized Order findById(String id) {
        for (Order o : readAll()) {
            if (o.getOrderId().equalsIgnoreCase(id.trim())) {
                return o;
            }
        }
        return null;
    }

    // ---------- UPDATE ----------
    public synchronized boolean updateDelivery(String id, String name, String phone,
                                               String address, String city) {
        List<Order> all = readAll();
        boolean found = false;
        for (Order o : all) {
            if (o.getOrderId().equalsIgnoreCase(id)) {
                o.setCustomerName(name);
                o.setPhone(phone);
                o.setAddress(address);
                o.setCity(city);
                // city may change, so delivery charge and total must be recalculated
                o.setDeliveryCharge(DeliveryChargeCalculator.calculate(city, o.getSubtotal()));
                o.recalculateTotal();
                found = true;
            }
        }
        if (found) {
            writeAll(all);
        }
        return found;
    }

    // ---------- DELETE ----------
    public synchronized boolean deleteOrder(String id) {
        List<Order> all = readAll();
        boolean removed = all.removeIf(o -> o.getOrderId().equalsIgnoreCase(id));
        if (removed) {
            writeAll(all);
        }
        return removed;
    }

    // ---------- helpers ----------
    public synchronized String generateOrderId() {
        int max = 1000;
        for (Order o : readAll()) {
            try {
                max = Math.max(max, Integer.parseInt(o.getOrderId().substring(3)));
            } catch (NumberFormatException ignored) { }
        }
        return "ORD" + (max + 1);
    }

    private void writeAll(List<Order> orders) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            for (Order o : orders) {
                bw.write(o.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not write orders", e);
        }
    }
}
