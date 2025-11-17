package backend.ProgramFunctions.InstructorManagement;

import backend.ProgramFunctions.UserAccountManagement.User;
import java.util.ArrayList;


public class Instructor extends User {
    ArrayList<String> createdCourses;

    //CLASS CONSTRUCTOR
    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, "instructor", username, email, passwordHash);
        this.createdCourses = new ArrayList<String>();
    }
    public Instructor( String username, String email, String passwordHash) {
        super("instructor", username, email, passwordHash);
        this.createdCourses = new ArrayList<String>();
    }

    //standard getters and setters
    public ArrayList<String> getCreatedCourses() {
        return createdCourses;
    }
    public void setCreatedCourses(ArrayList<String> createdCourses) {
        this.createdCourses = createdCourses;
    }

}
