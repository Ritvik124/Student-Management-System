package com.sms.model;

public class Course {
    private int    id;
    private String courseCode;
    private String courseName;
    private String instructor;
    private int    credits;
    private String department;

    public Course() {}

    public Course(String courseCode, String courseName,
                  String instructor, int credits, String department) {
        this.courseCode  = courseCode;
        this.courseName  = courseName;
        this.instructor  = instructor;
        this.credits     = credits;
        this.department  = department;
    }

    public int    getId()          { return id; }
    public String getCourseCode()  { return courseCode; }
    public String getCourseName()  { return courseName; }
    public String getInstructor()  { return instructor; }
    public int    getCredits()     { return credits; }
    public String getDepartment()  { return department; }

    public void setId(int id)                  { this.id = id; }
    public void setCourseCode(String c)        { this.courseCode = c; }
    public void setCourseName(String c)        { this.courseName = c; }
    public void setInstructor(String i)        { this.instructor = i; }
    public void setCredits(int c)              { this.credits = c; }
    public void setDepartment(String d)        { this.department = d; }

    @Override
    public String toString() {
        return String.format("Course{id=%d, code='%s', name='%s', instructor='%s', credits=%d}",
            id, courseCode, courseName, instructor, credits);
    }
}
