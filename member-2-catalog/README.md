# Member 2 - Product & Catalog Management

**Branch Name:** `Member_02_Product_&_Catalog_Management`  
**Data File Owned:** `items.txt` (stored in `<user.home>/GroceryData/items.txt`)

---

## 1. Responsibilities & Scope
Member 2 is responsible for the complete lifecycle of product inventory and catalog browsing:
- Product catalog browsing with live category filtering, keyword search, and dynamic image previews.
- Product details inspection including pricing, discounted rates, stock levels, and specialized expiration/shelf-life attributes.
- Secure administrative product management (adding items, editing price/stock/metadata, and deleting obsolete items).
- Inter-module inventory management methods to check stock availability (`isInStock()`), reduce stock during orders (`reduceStock()`), and restore stock upon cancellations (`restoreStock()`).

---

## 2. Object-Oriented Programming (OOP) Implementation
- **Abstraction:** Abstract base class `Product` defines standard core fields (`productId`, `name`, `category`, `price`, `stock`, `description`, `type`, `extra`, `imageUrl`) and enforces abstract method contracts:
  - `public abstract String displayInfo();`
  - `public abstract double getDiscountedPrice();`
- **Inheritance:**
  - `PerishableProduct` extends `Product`: Represents fresh goods that expire (e.g., Dairy, Fruits, Vegetables). Adds `expiryDate` tracking and offers automated promotional clearance discounts (10% off).
  - `NonPerishableProduct` extends `Product`: Represents shelf-stable pantry items (e.g., Grains, Canned Goods, Beverages). Adds `shelfLifeMonths` and automated bundle discounts (5% off).
- **Polymorphism:** `ProductDAO` parses raw text lines polymorphically into concrete subclass instances. Customer and admin catalogs invoke `displayInfo()` and `getDiscountedPrice()` dynamically based on the product type.
- **Encapsulation:** All fields in `Product` and its subclasses are strictly `private`, managed through validated getters and setters (ensuring prices and stock counts can never be negative).

---

## 3. CRUD Operations
| Operation | Servlet & URL | HTTP Method | Description |
|-----------|---------------|-------------|-------------|
| **Create** | `AddProductServlet` (`/admin/products/add`) | GET / POST | Admin creates new perishable or non-perishable grocery products with stock details, custom categories, and image URLs, saving directly to `items.txt`. |
| **Read** | `CatalogServlet` (`/catalog`) | GET | Customers browse searchable product catalogs with dynamic category filters and inventory badges. |
| **Read** | `ProductDetailsServlet` (`/product-details`) | GET | Displays detailed item specs, storage/validity details, and interactive quantity selectors. |
| **Update** | `EditProductServlet` (`/admin/products/edit`) | GET / POST | Admin modifies product pricing, stock availability, descriptions, and expiration metadata. |
| **Delete** | `AdminProductServlet` (`/admin/products`) | POST (Action=`delete`) | Removes discontinued or out-of-stock items completely from `items.txt`. |

---

## 4. UI Pages (JSP)
1. **`catalog.jsp`** (`/catalog`): Editorial card grid featuring search bars, category selectors, stock status badges, dynamic image previews, and "Add to Cart" actions.
2. **`product-details.jsp`** (`/product-details`): Single product hero page highlighting pricing, stock availability, polymorphic storage info, and quantity pickers.
3. **`admin-product-list.jsp`** (`/admin/products`): Admin inventory dashboard displaying all items, low-stock alerts, and quick-access Edit/Delete buttons.
4. **`add-product.jsp`** (`/admin/products/add`): Comprehensive form supporting dynamic custom categories, perishable/non-perishable type switching, and 1-click preset image selectors.
5. **`edit-product.jsp`** (`/admin/products/edit`): Pre-populated update form for modifying existing inventory records safely.
