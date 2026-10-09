package service;

import model.User;
import repository.UserRepository;

import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    // CREATE - Register a new user
    public boolean registerUser(User user) {

        if (user == null) {
            return false;
        }

        if (user.getName() == null || user.getName().isBlank()
                || user.getEmail() == null || user.getEmail().isBlank()
                || user.getPassword() == null || user.getPassword().isBlank()) {
            return false;
        }

        if (findUserByEmail(user.getEmail()) != null) {
            return false;
        }

        userRepository.save(user);
        return true;
    }

    // READ - Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // READ - Find user by email
    public User findUserByEmail(String email) {

        if (email == null || email.isBlank()) {
            return null;
        }

        for (User user : userRepository.findAll()) {
            if (user.getEmail() != null
                    && user.getEmail().equalsIgnoreCase(email.trim())) {
                return user;
            }
        }

        return null;
    }

    // LOGIN - Validate email and password
    public User login(String email, String password) {

        if (email == null || password == null) {
            return null;
        }

        User user = findUserByEmail(email);

        if (user == null) {
            return null;
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            return null;
        }

        if (user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }
    // UPDATE - Update user profile
    public boolean updateUserProfile(
            String email, String phone, String address) {

        User user = findUserByEmail(email);

        if (user == null) {
            return false;
        }

        if ("INACTIVE".equalsIgnoreCase(user.getStatus())) {
            return false;
        }

        if (phone == null || phone.isBlank()
                || address == null || address.isBlank()) {
            return false;
        }

        user.setPhone(phone.trim());
        user.setAddress(address.trim());

        return userRepository.update(user);
    }
    // DELETE - Deactivate user account
    public boolean deactivateUser(String email) {

        User user = findUserByEmail(email);

        if (user == null) {
            return false;
        }

        if ("INACTIVE".equalsIgnoreCase(user.getStatus())) {
            return false;
        }

        user.setStatus("INACTIVE");

        return userRepository.update(user);
    }
}

