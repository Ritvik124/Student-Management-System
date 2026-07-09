package com.sms.model;

public class Student {
    private int id;
    private String rollNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String department;
    private int year;
    private double cgpa;
    private String address;

    public Student() {}

    public Student(String rollNumber, String firstName, String lastName,
                   String email, String phone, String department, int year,
                   double cgpa, String address) {
        this.rollNumber = rollNumber;
        this.firstName  = firstName;
        this.lastName   = lastName;
        this.email      = email;
        this.phone      = phone;
        this.department = department;
        this.year       = year;
        this.cgpa       = cgpa;
        this.address    = address;
    }

    // Getters
    public int    getId()         { return id; }
    public String getRollNumber() { return rollNumber; }
    public String getFirstName()  { return firstName; }
    public String getLastName()   { return lastName; }
    public String getEmail()      { return email; }
    public String getPhone()      { return phone; }
    public String getDepartment() { return department; }
    public int    getYear()       { return year; }
    public double getCgpa()       { return cgpa; }
    public String getAddress()    { return address; }

    // Setters
    public void setId(int id)               { this.id = id; }
    public void setRollNumber(String r)     { this.rollNumber = r; }
    public void setFirstName(String fn)     { this.firstName = fn; }
    public void setLastName(String ln)      { this.lastName = ln; }
    public void setEmail(String e)          { this.email = e; }
    public void setPhone(String p)          { this.phone = p; }
    public void setDepartment(String d)     { this.department = d; }
    public void setYear(int y)              { this.year = y; }
    public void setCgpa(double c)           { this.cgpa = c; }
    public void setAddress(String a)        { this.address = a; }

    public String getFullName() { return firstName + " " + lastName; }

    @Override
    public String toString() {
        return String.format(
            "Student{id=%d, roll='%s', name='%s %s', dept='%s', year=%d, cgpa=%.2f}",
            id, rollNumber, firstName, lastName, department, year, cgpa);
    }
}
