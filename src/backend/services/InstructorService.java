package backend.services;

import java.util.ArrayList;
import backend.databaseManager.*;
import backend.models.*;


public class InstructorService {
    private String instructorId;
    private Instructor instructor;
    private CourseDatabaseManager Cdb;
    private UsersDatabaseManager Udb;
    private ArrayList<Course> createdCourses;

    // CLASS CONSTRUCTORS
    public InstructorService(Instructor instructor) {
        this.instructor = instructor;
        this.instructorId = instructor.getUserId();
        Cdb = new CourseDatabaseManager();
        Udb = new UsersDatabaseManager();
        this.createdCourses = new ArrayList<Course>();
        try {
            getCreatedCoursesOfInstructor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public InstructorService(String instructorID) {
        Cdb = new CourseDatabaseManager();
        Udb = new UsersDatabaseManager();
        this.createdCourses = new ArrayList<Course>();
        this.instructorId = instructorID;
        this.instructor = (Instructor) Udb.getUser(instructorID);
        if (this.instructor == null) {
            System.out.println("instructor with this id not found ");
        }
        try {
            getCreatedCoursesOfInstructor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // METHOD TO GET ALL CREATED COURSES for AN INSTRUCTOR
    private void getCreatedCoursesOfInstructor() {
        if (instructor == null) {
            System.out.println("getCreatedCoursesOfInstructor :instructor not found");
            return;
        }
        ArrayList<String> createdCoursesIds = instructor.getCreatedCourses();
        ArrayList<Course> allCourses = Cdb.getAllCourses();
        for (int i = 0; i < allCourses.size(); i++) {
            String id = allCourses.get(i).getCourseId();
            if (createdCoursesIds.contains(id)) {
                createdCourses.add(Cdb.getCourse(id));
            }
        }
    }

    // METHODS TO MANIPULATE COURSES
    public void addCourse(Course course) {
        refresh();
        if (instructor == null) {
            System.out.println("addCourse : instructor not found");
            return;
        }
        String id = Cdb.addCourse(course);
        instructor.addCreatedCourse(id);
        try {
            Cdb.update(course);
            Udb.update(instructor);
            try {
                Cdb.SaveCoursesToFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Udb.SaveUsersToFile();
                System.out.println("Course ID:" + course.getCourseId() + " added successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void updateCourse(Course updatedCourse) {
        refresh();
        Course course = Cdb.getCourse(updatedCourse.getCourseId());
        if (course == null) {
            System.out.println("updateCourse : course not registered to be updated");
            return;
        }
        try {
            Cdb.update(updatedCourse);
            Cdb.SaveCoursesToFile();
            System.out.println("Course ID:" + updatedCourse.getCourseId() + " updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCourse(String courseID) {
        refresh();
        Course course = Cdb.getCourse(courseID);
        if (course == null) {
            System.out.println("deleteCourse : course not registered to be deleted");
            return;
        }
        try {
            Cdb.deleteCourse(courseID);
            Cdb.SaveCoursesToFile();
            System.out.println("Course ID:" + courseID + " deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // METHODS TO MANIPULATE LESSONS
    public void addLesson(String courseID, Lesson lesson) {
        refresh();
        Course course = Cdb.getCourse(courseID);
        if (course == null) {
            System.out.println("addLesson : course not registered");
            return;
        }
        if (lesson == null) {
            System.out.println("addLesson : lesson does not exists");
            return;
        }
        try {
            course.addLesson(lesson);
            Cdb.update(course);
            Cdb.SaveCoursesToFile();
            System.out.println("Lesson ID:" + lesson.getLessonId() + " added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editLesson(String courseID, Lesson lesson) {
        refresh();
        Course course = Cdb.getCourse(courseID);
        if (course == null) {
            System.out.println("editLesson : course not registered");
            return;
        }
        if (lesson == null) {
            System.out.println("editLesson : lesson does not exists");
            return;
        }
        try {
            course.editLesson(lesson.getLessonId(), lesson);
            Cdb.update(course);
            Cdb.SaveCoursesToFile();
            System.out.println("Lesson ID:" + lesson.getLessonId() + " edited successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteLesson(String courseID, String lessonID) {
        refresh();
        Course course = Cdb.getCourse(courseID);
        if (course == null) {
            System.out.println("deleteLesson : course not registered");
            return;
        }
        // check if the lesson exists
        Lesson lesson = null;
        for (int i = 0; i < course.getLessons().size(); i++) {
            if (course.getLessons().get(i).getLessonId().equals(lessonID)) {
                lesson = course.getLessons().get(i);
                break;
            }
        }
        if (lesson == null) {
            System.out.println("deleteLesson : lesson does not exists");
            return;
        }
        try {
            course.removeLesson(lessonID);
            Cdb.update(course);
            Cdb.SaveCoursesToFile();
            System.out.println("Lesson ID:" + lessonID + " deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // USED TO KEEP THE SERVICE UP TO DATE
    public void refresh() {
        Cdb = new CourseDatabaseManager();
        Udb = new UsersDatabaseManager();
        this.createdCourses = new ArrayList<Course>();
        this.instructor = (Instructor) Udb.getUser(instructorId);
        if (this.instructor == null) {
            System.out.println("instructor with this id not found");
        }
        try {
            getCreatedCoursesOfInstructor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // getters
    public ArrayList<Course> getCreatedCourses() {
        refresh();
        System.out.println("Created Courses retreived successfully, size:" + createdCourses.size() + ".");
        return createdCourses;
    }

    public ArrayList<Lesson> getLessons(String courseID) {
        refresh();
        Course course = Cdb.getCourse(courseID);
        if (course == null) {
            System.out.println("getLessons : course not registered");
            return null;
        }
        System.out.println("Lessons of Course ID:" + course.getCourseId() + 
        " retreived successfully, size:" + course.getLessons().size() + ".");
        return course.getLessons();
    }

    public ArrayList<Student> getEnrolledStudents(String courseID) {
        refresh();
        Course course = Cdb.getCourse(courseID);
        if (course == null) {
            System.out.println("getEnrolledStudents : course not registered");
            return null;
        }
        ArrayList<String> studentIds = course.getStudents();
        ArrayList<Student> enrolledStudents = new ArrayList<>();
        for (int i = 0; i < studentIds.size(); i++) {
            enrolledStudents.add((Student) Udb.getUser(studentIds.get(i)));
        }
        System.out.println("Enrolled Students of Course ID:" + course.getCourseId() + 
        " retreived successfully, size:" + enrolledStudents.size() + ".");
        return enrolledStudents;
    }

}
