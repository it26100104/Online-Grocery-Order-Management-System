# Online Grocery Order Management System

**Course:** SE1020 - Object Oriented Programming  
**Team Structure:** 6 Members (Isolated modular architecture)  
**Tech Stack:** Java 17, Jakarta Servlets, JSP, JSTL, Pure CSS, Flat-File Storage  

---

## ⚡ Super Easy 1-Click Launch (No Tomcat Setup Required!)

You do **NOT** need to configure Tomcat servers, plugins, or artifacts in IntelliJ. We have embedded Tomcat directly into the project!

### Option A: From IntelliJ IDEA (Easiest)
1. Open this project in **IntelliJ IDEA**.
2. Open [`src/main/java/com/grocery/Main.java`](src/main/java/com/grocery/Main.java).
3. Click the green **Run (▶)** button next to `main`.
4. Open your browser at:
   ```
   http://localhost:8080/
   ```

### Option B: From Windows Explorer (Double Click)
- Simply double-click **`run.bat`** in this folder!

### Option C: From Terminal / Command Prompt
```bash
mvn clean compile exec:java
```

### Option D: Traditional WAR Submission for University
When submitting your project to your lecturer, you can still build a standard `.war` file:
```bash
mvn clean package
```
The file will be generated at `target/grocery-app.war` and can be deployed to any standard Apache Tomcat 10.1 server.

---

## 🔑 Default Accounts (Pre-Seeded)

On the first run, all data files are created automatically in `<user.home>/GroceryData` with these ready-to-test accounts:

| Role | Email | Password | What You Can Access |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin@grocery.com` | `Admin@123` | Full admin management, inventory, orders, deliveries, reports |
| **Customer** | `john@grocery.com` | `Customer@123` | Catalog, cart, wishlist, placing orders, tracking deliveries |

---

## 📁 Data Storage Location

All data is stored in UTF-8 text files in:
```
<user.home>/GroceryData/
```
*(On Windows: `C:\Users\<YourUsername>\GroceryData\`)*

- **Why here?** Maven rebuilds clean the `target/` folder. Storing files here guarantees your test data is **never wiped** when you rebuild the code.
- **Want to reset everything to fresh test data?** Just delete the `GroceryData` folder and rerun `Main.java` — the app will re-seed all starter data automatically.

---

## 👥 6-Member Team Breakdown

| Member | Module | Domain Classes (OOP) | Data File | JSP Folder |
| :--- | :--- | :--- | :--- | :--- |
| **Member 1** | User Auth & Profile | `User` → `Customer`, `AdminUser` | `users.txt` | `WEB-INF/views/auth/` |
| **Member 2** | Catalog & Stock | `Product` → `PerishableProduct`, `NonPerishableProduct` | `items.txt` | `WEB-INF/views/catalog/` |
| **Member 3** | Cart & Wishlist | `ListItem` → `CartItem`, `WishlistItem` | `cart.txt`, `wishlist.txt` | `WEB-INF/views/cart/` |
| **Member 4** | Order Processing | `Order` → `RegularOrder`, `BulkOrder`, `OrderItem` | `orders.txt` | `WEB-INF/views/order/` |
| **Member 5** | Payment & Invoicing | `Payment` → `CashOnDeliveryPayment`, `CardPayment`, `WalletPayment` | `payments.txt` | `WEB-INF/views/payment/` |
| **Member 6** | Delivery & Analytics | `Delivery` → `StandardDelivery`, `ExpressDelivery`; `Report` → `SalesReport`, `DeliveryReport` | `deliveries.txt`, `reports.txt` | `WEB-INF/views/delivery/` |
