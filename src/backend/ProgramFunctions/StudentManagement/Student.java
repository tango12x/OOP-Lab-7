package backend.ProgramFunctions.StudentManagement;
import backend.ProgramFunctions.UserAccountManagement.User;
import java.util.ArrayList;


public class Student extends User {
    private ArrayList<String> enrolledCourses; //RELATION BETWEEN COURSES AND STUDENT IS AGGREGATION
    private ArrayList<ArrayList<String>> progress; //RELATION BETWEEN PROGRESS AND STUDENT IS AGGREGATION

    //CLASS CONSTRUCTOR IN CASE OF ID IS GIVEN
    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, "student", username, email, passwordHash);}
    //OVERLOADING CONSTRUCTOR IN CASE OF ID IS NOT GIVEN
    public Student(String username, String email, String passwordHash) {
        super("student", username, email, passwordHash);}

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
