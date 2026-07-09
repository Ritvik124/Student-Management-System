package com.sms.dao;

import com.sms.model.Course;
import com.sms.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public boolean addCourse(Course c) {
        String sql = """
            INSERT INTO courses (course_code, course_name, instructor, credits, department)
            VALUES (?, ?, ?, ?, ?)""";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCourseCode());
            ps.setString(2, c.getCourseName());
            ps.setString(3, c.getInstructor());
            ps.setInt   (4, c.getCredits());
            ps.setString(5, c.getDepartment());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Add course error: " + e.getMessage());
            return false;
        }
    }

    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY course_code";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement  st   = conn.createStatement();
             ResultSet  rs   = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.err.println("Get all courses error: " + e.getMessage());
        }
        return list;
    }

    public Course getCourseById(int id) {
        String sql = "SELECT * FROM courses WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            System.err.println("Get course by id error: " + e.getMessage());
        }
        return null;
    }

    public Course getCourseByCode(String code) {
        String sql = "SELECT * FROM courses WHERE course_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            System.err.println("Get by code error: " + e.getMessage());
        }
        return null;
    }

    public boolean updateCourse(Course c) {
        String sql = """
            UPDATE courses SET
              course_name=?, instructor=?, credits=?, department=?
            WHERE id=?""";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCourseName());
            ps.setString(2, c.getInstructor());
            ps.setInt   (3, c.getCredits());
            ps.setString(4, c.getDepartment());
            ps.setInt   (5, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update course error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCourse(int id) {
        String sql = "DELETE FROM courses WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete course error: " + e.getMessage());
            return false;
        }
    }

    public int getTotalCount() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement  st   = conn.createStatement();
             ResultSet  rs   = st.executeQuery("SELECT COUNT(*) FROM courses")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    private Course map(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setId        (rs.getInt   ("id"));
        c.setCourseCode(rs.getString("course_code"));
        c.setCourseName(rs.getString("course_name"));
        c.setInstructor(rs.getString("instructor"));
        c.setCredits   (rs.getInt   ("credits"));
        c.setDepartment(rs.getString("department"));
        return c;
    }
}
