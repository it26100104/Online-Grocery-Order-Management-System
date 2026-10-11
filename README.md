# Shared Foundation - Platform Core & Integration Guide

This folder contains the common foundation shared across all 6 members:
- **Embedded Tomcat & Startup:** Main.java, pom.xml, 
un.bat
- **Utility Layer:** FileHandler.java (thread-safe flat file I/O to <user.home>/GroceryData), IdGenerator.java, PasswordUtil.java (SHA-256), ActivityLogger.java
- **Web Infrastructure:** AuthFilter.java (session & role guard), AppContextListener.java (database seeding)
- **Design System:** style.css (Aura Grocery modern dark theme with deep space obsidian, slate charcoal, and vibrant emerald design system), 
avbar.jsp, header.jsp, ooter.jsp, index.jsp

---

