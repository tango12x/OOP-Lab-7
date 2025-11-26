package backend.models;

import java.util.ArrayList;
import java.util.HashMap;
import backend.models.parents.User;

public class Student extends User {

    private HashMap<String, studentCourseInfo> enrolledCourses;

    // CUSTOM CLASS FOR STUDENT COURSE INFO
    public static class studentCourseInfo {
        private String courseId;
        private ArrayList<String> progress;
        private Certificate certificate;
        private HashMap<String, ArrayList<Integer>> quizAttempts;

        // CLASS CONSTRUCTORS
        public studentCourseInfo(String courseId) {
            this.courseId = courseId;
            this.progress = new ArrayList<String>();
            this.certificate = null;
            this.quizAttempts = new HashMap<String, ArrayList<Integer>>();
        }

        public studentCourseInfo(String courseId, ArrayList<String> progress, Certificate certificate,
                HashMap<String, ArrayList<Integer>> quizAttempts) {
            this.courseId = courseId;
            this.progress = progress;
            this.certificate = certificate;
            this.quizAttempts = quizAttempts;
        }

        // GETTERS AND SETTERS
        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getCourseId() {
            return courseId;
        }

        public void setCertificate(Certificate certificate) {
            this.certificate = certificate;
        }

        public Certificate getCertificate() {
            return certificate;
        }

        public void setProgress(ArrayList<String> progress) {
            this.progress = progress;
        }

        public ArrayList<String> getProgress() {
            if (progress == null) {
                progress = new ArrayList<String>();
            }
            return progress;
        }

        public void setQuizAttempts(HashMap<String, ArrayList<Integer>> quizAttempts) {
            this.quizAttempts = quizAttempts;
        }

        public HashMap<String, ArrayList<Integer>> getQuizAttempts() {
            if (quizAttempts == null) {
                quizAttempts = new HashMap<String, ArrayList<Integer>>();
            }
            return quizAttempts;
        }

        public int numCompleted() {
            return progress.size();
        }

        // MARK LESSON AS COMPLETE
        public void markLessonComplete(String lessonId) {
            if (lessonId.trim().isEmpty() || lessonId == null) {
                System.out.println("markLessonComplete: lessonId is null or empty");
                return;
            }
            if (progress == null) {
                progress = new ArrayList<String>();
            }
            if (!progress.contains(lessonId)) {
                progress.add(lessonId);
            }
        }

        // // ADD QUIZ ATTEMPT
        // public void addQuizAttempt(String lessonId, int score) {
        // if(quizAttempts == null) {
        // quizAttempts = new HashMap<String, ArrayList<Integer>>();
        // System.out.println("addQuizAttempt: quizAttempts is null , intiating new
        // course attempts");
        // }
        // if (lessonId.trim().isEmpty() || lessonId == null) {
        // System.out.println("addQuizAttempt: lessonId is null or empty");
        // return;
        // }
        // ArrayList<Integer> lessonQuizAttempts = quizAttempts.get(lessonId);
        // if (lessonQuizAttempts == null) {
        // System.out.println("addQuizAttempt: lesson attempts for course ID:" +
        // courseId +
        // "for lesson ID:" + lessonId );
        // // adding array list
        // quizAttempts.put(lessonId, new ArrayList<Integer>());
        // }
        // lessonQuizAttempts.add(score);
        // System.out.println("Quiz attempt added successfully for lesson: " +
        // lessonId);

