package com.sms;

import com.sms.model.Student;
import com.sms.service.CourseService;
import com.sms.service.GradeService;
import com.sms.service.StudentService;
import com.sms.ui.ConsoleUI;
import com.sms.util.DatabaseConnection;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        if (!isApiMode()) {
            new ConsoleUI().start();
            DatabaseConnection.closeConnection();
            return;
        }

        StudentService students = new StudentService();
        CourseService courses = new CourseService();
        GradeService grades = new GradeService();
        Javalin app = Javalin.create(config -> config.plugins.enableCors(cors ->
                cors.add(rule -> allowedOrigins().forEach(rule::allowHost))));

        app.exception(IllegalArgumentException.class, (error, ctx) ->
                ctx.status(HttpStatus.BAD_REQUEST).json(Map.of("message", error.getMessage())));
        app.exception(Exception.class, (error, ctx) -> {
            if (!ctx.res().isCommitted()) {
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of("message", "The service could not complete your request."));
            }
            System.err.println("Request error: " + error.getMessage());
        });
        app.get("/health", ctx -> ctx.json(Map.of("status", "ok", "service", "bcrec-student-portal")));
        app.get("/", ctx -> serveAsset(ctx, "frontend/index.html", "text/html"));
        app.get("/style.css", ctx -> serveAsset(ctx, "frontend/style.css", "text/css"));
        app.get("/app.js", ctx -> serveAsset(ctx, "frontend/app.js", "application/javascript"));
        app.get("/api/stats", ctx -> ctx.json(Map.of(
                "totalStudents", students.getTotalStudents(),
                "totalCourses", courses.getTotalCourses(),
                "averageCgpa", grades.getAverageCgpa())));
        app.get("/api/students", ctx -> {
            String query = ctx.queryParam("q");
            ctx.json(query == null || query.isBlank() ? students.getAllStudents() : students.searchStudents(query.trim()));
        });
        app.get("/api/students/{id}", ctx -> {
            Student student = students.getStudentById(pathId(ctx.pathParam("id")));
            if (student == null) ctx.status(HttpStatus.NOT_FOUND).json(Map.of("message", "Student not found."));
            else ctx.json(student);
        });
        app.post("/api/students", ctx -> {
            Student student = ctx.bodyAsClass(Student.class);
            String result = students.addStudent(student);
            if ("SUCCESS".equals(result)) ctx.status(HttpStatus.CREATED).json(student);
            else ctx.status(HttpStatus.BAD_REQUEST).json(Map.of("message", result));
        });
        app.put("/api/students/{id}", ctx -> {
            Student student = ctx.bodyAsClass(Student.class);
            Student existing = students.getStudentById(pathId(ctx.pathParam("id")));
            if (existing == null) {
                ctx.status(HttpStatus.NOT_FOUND).json(Map.of("message", "Student not found."));
                return;
            }
            student.setId(existing.getId());
            // Student IDs are stable identifiers and are deliberately not editable.
            student.setRollNumber(existing.getRollNumber());
            String result = students.updateStudent(student);
            if ("SUCCESS".equals(result)) ctx.json(student);
            else ctx.status(HttpStatus.BAD_REQUEST).json(Map.of("message", result));
        });
        app.delete("/api/students/{id}", ctx -> {
            String result = students.deleteStudent(pathId(ctx.pathParam("id")));
            if ("SUCCESS".equals(result)) ctx.status(HttpStatus.NO_CONTENT);
            else ctx.status(HttpStatus.NOT_FOUND).json(Map.of("message", result));
        });

        app.get("/api/courses", ctx -> ctx.json(courses.getAllCourses()));
        app.get("/api/grades", ctx -> ctx.json(grades.getAllGrades()));
        app.start("0.0.0.0", parsePort(System.getenv("PORT")));
    }

    private static boolean isApiMode() {
        return System.getenv("PORT") != null || System.getenv("DATABASE_URL") != null || System.getenv("DB_HOST") != null;
    }

    private static int parsePort(String value) {
        try { return value == null ? 8080 : Integer.parseInt(value); }
        catch (NumberFormatException e) { return 8080; }
    }

    private static int pathId(String value) {
        try {
            int id = Integer.parseInt(value);
            if (id < 1) throw new NumberFormatException();
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid student ID.");
        }
    }

    private static Set<String> allowedOrigins() {
        Set<String> origins = new LinkedHashSet<>(Arrays.asList("http://localhost:3000", "http://localhost:5500", "http://127.0.0.1:5500"));
        String configured = System.getenv("FRONTEND_URLS");
        if (configured != null) Arrays.stream(configured.split(",")).map(String::trim)
                .filter(value -> !value.isEmpty()).forEach(origins::add);
        return origins;
    }

    private static void serveAsset(io.javalin.http.Context ctx, String filename, String contentType) throws Exception {
        ctx.contentType(contentType).result(Files.readString(Path.of(filename)));
    }
}
