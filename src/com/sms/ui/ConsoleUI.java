package com.sms.ui;

import com.sms.model.*;
import com.sms.service.*;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner        sc  = new Scanner(System.in);
    private final StudentService ss  = new StudentService();
    private final CourseService  cs  = new CourseService();
    private final GradeService   gs  = new GradeService();

    // ── ANSI colours ────────────────────────────────────────────────────────
    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";
    private static final String CYAN   = "\u001B[36m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED    = "\u001B[31m";
    private static final String BLUE   = "\u001B[34m";

    // ════════════════════════════════════════════════════════════════════════
    public void start() {
        printBanner();
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1  -> studentMenu();
                case 2  -> courseMenu();
                case 3  -> gradeMenu();
                case 4  -> reportsMenu();
                case 0  -> { running = false; bye(); }
                default -> error("Invalid option.");
            }
        }
    }

    // ── STUDENT MENU ────────────────────────────────────────────────────────
    private void studentMenu() {
        while (true) {
            header("STUDENT MANAGEMENT");
            System.out.println("  1. Add Student");
            System.out.println("  2. View All Students");
            System.out.println("  3. Search Student");
            System.out.println("  4. Update Student");
            System.out.println("  5. Delete Student");
            System.out.println("  6. View by Department");
            System.out.println("  0. Back");
            int c = readInt("Choice: ");
            switch (c) {
                case 1 -> addStudent();
                case 2 -> viewAllStudents();
                case 3 -> searchStudent();
                case 4 -> updateStudent();
                case 5 -> deleteStudent();
                case 6 -> viewByDept();
                case 0 -> { return; }
                default -> error("Invalid option.");
            }
        }
    }

    private void addStudent() {
        header("ADD STUDENT");
        Student s = new Student();
        s.setRollNumber(readLine("Roll Number    : "));
        s.setFirstName (readLine("First Name     : "));
        s.setLastName  (readLine("Last Name      : "));
        s.setEmail     (readLine("Email          : "));
        s.setPhone     (readLine("Phone          : "));
        s.setDepartment(readLine("Department     : "));
        s.setYear      (readInt ("Year (1-6)     : "));
        s.setCgpa      (readDouble("CGPA (0-10)  : "));
        s.setAddress   (readLine("Address        : "));
        String res = ss.addStudent(s);
        if ("SUCCESS".equals(res)) success("Student added successfully!");
        else error(res);
    }

    private void viewAllStudents() {
        header("ALL STUDENTS");
        List<Student> list = ss.getAllStudents();
        if (list.isEmpty()) { info("No students found."); return; }
        printStudentTable(list);
        info("Total: " + list.size() + " student(s).");
    }

    private void searchStudent() {
        header("SEARCH STUDENT");
        String kw = readLine("Enter keyword (name/roll/email/dept): ");
        List<Student> list = ss.searchStudents(kw);
        if (list.isEmpty()) info("No students match '" + kw + "'.");
        else printStudentTable(list);
    }

    private void updateStudent() {
        header("UPDATE STUDENT");
        String roll = readLine("Enter Roll Number to update: ");
        Student s = ss.getStudentByRoll(roll);
        if (s == null) { error("Student not found."); return; }
        System.out.println("Leave blank to keep current value.");
        String fn = readLine("First Name     [" + s.getFirstName()  + "]: ");
        String ln = readLine("Last Name      [" + s.getLastName()   + "]: ");
        String em = readLine("Email          [" + s.getEmail()      + "]: ");
        String ph = readLine("Phone          [" + s.getPhone()      + "]: ");
        String dp = readLine("Department     [" + s.getDepartment() + "]: ");
        String yr = readLine("Year           [" + s.getYear()       + "]: ");
        String cg = readLine("CGPA           [" + s.getCgpa()       + "]: ");
        String ad = readLine("Address        [" + s.getAddress()    + "]: ");

        if (!fn.isBlank()) s.setFirstName(fn);
        if (!ln.isBlank()) s.setLastName(ln);
        if (!em.isBlank()) s.setEmail(em);
        if (!ph.isBlank()) s.setPhone(ph);
        if (!dp.isBlank()) s.setDepartment(dp);
        if (!yr.isBlank()) s.setYear(Integer.parseInt(yr));
        if (!cg.isBlank()) s.setCgpa(Double.parseDouble(cg));
        if (!ad.isBlank()) s.setAddress(ad);

        String res = ss.updateStudent(s);
        if ("SUCCESS".equals(res)) success("Student updated successfully!");
        else error(res);
    }

    private void deleteStudent() {
        header("DELETE STUDENT");
        String roll = readLine("Enter Roll Number to delete: ");
        Student s = ss.getStudentByRoll(roll);
        if (s == null) { error("Student not found."); return; }
        System.out.println("  → " + s.getFullName() + " (" + s.getRollNumber() + ")");
        String confirm = readLine("Confirm delete? (yes/no): ");
        if ("yes".equalsIgnoreCase(confirm)) {
            if ("SUCCESS".equals(ss.deleteStudent(s.getId()))) success("Deleted.");
            else error("Delete failed.");
        } else info("Cancelled.");
    }

    private void viewByDept() {
        header("STUDENTS BY DEPARTMENT");
        String dept = readLine("Department: ");
        List<Student> list = ss.getStudentsByDept(dept);
        if (list.isEmpty()) info("No students in '" + dept + "'.");
        else printStudentTable(list);
    }

    // ── COURSE MENU ─────────────────────────────────────────────────────────
    private void courseMenu() {
        while (true) {
            header("COURSE MANAGEMENT");
            System.out.println("  1. Add Course");
            System.out.println("  2. View All Courses");
            System.out.println("  3. Update Course");
            System.out.println("  4. Delete Course");
            System.out.println("  0. Back");
            int c = readInt("Choice: ");
            switch (c) {
                case 1 -> addCourse();
                case 2 -> viewAllCourses();
                case 3 -> updateCourse();
                case 4 -> deleteCourse();
                case 0 -> { return; }
                default -> error("Invalid option.");
            }
        }
    }

    private void addCourse() {
        header("ADD COURSE");
        Course c = new Course();
        c.setCourseCode(readLine("Course Code  : "));
        c.setCourseName(readLine("Course Name  : "));
        c.setInstructor(readLine("Instructor   : "));
        c.setCredits   (readInt ("Credits (1-6): "));
        c.setDepartment(readLine("Department   : "));
        String res = cs.addCourse(c);
        if ("SUCCESS".equals(res)) success("Course added successfully!");
        else error(res);
    }

    private void viewAllCourses() {
        header("ALL COURSES");
        List<Course> list = cs.getAllCourses();
        if (list.isEmpty()) { info("No courses found."); return; }
        printCourseTable(list);
    }

    private void updateCourse() {
        header("UPDATE COURSE");
        String code = readLine("Enter Course Code: ");
        Course c = cs.getCourseByCode(code);
        if (c == null) { error("Course not found."); return; }
        String nm = readLine("Course Name  [" + c.getCourseName()  + "]: ");
        String in = readLine("Instructor   [" + c.getInstructor()  + "]: ");
        String cr = readLine("Credits      [" + c.getCredits()     + "]: ");
        String dp = readLine("Department   [" + c.getDepartment()  + "]: ");
        if (!nm.isBlank()) c.setCourseName(nm);
        if (!in.isBlank()) c.setInstructor(in);
        if (!cr.isBlank()) c.setCredits(Integer.parseInt(cr));
        if (!dp.isBlank()) c.setDepartment(dp);
        String res = cs.updateCourse(c);
        if ("SUCCESS".equals(res)) success("Course updated!");
        else error(res);
    }

    private void deleteCourse() {
        header("DELETE COURSE");
        String code = readLine("Enter Course Code: ");
        Course c = cs.getCourseByCode(code);
        if (c == null) { error("Course not found."); return; }
        System.out.println("  → " + c.getCourseCode() + " – " + c.getCourseName());
        if ("yes".equalsIgnoreCase(readLine("Confirm? (yes/no): "))) {
            if ("SUCCESS".equals(cs.deleteCourse(c.getId()))) success("Deleted.");
            else error("Delete failed.");
        } else info("Cancelled.");
    }

    // ── GRADE MENU ──────────────────────────────────────────────────────────
    private void gradeMenu() {
        while (true) {
            header("GRADE MANAGEMENT");
            System.out.println("  1. Add Grade");
            System.out.println("  2. View All Grades");
            System.out.println("  3. View Grades by Student");
            System.out.println("  4. View Grades by Course");
            System.out.println("  5. Update Grade");
            System.out.println("  6. Delete Grade");
            System.out.println("  0. Back");
            int c = readInt("Choice: ");
            switch (c) {
                case 1 -> addGrade();
                case 2 -> viewAllGrades();
                case 3 -> viewGradesByStudent();
                case 4 -> viewGradesByCourse();
                case 5 -> updateGrade();
                case 6 -> deleteGrade();
                case 0 -> { return; }
                default -> error("Invalid option.");
            }
        }
    }

    private void addGrade() {
        header("ADD GRADE");
        String roll = readLine("Student Roll Number: ");
        Student st = ss.getStudentByRoll(roll);
        if (st == null) { error("Student not found."); return; }
        String code = readLine("Course Code        : ");
        Course co = cs.getCourseByCode(code);
        if (co == null) { error("Course not found."); return; }
        Grade g = new Grade();
        g.setStudentId    (st.getId());
        g.setCourseId     (co.getId());
        g.setMarksObtained(readDouble("Marks Obtained   : "));
        g.setTotalMarks   (readDouble("Total Marks      : "));
        g.setSemester     (readLine  ("Semester (e.g. S1-2024): "));
        String res = gs.addGrade(g);
        if ("SUCCESS".equals(res))
            success("Grade added! Letter grade: " + g.getGrade());
        else error(res);
    }

    private void viewAllGrades() {
        header("ALL GRADES");
        List<Grade> list = gs.getAllGrades();
        if (list.isEmpty()) { info("No grades found."); return; }
        printGradeTable(list);
    }

    private void viewGradesByStudent() {
        header("GRADES BY STUDENT");
        String roll = readLine("Roll Number: ");
        Student st = ss.getStudentByRoll(roll);
        if (st == null) { error("Student not found."); return; }
        List<Grade> list = gs.getGradesByStudent(st.getId());
        if (list.isEmpty()) info("No grades for this student.");
        else printGradeTable(list);
    }

    private void viewGradesByCourse() {
        header("GRADES BY COURSE");
        String code = readLine("Course Code: ");
        Course co = cs.getCourseByCode(code);
        if (co == null) { error("Course not found."); return; }
        List<Grade> list = gs.getGradesByCourse(co.getId());
        if (list.isEmpty()) info("No grades for this course.");
        else printGradeTable(list);
    }

    private void updateGrade() {
        header("UPDATE GRADE");
        viewAllGrades();
        int id = readInt("Enter Grade ID to update: ");
        // we trust the user to supply valid id
        Grade g = new Grade();
        g.setId           (id);
        g.setMarksObtained(readDouble("New Marks Obtained: "));
        g.setTotalMarks   (readDouble("New Total Marks   : "));
        g.setSemester     (readLine  ("New Semester      : "));
        String res = gs.updateGrade(g);
        if ("SUCCESS".equals(res)) success("Grade updated!");
        else error(res);
    }

    private void deleteGrade() {
        header("DELETE GRADE");
        viewAllGrades();
        int id = readInt("Enter Grade ID to delete: ");
        if ("yes".equalsIgnoreCase(readLine("Confirm? (yes/no): "))) {
            if ("SUCCESS".equals(gs.deleteGrade(id))) success("Deleted.");
            else error("Delete failed.");
        } else info("Cancelled.");
    }

    // ── REPORTS MENU ────────────────────────────────────────────────────────
    private void reportsMenu() {
        while (true) {
            header("REPORTS & STATISTICS");
            System.out.println("  1. Summary Statistics");
            System.out.println("  2. Top Students (by CGPA)");
            System.out.println("  3. Department-wise Count");
            System.out.println("  0. Back");
            int c = readInt("Choice: ");
            switch (c) {
                case 1 -> summaryStats();
                case 2 -> topStudents();
                case 3 -> deptWiseCount();
                case 0 -> { return; }
                default -> error("Invalid option.");
            }
        }
    }

    private void summaryStats() {
        header("SUMMARY STATISTICS");
        int    totalStudents = ss.getTotalStudents();
        int    totalCourses  = cs.getTotalCourses();
        int    totalGrades   = gs.getAllGrades().size();
        double avgCgpa       = gs.getAverageCgpa();
        System.out.printf("  %-25s : %d%n",   "Total Students",    totalStudents);
        System.out.printf("  %-25s : %d%n",   "Total Courses",     totalCourses);
        System.out.printf("  %-25s : %d%n",   "Total Grade Records",totalGrades);
        System.out.printf("  %-25s : %.2f%n", "Average CGPA",      avgCgpa);
        pause();
    }

    private void topStudents() {
        header("TOP STUDENTS BY CGPA");
        List<Student> list = ss.getAllStudents();
        list.sort((a, b) -> Double.compare(b.getCgpa(), a.getCgpa()));
        int rank = 1;
        for (Student s : list) {
            System.out.printf("  %2d. %-20s  Roll: %-10s  CGPA: %.2f  Dept: %s%n",
                rank++, s.getFullName(), s.getRollNumber(), s.getCgpa(), s.getDepartment());
            if (rank > 10) break;
        }
        pause();
    }

    private void deptWiseCount() {
        header("DEPARTMENT-WISE STUDENT COUNT");
        List<Student> all = ss.getAllStudents();
        java.util.Map<String, Long> map = new java.util.TreeMap<>();
        for (Student s : all)
            map.merge(s.getDepartment(), 1L, Long::sum);
        map.forEach((dept, cnt) ->
            System.out.printf("  %-30s : %d%n", dept, cnt));
        pause();
    }

    // ── PRINT HELPERS ────────────────────────────────────────────────────────
    private void printStudentTable(List<Student> list) {
        String line = "-".repeat(110);
        System.out.println(line);
        System.out.printf(BOLD + "  %-5s %-12s %-22s %-28s %-15s %4s %5s%n" + RESET,
            "ID","Roll","Name","Email","Department","Year","CGPA");
        System.out.println(line);
        for (Student s : list)
            System.out.printf("  %-5d %-12s %-22s %-28s %-15s %4d %5.2f%n",
                s.getId(), s.getRollNumber(), s.getFullName(),
                s.getEmail(), s.getDepartment(), s.getYear(), s.getCgpa());
        System.out.println(line);
    }

    private void printCourseTable(List<Course> list) {
        String line = "-".repeat(90);
        System.out.println(line);
        System.out.printf(BOLD + "  %-5s %-12s %-35s %-20s %7s %-15s%n" + RESET,
            "ID","Code","Name","Instructor","Credits","Department");
        System.out.println(line);
        for (Course c : list)
            System.out.printf("  %-5d %-12s %-35s %-20s %7d %-15s%n",
                c.getId(), c.getCourseCode(), c.getCourseName(),
                c.getInstructor(), c.getCredits(), c.getDepartment());
        System.out.println(line);
    }

    private void printGradeTable(List<Grade> list) {
        String line = "-".repeat(100);
        System.out.println(line);
        System.out.printf(BOLD + "  %-4s %-22s %-12s %-25s %8s %6s %-5s %-12s%n" + RESET,
            "ID","Student","Course","Course Name","Marks","Total","Grade","Semester");
        System.out.println(line);
        for (Grade g : list)
            System.out.printf("  %-4d %-22s %-12s %-25s %8.1f %6.1f %-5s %-12s%n",
                g.getId(), g.getStudentName(), g.getCourseCode(),
                g.getCourseName(), g.getMarksObtained(),
                g.getTotalMarks(), g.getGrade(), g.getSemester());
        System.out.println(line);
    }

    // ── I/O HELPERS ──────────────────────────────────────────────────────────
    private String readLine(String prompt) {
        System.out.print(CYAN + prompt + RESET);
        return sc.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(CYAN + prompt + RESET);
                int v = Integer.parseInt(sc.nextLine().trim());
                return v;
            } catch (NumberFormatException e) {
                error("Please enter a valid integer.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(CYAN + prompt + RESET);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                error("Please enter a valid number.");
            }
        }
    }

    private void header(String title) {
        System.out.println();
        System.out.println(BOLD + BLUE + "╔══════════════════════════════════════╗" + RESET);
        System.out.printf (BOLD + BLUE + "║  %-36s║%n" + RESET, title);
        System.out.println(BOLD + BLUE + "╚══════════════════════════════════════╝" + RESET);
    }

    private void success(String msg) { System.out.println(GREEN + "✔  " + msg + RESET); pause(); }
    private void error  (String msg) { System.out.println(RED   + "✖  " + msg + RESET); pause(); }
    private void info   (String msg) { System.out.println(YELLOW + "ℹ  " + msg + RESET); pause(); }

    private void pause() {
        System.out.print(YELLOW + "\nPress Enter to continue..." + RESET);
        sc.nextLine();
    }

    private void printBanner() {
        System.out.println(BOLD + CYAN);
        System.out.println("  ╔════════════════════════════════════════════════╗");
        System.out.println("  ║     STUDENT MANAGEMENT SYSTEM  v1.0           ║");
        System.out.println("  ║     Java + SQLite  |  MVC Architecture        ║");
        System.out.println("  ╚════════════════════════════════════════════════╝");
        System.out.println(RESET);
    }

    private void printMainMenu() {
        System.out.println(BOLD + "\n  ── MAIN MENU ──────────────────────" + RESET);
        System.out.println("  1. 🎓 Student Management");
        System.out.println("  2. 📚 Course Management");
        System.out.println("  3. 📊 Grade Management");
        System.out.println("  4. 📈 Reports & Statistics");
        System.out.println("  0. 🚪 Exit");
        System.out.println(BOLD + "  ────────────────────────────────────" + RESET);
    }

    private void bye() {
        System.out.println(GREEN + "\n  Goodbye! Thank you for using SMS.\n" + RESET);
    }
}
