package com.sms.service;

import com.sms.dao.StudentDAO;
import com.sms.model.Student;
import com.sms.util.ValidationUtil;

import java.util.List;

public class StudentService {

    private final StudentDAO dao = new StudentDAO();

    public String addStudent(Student s) {
        if (!ValidationUtil.isNotBlank(s.getRollNumber())) return "Roll number is required.";
        if (!ValidationUtil.isNotBlank(s.getFirstName()))  return "First name is required.";
        if (!ValidationUtil.isNotBlank(s.getLastName()))   return "Last name is required.";
        if (!ValidationUtil.isValidEmail(s.getEmail()))    return "Invalid email address.";
        if (!ValidationUtil.isValidPhone(s.getPhone()))    return "Invalid phone number.";
        if (!ValidationUtil.isNotBlank(s.getDepartment())) return "Department is required.";
        if (!ValidationUtil.isValidYear(s.getYear()))      return "Year must be 1-6.";
        if (!ValidationUtil.isValidCgpa(s.getCgpa()))      return "CGPA must be 0.0 – 10.0.";
        if (dao.getStudentByRoll(s.getRollNumber()) != null)
            return "Roll number already exists.";
        return dao.addStudent(s) ? "SUCCESS" : "Failed to add student.";
    }

    public String updateStudent(Student s) {
        if (!ValidationUtil.isNotBlank(s.getFirstName()))  return "First name is required.";
        if (!ValidationUtil.isNotBlank(s.getLastName()))   return "Last name is required.";
        if (!ValidationUtil.isValidEmail(s.getEmail()))    return "Invalid email address.";
        if (!ValidationUtil.isValidPhone(s.getPhone()))    return "Invalid phone number.";
        if (!ValidationUtil.isNotBlank(s.getDepartment())) return "Department is required.";
        if (!ValidationUtil.isValidYear(s.getYear()))      return "Year must be 1-6.";
        if (!ValidationUtil.isValidCgpa(s.getCgpa()))      return "CGPA must be 0.0 – 10.0.";
        return dao.updateStudent(s) ? "SUCCESS" : "Failed to update student.";
    }

    public String deleteStudent(int id) {
        return dao.deleteStudent(id) ? "SUCCESS" : "Student not found.";
    }

    public List<Student> getAllStudents()                   { return dao.getAllStudents(); }
    public Student       getStudentById(int id)            { return dao.getStudentById(id); }
    public Student       getStudentByRoll(String roll)     { return dao.getStudentByRoll(roll); }
    public List<Student> searchStudents(String kw)         { return dao.searchStudents(kw); }
    public List<Student> getStudentsByDept(String dept)    { return dao.getStudentsByDepartment(dept); }
    public int           getTotalStudents()                { return dao.getTotalCount(); }
}
