package backend.models;

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

    // <<< ADDED for Lab 8
    private String approvalStatus;   // PENDING, APPROVED, REJECTED

    // CLASS CONSTRUCTOR IN CASE OF DESCRIPTION IS GIVEN
    public Course(String courseId, String title, String instructorId, String description) {
        this.courseId = courseId;
        this.title = title;
        this.instructorId = instructorId;
        this.description = description;
        this.lessons = new ArrayList<Lesson>();
        this.students = new ArrayList<String>();

        // <<< ADDED
        this.approvalStatus = "PENDING";
    }

    // OVERLOADING CONSTRUCTOR IN CASE THAT DESCRIPTION IS NOT GIVEN
    public Course(String title, String instructorId, String description) {
        this.title = title;
        this.instructorId = instructorId;
        this.description = description;
        this.lessons = new ArrayList<Lesson>();
        this.students = new ArrayList<String>();

        // <<< ADDED
        this.approvalStatus = "PENDING";
    }

    // standard getters and setters
    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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

    public String getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // METHOD TO ADD LESSON
    public void addLesson(Lesson lesson) {
        if (lesson == null) return;
        if (lessons.contains(lesson)) return;
        lessons.add(lesson);
    }

    // METHOD TO REMOVE LESSON
    public void removeLesson(String lessonId) {
        if (lessonId == null || lessonId.trim().isEmpty()) return;
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getLessonId().equals(lessonId)) {
                lessons.remove(i);
                return;
            }
        }
    }

    // METHOD TO EDIT SPECIFIC LESSON
    public void editLesson(String oldLessonId, Lesson newLesson) {
        if (oldLessonId == null || newLesson == null) return;
        if (oldLessonId.trim().isEmpty()) return;

        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getLessonId().equals(oldLessonId)) {
                if (!newLesson.getLessonId().equals(oldLessonId)) return;
                lessons.set(i, newLesson);
                return;
            }
        }
    }

    public String generateLessonId(){
        return "L" + String.format("%d", this.lessons.size() + 1);
    }

    //adding students to course
    public void addStudent(String studentId){
    // return if student is enrolled
    public boolean isStudentEnrolled(String studentId) {
        for (String id : students) {
            if (id.equals(studentId)) {
                return true;
            }
        }
        return false;
    }

    public void addStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) return;
        if (isStudentEnrolled(studentId)) return;
        students.add(studentId);
    }

    // <<< FIXED: These no longer throw unsupported exception
    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String status) {
        this.approvalStatus = status;
    }
}
