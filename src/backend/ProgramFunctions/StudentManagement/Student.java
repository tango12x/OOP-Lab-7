package backend.ProgramFunctions.StudentManagement;

import backend.ProgramFunctions.UserAccountManagement.User;
import java.util.ArrayList;


public class Student extends User {
    private ArrayList<String> enrolledCourses;
    private ArrayList<ArrayList<String>> progress;

    //CLASS CONSTRUCTOR
    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, "student", username, email, passwordHash);
        this.enrolledCourses = new ArrayList<String>();
        this.progress = new ArrayList<ArrayList<String>>();
    }
    public Student(String username, String email, String passwordHash) {
        super("student", username, email, passwordHash);
        this.enrolledCourses = new ArrayList<String>();
        this.progress = new ArrayList<ArrayList<String>>();
    }

    //standard getters and setters
    public ArrayList<String> getEnrolledCourses() {
        return enrolledCourses;
    }
    public void setEnrolledCourses(ArrayList<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }
    public ArrayList<ArrayList<String>> getProgress() {
        return progress;
    }
    public void setProgress(ArrayList<ArrayList<String>> progress) {
        this.progress = progress;
    }

}
