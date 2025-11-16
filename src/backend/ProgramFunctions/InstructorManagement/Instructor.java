package backend.ProgramFunctions.InstructorManagement;

import backend.ProgramFunctions.UserAccountManagement.User;
import backend.ProgramFunctions.CourseManagement.Course;
import java.util.ArrayList;


public class Instructor extends User {
    ArrayList<Course> createdCourses;

    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, "instructor", username, email, passwordHash);
    }
}
