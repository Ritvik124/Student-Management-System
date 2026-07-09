package com.sms.dao;

import com.sms.model.Grade;
import com.sms.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {

    public boolean addGrade(Grade g) {
        String sql = """
            INSERT INTO grades
              (student_id, course_id, marks_obtained, total_marks, grade, semester)
            VALUES (?, ?, ?, ?, ?, ?)""";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, g.getStudentId());
            ps.setInt   (2, g.getCourseId());
            ps.setDouble(3, g.getMarksObtained());
            ps.setDouble(4, g.getTotalMarks());
            ps.setString(5, g.getGrade());
            ps.setString(6, g.getSemester());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Add grade error: " + e.getMessage());
            return false;
        }
    }

    public List<Grade> getAllGrades() {
        List<Grade> list = new ArrayList<>();
        String sql = """
            SELECT g.*,
                   s.first_name || ' ' || s.last_name AS student_name,
                   c.course_code, c.course_name
            FROM grades g
            JOIN students s ON g.student_id = s.id
            JOIN courses  c ON g.course_id  = c.id
            ORDER BY s.roll_number, c.course_code""";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement  st   = conn.createStatement();
             ResultSet  rs   = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapFull(rs));
        } catch (SQLException e) {
            System.err.println("Get all grades error: " + e.getMessage());
        }
        return list;
    }

    public List<Grade> getGradesByStudent(int studentId) {
        List<Grade> list = new ArrayList<>();
        String sql = """
            SELECT g.*,
                   s.first_name || ' ' || s.last_name AS student_name,
                   c.course_code, c.course_name
            FROM grades g
            JOIN students s ON g.student_id = s.id
            JOIN courses  c ON g.course_id  = c.id
            WHERE g.student_id = ?
            ORDER BY g.semester, c.course_code""";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapFull(rs));
        } catch (SQLException e) {
            System.err.println("Get grades by student error: " + e.getMessage());
        }
        return list;
    }

    public List<Grade> getGradesByCourse(int courseId) {
        List<Grade> list = new ArrayList<>();
        String sql = """
            SELECT g.*,
                   s.first_name || ' ' || s.last_name AS student_name,
                   c.course_code, c.course_name
            FROM grades g
            JOIN students s ON g.student_id = s.id
            JOIN courses  c ON g.course_id  = c.id
            WHERE g.course_id = ?
            ORDER BY s.roll_number""";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapFull(rs));
        } catch (SQLException e) {
            System.err.println("Get grades by course error: " + e.getMessage());
        }
        return list;
    }

    public boolean updateGrade(Grade g) {
        String sql = """
            UPDATE grades SET
              marks_obtained=?, total_marks=?, grade=?, semester=?
            WHERE id=?""";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, g.getMarksObtained());
            ps.setDouble(2, g.getTotalMarks());
            ps.setString(3, g.getGrade());
            ps.setString(4, g.getSemester());
            ps.setInt   (5, g.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update grade error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteGrade(int id) {
        String sql = "DELETE FROM grades WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete grade error: " + e.getMessage());
            return false;
        }
    }

    public double getAverageCgpa() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement  st   = conn.createStatement();
             ResultSet  rs   = st.executeQuery("SELECT AVG(cgpa) FROM students")) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        } catch (SQLException e) {
            return 0.0;
        }
    }

    private Grade mapFull(ResultSet rs) throws SQLException {
        Grade g = new Grade();
        g.setId           (rs.getInt   ("id"));
        g.setStudentId    (rs.getInt   ("student_id"));
        g.setCourseId     (rs.getInt   ("course_id"));
        g.setMarksObtained(rs.getDouble("marks_obtained"));
        g.setTotalMarks   (rs.getDouble("total_marks"));
        g.setGrade        (rs.getString("grade"));
        g.setSemester     (rs.getString("semester"));
        g.setStudentName  (rs.getString("student_name"));
        g.setCourseCode   (rs.getString("course_code"));
        g.setCourseName   (rs.getString("course_name"));
        return g;
    }
}
