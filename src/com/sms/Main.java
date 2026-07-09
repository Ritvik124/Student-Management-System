package com.sms;

import com.sms.model.*;
import com.sms.service.*;
import com.sms.ui.ConsoleUI;
import com.sms.util.DatabaseConnection;

public class Main {

    public static void main(String[] args) {
        // 1. Init DB
        DatabaseConnection.initializeDatabase();

        // 2. Seed demo data if DB is fresh
        seedDemoData();

        // 3. Launch interactive UI
        new ConsoleUI().start();

        // 4. Close connection on exit
        DatabaseConnection.closeConnection();
    }

    /** Inserts a handful of records so the app is immediately usable. */
    private static void seedDemoData() {
        StudentService ss = new StudentService();
        CourseService  cs = new CourseService();
        GradeService   gs = new GradeService();

        // Only seed if empty
        if (ss.getTotalStudents() > 0) return;

        System.out.println("[SEED] Inserting demo data...");

        // ── Students ────────────────────────────────────────────────────────
        String[][] students = {
            {"CS2024001","Alice","Johnson","alice@uni.edu","9876543210","Computer Science","2","8.75","123 Main St"},
            {"CS2024002","Bob","Smith","bob@uni.edu","9876543211","Computer Science","2","7.80","456 Oak Ave"},
            {"EC2024001","Carol","White","carol@uni.edu","9876543212","Electronics","3","9.10","789 Pine Rd"},
            {"ME2024001","David","Brown","david@uni.edu","9876543213","Mechanical","1","7.50","321 Elm St"},
            {"CS2024003","Eva","Davis","eva@uni.edu","9876543214","Computer Science","2","8.20","654 Maple Dr"},
            {"EC2024002","Frank","Miller","frank@uni.edu","9876543215","Electronics","1","6.90","987 Cedar Ln"},
        };
        for (String[] d : students) {
            Student s = new Student(d[0],d[1],d[2],d[3],d[4],d[5],
                Integer.parseInt(d[6]), Double.parseDouble(d[7]), d[8]);
            ss.addStudent(s);
        }

        // ── Courses ─────────────────────────────────────────────────────────
        Object[][] courses = {
            {"CS101","Data Structures","Dr. Alan Turing",4,"Computer Science"},
            {"CS102","Algorithms","Dr. Ada Lovelace",4,"Computer Science"},
            {"EC101","Digital Electronics","Dr. Nikola Tesla",3,"Electronics"},
            {"ME101","Thermodynamics","Dr. James Watt",3,"Mechanical"},
            {"MA101","Engineering Mathematics","Dr. Euler",4,"Mathematics"},
        };
        for (Object[] d : courses) {
            Course c = new Course((String)d[0],(String)d[1],(String)d[2],
                (int)d[3],(String)d[4]);
            cs.addCourse(c);
        }

        // ── Grades ──────────────────────────────────────────────────────────
        // studentId(1-6) courseId(1-5) marks total semester
        int[][] gradeData = {
            {1,1,88,100},{1,2,92,100},{1,5,85,100},
            {2,1,72,100},{2,2,68,100},{2,5,75,100},
            {3,3,95,100},{3,5,88,100},
            {4,4,78,100},{4,5,70,100},
            {5,1,84,100},{5,2,90,100},
            {6,3,65,100},{6,5,60,100},
        };
        for (int[] d : gradeData) {
            Grade g = new Grade(d[0],d[1],d[2],d[3],"","S1-2024");
            gs.addGrade(g);
        }
        System.out.println("[SEED] Demo data inserted.\n");
    }
}
