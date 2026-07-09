package com.sms.util;

public class ValidationUtil {

    private ValidationUtil() {}

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public static boolean isValidPhone(String phone) {
        return phone == null || phone.isEmpty() || phone.matches("^[+]?[0-9\\-\\s]{7,15}$");
    }

    public static boolean isValidCgpa(double cgpa) {
        return cgpa >= 0.0 && cgpa <= 10.0;
    }

    public static boolean isValidYear(int year) {
        return year >= 1 && year <= 6;
    }

    public static boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static String calculateGrade(double marks, double total) {
        double pct = (marks / total) * 100;
        if (pct >= 90) return "A+";
        if (pct >= 80) return "A";
        if (pct >= 70) return "B+";
        if (pct >= 60) return "B";
        if (pct >= 50) return "C+";
        if (pct >= 40) return "C";
        return "F";
    }
}
