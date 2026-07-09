package com.sms.service;

import com.sms.dao.CourseDAO;
import com.sms.model.Course;
import com.sms.util.ValidationUtil;

import java.util.List;

public class CourseService {

    private final CourseDAO dao = new CourseDAO();

    public String addCourse(Course c) {
        if (!ValidationUtil.isNotBlank(c.getCourseCode()))  return "Course code is required.";
        if (!ValidationUtil.isNotBlank(c.getCourseName()))  return "Course name is required.";
        if (!ValidationUtil.isNotBlank(c.getInstructor()))  return "Instructor is required.";
        if (!ValidationUtil.isNotBlank(c.getDepartment()))  return "Department is required.";
        if (c.getCredits() < 1 || c.getCredits() > 6)      return "Credits must be 1-6.";
        if (dao.getCourseByCode(c.getCourseCode()) != null) return "Course code already exists.";
        return dao.addCourse(c) ? "SUCCESS" : "Failed to add course.";
    }

    public String updateCourse(Course c) {
        if (!ValidationUtil.isNotBlank(c.getCourseName()))  return "Course name is required.";
        if (!ValidationUtil.isNotBlank(c.getInstructor()))  return "Instructor is required.";
        if (!ValidationUtil.isNotBlank(c.getDepartment()))  return "Department is required.";
        if (c.getCredits() < 1 || c.getCredits() > 6)      return "Credits must be 1-6.";
        return dao.updateCourse(c) ? "SUCCESS" : "Failed to update course.";
    }

    public String deleteCourse(int id) {
        return dao.deleteCourse(id) ? "SUCCESS" : "Course not found.";
    }

    public List<Course> getAllCourses()              { return dao.getAllCourses(); }
    public Course       getCourseById(int id)        { return dao.getCourseById(id); }
    public Course       getCourseByCode(String code) { return dao.getCourseByCode(code); }
    public int          getTotalCourses()            { return dao.getTotalCount(); }
}
