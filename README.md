# 🚀 AP Automation — Accounts Payable Automation System

<p align="center">
  <b>An Intelligent Full-Stack Invoice Processing & Accounts Payable Automation Platform</b>
</p>

<p align="center">
  Upload invoices → Extract data → Perform 3-Way Matching → Approve → Pay → Maintain Audit Trail
</p>

---

## 📌 Overview

**AP Automation** is a full-stack Accounts Payable automation system that streamlines the complete invoice processing lifecycle.

The system allows users to upload PDF invoices, automatically extract invoice information, validate invoices against Purchase Orders and Receiving Reports using **3-Way Matching**, route invoices for approval, process payments, and maintain a complete audit history.

The goal is to reduce manual invoice processing, minimize errors, and improve payment workflow efficiency.

---

# ✨ Key Features

## 📄 Invoice Processing

- Upload PDF invoices
- Automatic invoice data extraction
- Extract:
  - Vendor details
  - Invoice number
  - PO number
  - Invoice date
  - Line items
  - Quantity
  - Price
  - Tax
  - Total amount
- Invoice status tracking
- Store uploaded invoice documents

---

## 🔍 3-Way Matching System

The system validates invoices by comparing:

```
Purchase Order
        +
Receiving Report
        +
Invoice
```

Matching checks:

✅ Vendor verification  
✅ PO number verification  
✅ Item verification  
✅ Quantity verification  
✅ Price verification  
✅ Total amount verification  

Invoice status:

- MATCHED
- NEEDS_REVIEW

---

## 👨‍💼 Approval Workflow

- Pending invoice approval queue
- Approve invoices
- Reject invoices
- Add approval remarks
- Maintain approval history

---

## 💳 Payment Management

- Record invoice payments
- Payment method tracking
- Transaction reference storage
- Update invoice payment status

---

## 📜 Audit Trail

Every important action is recorded:

- Invoice upload
- Data extraction
- Matching result
- Approval decision
- Payment completion

Users can view the complete invoice history anytime.

---

# 🛠 Technology Stack

## Backend

| Technology | Usage |
|------------|-------|
| Java 21 | Programming Language |
| Spring Boot | Backend Framework |
| Spring Security | Security Framework |
| JWT | Authentication & Authorization |
| Spring Data JPA | Database Access |
| Hibernate | ORM Framework |
| PostgreSQL | Database |
| Maven | Build Tool |

---

## Frontend

| Technology | Usage |
|------------|-------|
| React 19 | User Interface |
| Vite | Frontend Build Tool |
| React Router | Navigation |
| Bootstrap 5 | UI Styling |
| Axios | API Communication |

---

## DevOps

| Technology | Usage |
|------------|-------|
| Docker | Containerization |
| Docker Compose | Multi-container Setup |
| Nginx | Frontend Server |
| Git | Version Control |

---

# 📂 Project Structure

```
AP-Automation
│
├── backend
│   └── ap-automation
│       ├── src
│       ├── uploads
│       ├── Dockerfile
│       └── pom.xml
│
├── frontend
│   └── ap-automation-frontend
│       ├── src
│       ├── Dockerfile
│       └── package.json
│
├── docker-compose.yml
├── DOCKER.md
└── README.md
```

---

# 🚀 Getting Started

The application can be executed in two ways:

1. Docker Setup (Recommended)
2. Manual Local Setup

---

# 🐳 Option 1: Run Using Docker (Recommended)

## Prerequisites

Install:

- Docker Desktop
- Docker Compose

---

## Clone Repository

```bash
git clone <repository-url>

cd Apautomation
```

---

## Build and Start Application

```bash
docker compose up --build
```

This starts:

- PostgreSQL Database
- Spring Boot Backend
- React Frontend with Nginx

---

## Application URLs

| Service | URL |
|---------|-----|
| Frontend | http://localhost:5173 |
| Backend API | http://localhost:8080 |

---

## Run in Background

```bash
docker compose up -d
```

---

## Stop Application

```bash
docker compose down
```

---

## Remove Database Data

```bash
docker compose down -v
```

---

## View Logs

```bash
docker compose logs -f
```

---

# 💻 Option 2: Local Installation

## Required Software

| Tool | Version |
|------|---------|
| Java | 21+ |
| Maven | 3.9+ |
| PostgreSQL | 14+ |
| Node.js | 18+ |
| npm | 9+ |

Check versions:

```bash
java -version

mvn -v

node -v

npm -v

psql --version
```

---

# 🗄 Database Setup

Create PostgreSQL database:

```bash
psql -U postgres
```

Run:

