package com.grocery.util;

import com.grocery.model.CartItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CartFileHandler {

    private final String filePath;

    public CartFileHandler(String filePath) {
        this.filePath = filePath;
    }

    // ---------- helper: file eka okkoma read karanna ----------
    private List<CartItem> readAll() {
        List<CartItem> items = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return items;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\\|");
                if (p.length == 5) {
                    items.add(new CartItem(p[0], p[1], p[2],
                            Double.parseDouble(p[3]), Integer.parseInt(p[4])));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return items;
    }

    // ---------- helper: list eka file ekata liyanna ----------
    private void writeAll(List<CartItem> items) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (CartItem item : items) {
                pw.println(item.toFileString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------- CREATE ----------
    public void addItem(CartItem newItem) {
        List<CartItem> items = readAll();
        for (CartItem item : items) {
            if (item.getUserId().equals(newItem.getUserId())
                    && item.getProductId().equals(newItem.getProductId())) {
                // already cart eke thiyenawa nam quantity eka wedi karanna
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                writeAll(items);
                return;
            }
        }
        items.add(newItem);
        writeAll(items);
    }

    // ---------- READ ----------
    public List<CartItem> getCartByUser(String userId) {
        List<CartItem> result = new ArrayList<>();
        for (CartItem item : readAll()) {
            if (item.getUserId().equals(userId)) {
                result.add(item);
            }
        }
        return result;
    }

    // ---------- UPDATE ----------
    public boolean updateQuantity(String userId, String productId, int newQuantity) {
        List<CartItem> items = readAll();
        for (CartItem item : items) {
            if (item.getUserId().equals(userId) && item.getProductId().equals(productId)) {
                item.setQuantity(newQuantity);
                writeAll(items);
                return true;
            }
        }
        return false;
    }

    // ---------- DELETE (ekak) ----------
    public boolean removeItem(String userId, String productId) {
        List<CartItem> items = readAll();
        boolean removed = items.removeIf(i ->
                i.getUserId().equals(userId) && i.getProductId().equals(productId));
        if (removed) {
            writeAll(items);
        }
        return removed;
    }

    // ---------- DELETE (cart eka okkoma clear) ----------
    public void clearCart(String userId) {
        List<CartItem> items = readAll();
        items.removeIf(i -> i.getUserId().equals(userId));
        writeAll(items);
    }
}