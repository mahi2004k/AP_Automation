# Running AP Automation with Docker

This lets you start the **database + backend + frontend** with one command,
without installing Java, Maven, Node, or PostgreSQL directly on your laptop.

> This project includes Docker support for running the complete application
> (PostgreSQL, Spring Boot, and React) with Docker Compose.

---

## Step 1 — Install Docker

Download and install **Docker Desktop** for your OS:
Download Docker Desktop:

https://www.docker.com/products/docker-desktop/


Verify it's working:
```bash
docker --version
docker compose version
```
(Modern Docker uses `docker compose` with a space — not the old standalone
`docker-compose` command. Either works if both are installed.)

---

## Step 2 — What got added to your project

```
Apautomation/
├── docker-compose.yml          ← orchestrates all 3 containers
├── .env.example                ← copy this to .env and fill in secrets
├── backend/ap-automation/
│   ├── Dockerfile              ← builds the Spring Boot API image
│   └── .dockerignore
└── frontend/ap-automation-frontend/
    ├── Dockerfile              ← builds the React app + serves via nginx
    ├── nginx.conf
    └── .dockerignore
```

Three containers will run:
| Container | What it is | Port on your laptop |
|---|---|---|
| `ap-automation-db` | PostgreSQL 16 | `5432` |
| `ap-automation-backend` | Spring Boot API | `8080` |
| `ap-automation-frontend` | React app (served by nginx) | `5173` |

---

## Step 3 — Configure your secrets

From the project root (the folder containing `docker-compose.yml`):

```bash
cp .env.example .env
```

Open `.env` and set at minimum:
- `DB_PASSWORD` — any password you want for the local Postgres container
- `JWT_SECRET` — generate one with `openssl rand -base64 32`

Leave `MAIL_USERNAME` / `MAIL_PASSWORD` blank if you don't need email
notifications — the app works fine without them.

---

## Step 4 — Build and start everything

From the project root:

```bash
docker compose up --build -d
```

- `--build` forces it to build the backend/frontend images from the
  Dockerfiles (needed the first time, and any time you change the code)
- `-d` runs it in the background ("detached")

First run will take a few minutes — it's downloading base images (Postgres,
Maven, Node, nginx) and running a full Maven build inside the container.

Watch the logs while it starts up:
```bash
docker compose logs -f
```
Press `Ctrl+C` to stop watching logs (this does **not** stop the containers).

You'll know it's ready when you see something like
`Started ApAutomationApplication in X seconds` in the backend logs.

---

## Step 5 — Use the app

Open **http://localhost:5173** in your browser. Register a user, log in, and
use it exactly as you would running it locally without Docker.

---

## Everyday commands

| What you want to do | Command |
|---|---|
| Start everything (already built) | `docker compose up -d` |
| Start and rebuild after code changes | `docker compose up --build -d` |
| Stop everything (keeps your data) | `docker compose down` |
| Stop and **wipe the database + uploads** | `docker compose down -v` |
| View logs for everything | `docker compose logs -f` |
| View logs for just the backend | `docker compose logs -f backend` |
| See what's running | `docker compose ps` |
| Rebuild just one service | `docker compose build backend` |
| Open a shell inside the backend container | `docker exec -it ap-automation-backend sh` |
| Connect to the Postgres container directly | `docker exec -it ap-automation-db psql -U postgres -d ap_automation_db` |

---

## Common issues

**"Port 8080/5432/5173 already in use"**
Something else on your laptop is already using that port (maybe you're
running the backend natively at the same time). Either stop that other
process, or change the left-hand side of the port mapping in
`docker-compose.yml`, e.g. `"8081:8080"` to use 8081 on your laptop instead.

**Frontend loads but every API call fails / CORS errors**
Check `VITE_API_BASE_URL` in your `.env` — it must be a URL your **browser**
can reach (like `http://localhost:8080`), not the internal Docker service
name `backend`. If you rebuilt the frontend without rebuilding after
changing this, remember Vite bakes it in at *build* time — you must run
`docker compose up --build frontend` again after changing it, a restart
alone won't pick up the change.

**Backend keeps restarting / can't connect to database**
Check `docker compose logs backend` — usually means Postgres wasn't ready
yet. The `depends_on: condition: service_healthy` in `docker-compose.yml`
should prevent this, but if it still happens, just run
`docker compose restart backend` once Postgres's health check has passed
(`docker compose ps` shows `healthy`).

**I changed some Java/React code and nothing changed in the browser**
Docker images are a snapshot — you must rebuild after code changes:
```bash
docker compose up --build -d
```

**Starting fresh / something's really broken**
```bash
docker compose down -v
docker compose up --build -d
```
This wipes the database and uploaded files and starts completely clean.
