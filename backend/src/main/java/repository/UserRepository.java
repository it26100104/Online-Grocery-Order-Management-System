package repository;

import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final String filePath = "backend/data/users.txt";

    // CREATE
    public void save(User user) {
        List<User> users = findAll();
        users.add(user);
        writeAll(users);
    }

    // READ
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = reader.readLine()) != null) {

                // Skip the header row
                if (line.startsWith("userId|")) {
                    continue;
                }

                String[] data = line.split("\\|", -1);

                if (data.length == 8) {
                    User user = new User(
                            data[0], data[1], data[2], data[3],
                            data[4], data[5], data[6], data[7]
                    );

                    users.add(user);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("User file not found.");
        } catch (IOException e) {
            System.out.println("Error reading users.");
        }

        return users;
    }

    // FIND USER BY EMAIL
    public User findByEmail(String email) {
        if (email == null) {
            return null;
        }

        for (User user : findAll()) {
            if (user.getEmail() != null
                    && user.getEmail().equalsIgnoreCase(email.trim())) {
                return user;
            }
        }

        return null;
    }

    // UPDATE OR DELETE
    public boolean update(User updatedUser) {
        List<User> users = findAll();
        boolean found = false;

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId()
                    .equals(updatedUser.getUserId())) {
                users.set(i, updatedUser);
                found = true;
                break;
            }
        }

        if (found) {
            writeAll(users);
        }

        return found;
    }

    // WRITE ALL USERS TO FILE
    private void writeAll(List<User> users) {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(filePath, false))) {

            writer.write(
                    "userId|name|email|password|phone|address|role|status"
            );
            writer.newLine();

            for (User user : users) {
                writer.write(
                        user.getUserId() + "|" +
                                user.getName() + "|" +
                                user.getEmail() + "|" +
                                user.getPassword() + "|" +
                                user.getPhone() + "|" +
                                user.getAddress() + "|" +
                                user.getRole() + "|" +
                                user.getStatus()
                );
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error writing users.");
        }
    }
}