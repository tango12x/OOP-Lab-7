package backend.ProgramFunctions.StudentManagement;

import org.json.JSONArray;
import java.util.ArrayList;
import backend.ProgramFunctions.CourseManagement.*;
import backend.ProgramFunctions.InstructorManagement.*;
import backend.JsonDatabaseManager.*;

public class StudentService {
    private Student student;
    private CourseDatabaseManager Cdb;
    private UsersDatabaseManager Udb;
    private ArrayList<Course> availableCourses;
    private ArrayList<Instructor> availableInstructors;
    private ArrayList<Course> enrolledCourses;
    private ArrayList<Instructor> enrolledInstructors;


    // CLASS CONSTRUCTORS
    public StudentService(Student student) {
        this.student = student;
        Cdb = new CourseDatabaseManager();
        Udb = new UsersDatabaseManager();
        this.availableCourses = new ArrayList<Course>();
        this.availableInstructors = new ArrayList<Instructor>();
        this.enrolledCourses = new ArrayList<Course>();
        this.enrolledInstructors = new ArrayList<Instructor>();
        try{
            getAvailableCoursesAndInstructors();
            getEnrolledCoursesAndInstructors();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StudentService(String studentId) {
        Cdb = new CourseDatabaseManager();
        Udb = new UsersDatabaseManager();
        this.student = (Student) Udb.getUser(studentId);
        if (this.student == null) {
            System.out.println("student with this id not found");
        }
        this.availableCourses = new ArrayList<Course>();
        this.availableInstructors = new ArrayList<Instructor>();
        this.enrolledCourses = new ArrayList<Course>();
        this.enrolledInstructors = new ArrayList<Instructor>();
        try{
            getAvailableCoursesAndInstructors();
            getEnrolledCoursesAndInstructors();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // METHOD TO GET ALL available COURSES AND INSTRUCTORS for a STUDENT (excluding
    // ENROLLED)
    private void getAvailableCoursesAndInstructors() {
        ArrayList<String> enrolledCourseIds = student.getEnrolledCourses();
        JSONArray allCourses = Cdb.getAllCourses();
        for (int i = 0; i < allCourses.length(); i++) {
            String id = allCourses.getJSONObject(i).getString("courseId");
            if (!enrolledCourseIds.contains(id)) {
                availableCourses.add(Cdb.getCourse(id));
                Course course = Cdb.getCourse(id);
                Instructor instructor = (Instructor) Udb.getUser(course.getInstructorId());
                this.availableInstructors.add(instructor);
            }
        }
    }

    // METHOD TO GET ALL available COURSES AND INSTRUCTORS for a STUDENT (excluding
    // ENROLLED)
    private void getEnrolledCoursesAndInstructors() {
        ArrayList<String> enrolledCourseIds = student.getEnrolledCourses();
        JSONArray allCourses = Cdb.getAllCourses();
        for (int i = 0; i < allCourses.length(); i++) {
            String id = allCourses.getJSONObject(i).getString("courseId");
            if (enrolledCourseIds.contains(id)) {
                enrolledCourses.add(Cdb.getCourse(id));
                Course course = Cdb.getCourse(id);
                Instructor instructor = (Instructor) Udb.getUser(course.getInstructorId());
                this.enrolledInstructors.add(instructor);
            }
        }
    }

    // METHOD TO ENROLL A STUDENT IN A COURSE AND SAVE THE DATA PERMANENTLY
    public void enrollInCourse(String courseID) {
        student.enroll(courseID);
        Course course = Cdb.getCourse(courseID);
        course.addStudent(student.getUserId());
        Udb.update(student);
        Udb.SaveUsersToFile();
        Cdb.update(course);
        Cdb.SaveCoursesToFile();
        //add enrolled to list of enrolled
        enrolledInstructors.add((Instructor) Udb.getUser(course.getInstructorId()));
        enrolledCourses.add(course);
        //delete enrolled course from list of available
        for (int i = 0; i < availableCourses.size(); i++) {
            if(courseID.equals(availableCourses.get(i).getCourseId())) {
                availableInstructors.remove(i);
                availableCourses.remove(i);
                break;
            }
        }
    }

    // METHOD TO MARK A LESSON COMPLETED PERMANENTLY
    public void markLessonCompleted(String courseID, String lessonID) {
        student.markLessonComplete(courseID, lessonID);
        Udb.update(student);
        Udb.SaveUsersToFile();
    }

    //METHOD TO RETURN IDs OF COMPLETED LESSONS
    public ArrayList<String> getCompletedLesson(String courseID) {
        int id = student.findCourseIndex(courseID);
        return student.getProgress().get(id);
    }

    //NOT IMPORTANT AS EVERYTHING IS ALWAYS REFRESHED 
    public void refresh(){
        Cdb = new CourseDatabaseManager();
        Udb = new UsersDatabaseManager();
        this.student = (Student) Udb.getUser(student.getUserId());
        System.out.println("student with this id not found");
        this.availableCourses = new ArrayList<Course>();
        this.availableInstructors = new ArrayList<Instructor>();
        this.enrolledCourses = new ArrayList<Course>();
        this.enrolledInstructors = new ArrayList<Instructor>();
        try{
            getAvailableCoursesAndInstructors();
            getEnrolledCoursesAndInstructors();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // standard getters
    public ArrayList<Course> getAvailableCourses() {
        return availableCourses;
    }
    public ArrayList<Instructor> getAvailableInstructors() {
        return availableInstructors;
    }
    public ArrayList<Course> getEnrolledCourses() {
        return enrolledCourses;
    }
    public ArrayList<Instructor> getEnrolledInstructors() {
        return enrolledInstructors;
    }
    public ArrayList<ArrayList<String>> getProgress() {
        return student.getProgress();
    }
}
