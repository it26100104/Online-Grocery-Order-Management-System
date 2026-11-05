package com.grocery.listener;

import com.grocery.util.ActivityLogger;
import com.grocery.util.FileHandler;
import com.grocery.util.PasswordUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.File;
import java.util.List;

/**
 * AppContextListener boots the application storage.
 * Automatically creates all 8 data files with starter data so every team member
 * can immediately test their module without manual setup.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("==================================================");
        System.out.println(" Starting Online Grocery Order Management System");
        System.out.println(" Data Storage: " + FileHandler.getDataDirectory());
        System.out.println("==================================================");

        FileHandler.ensureDataDirectoryExists();

        // 1. users.txt: Seed admin and test customer if empty
        if (FileHandler.readAll("users.txt").isEmpty()) {
            String adminHash = PasswordUtil.hash("Admin@123");
            String custHash = PasswordUtil.hash("Customer@123");
            FileHandler.append("users.txt", "U001|ADMIN|System Administrator|admin@grocery.com|+94 77 123 4567|Main HQ, Colombo|" + adminHash + "|ACTIVE");
            FileHandler.append("users.txt", "U002|CUSTOMER|John Doe|john@grocery.com|+94 71 987 6543|No 45, Flower Road, Colombo|" + custHash + "|ACTIVE");
            FileHandler.append("users.txt", "U003|CUSTOMER|Sarah Smith|sarah@grocery.com|+94 76 555 4321|No 12, Kandy Road, Kiribathgoda|" + custHash + "|ACTIVE");
            System.out.println("[Data] Initialized users.txt (Admin + Customers)");
        }

        // 2. items.txt: Seed catalog products if empty
        if (FileHandler.readAll("items.txt").isEmpty()) {
            FileHandler.append("items.txt", "P001|Fresh Organic Milk (1L)|Dairy|350.00|50|Full cream pasteurized fresh cow milk|PERISHABLE|2026-10-25");
            FileHandler.append("items.txt", "P002|Red Apples (1kg)|Fruits|780.00|40|Crisp imported Washington red apples|PERISHABLE|2026-11-05");
            FileHandler.append("items.txt", "P003|Basmati Rice (5kg)|Grains|2450.00|30|Premium aged long-grain aromatic rice|NON_PERISHABLE|24");
            FileHandler.append("items.txt", "P004|Ceylon Black Tea (400g)|Beverages|620.00|60|Pure Ceylon BOPF loose leaf black tea|NON_PERISHABLE|36");
            FileHandler.append("items.txt", "P005|Farm Fresh Eggs (Pack of 10)|Poultry|450.00|80|Graded fresh brown farm eggs|PERISHABLE|2026-10-30");
            FileHandler.append("items.txt", "P006|Extra Virgin Olive Oil (500ml)|Cooking Oil|1850.00|25|Cold-pressed Mediterranean pure olive oil|NON_PERISHABLE|18");
            System.out.println("[Data] Initialized items.txt (Products)");
        }

        // 3. cart.txt: Seed sample cart
        if (FileHandler.readAll("cart.txt").isEmpty()) {
            FileHandler.append("cart.txt", "C001|U002|P001|Fresh Organic Milk (1L)|350.00|2");
            FileHandler.append("cart.txt", "C002|U002|P003|Basmati Rice (5kg)|2450.00|1");
        }

        // 4. wishlist.txt: Seed sample wishlist
        if (FileHandler.readAll("wishlist.txt").isEmpty()) {
            FileHandler.append("wishlist.txt", "W001|U002|P004|Ceylon Black Tea (400g)|620.00");
            FileHandler.append("wishlist.txt", "W002|U003|P002|Red Apples (1kg)|780.00");
        }

        // 5. orders.txt: Seed sample orders
        if (FileHandler.readAll("orders.txt").isEmpty()) {
            FileHandler.append("orders.txt", "O001|U002|REGULAR|P001:Fresh Organic Milk (1L):350.00:2;P004:Ceylon Black Tea (400g):620.00:1|1320.00|105.60|1425.60|No 45, Flower Road, Colombo|DELIVERED|2026-10-10 14:30:00");
            FileHandler.append("orders.txt", "O002|U003|REGULAR|P003:Basmati Rice (5kg):2450.00:1|2450.00|196.00|2646.00|No 12, Kandy Road, Kiribathgoda|PENDING|2026-10-11 10:15:00");
        }

        // 6. payments.txt: Seed transactions and saved methods
        if (FileHandler.readAll("payments.txt").isEmpty()) {
            FileHandler.append("payments.txt", "TXN|PAY001|O001|U002|CARD|1425.60|PAID|2026-10-10 14:32:00");
            FileHandler.append("payments.txt", "TXN|PAY002|O002|U003|COD|2646.00|UNPAID|2026-10-11 10:15:00");
            FileHandler.append("payments.txt", "METHOD|M001|U002|CARD|Commercial Bank Visa Debit|4242");
            FileHandler.append("payments.txt", "METHOD|M002|U002|WALLET|FriMi Digital Wallet|9012");
        }

        // 7. deliveries.txt: Seed sample deliveries
        if (FileHandler.readAll("deliveries.txt").isEmpty()) {
            FileHandler.append("deliveries.txt", "D001|O001|U002|STANDARD|No 45, Flower Road, Colombo|Kamal Perera|DELIVERED|2026-10-10 17:00:00|250.00");
            FileHandler.append("deliveries.txt", "D002|O002|U003|EXPRESS|No 12, Kandy Road, Kiribathgoda|Sunil Silva|SCHEDULED|2026-10-11 11:00:00|500.00");
        }

        // 8. reports.txt: Seed initial audit log
        if (FileHandler.readAll("reports.txt").isEmpty()) {
            ActivityLogger.log("SYSTEM", "INIT", "System initialized with complete starter dataset");
        }

        System.out.println(" All modules ready for development and testing.");
        System.out.println(" Open http://localhost:8080/ in your browser");
        System.out.println("==================================================");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[AppContextListener] Application stopped.");
    }
}
