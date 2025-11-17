package backend.ProgramFunctions.StudentManagement;

import backend.ProgramFunctions.UserAccountManagement.User;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Requirement 3: Manage student data (Corrected Version).
 * Stores IDs of courses, not full Course objects.
 */
public class Student extends User {

    // Store ONLY the IDs
    private ArrayList<String> enrolledCourses; // List of course IDs

    // Map<CourseID, List_of_completed_LessonIDs>
    private HashMap<String, ArrayList<String>> progress;

    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, "student", username, email, passwordHash);

        // Initialize the new fields
        this.enrolledCourses = new ArrayList<>();
        this.progress = new HashMap<>();
    }

    // --- Getters ---
    public ArrayList<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public HashMap<String, ArrayList<String>> getProgress() {
        return progress;
    }

    // --- Setters (for loading from JSON) ---
    public void setEnrolledCourses(ArrayList<String> courseIds) {
        this.enrolledCourses = courseIds;
    }

    public void setProgress(HashMap<String, ArrayList<String>> progress) {
        this.progress = progress;
    }

    // --- Methods to manage student data ---
    public void enrollInCourse(String courseId) {
        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
            // Also create an empty progress list for this new course
            progress.putIfAbsent(courseId, new ArrayList<>());
        }
    }

    public boolean isEnrolled(String courseId) {
        return enrolledCourses.contains(courseId);
    }

    public void markLessonComplete(String courseId, String lessonId) {
        // Ensure the student is enrolled and has a progress map
        if (progress.containsKey(courseId)) {
            // Get the list of completed lessons for that course
            ArrayList<String> completedLessons = progress.get(courseId);
            if (!completedLessons.contains(lessonId)) {
                completedLessons.add(lessonId);
            }
        }
    }

    public boolean isLessonComplete(String courseId, String lessonId) {
        if (progress.containsKey(courseId)) {
            return progress.get(courseId).contains(lessonId);
        }
        return false;
    }
}