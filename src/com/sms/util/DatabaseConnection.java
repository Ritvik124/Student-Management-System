package com.sms.util;

import java.sql.*;

public class DatabaseConnection {
    private static Connection connection = null;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String dbUrl = System.getenv("DATABASE_URL");
            
            if (dbUrl != null && !dbUrl.isEmpty()) {
                // If on render, DATABASE_URL looks like postgres://user:pass@host:port/dbname
                // JDBC needs jdbc:postgresql://host:port/dbname?user=user&password=pass
                try {
                    if (dbUrl.startsWith("postgres://")) {
                        dbUrl = dbUrl.replace("postgres://", "jdbc:postgresql://");
                        String[] parts = dbUrl.split("@");
                        String credentials = parts[0].replace("jdbc:postgresql://", "");
                        String hostAndDb = parts[1];
                        String[] creds = credentials.split(":");
                        String user = creds[0];
                        String pass = creds.length > 1 ? creds[1] : "";
                        dbUrl = "jdbc:postgresql://" + hostAndDb + "?user=" + user + "&password=" + pass + "&sslmode=require";
                    }
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(dbUrl);
                } catch (Exception e) {
                    throw new SQLException("Error connecting to PostgreSQL: " + e.getMessage());
                }
            } else {
                try {
                    Class.forName("org.sqlite.JDBC");
                    connection = DriverManager.getConnection("jdbc:sqlite:sms.db");
                } catch (ClassNotFoundException e) {
                    throw new SQLException("SQLite JDBC driver not found: " + e.getMessage());
                }
            }
            connection.setAutoCommit(true);
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

    public static void initializeDatabase() {
        boolean isPostgres = System.getenv("DATABASE_URL") != null && !System.getenv("DATABASE_URL").isEmpty();
        String primaryKeyType = isPostgres ? "SERIAL PRIMARY KEY" : "INTEGER PRIMARY KEY AUTOINCREMENT";

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
}
