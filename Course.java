
package Backend;

public class Course {
    private String courseId;
    private String courseName;
    private String instructorName;
    private String description;

    public Course(String courseId, String courseName, String instructorName, String description) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.description = description;
    }

    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public String getInstructorName() { return instructorName; }
    public String getDescription() { return description; }
}
