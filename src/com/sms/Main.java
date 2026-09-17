package com.sms;

import com.sms.model.*;
import com.sms.service.*;
import com.sms.ui.ConsoleUI;
import com.sms.util.DatabaseConnection;
import io.javalin.Javalin;

public class Main {

    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        seedDemoData();

        String portEnv = System.getenv("PORT");
        if (portEnv != null || System.getenv("DATABASE_URL") != null) {
            int port = portEnv != null ? Integer.parseInt(portEnv) : 8080;
            Javalin app = Javalin.create(config -> {
                config.plugins.enableCors(cors -> {
                    cors.add(it -> {
                        String frontendUrl = System.getenv("FRONTEND_URL");
                        if (frontendUrl != null && !frontendUrl.isEmpty()) {
                            it.allowHost(frontendUrl);
                        } else {
                            it.anyHost();
                        }
                    });
                });
            }).start(port);

            StudentService ss = new StudentService();
            CourseService cs = new CourseService();
            GradeService gs = new GradeService();

            app.get("/health", ctx -> ctx.json("{\"status\":\"ok\"}"));

            app.get("/api/students", ctx -> ctx.json(ss.getAllStudents()));
            app.post("/api/students", ctx -> {
                Student s = ctx.bodyAsClass(Student.class);
                String result = ss.addStudent(s);
                if ("SUCCESS".equals(result)) ctx.status(201).json(s);
                else ctx.status(400).result(result);
            });
            app.put("/api/students/{id}", ctx -> {
                Student s = ctx.bodyAsClass(Student.class);
                s.setId(Integer.parseInt(ctx.pathParam("id")));
                String result = ss.updateStudent(s);
                if ("SUCCESS".equals(result)) ctx.json(s);
                else ctx.status(400).result(result);
            });
            app.delete("/api/students/{id}", ctx -> {
                String result = ss.deleteStudent(Integer.parseInt(ctx.pathParam("id")));
                if ("SUCCESS".equals(result)) ctx.status(204);
                else ctx.status(400).result(result);
            });

            app.get("/api/courses", ctx -> ctx.json(cs.getAllCourses()));
            app.post("/api/courses", ctx -> {
                Course c = ctx.bodyAsClass(Course.class);
                String result = cs.addCourse(c);
                if ("SUCCESS".equals(result)) ctx.status(201).json(c);
                else ctx.status(400).result(result);
            });
            app.delete("/api/courses/{id}", ctx -> {
                String result = cs.deleteCourse(Integer.parseInt(ctx.pathParam("id")));
                if ("SUCCESS".equals(result)) ctx.status(204);
                else ctx.status(400).result(result);
            });

            app.get("/api/grades", ctx -> ctx.json(gs.getAllGrades()));
            app.post("/api/grades", ctx -> {
                Grade g = ctx.bodyAsClass(Grade.class);
                String result = gs.addGrade(g);
                if ("SUCCESS".equals(result)) ctx.status(201).json(g);
                else ctx.status(400).result(result);
            });
            app.delete("/api/grades/{id}", ctx -> {
                String result = gs.deleteGrade(Integer.parseInt(ctx.pathParam("id")));
                if ("SUCCESS".equals(result)) ctx.status(204);
                else ctx.status(400).result(result);
            });
        } else {
            new ConsoleUI().start();
            DatabaseConnection.closeConnection();
        }
    }

    private static void seedDemoData() {
        StudentService ss = new StudentService();
        CourseService  cs = new CourseService();
        GradeService   gs = new GradeService();
        if (ss.getTotalStudents() > 0) return;

        String[][] students = {
            {"CS2024001","Alice","Johnson","alice@uni.edu","9876543210","Computer Science","2","8.75","123 Main St"}
        };
        for (String[] d : students) {
            Student s = new Student(d[0],d[1],d[2],d[3],d[4],d[5], Integer.parseInt(d[6]), Double.parseDouble(d[7]), d[8]);
            ss.addStudent(s);
        }
        Object[][] courses = { {"CS101","Data Structures","Dr. Alan Turing",4,"Computer Science"} };
        for (Object[] d : courses) {
            Course c = new Course((String)d[0],(String)d[1],(String)d[2], (int)d[3],(String)d[4]);
            cs.addCourse(c);
        }
        int[][] gradeData = { {1,1,88,100} };
        for (int[] d : gradeData) {
            Grade g = new Grade(d[0],d[1],d[2],d[3],"","S1-2024");
            gs.addGrade(g);
        }
    }
}
