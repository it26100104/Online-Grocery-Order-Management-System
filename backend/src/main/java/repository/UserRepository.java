package repository;



import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

    public class UserRepository {

        private final String filePath = "data/users.txt";

        // CREATE
        public void save(User user) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

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

            } catch (IOException e) {
                System.out.println("Error saving user.");
            }
        }

        // READ
        public List<User> findAll() {

            List<User> users = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

                String line;

                while ((line = reader.readLine()) != null) {

                    String[] data = line.split("\\|");

                    if (data.length == 8) {

                        User user = new User(
                                data[0],
                                data[1],
                                data[2],
                                data[3],
                                data[4],
                                data[5],
                                data[6],
                                data[7]
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

            for (User user : findAll()) {

                if (user.getEmail().equalsIgnoreCase(email)) {
                    return user;
                }
            }

            return null;
        }
    }

