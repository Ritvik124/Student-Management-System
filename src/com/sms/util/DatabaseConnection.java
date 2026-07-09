package com.sms.util;

import java.sql.*;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:sqlite:sms.db";
    private static Connection connection = null;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DB_URL);
                connection.setAutoCommit(true);
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQLite JDBC driver not found: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    /** Create all tables if they don't exist. */
    public static void initializeDatabase() {
        String students = """
            CREATE TABLE IF NOT EXISTS students (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                roll_number TEXT    UNIQUE NOT NULL,
                first_name  TEXT    NOT NULL,
                last_name   TEXT    NOT NULL,
                email       TEXT    UNIQUE NOT NULL,
                phone       TEXT,
                department  TEXT    NOT NULL,
                year        INTEGER NOT NULL,
                cgpa        REAL    DEFAULT 0.0,
                address     TEXT,
                created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
            )""";

        String courses = """
            CREATE TABLE IF NOT EXISTS courses (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                course_code TEXT    UNIQUE NOT NULL,
                course_name TEXT    NOT NULL,
                instructor  TEXT    NOT NULL,
                credits     INTEGER NOT NULL,
                department  TEXT    NOT NULL,
                created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
            )""";

        String grades = """
            CREATE TABLE IF NOT EXISTS grades (
                id             INTEGER PRIMARY KEY AUTOINCREMENT,
                student_id     INTEGER NOT NULL,
                course_id      INTEGER NOT NULL,
                marks_obtained REAL    NOT NULL,
                total_marks    REAL    NOT NULL,
                grade          TEXT    NOT NULL,
                semester       TEXT    NOT NULL,
                created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (student_id) REFERENCES students(id),
                FOREIGN KEY (course_id)  REFERENCES courses(id),
                UNIQUE (student_id, course_id, semester)
            )""";

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
}
