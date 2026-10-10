package com.grocery.service;

import com.grocery.model.CartItem;
import com.grocery.util.CartFileHandler;

import java.util.List;

public class CartService {

    private final CartFileHandler cartHandler;
    private final BillCalculator calculator;

    // BillCalculator type eken gannawa, so ona tax calculator ekak dena puluwan (polymorphism)
    public CartService(CartFileHandler cartHandler, BillCalculator calculator) {
        this.cartHandler = cartHandler;
        this.calculator = calculator;
    }

    // CREATE
    public void addToCart(String userId, String productId, String productName,
                          double price, int quantity) {
        cartHandler.addItem(new CartItem(userId, productId, productName, price, quantity));
    }

    // READ
    public List<CartItem> getCart(String userId) {
        return cartHandler.getCartByUser(userId);
    }

    public double getSubtotal(String userId) {
        return calculator.calculateSubtotal(getCart(userId));
    }

    public double getTax(String userId) {
        return calculator.calculateTax(getSubtotal(userId));
    }

    public double getTotal(String userId) {
        return calculator.calculateTotal(getCart(userId));
    }

    // UPDATE
    public void increaseQuantity(String userId, String productId) {
        for (CartItem item : getCart(userId)) {
            if (item.getProductId().equals(productId)) {
                cartHandler.updateQuantity(userId, productId, item.getQuantity() + 1);
                return;
            }
        }
    }

    public void decreaseQuantity(String userId, String productId) {
        for (CartItem item : getCart(userId)) {
            if (item.getProductId().equals(productId)) {
                if (item.getQuantity() <= 1) {
                    cartHandler.removeItem(userId, productId); // 1 nam remove karanawa
                } else {
                    cartHandler.updateQuantity(userId, productId, item.getQuantity() - 1);
                }
                return;
            }
        }
    }

    // DELETE
    public void removeItem(String userId, String productId) {
        cartHandler.removeItem(userId, productId);
    }

    public void clearCart(String userId) {
        cartHandler.clearCart(userId);
    }
}