package backend.models;

import java.util.ArrayList;

import backend.models.parents.User;


public class Student extends User {
    private ArrayList<String> enrolledCourses;
    private ArrayList<ArrayList<String>> progress;
    private ArrayList<Certificate> certificates;
    //RELATION BETWEEN COURSES AND STUDENT IS AGGREGATION
    //RELATION BETWEEN PROGRESS AND STUDENT IS AGGREGATION

    //CLASS CONSTRUCTOR IN CASE OF ID IS GIVEN
    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, "student", username, email, passwordHash);
        this.enrolledCourses = new ArrayList<String>();
        this.progress = new ArrayList<ArrayList<String>>();
        this.certificates = new ArrayList<Certificate>();
    }
    //OVERLOADING CONSTRUCTOR IN CASE OF ID IS NOT GIVEN
    public Student(String username, String email, String passwordHash) {
        super("student", username, email, passwordHash);
        this.enrolledCourses = new ArrayList<String>();
        this.progress = new ArrayList<ArrayList<String>>();
        this.certificates = new ArrayList<Certificate>();
    }

    //standard getters and setters
    public ArrayList<String> getEnrolledCourses() {
        if(enrolledCourses == null) enrolledCourses = new ArrayList<>();
        return enrolledCourses;
    }
    public void setEnrolledCourses(ArrayList<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }
    public ArrayList<ArrayList<String>> getProgress() {
        return progress;
    }
    public void setProgress(ArrayList<ArrayList<String>> progress) {
        this.progress = progress;
    }
    public ArrayList<Certificate> getCertificates() {
        return certificates;
    }
    public void setCertificates(ArrayList<Certificate> certificates) {
        this.certificates = certificates;
    }

    //ENROLL COURSE IF NOT ALREADY ENROLLED
    public void enroll(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) return;
        for (String id : enrolledCourses) {
            if (id.equals(courseId)) {
                return;}}
        enrolledCourses.add(courseId);
        progress.add(new ArrayList<>());}

    // FIND THE COURSE INDEX IN ENROLLED COURSE USING COURSE ID
    public int findCourseIndex(String courseId) {
        for (int i = 0; i < enrolledCourses.size(); i++) {
            if (enrolledCourses.get(i).equals(courseId)) {
                return i;}}
        return -1;}

    //MARK LESSON AS COMPLETE
    public void markLessonComplete(String courseId, String lessonId) {
        if (courseId == null || lessonId == null) return;
        if (courseId.trim().isEmpty() || lessonId.trim().isEmpty()) return;
        int index = findCourseIndex(courseId);
        if (index == -1) {
            return;}
        ArrayList<String> completedLessons = progress.get(index);
        if (!completedLessons.contains(lessonId)) {
            completedLessons.add(lessonId);}}

    //return if student is enrolled in the course or not
    public boolean isEnrolled(String courseId){
        for (String id : enrolledCourses) {
            if (id.equals(courseId)) {
                return true;}
            }
            return false;
    }
    public void addCertificate(Certificate certificate) {
        certificates.add(certificate);
    }

}
