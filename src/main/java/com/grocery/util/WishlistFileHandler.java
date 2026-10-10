package com.grocery.util;

import com.grocery.model.CartItem;
import com.grocery.model.WishlistItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistFileHandler {

    private final String filePath;

    public WishlistFileHandler(String filePath) {
        this.filePath = filePath;
    }

    private List<WishlistItem> readAll() {
        List<WishlistItem> items = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return items;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\\|");
                if (p.length == 4) {
                    items.add(new WishlistItem(p[0], p[1], p[2], Double.parseDouble(p[3])));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return items;
    }

    private void writeAll(List<WishlistItem> items) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (WishlistItem item : items) {
                pw.println(item.toFileString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // CREATE
    public boolean addItem(WishlistItem newItem) {
        List<WishlistItem> items = readAll();
        for (WishlistItem item : items) {
            if (item.getUserId().equals(newItem.getUserId())
                    && item.getProductId().equals(newItem.getProductId())) {
                return false; // already wishlist eke thiyenawa
            }
        }
        items.add(newItem);
        writeAll(items);
        return true;
    }

    // READ
    public List<WishlistItem> getWishlistByUser(String userId) {
        List<WishlistItem> result = new ArrayList<>();
        for (WishlistItem item : readAll()) {
            if (item.getUserId().equals(userId)) {
                result.add(item);
            }
        }
        return result;
    }

    // UPDATE (wishlist item ekak cart ekata move karanna)
    public boolean moveToCart(String userId, String productId, CartFileHandler cartHandler) {
        List<WishlistItem> items = readAll();
        for (WishlistItem item : items) {
            if (item.getUserId().equals(userId) && item.getProductId().equals(productId)) {
                cartHandler.addItem(new CartItem(userId, productId,
                        item.getProductName(), item.getPrice(), 1));
                items.remove(item);
                writeAll(items);
                return true;
            }
        }
        return false;
    }

    // DELETE (ekak)
    public boolean removeItem(String userId, String productId) {
        List<WishlistItem> items = readAll();
        boolean removed = items.removeIf(i ->
                i.getUserId().equals(userId) && i.getProductId().equals(productId));
        if (removed) {
            writeAll(items);
        }
        return removed;
    }

    // DELETE (okkoma clear)
    public void clearWishlist(String userId) {
        List<WishlistItem> items = readAll();
        items.removeIf(i -> i.getUserId().equals(userId));
        writeAll(items);
    }
}