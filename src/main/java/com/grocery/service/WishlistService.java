package com.grocery.service;

import com.grocery.model.WishlistItem;
import com.grocery.util.CartFileHandler;
import com.grocery.util.WishlistFileHandler;

import java.util.List;

public class WishlistService {

    private final WishlistFileHandler wishlistHandler;
    private final CartFileHandler cartHandler;

    public WishlistService(WishlistFileHandler wishlistHandler, CartFileHandler cartHandler) {
        this.wishlistHandler = wishlistHandler;
        this.cartHandler = cartHandler;
    }

    // CREATE (already thiyenawa nam false)
    public boolean addToWishlist(String userId, String productId,
                                 String productName, double price) {
        return wishlistHandler.addItem(new WishlistItem(userId, productId, productName, price));
    }

    // READ
    public List<WishlistItem> getWishlist(String userId) {
        return wishlistHandler.getWishlistByUser(userId);
    }

    // UPDATE (wishlist eken cart ekata move)
    public boolean moveToCart(String userId, String productId) {
        return wishlistHandler.moveToCart(userId, productId, cartHandler);
    }

    // DELETE
    public boolean removeItem(String userId, String productId) {
        return wishlistHandler.removeItem(userId, productId);
    }

    public void clearWishlist(String userId) {
        wishlistHandler.clearWishlist(userId);
    }
}