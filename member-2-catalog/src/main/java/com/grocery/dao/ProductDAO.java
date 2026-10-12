package com.grocery.dao;

import com.grocery.model.NonPerishableProduct;
import com.grocery.model.PerishableProduct;
import com.grocery.model.Product;
import com.grocery.util.ActivityLogger;
import com.grocery.util.FileHandler;
import com.grocery.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Data Access Object for Product inventory management.
 * Interacts strictly with items.txt using FileHandler.
 * Format: productId|name|category|price|stock|description|type|extra
 */
// ENCAPSULATION: Encapsulates all persistence logic and inventory operations for products
public class ProductDAO {

    private static final String FILE_NAME = "items.txt";

    /**
     * Converts a raw text line into a concrete PerishableProduct or NonPerishableProduct.
     */
    // POLYMORPHISM: Factory method dynamically creating the appropriate Product subclass
    private Product parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] tokens = line.split("\\|");
        if (tokens.length < 8) {
            return null;
        }

        String productId = tokens[0].trim();
        String name = tokens[1].trim();
        String category = tokens[2].trim();
        double price;
        int stock;

        try {
            price = Double.parseDouble(tokens[3].trim());
            stock = Integer.parseInt(tokens[4].trim());
        } catch (NumberFormatException e) {
            return null;
        }

        String description = tokens[5].trim();
        String type = tokens[6].trim();
        String extra = tokens[7].trim();
        String imageUrl = (tokens.length >= 9 && !tokens[8].trim().isEmpty())
                ? tokens[8].trim()
                : Product.getDefaultImageFor(name, category);

        if ("PERISHABLE".equalsIgnoreCase(type)) {
            return new PerishableProduct(productId, name, category, price, stock, description, extra, imageUrl);
        } else {
            return new NonPerishableProduct(productId, name, category, price, stock, description, extra, imageUrl);
        }
    }

    /**
     * Retrieves all products from items.txt.
     */
    public synchronized List<Product> getAllProducts() {
        List<String> lines = FileHandler.readAll(FILE_NAME);
        List<Product> products = new ArrayList<>();
        for (String line : lines) {
            Product p = parseLine(line);
            if (p != null) {
                products.add(p);
            }
        }
        return products;
    }

    /**
     * Finds a single product by its unique product ID.
     */
    public synchronized Product getById(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            return null;
        }
        for (Product p : getAllProducts()) {
            if (p.getProductId().equalsIgnoreCase(productId.trim())) {
                return p;
            }
        }
        return null;
    }

    public synchronized Product getProductById(String productId) {
        return getById(productId);
    }

    /**
     * Filters products by keyword search and/or category.
     */
    public synchronized List<Product> searchAndFilter(String query, String category) {
        List<Product> all = getAllProducts();
        List<Product> results = new ArrayList<>();

        String cleanQuery = (query != null) ? query.trim().toLowerCase(Locale.ROOT) : "";
        String cleanCat = (category != null) ? category.trim() : "";

        for (Product p : all) {
            boolean matchesQuery = cleanQuery.isEmpty()
                    || p.getName().toLowerCase(Locale.ROOT).contains(cleanQuery)
                    || p.getDescription().toLowerCase(Locale.ROOT).contains(cleanQuery);

            boolean matchesCategory = cleanCat.isEmpty()
                    || cleanCat.equalsIgnoreCase("ALL")
                    || p.getCategory().equalsIgnoreCase(cleanCat);

            if (matchesQuery && matchesCategory) {
                results.add(p);
            }
        }
        return results;
    }

    /**
     * Extracts distinct categories present across all products.
     */
    public synchronized List<String> getCategories() {
        List<Product> products = getAllProducts();
        List<String> categories = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategory() != null && !p.getCategory().trim().isEmpty() && !categories.contains(p.getCategory().trim())) {
                categories.add(p.getCategory().trim());
            }
        }
        // Include common standard grocery categories if not yet in list
        String[] defaults = {"Dairy", "Fruits", "Vegetables", "Produce", "Grains", "Bakery", "Cooking Oil", "Beverages", "Pantry"};
        for (String def : defaults) {
            if (!categories.contains(def)) {
                categories.add(def);
            }
        }
        return categories;
    }

    /**
     * Adds a new product to items.txt with sequential ID generation (e.g. P001).
     */
    public synchronized boolean addProduct(Product product) {
        if (product == null || product.getName() == null || product.getName().trim().isEmpty()) {
            return false;
        }

        String nextId = IdGenerator.getNextId("P");
        product.setProductId(nextId);

        FileHandler.append(FILE_NAME, product.toDataLine());
        ActivityLogger.log("SYSTEM", "ADD_PRODUCT", "Product added: " + product.getProductId() + " (" + product.getName() + ")");
        return true;
    }

    /**
     * Updates an existing product in items.txt.
     */
    public synchronized boolean updateProduct(Product product) {
        if (product == null || product.getProductId() == null) {
            return false;
        }

        List<Product> products = getAllProducts();
        boolean found = false;
        List<String> lines = new ArrayList<>();

        for (Product p : products) {
            if (p.getProductId().equalsIgnoreCase(product.getProductId())) {
                lines.add(product.toDataLine());
                found = true;
            } else {
                lines.add(p.toDataLine());
            }
        }

        if (found) {
            FileHandler.writeAll(FILE_NAME, lines);
            ActivityLogger.log("SYSTEM", "UPDATE_PRODUCT", "Product updated: " + product.getProductId());
        }
        return found;
    }

    /**
     * Deletes a product by completely removing its row from items.txt.
     */
    public synchronized boolean deleteProduct(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            return false;
        }

        List<Product> products = getAllProducts();
        boolean found = false;
        List<String> lines = new ArrayList<>();

        for (Product p : products) {
            if (p.getProductId().equalsIgnoreCase(productId.trim())) {
                found = true; // Skip this product to remove it from file
            } else {
                lines.add(p.toDataLine());
            }
        }

        if (found) {
            FileHandler.writeAll(FILE_NAME, lines);
            ActivityLogger.log("SYSTEM", "DELETE_PRODUCT", "Product removed: " + productId);
        }
        return found;
    }

    /**
     * Checks if a product has sufficient stock.
     */
    public synchronized boolean isInStock(String productId, int quantity) {
        Product p = getById(productId);
        return p != null && p.getStock() >= quantity && quantity > 0;
    }

    /**
     * Deducts stock when an order is placed.
     */
    public synchronized boolean reduceStock(String productId, int quantity) {
        Product p = getById(productId);
        if (p == null || p.getStock() < quantity || quantity <= 0) {
            return false;
        }

        p.setStock(p.getStock() - quantity);
        boolean success = updateProduct(p);
        if (success) {
            ActivityLogger.log("SYSTEM", "REDUCE_STOCK", "Reduced " + quantity + " units of " + productId);
        }
        return success;
    }

    /**
     * Restores stock when an order is cancelled.
     */
    public synchronized boolean restoreStock(String productId, int quantity) {
        Product p = getById(productId);
        if (p == null || quantity <= 0) {
            return false;
        }

        p.setStock(p.getStock() + quantity);
        boolean success = updateProduct(p);
        if (success) {
            ActivityLogger.log("SYSTEM", "RESTORE_STOCK", "Restored " + quantity + " units of " + productId);
        }
        return success;
    }
}
