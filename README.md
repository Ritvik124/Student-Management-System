# 🎓 Student Management System
### Java + SQLite | MVC Architecture | Console Application

---

## 📁 Project Structure

```
StudentManagementSystem/
├── src/
│   └── com/sms/
│       ├── Main.java                    ← Entry point + demo seeder
│       ├── model/
│       │   ├── Student.java
│       │   ├── Course.java
│       │   └── Grade.java
│       ├── dao/                         ← All SQL queries (JDBC)
│       │   ├── StudentDAO.java
│       │   ├── CourseDAO.java
│       │   └── GradeDAO.java
│       ├── service/                     ← Business logic + validation
│       │   ├── StudentService.java
│       │   ├── CourseService.java
│       │   └── GradeService.java
│       ├── ui/
│       │   └── ConsoleUI.java           ← Full interactive menu
│       └── util/
│           ├── DatabaseConnection.java  ← SQLite connection + schema
│           └── ValidationUtil.java
├── /                                 ← Place sqlite-jdbc JAR here
├── out/                                 ← Compiled .class files (auto-created)
├── run.sh                               ← Build & run (Linux/macOS)
├── run.bat                              ← Build & run (Windows)
└── README.md
```

---

## ⚙️ Prerequisites

| Tool     | Version      |
|----------|-------------|
| Java JDK | 11 or higher |
| SQLite JDBC | 3.x (JAR) |

---

## 🚀 Quick Start

### Step 1 – Download required JARs
Download these files and place them in the `lib/` folder:
```
https://github.com/xerial/sqlite-jdbc/releases/download/3.45.0.0/sqlite-jdbc-3.45.0.0.jar
https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.16/slf4j-api-2.0.16.jar
```

The SQLite JDBC driver needs the SLF4J API at runtime.

### Step 2 – Run

**Linux / macOS:**
```bash
chmod +x run.sh
./run.sh
```

**Windows:**
```
run.bat
```

**Manual (any OS):**
```bash
# Compile
javac -cp "lib/sqlite-jdbc-3.45.0.0.jar" -d out $(find src -name "*.java")

# Run
java -cp "out:lib/sqlite-jdbc-3.45.0.0.jar:lib/slf4j-api-2.0.16.jar" com.sms.Main
# Windows: use semicolons:  out;lib/sqlite-jdbc-3.45.0.0.jar;lib/slf4j-api-2.0.16.jar
```

The database file `sms.db` is created automatically in the working directory.
Demo data (6 students, 5 courses, 14 grades) is auto-seeded on first launch.

---

## 🎮 Features

### Student Management
- ✅ Add student with full validation (email, phone, CGPA, year)
- ✅ View all students in a formatted table
- ✅ Search by name, roll, email, or department
- ✅ Update any field (blank = keep current)
- ✅ Delete with confirmation
- ✅ Filter by department

### Course Management
- ✅ Add / Update / Delete courses
- ✅ View all courses in a formatted table

### Grade Management
- ✅ Assign grades by roll number + course code
- ✅ Automatic letter-grade calculation (A+/A/B+/B/C+/C/F)
- ✅ View all grades / by student / by course
- ✅ Update and delete grades

### Reports
- ✅ Summary statistics (total students/courses/grades, avg CGPA)
- ✅ Top 10 students by CGPA
- ✅ Department-wise student count

---

## 🗄️ Database Schema (SQLite)

```sql
CREATE TABLE students (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    roll_number TEXT UNIQUE NOT NULL,
    first_name  TEXT NOT NULL,
    last_name   TEXT NOT NULL,
    email       TEXT UNIQUE NOT NULL,
    phone       TEXT,
    department  TEXT NOT NULL,
    year        INTEGER NOT NULL,
    cgpa        REAL DEFAULT 0.0,
    address     TEXT,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE courses (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    course_code TEXT UNIQUE NOT NULL,
    course_name TEXT NOT NULL,
    instructor  TEXT NOT NULL,
    credits     INTEGER NOT NULL,
    department  TEXT NOT NULL,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE grades (
    id             INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id     INTEGER NOT NULL REFERENCES students(id),
    course_id      INTEGER NOT NULL REFERENCES courses(id),
    marks_obtained REAL NOT NULL,
    total_marks    REAL NOT NULL,
    grade          TEXT NOT NULL,
    semester       TEXT NOT NULL,
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (student_id, course_id, semester)
);
```

---

## 🏗️ Architecture

```
UI Layer (ConsoleUI)
       │
       ▼
Service Layer (StudentService / CourseService / GradeService)
  Validation, business rules
       │
       ▼
DAO Layer (StudentDAO / CourseDAO / GradeDAO)
  All SQL queries via JDBC PreparedStatements
       │
       ▼
DatabaseConnection (SQLite via sqlite-jdbc)
```

---

## 📝 Grade Scale

| Percentage | Grade |
|-----------|-------|
| ≥ 90%     | A+    |
| ≥ 80%     | A     |
| ≥ 70%     | B+    |
| ≥ 60%     | B     |
| ≥ 50%     | C+    |
| ≥ 40%     | C     |
| < 40%     | F     |

---

## 🔐 Validation Rules

- Roll number: unique, required
- Email: valid format, unique
- Phone: 7-15 digits (optional)
- Year: 1–6
- CGPA: 0.0–10.0
- Credits: 1–6
- Marks: 0 ≤ marks ≤ total

---

*Built with Java 11+ and SQLite (zero external dependencies except the JDBC driver)*
(base) rohit@LG ~ % cd /Users/rohit/Desktop/GATE\ 2027/StudentManagementSystem && chmod +x run.sh && ./run.sh ONLY THIS IS HELP TO RUN THIS

