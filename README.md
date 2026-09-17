# 🎓 Student Management System

### Java Javalin REST API | Vanilla JS Frontend | PostgreSQL

---

## 🚀 Live Demo

- **Frontend:** [Netlify Deployment URL here]
- **Backend:** [Render Deployment URL here]

---

## 🎮 Features
- **Student Management:**
  - Add student
  - Update student
  - Delete student
  - Search/filter
- **Course Management:**
  - Add / Update / Delete courses
- **Grade Management:**
  - Add / Update / Delete grades
- **Database Integration:** SQLite (local) / PostgreSQL (production)

---

## 🛠️ Technology Stack
- **Backend:** Java 11, Javalin (REST API), JDBC
- **Frontend:** HTML, CSS, Vanilla JavaScript
- **Database:** PostgreSQL (Production) / SQLite (Local)
- **Build Tool:** Maven, Docker

---

## 💻 Local Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Ritvik124/Student-Management-System.git
   cd Student-Management-System
   ```

2. **Run Backend (Local)**
   Using Maven:
   ```bash
   mvn clean package
   java -jar target/student-management-system-1.0-SNAPSHOT.jar
   ```
   *The backend will run on `http://localhost:8080`. It uses a local SQLite database (`sms.db`) by default.*

3. **Run Frontend (Local)**
   Open `frontend/index.html` in your browser. 
   *(Alternatively, use a local server like `npx serve frontend`)*

---

## 🔐 Environment Variables

For production deployment, configure the following environment variables:

| Variable       | Description                                      | Example                           |
|----------------|--------------------------------------------------|-----------------------------------|
| `DATABASE_URL` | PostgreSQL connection string provided by Render | `postgres://user:pass@host/db`    |
| `PORT`         | Port for the backend server                      | `8080`                            |
| `FRONTEND_URL` | The deployed URL of your Netlify frontend       | `https://my-frontend.netlify.app` |

---

## 🌍 Deployment

### 1. Render Backend Deployment
This repository is configured for 1-click deployment on Render.
1. Connect this repository to Render.
2. Render will automatically detect `render.yaml` and deploy a Web Service (Docker) and a PostgreSQL database.
3. Configure `FRONTEND_URL` in the Render dashboard once Netlify is deployed.

### 2. Netlify Frontend Deployment
This repository is configured for Netlify.
1. Connect this repository to Netlify.
2. Set the Base directory to `frontend/`.
3. Set the Publish directory to `frontend/` (or leave blank if Netlify detects it).
4. Add the `API_BASE_URL` environment variable pointing to your deployed Render URL.

---

## 📊 Deployment Status

- **Frontend:** PENDING 
- **Backend:** PENDING
- **Database:** PENDING

*(Update status to LIVE and CONNECTED once you deploy on your accounts)*
