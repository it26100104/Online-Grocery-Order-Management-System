package com.grocery.model;

/**
 * Customer entity representing standard shoppers on the grocery platform.
 */
// INHERITANCE: Inherits core user state and behavior from abstract User class
public class Customer extends User {

    public Customer() {
        super();
    }

    public Customer(String userId, String fullName, String email, String phone,
                    String address, String passwordHash, String status) {
        super(userId, fullName, email, phone, address, passwordHash, status);
    }

    // POLYMORPHISM: Subclass-specific implementation of getRole
    @Override
    public String getRole() {
        return "CUSTOMER";
    }

    // POLYMORPHISM: Subclass-specific destination after successful authentication
    @Override
    public String getDashboardUrl() {
        return "/catalog";
    }
}
