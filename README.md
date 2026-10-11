# Member 1 - User Authentication & Account Management

**Branch Name:** eature/member-1-auth  
**Data File Owned:** users.txt (stored in <user.home>/GroceryData/users.txt)  

---

## 1. Responsibilities & Scope
Member 1 is responsible for the complete user lifecycle:
- Customer and Admin registration with SHA-256 password hashing.
- Secure session-based authentication (login/logout).
- Customer profile view and editing (name, phone, delivery address, password update).
- Admin customer management (view all registered users, activate/deactivate accounts).

---

## 2. Object-Oriented Programming (OOP) Implementation
- **Abstraction:** Abstract class User defines common user attributes (userId, FullName, email, phone, Address, status) and abstract methods:
  - Abstract String getRole()
  - Abstract String getDashboardUrl()
- **Inheritance:**
  - Customer extends User: Overrides getRole() returning "CUSTOMER" and getDashboardUrl() returning "/catalog".
  - AdminUser extends User: Overrides getRole() returning "ADMIN" and getDashboardUrl() returning "/admin/dashboard".
- **Polymorphism:** UserDAO works with polymorphic User references. Depending on the 
ole field in users.txt, it instantiates either a Customer or AdminUser.
- **Encapsulation:** All fields in User, Customer, and AdminUser are private. Access is strictly controlled via getters and setters with validation (e.g., email format, empty check, status check).

---

## 3. CRUD Operations
| Operation | Servlet & URL | HTTP Method | Description |
|-----------|---------------|-------------|-------------|
| **Create** | RegisterServlet (/register) | POST | Registers new customer or admin, validates email uniqueness, hashes password, saves to users.txt. |
| **Read** | ProfileServlet (/profile) | GET | Fetches logged-in user profile details for editing. |
| **Read** | AdminUserServlet (/admin/users) | GET | Lists all users with status badges (Admin only). |
| **Update** | ProfileServlet (/profile) | POST | Updates profile details (name, phone, address, optional password update). |
| **Delete** | AdminUserServlet (/admin/users) | POST (Action=deactivate) | Soft deletes a user by setting status to INACTIVE. |
| **Delete** | ProfileServlet (/profile) | POST (Action=deletePermanent) | Customer permanently purges their own account from users.txt. |
| **Delete** | AdminUserServlet (/admin/users) | POST (Action=deletePermanent) | Administrator permanently deletes user account from storage. |

---

## 4. UI Pages (JSP)
1. login.jsp (/login): Minimalist dark editorial login form.
2. 
egister.jsp (/register): User registration with instant client-side validation.
3. profile.jsp (/profile): User account details and address update form.
4. user-list.jsp (/admin/users): Administrative table displaying all registered users with status toggles.

---