```sql
CREATE DATABASE ap_automation_db;
```

Exit:

```sql
\q
```

Hibernate automatically creates tables:

```
spring.jpa.hibernate.ddl-auto=update
```

No manual SQL scripts are required.

---

# ⚙ Backend Setup

Navigate:

```bash
cd backend/ap-automation
```

Create environment file:

```bash
cp .env.example .env
```

Configure:

```
DB_PASSWORD=your_password

JWT_SECRET=your_secret_key

MAIL_USERNAME=email_username

MAIL_PASSWORD=email_password
```

---

## Run Backend

Windows:

```bash
mvnw.cmd spring-boot:run
```

Linux/Mac:

```bash
./mvnw spring-boot:run
```

Backend runs on:

```
http://localhost:8080
```

Uploaded invoice files are stored:

```
backend/ap-automation/uploads/
```

---

# 🔎 Backend API Test

Register User:

```bash
curl http://localhost:8080/api/auth/register \
-H "Content-Type: application/json" \
-d '{"fullName":"Test User","email":"test@example.com","password":"password123","role":"ADMIN"}'
```

---

# 🎨 Frontend Setup

Navigate:

```bash
cd frontend/ap-automation-frontend
```

Create environment file:

```bash
cp .env.example .env
```

Install dependencies:

```bash
npm install
```

Run application:

```bash
npm run dev
```

Frontend runs on:

```
http://localhost:5173
```

---

## Production Build

```bash
npm run build
```

Test production build:

```bash
npm run preview
```

---

# 📖 Application Workflow

```
User Login

      ↓

Create Purchase Order

      ↓

Create Receiving Report

      ↓

Upload Invoice PDF

      ↓

Invoice Data Extraction

      ↓

3-Way Matching

      ↓

Approval Process

      ↓

Payment Processing

      ↓

Audit History
```

---

# 🔗 Main Modules

## 🔐 Authentication Module

Features:

- User Registration
- Login
- JWT Authentication
- Secure API Access


---

## 📦 Purchase Order Module

Features:

- Create Purchase Orders
- Add Line Items
- Update Orders
- Delete Orders
- View Orders

---

## 🚚 Receiving Report Module

Features:

- Record received items
- Link with Purchase Orders
- Track received quantity

---

## 📄 Invoice Module

Features:

- Upload Invoice PDF
- Extract invoice information
- View invoice details
- Download invoice file

---

## 🔍 Matching Module

Features:

- Compare Invoice
- Compare Purchase Order
- Compare Receiving Report
- Generate matching result

---

## ✅ Approval Module

Features:

- View pending approvals
- Approve invoices
- Reject invoices
- Add remarks

---

## 💳 Payment Module

Features:

- Record payment
- Store transaction details
- Update payment status

---

## 📜 Audit Module

Tracks:

- User actions
- Invoice processing history
- Approval history
- Payment history

---

# ⚠ Known Limitations

Currently:

- Invoice extraction is rule-based
- Supports text-based PDFs only
- Scanned image PDFs require OCR support
- Role permissions are not fully enforced
- No automated test cases included
- Single tenant support
- Single currency assumption

---

# ✅ Completed Improvements

✔ Fixed invoice upload crash caused by missing uploaded user

✔ Fixed login issue when users had no role

✔ Added missing invoice APIs:

- GET `/api/invoices`
- GET `/api/invoices/{id}`
- GET `/api/invoices/{id}/file`

✔ Added approval pending API

✔ Added email notification support

✔ Added proper HTTP error responses

✔ Removed hardcoded credentials

✔ Added environment-based configuration

✔ Completed missing frontend pages:

- Invoice List
- Invoice Upload
- Invoice Details
- Receiving Reports
- Matching
- Approvals
- Payments
- Audit Logs

✔ Completed frontend API integration

✔ Fixed Purchase Order delete functionality

✔ Fixed filename casing issues for Linux deployment

✔ Added Docker support

✔ Added production environment configuration

---

# 🚀 Future Enhancements

- AI-based invoice extraction
- OCR support using Tesseract
- Machine Learning invoice classification
- Role Based Access Control
- Dashboard analytics
- Export reports to Excel/PDF
- Email workflow automation
- Real-time notifications
- Unit & Integration Testing
- Cloud deployment
- Multi-company support
- Multi-currency support

---

# 👨‍💻 Author

## Mahesh Konnur

Java Full Stack Developer

### Skills

- Java
- Spring Boot
- Spring Security
- JWT
- React
- PostgreSQL
- Docker
- REST APIs

---

# ⭐ Support

If you find this project useful, consider giving it a ⭐ on GitHub.
