# Shared Foundation - Platform Core & Integration Guide

This folder contains the common foundation shared across all 6 members:
- **Embedded Tomcat & Startup:** Main.java, pom.xml, un.bat
- **Utility Layer:** FileHandler.java (thread-safe flat file I/O to <user.home>/GroceryData), IdGenerator.java, PasswordUtil.java (SHA-256), ActivityLogger.java
- **Web Infrastructure:** AuthFilter.java (session & role guard), AppContextListener.java (database seeding)
- **Design System:** style.css (Aura Grocery modern dark theme with deep space obsidian, slate charcoal, and vibrant emerald design system), 
avbar.jsp, header.jsp, ooter.jsp, index.jsp

---

## How the Team Integrates Work into Main
1. Team lead creates repository and pushes the foundation on main.
2. Each member receives their folder (member-1-auth, member-2-catalog, etc.).
3. Each member checks out their branch:
   - Member 1 -> eature/member-1-auth
   - Member 2 -> eature/member-2-catalog
   - Member 3 -> eature/member-3-cart
   - Member 4 -> eature/member-4-orders
   - Member 5 -> eature/member-5-payments
   - Member 6 -> eature/member-6-delivery
4. Each member commits their files and pushes their branch.
5. Member opens a Pull Request on GitHub.
6. Because every member's code is strictly segregated into unique sub-packages (servlet/auth, servlet/catalog, etc.) and unique view folders (iews/auth, iews/catalog, etc.), **there are ZERO merge conflicts**.
7. Team lead merges each PR into main.
8. Running un.bat launches the complete integrated application at http://localhost:8080/.
