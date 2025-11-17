package backend.ProgramService;

import backend.JsonDatabaseManager.JsonDatabaseManager;
import backend.ProgramFunctions.CourseManagement.Course;
import backend.ProgramFunctions.InstructorManagement.Instructor;
import backend.ProgramFunctions.LessonAndLearningFeatures.Lesson;
import backend.ProgramFunctions.StudentManagement.Student;
import backend.ProgramFunctions.UserAccountManagement.User;
import backend.SecurityAndValidation.PasswordHasher;
import backend.SecurityAndValidation.Validator;
import java.util.ArrayList;
// this class is responsible for every step in backend like loggingg in registering new users creating new coourse and enrolling students and updates,etc
//all these things are handled by this class
public class ProgramService {
    //these variables hold all app info in memory after loading from files
    private JsonDatabaseManager dbManager;
    private ArrayList<User> allUsers;
    private ArrayList<Course> allCourses;

    public ProgramService() {
        this.dbManager = new JsonDatabaseManager();
        // Load all data from files into memory right away
        this.allUsers = dbManager.loadUsers();
        this.allCourses = dbManager.loadCourses(); // solve it in the jsondatabasemanger
    }
public void saveData(){
    dbManager.saveUsers(allUsers);
    dbManager.saveCourses(allCourses);// add it in the json class
}
    public User login(String email, String password) {
        // 1. Validate inputs aren't empty and email is valid
        if (!Validator.isFilled(email) || !Validator.isFilled(password) || !Validator.isValidEmail(email)) {
            return null;
        }
        String hashed = PasswordHasher.hashPassword(password);
        if (hashed == null) return null;

        for (User u : allUsers) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPasswordHash().equals(hashed)) {
                return u; // Success: Found user
            }
        }
        return null; // Failure: No match
    }
public User register(String username, String email, String password, String role){
    if (!Validator.isFilled(username) || !Validator.isFilled(email) || !Validator.isFilled(password)) {
        return null; // Validation failed
    }
    if(!Validator.isValidEmail(email)) return null;

    if(findUserByEmail(email) != null){
        return null; //email in use
    }
int maxId =0;
    for(User u : allUsers){
        try {
            int currentId = Integer.parseInt(u.getUserId().substring(1)); // "U1" -> 1
            if (currentId > maxId) {
                maxId = currentId;
            }}
catch(Exception e){
                // Ignore IDs not in the "U123" format
            }
    }

            String newUserId = "U" + (maxId + 1);

            String hashed = PasswordHasher.hashPassword(password);
            User newUser;

            if (role.equals("student")) {
                newUser = new Student(newUserId, username, email, hashed);
            } else {
                newUser = new Instructor(newUserId, username, email, hashed);
            }

            this.allUsers.add(newUser);
            saveData();
            return newUser;
    }
    public User findUserByEmail(String email) {
        for (User u : allUsers) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }


    public User findUserById(String userId) {
        for (User u : allUsers) {
            if (u.getUserId().equals(userId)) {
                return u;
            }
        }
        return null;
    }

public Course findCourseById(String courseId){
        for(Course c : allCourses){
            if(c.getCourseId().equals(courseId))
                return c;
        }
return null;
}// method to find course by id

    public ArrayList<Course> getAllCourses() {
        return this.allCourses;
    }

public Course createCourse(String title, String description, Instructor instructor){
        if(!Validator.isFilled(title) || !Validator.isFilled(description)){
            return null;
        }
        int maxId = 0 ;
        for (Course c : allCourses){
            try
            {
              int currentId = Integer.parseInt(c.getCourseId().substring(1)); //
              if(currentId > maxId){
                  maxId = currentId;
              }
            }
              catch(Exception e){

            }
            }
    String newCourseId = "C" + (maxId + 1);

//    Course newCourse = new Course(newCourseId, title, description, instructor.getUserId());
//    this.allCourses.add(newCourse);
//    instructor.addCreatedCourse(newCourseId);
    Course newCourse = new Course(newCourseId, title, description, instructor.getUserId());
    this.allCourses.add(newCourse);
    instructor.addCreatedCourse(newCourseId);

    saveData(); // This is also critical
    return newCourse;
}

    public boolean enrollStudentInCourse(Student student, Course course) {
        if (student == null || course == null) return false;

        course.addStudent(student.getUserId());
        student.enrollInCourse(course.getCourseId());

        saveData();
        return true;
    }

    public Lesson addLessonToCourse(Course course, String title, String content) {
        if (course == null || !Validator.isFilled(title) || !Validator.isFilled(content)) {
            return null;
        }

        String newLessonId = course.getCourseId() + "-L" + (course.getLessons().size() + 1);

        Lesson newLesson = new Lesson(newLessonId, title, content);
        course.addLesson(newLesson);
        saveData();
        return newLesson;
    }
    public void updateLesson(Lesson lesson, String title, String content) {
        if (lesson == null) return;
        if (Validator.isFilled(title)) {
            lesson.setTitle(title);
        }
        if (Validator.isFilled(content)) {
            lesson.setContent(content);
        }
        saveData();
    }


    public void deleteLesson(Course course, Lesson lesson) {
        if (course == null || lesson == null) return;
        course.removeLesson(lesson.getLessonId()); //add remove in the class
        saveData();
    }
    public void markLessonComplete(Student student, String courseId, String lessonId) {
        student.markLessonComplete(courseId, lessonId);
        saveData();
    }

}









