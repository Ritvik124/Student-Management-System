package com.sms.model;

public class Grade {
    private int    id;
    private int    studentId;
    private int    courseId;
    private double marksObtained;
    private double totalMarks;
    private String grade;
    private String semester;

    // for display
    private String studentName;
    private String courseCode;
    private String courseName;

    public Grade() {}

    public Grade(int studentId, int courseId,
                 double marksObtained, double totalMarks,
                 String grade, String semester) {
        this.studentId     = studentId;
        this.courseId      = courseId;
        this.marksObtained = marksObtained;
        this.totalMarks    = totalMarks;
        this.grade         = grade;
        this.semester      = semester;
    }

    public int    getId()             { return id; }
    public int    getStudentId()      { return studentId; }
    public int    getCourseId()       { return courseId; }
    public double getMarksObtained()  { return marksObtained; }
    public double getTotalMarks()     { return totalMarks; }
    public String getGrade()          { return grade; }
    public String getSemester()       { return semester; }
    public String getStudentName()    { return studentName; }
    public String getCourseCode()     { return courseCode; }
    public String getCourseName()     { return courseName; }

    public void setId(int id)                       { this.id = id; }
    public void setStudentId(int s)                 { this.studentId = s; }
    public void setCourseId(int c)                  { this.courseId = c; }
    public void setMarksObtained(double m)          { this.marksObtained = m; }
    public void setTotalMarks(double t)             { this.totalMarks = t; }
    public void setGrade(String g)                  { this.grade = g; }
    public void setSemester(String s)               { this.semester = s; }
    public void setStudentName(String sn)           { this.studentName = sn; }
    public void setCourseCode(String cc)            { this.courseCode = cc; }
    public void setCourseName(String cn)            { this.courseName = cn; }

    public double getPercentage() {
        return totalMarks > 0 ? (marksObtained / totalMarks) * 100 : 0;
    }

    @Override
    public String toString() {
        return String.format(
            "Grade{student='%s', course='%s', marks=%.1f/%.1f, grade='%s', sem='%s'}",
            studentName, courseName, marksObtained, totalMarks, grade, semester);
    }
}
