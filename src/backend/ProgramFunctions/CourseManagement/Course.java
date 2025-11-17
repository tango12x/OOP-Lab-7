
package backend.ProgramFunctions.CourseManagement;
import backend.ProgramFunctions.LessonAndLearningFeatures.Lesson;

import java.util.ArrayList;

public class Course {
    private String courseId;
    private String title;
    private String instructorId;
    private String description;
    private ArrayList<Lesson> lessons;
    private ArrayList<String> students;

    //CLASS CONSTRUCTOR
    public Course(String courseId, String title, String instructorId, String description) {
        this.courseId = courseId;
        this.title = title;
        this.instructorId = instructorId;
        this.description = description;
        this.lessons = new ArrayList<Lesson>();
        this.students = new ArrayList<String>();
    }
    public Course(String title, String instructorId, String description) {
        this.title = title;
        this.instructorId = instructorId;
        this.description = description;
        this.lessons = new ArrayList<Lesson>();
        this.students = new ArrayList<String>();
    }

    //standard getters and setters
    public ArrayList<Lesson> getLessons() {
        return lessons;
    }
    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }
    public ArrayList<String> getStudents() {
        return students;
    }
    public void setStudents(ArrayList<String> students) {
        this.students = students;
    }
    public String getCourseId() { return courseId; }
    public String getTitle() { return title; }
    public String getInstructorId() { return instructorId; }
    public String getDescription() { return description; }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
