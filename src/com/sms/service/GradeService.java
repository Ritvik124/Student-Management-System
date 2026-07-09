package com.sms.service;

import com.sms.dao.GradeDAO;
import com.sms.model.Grade;
import com.sms.util.ValidationUtil;

import java.util.List;

public class GradeService {

    private final GradeDAO dao = new GradeDAO();

    public String addGrade(Grade g) {
        if (g.getStudentId() <= 0)             return "Invalid student.";
        if (g.getCourseId() <= 0)              return "Invalid course.";
        if (g.getMarksObtained() < 0)          return "Marks cannot be negative.";
        if (g.getTotalMarks() <= 0)            return "Total marks must be positive.";
        if (g.getMarksObtained() > g.getTotalMarks())
                                               return "Marks exceed total marks.";
        if (!ValidationUtil.isNotBlank(g.getSemester())) return "Semester is required.";
        // auto-calculate grade letter
        g.setGrade(ValidationUtil.calculateGrade(g.getMarksObtained(), g.getTotalMarks()));
        return dao.addGrade(g) ? "SUCCESS" : "Failed to add grade (duplicate entry?).";
    }

    public String updateGrade(Grade g) {
        if (g.getMarksObtained() < 0)          return "Marks cannot be negative.";
        if (g.getTotalMarks() <= 0)            return "Total marks must be positive.";
        if (g.getMarksObtained() > g.getTotalMarks())
                                               return "Marks exceed total marks.";
        g.setGrade(ValidationUtil.calculateGrade(g.getMarksObtained(), g.getTotalMarks()));
        return dao.updateGrade(g) ? "SUCCESS" : "Failed to update grade.";
    }

    public String deleteGrade(int id) {
        return dao.deleteGrade(id) ? "SUCCESS" : "Grade not found.";
    }

    public List<Grade> getAllGrades()                    { return dao.getAllGrades(); }
    public List<Grade> getGradesByStudent(int sid)      { return dao.getGradesByStudent(sid); }
    public List<Grade> getGradesByCourse(int cid)       { return dao.getGradesByCourse(cid); }
    public double      getAverageCgpa()                 { return dao.getAverageCgpa(); }
}
