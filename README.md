# Member 4 - Order Processing & Lifecycle

**Branch Name:** eature/member-4-orders  
**Data File Owned:** orders.txt (stored in <user.home>/GroceryData/orders.txt)  

---

## 1. Responsibilities & Scope
Member 4 handles order placement, receipt generation, and lifecycle state management:
- Converting active cart into an immutable Order record.
- Deducting product inventory via ProductDAO.reduceStock().
- Clearing customer cart via CartDAO.clearCart().
- Customer order history and printable order receipts.
- Admin status updates (PENDING -> PROCESSING -> SHIPPED -> DELIVERED).
- Order cancellation (restoring product stock if cancelled while PENDING).

---

## 2. Object-Oriented Programming (OOP) Implementation
- **Abstraction:** Abstract class Order encapsulates order attributes (orderId, userId, orderType, items, subtotal, 	ax, 	otal, deliveryAddress, status, orderDate) and declares:
  - Abstract double calculateTotal()
  - Abstract String getOrderTypeLabel()
- **Inheritance:**
  - RegularOrder extends Order: Standard retail order pricing (subtotal + tax).
  - BulkOrder extends Order: Wholesale bulk order providing a 10% volume discount on orders exceeding $100.
- **Composition:** OrderItem represents snapshot item records (productId, 
ame, price, qty) preserved at checkout time even if catalog prices change later.
- **Polymorphism:** Order processing applies discounts polymorphically based on orderType.

---

## 3. CRUD Operations
| Operation | Servlet & URL | HTTP Method | Description |
|-----------|---------------|-------------|-------------|
| **Create** | OrderServlet (/orders) | POST (ction=place) | Converts cart items into a confirmed order, deducts inventory, empties cart. |
| **Read** | OrderServlet (/orders) | GET | Displays customer's personal order history. |
| **Read** | OrderReceiptServlet (/order/receipt) | GET | Printable itemized receipt with tax breakdown and dispatch status. |
| **Read** | AdminOrderServlet (/admin/orders) | GET | Admin view of all orders across the system. |
| **Update** | AdminOrderServlet (/admin/orders) | POST (ction=updateStatus) | Advances order status through legal state transitions. |
| **Delete** | OrderServlet (/orders) | POST (ction=cancel) | Cancels order (if still PENDING) and restores stock back to catalog. |

---

## 4. UI Pages (JSP)
1. order-history.jsp (/orders): Customer timeline of past orders with total amounts, status badges, receipt links, and cancel button.
2. order-receipt.jsp (/order/receipt): Itemized receipt invoice layout with subtotal, tax, bulk discount (if applicable), and total.
3. dmin-order-list.jsp (/admin/orders): Admin management table with status dropdown transitions (PROCESSING, SHIPPED, DELIVERED, CANCELLED).

---

## 5. Data File Format (orders.txt)
`
orderId|userId|orderType|items|subtotal|tax|total|address|status|date
`
- items format: productId:name:price:qty;productId:name:price:qty
- Example:
  O001|U002|REGULAR|P001:Organic Whole Milk:3.50:2;P002:Brown Eggs Dozen:4.20:1|11.20|0.90|12.10|123 Maple Street|PENDING|2026-10-11 18:30

---


