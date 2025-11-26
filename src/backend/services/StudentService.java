package backend.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import backend.databaseManager.*;
import backend.models.*;
import backend.models.Student.studentCourseInfo;
import frontend.student.CertificateViewerFrame;

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
        try {
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
        try {
            getAvailableCoursesAndInstructors();
            getEnrolledCoursesAndInstructors();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // METHOD TO GET ALL available COURSES AND INSTRUCTORS for a STUDENT (excluding
    // ENROLLED)
    private void getAvailableCoursesAndInstructors() {
        ArrayList<Course> allCourses = Cdb.getApprovedCourses();
        for (int i = 0; i < allCourses.size(); i++) {
            String id = allCourses.get(i).getCourseId();
            if (!student.getEnrolledCourses().containsKey(id)) {
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
        ArrayList<Course> allCourses = Cdb.getAllCourses();
        for (int i = 0; i < allCourses.size(); i++) {
            String id = allCourses.get(i).getCourseId();
            if (student.getEnrolledCourses().containsKey(id)) {
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
        // add enrolled to list of enrolled
        enrolledInstructors.add((Instructor) Udb.getUser(course.getInstructorId()));
        enrolledCourses.add(course);
        // delete enrolled course from list of available
        for (int i = 0; i < availableCourses.size(); i++) {
            if (courseID.equals(availableCourses.get(i).getCourseId())) {
                availableInstructors.remove(i);
                availableCourses.remove(i);
                break;
            }
        }
    }

    // METHOD TO MARK A LESSON COMPLETED PERMANENTLY
    public void markLessonCompleted(String courseID, String lessonID) {
        student.markLessonComplete(courseID, lessonID);
        if (student.numCompleted(courseID) == Cdb.getCourse(courseID).getLessons().size()) {
            System.out.println("Student ID: " + student.getUserId() + " has completed Course: " + courseID);
            Certificate cert = generateCertificate(courseID);
            student.addCertificate(courseID, cert);
            System.out.println("Certificate generated for course: " + courseID + 
            " for student: " + student.getUserId());
        }
        Udb.update(student);
        Udb.SaveUsersToFile();
    }

    //!NTST
    // GENERATE CERTIFICATE
    public Certificate generateCertificate(String courseID) {
        System.out.println("Generating certificate for course: " + courseID + " for student: " + student.getUserId());
        String studentId = student.getUserId();
        String studentName = student.getUsername();
        String courseTitle = Cdb.getCourse(courseID).getTitle();
        // !MILDLY ERROR PRONE
        String instructorName = Udb.getUser(Cdb.getCourse(courseID).getInstructorId()).getUsername();
        double finalScore = 0;
        studentCourseInfo courseInfo = student.getEnrolledCourses().get(courseID);
        // EACH LESSION HAS 100 MARKS
        double totalMarksofCourse = Cdb.getCourse(courseID).getLessons().size() * 100;
        // GET MAX OF EACH ATTEMPT ,LAMBDA EXPRESSION IS USED
        double totalMarksObtained = courseInfo.getQuizAttempts().values().stream()
                .filter(attempts -> !attempts.isEmpty())
                .mapToInt(Collections::max)
                .sum();
        finalScore = (double) (totalMarksObtained / totalMarksofCourse) * 100;
        Certificate cert = new Certificate(studentId, studentName,
                courseID, courseTitle, instructorName, finalScore);
        return cert;
    }

    // METHOD TO RETURN IDs OF COMPLETED LESSONS
    public ArrayList<String> getCompletedLesson(String courseID) {
        return student.getEnrolledCourses().get(courseID).getProgress();
    }

    // NOT IMPORTANT AS EVERYTHING IS ALWAYS REFRESHED
    public void refresh() {
        Cdb = new CourseDatabaseManager();
        Udb = new UsersDatabaseManager();
        this.student = (Student) Udb.getUser(student.getUserId());
        System.out.println("student with this id not found");
        this.availableCourses = new ArrayList<Course>();
        this.availableInstructors = new ArrayList<Instructor>();
        this.enrolledCourses = new ArrayList<Course>();
        this.enrolledInstructors = new ArrayList<Instructor>();
        try {
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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        
        StudentService s = new StudentService("U2");
        Certificate c = s.generateCertificate("C1");
        CertificateViewerFrame viewer = new CertificateViewerFrame(c);
                    viewer.setVisible(true);
                    viewer.setLocationRelativeTo(null);
    }
}
