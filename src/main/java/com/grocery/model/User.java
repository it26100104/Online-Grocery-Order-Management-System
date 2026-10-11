package com.grocery.model;

/**
 * Abstract base class representing a generic user in the system.
 * Serves as the blueprint for Customer and AdminUser.
 */
// ABSTRACTION: Defines common attributes and contract methods for all user types
public abstract class User {

    // ENCAPSULATION: Private fields accessible only via validated getters and setters
    private String userId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String passwordHash;
    private String status; // ACTIVE or INACTIVE

    public User() {
    }

    public User(String userId, String fullName, String email, String phone,
                String address, String passwordHash, String status) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.passwordHash = passwordHash;
        this.status = (status != null && !status.trim().isEmpty()) ? status.trim().toUpperCase() : "ACTIVE";
    }

    // POLYMORPHISM: Abstract methods overridden by subclasses to define role-specific behavior
    public abstract String getRole();

    public abstract String getDashboardUrl();

    // Getters and Setters with Encapsulation
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName != null && !fullName.trim().isEmpty()) {
            this.fullName = fullName.trim();
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            this.email = email.trim().toLowerCase();
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone != null) {
            this.phone = phone.trim();
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address != null) {
            this.address = address.trim();
        }
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status != null && (status.equalsIgnoreCase("ACTIVE") || status.equalsIgnoreCase("INACTIVE"))) {
            this.status = status.toUpperCase();
        }
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }

    /**
     * Serializes this user into the pipe-delimited storage format:
     * userId|role|fullName|email|phone|address|passwordHash|status
     */
    public String toDataLine() {
        return String.format("%s|%s|%s|%s|%s|%s|%s|%s",
                userId,
                getRole(),
                fullName,
                email,
                phone,
                address,
                passwordHash,
                status);
    }
}
