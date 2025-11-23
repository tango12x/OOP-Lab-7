package backend.services;

import org.json.JSONArray;
import java.util.ArrayList;
import backend.databaseManager.*;
import backend.models.*;
import backend.util.*;

public class InstructorService {
    private Instructor instructor;
    private CourseDatabaseManager Cdb;
    private UsersDatabaseManager Udb;
    private ArrayList<Course> createdCourses;


    // CLASS CONSTRUCTORS
    public InstructorService(Instructor instructor) {
        this.instructor = instructor;
        Cdb = new CourseDatabaseManager();
        Udb = new UsersDatabaseManager();
        this.createdCourses = new ArrayList<Course>();
        try{
            getCreatedCoursesOfInstructor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public InstructorService(String instructorID) {
        Cdb = new CourseDatabaseManager();
        Udb = new UsersDatabaseManager();
        this.createdCourses = new ArrayList<Course>();
        this.instructor = (Instructor) Udb.getUser(instructorID);
        if (this.instructor == null ) {
            System.out.println("instructor with this id not found");
        }
        try{
            getCreatedCoursesOfInstructor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    // METHOD TO GET ALL CREATED COURSES for AN INSTRUCTOR 
    private void getCreatedCoursesOfInstructor() {
        ArrayList<String> createdCoursesIds = instructor.getCreatedCourses();
        ArrayList<Course> allCourses = Cdb.getAllCourses();
        for (int i = 0; i < allCourses.size(); i++) {
            String id = allCourses.get(i).getCourseId();
            if (createdCoursesIds.contains(id)) {
                createdCourses.add(Cdb.getCourse(id));
            }
        }
    }
    
    public void deleteCourse(String courseID) {
        Cdb.deleteCourse(courseID);
        Cdb.SaveCoursesToFile();
        System.out.println("Course deleted successfully.");
    }

    public void addLesson(String courseID,Lesson lesson) {
        Course course = Cdb.getCourse(courseID);
        course.addLesson(lesson);
        Cdb.update(course);
        Cdb.SaveCoursesToFile();
    }

    public void deleteLesson(String courseID, String lessonID) {
        Course course = Cdb.getCourse(courseID);
        for (int i = 0; i < course.getLessons().size(); i++) {
            if(course.getLessons().get(i).getLessonId().equals(lessonID)){
                course.getLessons().remove(i);
            }
        }
        Cdb.update(course);
        Cdb.SaveCoursesToFile();
    }

    public void editLesson(String courseID, Lesson lesson) {
        Course course = Cdb.getCourse(courseID);
        course.editLesson(lesson.getLessonId(),lesson);
        Cdb.update(course);
        Cdb.SaveCoursesToFile();
    }


    public void addCreatedCourse(Course course) {
        String id = Cdb.addCourse(course);
        instructor.addCreatedCourse(id);
        Cdb.update(course);
        Cdb.SaveCoursesToFile();
    }

    //NOT IMPORTANT AS EVERYTHING IS ALWAYS REFRESHED 
    public void refresh(){
        Cdb = new CourseDatabaseManager();
        Udb = new UsersDatabaseManager();
        this.createdCourses = new ArrayList<Course>();
        this.instructor = (Instructor) Udb.getUser(instructor.getUserId());
        if (this.instructor == null ) {
            System.out.println("instructor with this id not found");
        }
        try{
            getCreatedCoursesOfInstructor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // standard getters
    public ArrayList<Course> getCreatedCourses() {
        return createdCourses;
    }
}
