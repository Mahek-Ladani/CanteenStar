# CanteenStar: Smart Canteen Ordering System 🍔☕

> A web-based canteen ordering system designed to eliminate long queues and streamline cafeteria operations for students, staff, and shopkeepers.

**Author:** Ladani Mahek B.  
**Enrollment No:** 24SE02CS091  
**Class:** CSE-5B (Batch-A)  

---

## 📌 Problem Statement
Break periods are limited (usually 30 to 45 minutes), and students often spend most of that time standing in long canteen lines. Shopkeepers struggle with verbal orders and paper slips. **CanteenStar** solves this by letting users order in advance and track food preparation in real time.

---

## ✨ Key Features

### 👤 Customer Portal
- **Quick Login:** Login with name, email, and contact number.
- **Order Types:** Choose between **Dine In**, **Pick Away (Takeaway)**, or **Preorder** (with scheduled pickup time).
- **Live Menu:** Browse menu items with real-time availability and prices.
- **Cart & Instant Checkout:** Dynamic cart calculation with total price.
- **Live Order Tracking:** Track statuses: `Pending` ➔ `Accepted` ➔ `Prepared` ➔ `Completed` (or `Declined`).
- **Order History:** View past orders anytime.

### 👨‍🍳 Shopkeeper Dashboard
- **Live Orders Stream:** Real-time incoming orders with newest and pending orders prioritized.
- **Order Status Management:** Accept, decline, mark prepared, and mark completed in one click.
- **Menu Availability Toggle:** Instantly toggle items between Available / Not Available when stock runs out.

---

## 🛠️ Technology Stack
- **Backend:** Java Servlets (Jakarta EE), JDBC
- **Frontend:** JSP, HTML5, CSS3, JavaScript
- **Database:** MySQL
- **Server:** Apache Tomcat 11
- **IDE:** Eclipse IDE

---

## 🚀 How to Set Up Locally

### 1. Database Setup
1. Open **MySQL Workbench**.
2. Run/Import the included SQL dump file:
   ```sql
   Dump20261009.sql
