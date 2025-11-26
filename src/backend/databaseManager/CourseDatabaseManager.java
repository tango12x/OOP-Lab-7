package backend.databaseManager;

import backend.models.*;

import java.util.ArrayList;


public class CourseDatabaseManager {
    private ArrayList<Course> courses;
    private ReadWrite db;
    private final String COURSES_FILE = "data/DatabaseJSONFiles/courses.json";

    // CLASS CONSTRUCTOR and INITIALIZER (READS THE FILE OR CREATES A NEW ONE IF NOT
    // EXIST)
    public CourseDatabaseManager() {
        db = new ReadWrite();
        courses = db.readFromFile(COURSES_FILE, Course.class);
    }

    // METHOD TO SEARCH AND RETURN THE COURSE IF EXIST IN THE DB
    public Course getCourse(String courseId) {
        try {
            if (courseId == null) {

                return null;
            }
            if (this.courses.size() == 0) {
                return null; // No users in the database
            }
            for (int i = 0; i < this.courses.size(); i++) {
                Course tempCourse = this.courses.get(i);
                if (tempCourse.getCourseId().equals(courseId)) {
                    // the line written under here is to make an new instance of courses array
                    // as editing the object passed as a reference may alter the data in the array
                    // so to prevent that we make a new instance of the array
                    this.courses = db.readFromFile(COURSES_FILE, Course.class);
                    return tempCourse;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // METHOD TO ADD A COURSE and RETURN THE GENERATED ID (USED AT COURSE CREATION
    // FUNCTIONALITY ONLY)
    public String addCourse(Course newCourse) {
        try {
            if (newCourse == null) {
                return "";
            }
            String id = generateId();
            newCourse.setCourseId(id);
            Course course = newCourse;
            this.courses.add(course);
            SaveCoursesToFile();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // USED TO UPDATE THE COURSE DETAILS (LIKE LESSONS ADDED OR STUDENTS ENROLLED)
    public void update(Course updatedCourse) {
        try {
            if (updatedCourse != null) {
                boolean found = false;
                String id = updatedCourse.getCourseId();
                for (int i = 0; i < courses.size(); i++) {
                    if (courses.get(i).getCourseId().equals(id)) {
                        found = true;
                        // courses.remove(i);
                        // courses.add(updatedCourse);
                        courses.set(i, updatedCourse);
                        SaveCoursesToFile();
                    }
                }
                if (!found) {
                    courses.add(updatedCourse);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // METHOD TO SAVE COURSES TO FILE
    public void SaveCoursesToFile() {
        try {
            db.writeToFile(COURSES_FILE, courses);
            // updates the courses array
            this.courses = db.readFromFile(COURSES_FILE, Course.class);
            System.out.println("Courses saved successfully to file.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Courses NOT saved successfully to file.");
        }
    }

    // METHOD TO DELETE A COURSE FROM THE DB
    public void deleteCourse(String courseId) {
        try {
            for (int i = 0; i < courses.size(); i++) {
                Course course = courses.get(i);
                if (course.getCourseId().equals(courseId)) {
                    courses.remove(i);
                    SaveCoursesToFile();
                    System.out.println("Course deleted successfully.");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // METHOD TO RETURN ALL COURSES JSON ARRAY (FOR VALIDATION PURPOSES)
    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> tempCourses = this.courses;
        this.courses = db.readFromFile(COURSES_FILE, Course.class);
        return tempCourses;
    }

    // METHOD TO GENERATE A UNIQUE ID
    public String generateId() {
        return "C" + String.format("%d", this.courses.size() + 1);
    }

    // APPROVE A COURSE
    public void approveCourse(String courseId,String adminId) {
        Course course = getCourse(courseId);
        if (course == null) {
            System.out.println("approveCourse: Course not found.");
            return;
        }
        try {
            course.setApprovalStatus("APPROVED");
            course.setApprovedBy(adminId);
            update(course);
            SaveCoursesToFile();
            System.out.println("Course ID:" + course.getCourseId() + " approved successfully" +
            "by admin ID:" + course.getApprovedBy());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // REJECT A COURSE
    public void rejectCourse(String courseId,String adminId) {
        Course course = getCourse(courseId);
        if (course == null) {
            System.out.println("rejectCourse : Course not found.");
            return;
        }
        try {
            course.setApprovalStatus("REJECTED");
            course.setApprovedBy(adminId);
            update(course);
            SaveCoursesToFile();
            System.out.println("Course ID:" + course.getCourseId() + " rejected successfully" +
            "by admin ID:" + course.getApprovedBy());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET PENDING COURSES, returns an empty arraylist if there are no pending
    // courses
    public ArrayList<Course> getPendingCourses() {
        ArrayList<Course> pendingList = new ArrayList<Course>();
        try {
            for (int i = 0; i < courses.size(); i++) {
                Course course = courses.get(i);
                if (course.getApprovalStatus().equals("PENDING")) {
                    // the line under is written like that to avoid passing the object as a
                    // reference
                    pendingList.add(getCourse(course.getCourseId()));
                }
            }
            System.out.println("returning pending list of courses , size: " + pendingList.size());
            return pendingList;
        } catch (Exception e) {
            e.printStackTrace();
            return pendingList;
        }
    }

    // GET APPROVED COURSES, returns an empty arraylist if there are no approved
    // courses
    public ArrayList<Course> getApprovedCourses() {
        ArrayList<Course> approvedList = new ArrayList<Course>();
        try {
            for (int i = 0; i < courses.size(); i++) {
                Course course = courses.get(i);
                if (course.getApprovalStatus().equals("APPROVED")) {
                    // the line under is written like that to avoid passing the object as a
                    // reference
                    approvedList.add(getCourse(course.getCourseId()));
                }
            }
            System.out.println("returning approved list of courses , size: " + approvedList.size());
            return approvedList;
        } catch (Exception e) {
            e.printStackTrace();
            return approvedList;
        }
    }

    // GET REJECTED COURSES, returns an empty arraylist if there are no rejected
    // courses
    public ArrayList<Course> getRejectedCourses() {
        ArrayList<Course> rejectedList = new ArrayList<Course>();
        try {
            for (int i = 0; i < courses.size(); i++) {
                Course course = courses.get(i);
                if (course.getApprovalStatus().equals("REJECTED")) {
                    // the line under is written like that to avoid passing the object as a
                    // reference
                    rejectedList.add(getCourse(course.getCourseId()));
                }
            }
            System.out.println("returning rejected list of courses , size: " + rejectedList.size());
            return rejectedList;
        } catch (Exception e) {
            e.printStackTrace();
            return rejectedList;
        }
    }

    // For testing purposes only
    public static void main(String[] args) {
        // test for all methods here
        CourseDatabaseManager coursesDB = new CourseDatabaseManager();
        Course newCourse = new Course("C1", "I1", "Description 11");
        coursesDB.addCourse(newCourse);
        Course course = coursesDB.getCourse("C1");
        ArrayList<Lesson> lessons = new ArrayList<Lesson>();
        lessons.add(new Lesson("L1", "Lesson 1", "C11", "Content 1"));
        lessons.add(new Lesson("L2", "Lesson 2", "C11", "Content 2"));
        course.setLessons(lessons);
        coursesDB.update(course);
        coursesDB.SaveCoursesToFile();

    }

}