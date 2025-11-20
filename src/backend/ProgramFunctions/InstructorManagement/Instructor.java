package backend.ProgramFunctions.InstructorManagement;
import backend.ProgramFunctions.UserAccountManagement.User;
import java.util.ArrayList;


public class Instructor extends User {

    ArrayList<String> createdCourses;
    //RELATION BETWEEN COURSES AND INSTRUCTOR IS AGGREGATION

    //CLASS CONSTRUCTOR IN CASE OF ID IS GIVEN
    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, "instructor", username, email, passwordHash);
        this.createdCourses = new ArrayList<String>();}

    //OVERLOADING CONSTRUCTOR IN CASE THE ID IS NOT GIVEN
    public Instructor( String username, String email, String passwordHash) {
        super("instructor", username, email, passwordHash);
        this.createdCourses = new ArrayList<String>();}

    //standard getters and setters
    public ArrayList<String> getCreatedCourses() { return createdCourses;}
    public void setCreatedCourses(ArrayList<String> createdCourses) {
        this.createdCourses = createdCourses;
    }

    //ADD NEW COURSE TO THE COURSES CREATED LIST
    public void addCreatedCourse(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) return;
        if (!createdCourses.contains(courseId)) {
            createdCourses.add(courseId);}}

    //REMOVE COURSE FROM CREATED COURSE LIST
    public void removeCreatedCourse(String courseId) {
        if (courseId != null && !courseId.trim().isEmpty()) {
            createdCourses.remove(courseId); }}
    
}
