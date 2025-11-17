package backend.ProgramFunctions.CourseManagement;

import backend.ProgramFunctions.LessonAndLearningFeatures.Lesson;
import java.util.ArrayList;

/**
 * Requirement 4: Manage courses (Corrected Version).
 * Holds its own Lesson objects (Composition) but holds Student IDs (Association).
 * UPDATED: Includes toString() for better JList display.
 */
public class Course {
    private String courseId;
    private String title;
    private String description;
    private String instructorId;   // Requirement: instructorId
    private ArrayList<Lesson> lessons;     // Requirement: lessons[] (Full objects)
    private ArrayList<String> students;    // Requirement: students[] (as IDs)

    public Course(String courseId, String title, String description, String instructorId) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;

        // Initialize the lists
        this.lessons = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    // --- Getters ---
    public String getCourseId() { return courseId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getInstructorId() { return instructorId; }
    public ArrayList<Lesson> getLessons() { return lessons; }
    public ArrayList<String> getStudents() { return students; }

    // Setters (for editing)
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    // --- Methods to manage the course ---
    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
    }

    public void removeLesson(String lessonId) {
        this.lessons.removeIf(lesson -> lesson.getLessonId().equals(lessonId));
    }

    public Lesson findLessonById(String lessonId) {
        for (Lesson l : lessons) {
            if (l.getLessonId().equals(lessonId)) {
                return l;
            }
        }
        return null;
    }

    public void addStudent(String studentId) {
        if (!this.students.contains(studentId)) {
            this.students.add(studentId);
        }
    }

    /**
     * This controls how the object appears in a JList.
     * @return The title of the course.
     */
    @Override
    public String toString() {
        return this.title; // Display the title in lists
    }
}