package com.grocery.model;

/**
 * AdminUser entity representing store administrators with management privileges.
 */
// INHERITANCE: Inherits core user state and behavior from abstract User class
public class AdminUser extends User {

    public AdminUser() {
        super();
    }

    public AdminUser(String userId, String fullName, String email, String phone,
                     String address, String passwordHash, String status) {
        super(userId, fullName, email, phone, address, passwordHash, status);
    }

    // POLYMORPHISM: Subclass-specific implementation of getRole
    @Override
    public String getRole() {
        return "ADMIN";
    }

    // POLYMORPHISM: Subclass-specific destination after successful authentication
    @Override
    public String getDashboardUrl() {
        return "/admin/products";
    }
}
