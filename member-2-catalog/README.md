# Member 2 - Product & Catalog Management

**Branch Name:** eature/member-2-catalog  
**Data File Owned:** items.txt (stored in <user.home>/GroceryData/items.txt)  

---

## 1. Responsibilities & Scope
Member 2 is responsible for product inventory and catalog browsing:
- Product catalog browsing with live category filtering and search.
- Product details inspection (pricing, stock levels, shelf-life, expiration).
- Admin product management (add product, edit price/stock, delete product).
- Inter-module inventory management methods (isInStock(),ReduceStock(), RestoreStock()).

---

## 2. Object-Oriented Programming (OOP) Implementation
- **Abstraction:**  bstract class Product defines standard fields (productId, Name, category, price, stock, description, 	type) and abstract methods:
  - Abstract String displayInfo()
  - Abstract double getDiscountedPrice()
  - Abstract String getExtraInfo()
- **Inheritance:**
  - PerishableProduct extends Product: Represents goods that expire (e.g., Dairy, Fresh Produce). Adds expiryDate and offers automated clearance discounts (10% off).
  - NonPerishableProduct extends Product: Represents shelf-stable goods (e.g., Grains, Canned Food). Adds shelfLifeMonths.
- **Polymorphism:** ProductDAO reads records polymorphic as Product. Catalogs display items polymorphically calling displayInfo() and getDiscountedPrice().
- **Encapsulation:** All product attributes are private with strict validation (price >= 0, stock >= 0).

---

## 3. CRUD Operations
| Operation | Servlet & URL | HTTP Method | Description |
|-----------|---------------|-------------|-------------|
| **Create** | AddProductServlet (/admin/products/add) | POST | Admin creates new perishable or non-perishable product. |
| **Read** | CatalogServlet (/catalog) | GET | Customers browse catalog with category filter and search keyword. |
| **Read** | ProductDetailsServlet (/product/details) | GET | Displays detailed specs, stock status, and shelf life of a single product. |
| **Update** | EditProductServlet (/admin/products/edit) | POST | Admin modifies price, stock quantity, description, or expiration date. |
| **Delete** | AdminProductServlet (/admin/products) | POST (Action=delete) | Removes item from items.txt. |

---

## 4. UI Pages (JSP)
1. catalog.jsp (/catalog): Editorial dark card grid with search, filter tabs, stock tags, and Add to Cart action.
2. product-details.jsp (/product/details): Single product hero page with pricing, inventory info, and quantity picker.
3.  dmin-product-list.jsp (/admin/products): Admin table of all products with stock indicators and Edit/Delete buttons.
4.  dd-product.jsp (/admin/products/add): Form to create perishable or non-perishable products.
5. edit-product.jsp (/admin/products/edit): Form to edit price, stock count, and metadata.

---

