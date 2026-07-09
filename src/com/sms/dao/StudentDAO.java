package com.sms.dao;

import com.sms.model.Student;
import com.sms.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // ── INSERT ──────────────────────────────────────────────────────────────
    public boolean addStudent(Student s) {
        String sql = """
            INSERT INTO students
              (roll_number, first_name, last_name, email, phone,
               department, year, cgpa, address)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)""";
        try (Connection c  = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getRollNumber());
            ps.setString(2, s.getFirstName());
            ps.setString(3, s.getLastName());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getPhone());
            ps.setString(6, s.getDepartment());
            ps.setInt   (7, s.getYear());
            ps.setDouble(8, s.getCgpa());
            ps.setString(9, s.getAddress());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Add student error: " + e.getMessage());
            return false;
        }
    }

    // ── SELECT ALL ──────────────────────────────────────────────────────────
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY roll_number";
        try (Connection c = DatabaseConnection.getConnection();
             Statement  st = c.createStatement();
             ResultSet  rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.err.println("Get all students error: " + e.getMessage());
        }
        return list;
    }

    // ── SELECT BY ID ────────────────────────────────────────────────────────
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection c  = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            System.err.println("Get by id error: " + e.getMessage());
        }
        return null;
    }

    // ── SELECT BY ROLL ──────────────────────────────────────────────────────
    public Student getStudentByRoll(String roll) {
        String sql = "SELECT * FROM students WHERE roll_number = ?";
        try (Connection c  = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, roll);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            System.err.println("Get by roll error: " + e.getMessage());
        }
        return null;
    }

    // ── SEARCH ──────────────────────────────────────────────────────────────
    public List<Student> searchStudents(String keyword) {
        List<Student> list = new ArrayList<>();
        String sql = """
            SELECT * FROM students
            WHERE first_name  LIKE ? OR last_name   LIKE ?
               OR roll_number LIKE ? OR email       LIKE ?
               OR department  LIKE ?
            ORDER BY roll_number""";
        String kw = "%" + keyword + "%";
        try (Connection c  = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) ps.setString(i, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.err.println("Search error: " + e.getMessage());
        }
        return list;
    }

    // ── SEARCH BY DEPARTMENT ────────────────────────────────────────────────
    public List<Student> getStudentsByDepartment(String dept) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE department = ? ORDER BY roll_number";
        try (Connection c  = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dept);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.err.println("Dept query error: " + e.getMessage());
        }
        return list;
    }

    // ── UPDATE ──────────────────────────────────────────────────────────────
    public boolean updateStudent(Student s) {
        String sql = """
            UPDATE students SET
              first_name=?, last_name=?, email=?, phone=?,
              department=?, year=?, cgpa=?, address=?
            WHERE id=?""";
        try (Connection c  = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getFirstName());
            ps.setString(2, s.getLastName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getDepartment());
            ps.setInt   (6, s.getYear());
            ps.setDouble(7, s.getCgpa());
            ps.setString(8, s.getAddress());
            ps.setInt   (9, s.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update error: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ──────────────────────────────────────────────────────────────
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection c  = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete error: " + e.getMessage());
            return false;
        }
    }

    // ── COUNT ───────────────────────────────────────────────────────────────
    public int getTotalCount() {
        try (Connection c = DatabaseConnection.getConnection();
             Statement  st = c.createStatement();
             ResultSet  rs = st.executeQuery("SELECT COUNT(*) FROM students")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    // ── MAPPER ──────────────────────────────────────────────────────────────
    private Student map(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId        (rs.getInt   ("id"));
        s.setRollNumber(rs.getString("roll_number"));
        s.setFirstName (rs.getString("first_name"));
        s.setLastName  (rs.getString("last_name"));
        s.setEmail     (rs.getString("email"));
        s.setPhone     (rs.getString("phone"));
        s.setDepartment(rs.getString("department"));
        s.setYear      (rs.getInt   ("year"));
        s.setCgpa      (rs.getDouble("cgpa"));
        s.setAddress   (rs.getString("address"));
        return s;
    }
}
