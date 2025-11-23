package backend.AdminMangment;

import backend.JsonDatabaseManager.CourseDatabaseManager;
import backend.ProgramFunctions.CourseManagement.Course;
import java.util.ArrayList;

public class AdminService {

    private CourseDatabaseManager courseDB;

    public AdminService() {
        courseDB = new CourseDatabaseManager();
    }

    // Get all pending courses
    public ArrayList<Course> getPendingCourses() {
        ArrayList<Course> all = new ArrayList<>();
        for (int i = 0; i < courseDB.getAllCourses().length(); i++) {
            String id = courseDB.getAllCourses().getJSONObject(i).getString("courseId");
            Course c = courseDB.getCourseWithoutLessons(id);
            if (c.getApprovalStatus().equals("PENDING")) {
                all.add(c);
            }
        }
        return all;
    }

    // Approve a course
    public void approveCourse(String courseId) {
        Course c = courseDB.getCourseWithoutLessons(courseId);  // FIXED
        if (c == null) return;

        c.setApprovalStatus("APPROVED");
        courseDB.update(c);
    }

    // Reject a course
    public void rejectCourse(String courseId) {
        Course c = courseDB.getCourseWithoutLessons(courseId);  // FIXED
        if (c == null) return;

        c.setApprovalStatus("REJECTED");
        courseDB.update(c);
    }

    // Get all approved courses (for dashboard)
    public ArrayList<Course> getApprovedCourses() {
        ArrayList<Course> all = new ArrayList<>();
        for (int i = 0; i < courseDB.getAllCourses().length(); i++) {
            String id = courseDB.getAllCourses().getJSONObject(i).getString("courseId");
            Course c = courseDB.getCourseWithoutLessons(id);
            if (c.getApprovalStatus().equals("APPROVED")) {
                all.add(c);
            }
        }
        return all;
    }
}
