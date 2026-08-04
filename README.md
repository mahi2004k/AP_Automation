# AP Automation — Accounts Payable Automation System

A full-stack invoice processing system: upload a PDF invoice, it gets
auto-extracted (vendor, PO number, line items, totals), matched against a
Purchase Order + Receiving Report (3-way match), routed for approval, and
finally paid — with a full audit trail at every step.

- **Backend:** Java 21, Spring Boot, Spring Security (JWT), PostgreSQL, JPA/Hibernate
- **Frontend:** React 19 + Vite, React Router, Bootstrap 5, Axios

---

## 1. Prerequisites

This project can be run in two ways:

### Option 1: Docker (Recommended)

Run the complete application (PostgreSQL + Spring Boot + React) using Docker Compose.

See **[DOCKER.md](./DOCKER.md)**.

### Option 2: Native Installation

Install Java, Maven, PostgreSQL and Node.js locally, then follow the steps below.

Install these on your laptop before starting:

| Tool | Version | Check with |
|---|---|---|
| Java (JDK) | 21+ | `java -version` |
| Maven | 3.9+ (or use the included `mvnw` wrapper) | `mvn -v` |
| PostgreSQL | 14+ | `psql --version` |
| Node.js | 18+ (tested on 22) | `node -v` |
| npm | 9+ | `npm -v` |

---


---

# Run with Docker (Recommended)

This project includes Docker support for the complete application.

### Prerequisites

- Docker Desktop
- Docker Compose

Clone the repository:

```bash
git clone <repository-url>
cd Apautomation
```

Build and start everything:

```bash
docker compose up --build
```

This starts:

- PostgreSQL
- Spring Boot Backend
- React Frontend (served by Nginx)

Open:

| Service | URL |
|---------|-----|
| Frontend | http://localhost:5173 |
| Backend API | http://localhost:8080 |

Run in background:

```bash
docker compose up -d
```

Stop containers:

```bash
docker compose down
```

Stop containers and remove database data:

```bash
docker compose down -v
```

View logs:

```bash
docker compose logs -f
```

After Docker is running you can skip directly to **Using the App**.

## 2. Database Setup

Create the database once:

```bash
psql -U postgres
```
```sql
CREATE DATABASE ap_automation_db;
\q
```

Hibernate will create/update all tables automatically on first run
(`spring.jpa.hibernate.ddl-auto=update`) — you don't need to run any SQL scripts.

---

## 3. Backend Setup (`backend/ap-automation`)

The backend reads its configuration from **environment variables** (no secrets
are committed to the repo). Copy the example file and edit it:

```bash
cd backend/ap-automation
cp .env.example .env   # then open .env and fill in your own values
```

Since Spring Boot does not read `.env` files automatically, export the
variables into your shell before running the app.

**macOS / Linux:**
```bash
export $(grep -v '^#' .env | xargs)
```

**Windows (PowerShell):**
```powershell
Get-Content .env | ForEach-Object {
    if ($_ -match '^\s*([^#=]+)=(.*)$') {
        [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2])
    }
}
```

At minimum, set `DB_PASSWORD` to your PostgreSQL password and `JWT_SECRET` to
your own random string (e.g. `openssl rand -base64 32`). `MAIL_USERNAME` /
`MAIL_PASSWORD` are optional — leave them blank if you don't need email
notifications; the app runs fine without them (email sending just gets
skipped/logged, it never blocks the workflow).

Run the backend:

```bash
./mvnw spring-boot:run        # macOS/Linux
mvnw.cmd spring-boot:run       # Windows
```

The API starts on **http://localhost:8080**. Uploaded invoice PDFs are stored
in `backend/ap-automation/uploads/` (created automatically).

### Quick sanity check
```bash
curl http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Test User","email":"test@example.com","password":"password123","role":"ADMIN"}'
```
You should get back a JSON object with your new user's id/email/role.

---

## 4. Frontend Setup (`frontend/ap-automation-frontend`)

```bash
cd frontend/ap-automation-frontend
cp .env.example .env     # defaults to http://localhost:8080 — fine for local dev
npm install
npm run dev
```

Open **http://localhost:5173** in your browser.

To build a production bundle:
```bash
npm run build      # outputs static files to dist/
npm run preview    # serve the production build locally to test it
```

> **Note:** `node_modules/` is intentionally not included in this delivery —
> native dependencies are platform-specific (Windows/Mac/Linux binaries
> differ), so always run `npm install` fresh on your own machine.

---

## 5. Using the App

1. **Register** an account (pick a role: Accountant / Manager / Admin — all
   roles currently have the same access; role-based permissions are a natural
   next step) and log in.
2. **Purchase Orders** → add a PO for a vendor with line items.
3. **Receiving Reports** → record what was actually received against that PO.
4. **Invoices** → upload a (text-based, not scanned-image) PDF invoice. It's
   automatically parsed for vendor, PO number, line items, and totals.
5. **Matching** → run the 3-way match for an extracted invoice. If the
   vendor, PO, quantities, and totals line up with the PO + receiving report,
   it moves to `MATCHED`; otherwise it's flagged `NEEDS_REVIEW`.
6. **Approvals** → matched invoices show up here for an approve/reject
   decision with remarks.
7. **Payments** → once approved, record a payment (method + transaction
   reference) to mark the invoice `PAID`.
8. **Audit Log** → every step (upload, extraction, match, approval, payment)
   is logged per-invoice and viewable at any time.

---


## 6. Known Limitations (be aware before calling this "production")

- **Invoice extraction is regex/rule-based**, tuned for a specific invoice
  layout — it works well on text-based PDFs that follow common invoice
  wording ("Invoice Number:", "Total:", etc.) but is not a general-purpose
  OCR/ML model. Scanned image PDFs (no embedded text layer) will not extract
  anything meaningful.
- **Roles are not yet enforced** — any logged-in user can approve, pay, and
  delete records regardless of their selected role. Add `@PreAuthorize`
  checks in the controllers if you need real role-based access control
  before deploying this for multiple people.
- **No automated tests** are included. Given this is being handed over as a
  working baseline, add unit/integration tests before treating it as a
  long-term production system.
- **Single-tenant, single-currency assumptions** in a few places (e.g. no
  multi-company support).

---

## 7. What Was Fixed / Completed From the Half-Finished Project

- Fixed a crash-on-upload bug (`uploadedBy` was never set on invoices).
- Fixed a crash-on-login bug (users registered without a role had no role,
  which crashed the login token generation).
- Added the missing `GET /api/invoices`, `GET /api/invoices/{id}`, and
  `GET /api/invoices/{id}/file` endpoints (previously upload-only).
- Added a `GET /api/approvals/pending` endpoint so there's a real approval
  queue.
- Wired up (best-effort, non-blocking) email notifications for
  approve/reject/payment events.
- Added proper 401/409 error responses for bad login / duplicate email
  instead of generic 500s.
- Removed hardcoded database and email credentials from
  `application.properties`, replaced with environment variables.
- Built the six frontend pages that had empty folders: Invoices (list,
  upload, detail), Receiving Reports (list, add), Matching, Approvals,
  Payments, and Audit Log — and filled in all six corresponding API files
  that were empty.
- Wired all new pages into the router; the sidebar links already existed but
  pointed nowhere.
- Fixed the Purchase Order delete button (previously had no click handler).
- Fixed a filename-casing bug (`purchaseOrderList.jsx` vs the imported
  `PurchaseOrderList`) that breaks on case-sensitive filesystems (Linux/CI).
