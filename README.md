# 📦 Restoflowing

**All-in-One Mobile Platform for Restaurant Operations**  
Inventory, staff scheduling, payroll, and announcements — all in one place.

---

## 🧩 Overview

**Restoflowing** is a mobile-first web platform designed to automate restaurant operations for small-to-medium restaurant owners in the U.S., especially those who are not tech-savvy or fluent in English.

- 👥 **Target Audience**: Korean-American restaurant owners in their 40s–50s
- 🎯 **Goal**: Digitize and automate paper-based restaurant workflows
- 🛠 **Stack**:
	- Frontend: **Next.js** (Client-side rendering)
	- Backend: **Spring Boot**, **MariaDB**, **JPA**, **Spring Security**
	- Deployment: **Docker**, **AWS Lightsail**, **Nginx**
	- CI/CD: **GitHub Actions** (SSH-based)

---

## 🔑 Features

### ✅ Inventory Management
- Role-based item management (Server / Kitchen)
- Modal-based UI for adding/editing/deleting items
- Real-time updates (no page reload)

### ✅ Restaurant & Role Management
- Multi-restaurant support per owner
- Roles: **Owner**, **Manager**, **Server**, **Kitchen**
- Owner-only signup, others join by invitation

### ✅ Payroll System
- Calculate total pay using hourly wage + tips
- View payroll per employee

### ✅ Scheduling System
- Owner assigns 2-week AM/PM shifts
- Staff can view their own schedules

### ✅ Announcement Board
- Owner/Manager posts messages to staff
- Real-time WebSocket delivery


---

## 🚀 Deployment

- **Domain**: [https://restoflowing.com](https://restoflowing.com) (Not available now)
- **Hosting**: AWS Lightsail ($12 plan)
- **Database**: MariaDB (manual install, non-Bitnami)
- **HTTPS**: Let’s Encrypt + Nginx reverse proxy
- **CI/CD**: GitHub Actions + SSH deploy to Lightsail instance

---

## 📁 Project Structure