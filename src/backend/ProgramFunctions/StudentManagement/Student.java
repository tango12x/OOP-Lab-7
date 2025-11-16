package backend.ProgramFunctions.StudentManagement;

import backend.ProgramFunctions.UserAccountManagement.User;
import backend.ProgramFunctions.CourseManagement.Course;
import backend.ProgramFunctions.LessonAndLearningFeatures.Lesson;
import java.util.ArrayList;

public class Student extends User {
    ArrayList<Course> enrolledCourses;
    ArrayList<ArrayList<Lesson>> progress;

    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, "student", username, email, passwordHash);
    }
}