        // }
        // ADD QUIZ ATTEMPT - Fixed version
        public void addQuizAttempt(String lessonId, int score) {
            if (quizAttempts == null) {
                quizAttempts = new HashMap<String, ArrayList<Integer>>();
                System.out.println("addQuizAttempt: quizAttempts is null, initiating new course attempts");
            }
            if (lessonId == null || lessonId.trim().isEmpty()) {
                System.out.println("addQuizAttempt: lessonId is null or empty");
                return;
            }

            // Get or create the list for this lesson
            ArrayList<Integer> lessonQuizAttempts = quizAttempts.get(lessonId);
            if (lessonQuizAttempts == null) {
                System.out.println("addQuizAttempt: lesson attempts for course ID:" + courseId +
                        " for lesson ID:" + lessonId +
                        " is null, initiating new course attempts");
                lessonQuizAttempts = new ArrayList<Integer>();
                quizAttempts.put(lessonId, lessonQuizAttempts);
            }

            // Now add the score - lessonQuizAttempts is guaranteed to be not null
            lessonQuizAttempts.add(score);
            System.out.println("Quiz attempt added successfully for lesson: " + lessonId +
                    " with score: " + score);
        }

    }

    // CLASS CONSTRUCTOR IN CASE OF ID IS GIVEN
    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, "student", username, email, passwordHash);
        this.enrolledCourses = new HashMap<String, studentCourseInfo>();
    }

    // OVERLOADING CONSTRUCTOR IN CASE OF ID IS NOT GIVEN
    public Student(String username, String email, String passwordHash) {
        super("student", username, email, passwordHash);
        this.enrolledCourses = new HashMap<String, studentCourseInfo>();
    }

    // standard getters and setters
    public HashMap<String, studentCourseInfo> getEnrolledCourses() {
        if (enrolledCourses == null)
            enrolledCourses = new HashMap<String, studentCourseInfo>();
        return enrolledCourses;
    }

    public void setEnrolledCourses(HashMap<String, studentCourseInfo> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    // ENROLL COURSE IF NOT ALREADY ENROLLED
    public void enroll(String courseId) {
        if (courseId == null || courseId.trim().isEmpty())
            return;
        if (enrolledCourses.containsKey(courseId)) {
            System.out.println("enroll: student is already enrolled in course ID:" + courseId);
            return;
        }
        studentCourseInfo newCourseInfo = new studentCourseInfo(courseId);
        enrolledCourses.put(courseId, newCourseInfo);
    }

    public int numCompleted(String courseId) {
        if (courseId == null || courseId.trim().isEmpty())
            return -1;
        if (!isEnrolled(courseId)) {
            System.out.println("markLessonComplete: student is not enrolled in course ID:" + courseId);
            return -1;
        }
        return enrolledCourses.get(courseId).numCompleted();
    }

    // MARK LESSON AS COMPLETE
    public void markLessonComplete(String courseId, String lessonId) {
        if (courseId == null || courseId.trim().isEmpty())
            return;
        if (!isEnrolled(courseId)) {
            System.out.println("markLessonComplete: student is not enrolled in course ID:" + courseId);
            return;
        }
        enrolledCourses.get(courseId).markLessonComplete(lessonId);
    }

    // return if student is enrolled in the course or not
    public boolean isEnrolled(String courseId) {
        return enrolledCourses.containsKey(courseId);
    }

    // adds a certificate to student
    public void addCertificate(String courseId, Certificate certificate) {
        if (courseId == null || courseId.trim().isEmpty()) {
            System.out.println("addCertificate: course ID is null or empty");
            return;
        }
        if (!isEnrolled(courseId)) {
            System.out.println("addCertificate: student is not enrolled in course ID:"
                    + courseId);
            return;
        }
        if (!courseId.equals(certificate.getCourseId())) {
            System.out.println("addCertificate: course ID does not match certificate course ID");
            return;
        }
        enrolledCourses.get(courseId).setCertificate(certificate);
    }

    // adds a quiz attempt
    public void addQuizAttempt(String courseId, String lessonId, int score) {
        if (courseId == null || courseId.trim().isEmpty()) {
            System.out.println("addCertificate: course ID is null or empty");
            return;
        }
        if (!isEnrolled(courseId)) {
            System.out.println("addCertificate: student is not enrolled in course ID:"
                    + courseId);
            return;
        }
        enrolledCourses.get(courseId).addQuizAttempt(lessonId, score);
    }
}
