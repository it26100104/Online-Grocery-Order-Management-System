package com.grocery.dao;

import com.grocery.model.AdminUser;
import com.grocery.model.Customer;
import com.grocery.model.User;
import com.grocery.util.ActivityLogger;
import com.grocery.util.FileHandler;
import com.grocery.util.IdGenerator;
import com.grocery.util.PasswordUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User entities.
 * Interacts strictly with users.txt using FileHandler.
 * Format: userId|role|fullName|email|phone|address|passwordHash|status
 */
// ENCAPSULATION: Encapsulates all file persistence and query logic for users
public class UserDAO {

    private static final String FILE_NAME = "users.txt";

    /**
     * Converts a raw pipe-delimited text line into a concrete User subclass.
     */
    // POLYMORPHISM: Factory method instantiating Customer or AdminUser based on role token
    private User parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] tokens = line.split("\\|");
        if (tokens.length < 8) {
            return null;
        }

        String userId = tokens[0].trim();
        String role = tokens[1].trim();
        String fullName = tokens[2].trim();
        String email = tokens[3].trim();
        String phone = tokens[4].trim();
        String address = tokens[5].trim();
        String passwordHash = tokens[6].trim();
        String status = tokens[7].trim();

        if ("ADMIN".equalsIgnoreCase(role)) {
            return new AdminUser(userId, fullName, email, phone, address, passwordHash, status);
        } else {
            return new Customer(userId, fullName, email, phone, address, passwordHash, status);
        }
    }

    /**
     * Retrieves all user records from storage.
     */
    public synchronized List<User> getAllUsers() {
        List<String> lines = FileHandler.readAll(FILE_NAME);
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            User user = parseLine(line);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    /**
     * Finds a user by their unique userId.
     */
    public synchronized User getUserById(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return null;
        }
        for (User user : getAllUsers()) {
            if (user.getUserId().equalsIgnoreCase(userId.trim())) {
                return user;
            }
        }
        return null;
    }

    /**
     * Finds a user by their email address (case-insensitive).
     */
    public synchronized User getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        for (User user : getAllUsers()) {
            if (user.getEmail().equalsIgnoreCase(email.trim())) {
                return user;
            }
        }
        return null;
    }

    /**
     * Validates user credentials during sign-in.
     * Returns the authenticated User if email, password and ACTIVE status match; otherwise null.
     */
    public synchronized User validateLogin(String email, String rawPassword) {
        if (email == null || rawPassword == null) {
            return null;
        }

        User user = getUserByEmail(email);
        if (user == null) {
            return null;
        }

        // Deactivated users cannot log in
        if (!user.isActive()) {
            return null;
        }

        boolean passwordMatches = PasswordUtil.verify(rawPassword, user.getPasswordHash());
        if (passwordMatches) {
            ActivityLogger.log(user.getUserId(), "LOGIN_SUCCESS", "User logged in: " + user.getEmail());
            return user;
        } else {
            ActivityLogger.log(user.getUserId(), "LOGIN_FAILED", "Failed login attempt for: " + user.getEmail());
            return null;
        }
    }

    /**
     * Creates a new user record in users.txt.
     */
    public synchronized boolean createUser(User user) {
        if (user == null || user.getEmail() == null) {
            return false;
        }

        // Email uniqueness validation
        if (getUserByEmail(user.getEmail()) != null) {
            return false;
        }

        // Generate sequential unique user ID (e.g. U001, U002)
        String newId = IdGenerator.getNextId("U");
        user.setUserId(newId);

        if (user.getStatus() == null || user.getStatus().trim().isEmpty()) {
            user.setStatus("ACTIVE");
        }

        FileHandler.append(FILE_NAME, user.toDataLine());
        ActivityLogger.log(user.getUserId(), "REGISTER_USER", "Registered new " + user.getRole() + ": " + user.getEmail());
        return true;
    }

    /**
     * Updates profile details (Full Name, Phone, Delivery Address).
     */
    public synchronized boolean updateProfile(String userId, String fullName, String phone, String address) {
        List<User> users = getAllUsers();
        boolean found = false;

        List<String> lines = new ArrayList<>();
        for (User u : users) {
            if (u.getUserId().equalsIgnoreCase(userId)) {
                u.setFullName(fullName);
                u.setPhone(phone);
                u.setAddress(address);
                found = true;
            }
            lines.add(u.toDataLine());
        }

        if (found) {
            FileHandler.writeAll(FILE_NAME, lines);
            ActivityLogger.log(userId, "UPDATE_PROFILE", "Profile details updated");
        }
        return found;
    }

    /**
     * Updates password after validating old password.
     */
    public synchronized boolean changePassword(String userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);
        if (user == null) {
            return false;
        }

        if (!PasswordUtil.verify(oldPassword, user.getPasswordHash())) {
            return false; // Old password incorrect
        }

        String newHash = PasswordUtil.hash(newPassword);
        List<User> users = getAllUsers();
        List<String> lines = new ArrayList<>();

        for (User u : users) {
            if (u.getUserId().equalsIgnoreCase(userId)) {
                u.setPasswordHash(newHash);
            }
            lines.add(u.toDataLine());
        }

        FileHandler.writeAll(FILE_NAME, lines);
        ActivityLogger.log(userId, "CHANGE_PASSWORD", "Password changed successfully");
        return true;
    }

    /**
     * Soft-deletes a user by updating status to INACTIVE.
     */
    public synchronized boolean deactivateUser(String userId) {
        return setUserStatus(userId, "INACTIVE", "DEACTIVATE_USER");
    }

    /**
     * Reactivates an inactive user account.
     */
    public synchronized boolean reactivateUser(String userId) {
        return setUserStatus(userId, "ACTIVE", "REACTIVATE_USER");
    }

    /**
     * Permanently deletes a user by removing their record from users.txt.
     */
    public synchronized boolean permanentlyDeleteUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }

        List<User> users = getAllUsers();
        boolean found = false;
        List<String> remainingLines = new ArrayList<>();

        for (User u : users) {
            if (u.getUserId().equalsIgnoreCase(userId.trim())) {
                found = true;
            } else {
                remainingLines.add(u.toDataLine());
            }
        }

        if (found) {
            FileHandler.writeAll(FILE_NAME, remainingLines);
            ActivityLogger.log(userId, "PERMANENT_DELETE_USER", "User account permanently purged from storage: " + userId);
        }
        return found;
    }

    private synchronized boolean setUserStatus(String userId, String status, String logAction) {
        List<User> users = getAllUsers();
        boolean found = false;
        List<String> lines = new ArrayList<>();

        for (User u : users) {
            if (u.getUserId().equalsIgnoreCase(userId)) {
                u.setStatus(status);
                found = true;
            }
            lines.add(u.toDataLine());
        }

        if (found) {
            FileHandler.writeAll(FILE_NAME, lines);
            ActivityLogger.log(userId, logAction, "User status updated to " + status);
        }
        return found;
    }
}
