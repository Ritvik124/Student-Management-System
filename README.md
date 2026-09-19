# BCREC STUDENT PORTAL

Student Management System — a Java 17, JDBC and MySQL student-record portal. Made by Ritvik Raj.

## Features

- Create, view, edit, delete and search student records
- Validated student ID, email, phone, semester and CGPA inputs
- Server-calculated student, course and average-CGPA dashboard statistics
- Responsive vanilla HTML/CSS/JavaScript frontend
- Prepared statements, environment-only database credentials and restricted CORS

## Structure

```text
src/com/sms/       Java/Javalin API, services, JDBC DAOs and models
frontend/          Netlify-ready static portal
render.yaml        Render web-service blueprint
netlify.toml       Netlify API proxy configuration
```

## Local setup

Requirements: Java 17+ and Maven. The project uses `sms.db` (SQLite) for a zero-config local demo; it is ignored by Git. To use MySQL locally or in production, define the MySQL variables below.

```bash
mvn clean package
PORT=8080 java -jar target/student-management-system-1.0-SNAPSHOT.jar
```

Serve `frontend/` with any static web server on port 5500. The frontend automatically calls `http://localhost:8080` during local development.

## Environment variables

| Variable | Required in production | Purpose |
| --- | --- | --- |
| `PORT` | Render supplies it | API listener port; defaults to `8080` |
| `DB_HOST` | Yes | MySQL hostname |
| `DB_PORT` | Yes | MySQL port, normally `3306` |
| `DB_NAME` | Yes | Database name |
| `DB_USER` | Yes | MySQL user |
| `DB_PASSWORD` | Yes | MySQL password |
| `FRONTEND_URLS` | Yes | Comma-separated allowed Netlify and local origins |
| `API_BASE_URL` | Netlify only | HTTPS URL of the Render API, used by the Netlify proxy |

Never commit a `.env` file or production secret. Copy `.env.example` for local reference only.

## Deployment architecture

`Browser → Netlify static frontend → Netlify /api proxy → Render Java API → MySQL`

1. Push this repository to GitHub.
2. In Render, create a **Web Service** from the repository. It uses the included Dockerfile and `render.yaml`. Add the five `DB_*` variables and set `FRONTEND_URLS` to the final Netlify origin.
3. Provision a reachable managed MySQL instance, create the database, and apply the application’s automatically created tables on first startup. Render does not provision MySQL through this blueprint; use your approved MySQL-compatible provider.
4. In Netlify, create a site using this repository. The `netlify.toml` publishes `frontend/`. Set `API_BASE_URL` to the final Render URL, for example `https://your-service.onrender.com` (without a trailing slash), then redeploy.
5. Verify `https://your-service.onrender.com/health`, then test create, refresh/read, edit, delete and search from the Netlify site.

## API

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/health` | Deployment health check |
| `GET` | `/api/stats` | Dashboard values |
| `GET` | `/api/students?q=...` | List or search students |
| `POST` | `/api/students` | Create a student |
| `GET` | `/api/students/{id}` | Read a student |
| `PUT` | `/api/students/{id}` | Update a student |
| `DELETE` | `/api/students/{id}` | Delete a student |

## Live URLs

Deployment URLs intentionally are not committed because no Render, Netlify, or MySQL credentials were supplied. Add the actual URLs here after deployment and live CRUD verification.
