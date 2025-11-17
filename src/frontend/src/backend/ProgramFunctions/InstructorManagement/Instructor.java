package backend.ProgramFunctions.InstructorManagement;

import backend.ProgramFunctions.UserAccountManagement.User;
import java.util.ArrayList;

/**
 * Requirement 2: Manage instructor data (Corrected Version).
 * Stores IDs of courses, not full Course objects.
 */
public class Instructor extends User {

    // Store ONLY the IDs
    private ArrayList<String> createdCourses; // List of course IDs

    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, "instructor", username, email, passwordHash);

        // Initialize the list
        this.createdCourses = new ArrayList<>();
    }

    //  Getter
    public ArrayList<String> getCreatedCourses() {
        return createdCourses;
    }

    // --- Setter (for loading from JSON) ---
    public void setCreatedCourses(ArrayList<String> courseIds) {
        this.createdCourses = courseIds;
    }

    // --- Method to manage instructor data ---
    public void addCreatedCourse(String courseId) {
        if (!createdCourses.contains(courseId)) {
            createdCourses.add(courseId);
        }
    }
}