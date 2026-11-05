# Online Grocery Order Management System

**Course:** SE1020 - Object Oriented Programming  
**Team Structure:** 6 Members (Isolated modular architecture)  
**Tech Stack:** Java 17, Jakarta Servlets, JSP, JSTL, Pure CSS, Flat-File Storage  

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
