package com.sms.util;

import java.sql.*;

public class DatabaseConnection {
    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        String host = System.getenv("DB_HOST");
        if (host != null && !host.isBlank()) {
            String port = valueOrDefault("DB_PORT", "3306");
            String name = required("DB_NAME");
            String user = required("DB_USER");
            String password = required("DB_PASSWORD");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://" + host + ":" + port + "/" + name
                        + "?useSSL=true&requireSSL=true&serverTimezone=UTC";
                return DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC driver is unavailable.", e);
            }
        }

        // Keeps existing Render/PostgreSQL deployments compatible while MySQL is preferred
        // whenever DB_HOST/DB_NAME/DB_USER/DB_PASSWORD are provided.
        String dbUrl = System.getenv("DATABASE_URL");
        if (dbUrl != null && !dbUrl.isBlank()) {
            try {
                Class.forName("org.postgresql.Driver");
                if (dbUrl.startsWith("postgres://")) dbUrl = "jdbc:postgresql://" + dbUrl.substring(11);
                if (dbUrl.startsWith("postgresql://")) dbUrl = "jdbc:postgresql://" + dbUrl.substring(13);
                return DriverManager.getConnection(dbUrl);
            } catch (ClassNotFoundException e) {
                throw new SQLException("PostgreSQL JDBC driver is unavailable.", e);
            }
        }

        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:sms.db");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver is unavailable.", e);
        }
    }

    public static void closeConnection() {
        // DAO methods use try-with-resources; retained for ConsoleUI compatibility.
    }

    public static void initializeDatabase() {
        boolean isMysql = System.getenv("DB_HOST") != null && !System.getenv("DB_HOST").isBlank();
        boolean isPostgres = !isMysql && System.getenv("DATABASE_URL") != null && !System.getenv("DATABASE_URL").isBlank();
        String primaryKeyType = isMysql ? "INTEGER AUTO_INCREMENT PRIMARY KEY"
                : (isPostgres ? "SERIAL PRIMARY KEY" : "INTEGER PRIMARY KEY AUTOINCREMENT");

        String students = "CREATE TABLE IF NOT EXISTS students (" +
                "id          " + primaryKeyType + "," +
                "roll_number VARCHAR(255) UNIQUE NOT NULL," +
                "first_name  VARCHAR(255) NOT NULL," +
                "last_name   VARCHAR(255) NOT NULL," +
                "email       VARCHAR(255) UNIQUE NOT NULL," +
                "phone       VARCHAR(50)," +
                "department  VARCHAR(255) NOT NULL," +
                "year        INTEGER NOT NULL," +
                "cgpa        REAL DEFAULT 0.0," +
                "address     TEXT," +
                "created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        String courses = "CREATE TABLE IF NOT EXISTS courses (" +
                "id          " + primaryKeyType + "," +
                "course_code VARCHAR(255) UNIQUE NOT NULL," +
                "course_name VARCHAR(255) NOT NULL," +
                "instructor  VARCHAR(255) NOT NULL," +
                "credits     INTEGER NOT NULL," +
                "department  VARCHAR(255) NOT NULL," +
                "created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        String grades = "CREATE TABLE IF NOT EXISTS grades (" +
                "id             " + primaryKeyType + "," +
                "student_id     INTEGER NOT NULL," +
                "course_id      INTEGER NOT NULL," +
                "marks_obtained REAL NOT NULL," +
                "total_marks    REAL NOT NULL," +
                "grade          VARCHAR(10) NOT NULL," +
                "semester       VARCHAR(50) NOT NULL," +
                "created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (student_id) REFERENCES students(id)," +
                "FOREIGN KEY (course_id)  REFERENCES courses(id)," +
                "UNIQUE (student_id, course_id, semester)" +
                ")";

        try (Connection conn = getConnection();
             Statement  stmt = conn.createStatement()) {
            stmt.execute(students);
            stmt.execute(courses);
            stmt.execute(grades);
            System.out.println("[DB] Tables initialised successfully.");
        } catch (SQLException e) {
            System.err.println("[DB] Init error: " + e.getMessage());
        }
    }

    private static String required(String key) throws SQLException {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) throw new SQLException(key + " must be configured when DB_HOST is set.");
        return value;
    }

    private static String valueOrDefault(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }
}
