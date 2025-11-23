package backend.services;

import backend.models.*;
import backend.databaseManager.*;

import java.util.ArrayList;

public class AdminService {

    String adminId;
    Admin admin;
    private CourseDatabaseManager Cdb;
    private UsersDatabaseManager Udb;

    public AdminService(String adminId) {
        Cdb = new CourseDatabaseManager();
        Udb = new UsersDatabaseManager();
        this.adminId = adminId;
        admin = (Admin) Udb.getUser(adminId);
        if (admin == null) {
            System.out.println("AdminService: Admin not found.");
        }
    }

    // Get all pending courses
    public ArrayList<Course> getPendingCourses() {
        return Cdb.getPendingCourses();
    }

    // Approve a course
    public void approveCourse(String courseId) {
        try {
            Cdb.approveCourse(courseId, adminId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reject a course
    public void rejectCourse(String courseId) {
        try {
            Cdb.rejectCourse(courseId, adminId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get all approved courses (for dashboard)
    public ArrayList<Course> getApprovedCourses() {
        return Cdb.getApprovedCourses();
    }

    // Get all rejected courses (for dashboard)
    public ArrayList<Course> getRejectedCourses() {
        return Cdb.getRejectedCourses();
    }

}
