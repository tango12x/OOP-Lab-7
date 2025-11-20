package backend.JsonDatabaseManager;

import org.json.JSONArray;
import org.json.JSONObject;

import backend.ProgramFunctions.UserAccountManagement.User;
import backend.ProgramFunctions.CourseManagement.Course;
import backend.ProgramFunctions.InstructorManagement.Instructor;
import backend.ProgramFunctions.StudentManagement.Student;
import backend.ProgramFunctions.LessonAndLearningFeatures.Lesson;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CourseDatabaseManager {
    private JSONArray courses;
    private final String COURSES_FILE = "data/DatabaseJSONFiles/courses.json";

    // CLASS CONSTRUCTOR and INITIALIZER (READS THE FILE OR CREATES A NEW ONE IF NOT
    // EXIST)
    public CourseDatabaseManager() {
        try {
            File file = new File(COURSES_FILE);
            String content = Files.readString(Paths.get(COURSES_FILE));
            System.out.println("Users loaded successfully.");
            this.courses = new JSONArray(content);
            System.out.println("Users loaded successfully2.");
        } catch (Exception e) {
            e.printStackTrace();
            // If file not found, create a new one
            File file = new File(COURSES_FILE);
            if (!file.exists()) {
                try {
                    if (file.createNewFile()) {
                        System.out.println("File created: " + file.getName());
                    }
                } catch (IOException e2) {
                    System.out.println("An error occurred.");
                    e2.printStackTrace();
                }
            }
            this.courses = new JSONArray();
            System.out.println("No existing users file found. Starting with an empty course database.");
        }

    }

    // METHOD TO SEARCH AND RETURN THE INDEX OF THE COURSE IF EXIST IN THE DB , -1
    // IF
    public int SearchCourseIndex(String courseId) {
        for (int i = 0; i < this.courses.length(); i++) {
            JSONObject obj = this.courses.getJSONObject(i);
            if (obj.getString("courseId").equals(courseId)) {
                return i; // Return the index if found
            }
        }
        return -1; // Return -1 if not found
    }

    // METHOD TO SEARCH AND RETURN THE COURSE IF EXIST IN THE DB
    public Course getCourse(String courseId) {
        Course course = null;
        try {
            if (this.courses.length() == 0) {
                return null; // No users in the database
            }
            // Search for the userId
            int userIndex = SearchCourseIndex(courseId);
            if (userIndex == -1) {
                return null; // User not found
            }
            JSONObject obj = this.courses.getJSONObject(userIndex);
            String title = obj.getString("title");
            String instructorId = obj.getString("instructorId");
            String description = obj.getString("description");
            course = new Course(courseId, title, instructorId, description);
            JSONArray students = obj.getJSONArray("students");
            course.setStudents(JsonDatabaseManager.toStringList(students));
            JSONArray lessons = obj.getJSONArray("lessons");
            course.setLessons(JSONToLesson(lessons));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return course;
    }

    // METHOD TO ADD A COURSE and RETURN THE GENERATED ID (USED AT COURSE CREATION
    // FUNCTIONALITY ONLY)
    public String addCourse(Course newCourse) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("courseId", generateId());
            obj.put("title", newCourse.getTitle());
            obj.put("instructorId", newCourse.getInstructorId());
            obj.put("description", newCourse.getDescription());

            // optional fields if exit now
            obj.put("lessons", newCourse.getLessons() == null ? new ArrayList<Lesson>()
                    : LessonToJSON(newCourse.getLessons()));
            obj.put("students", newCourse.getStudents() == null ? new ArrayList<String>()
                    : JsonDatabaseManager.toJSONArray(newCourse.getStudents()));
            this.courses.put(obj);
            return obj.getString("courseId");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // USED TO UPDATE THE COURSE DETAILS (LIKE LESSONS ADDED OR STUDENTS ENROLLED)
    public void update(Course updatedCourse) {
        int courseIndex = SearchCourseIndex(updatedCourse.getCourseId());
        if (courseIndex == -1) {
            addCourse(updatedCourse);
            return;
        }
        try {
            JSONObject obj = this.courses.getJSONObject(courseIndex);
            // optional fields if exit now
            obj.put("lessons", updatedCourse.getLessons() == null ? new ArrayList<Lesson>()
                    : LessonToJSON(updatedCourse.getLessons()));
            obj.put("students", updatedCourse.getStudents() == null ? new ArrayList<String>()
                    : JsonDatabaseManager.toJSONArray(updatedCourse.getStudents()));
            //this.courses.put(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // METHOD TO SAVE COURSES TO FILE
    public void SaveCoursesToFile() {
        try {
            FileWriter writer = new FileWriter(COURSES_FILE);
            writer.write(this.courses.toString(4)); // pretty print
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // METHOD TO DELETE A COURSE FROM THE DB
    public void deleteCourse(String courseId) {
        int courseIndex = SearchCourseIndex(courseId);
        if (courseIndex != -1) {
            this.courses.remove(courseIndex);
        }
    }

    // METHOD TO RETURN ALL COURSES JSON ARRAY (FOR VALIDATION PURPOSES)
    public JSONArray getAllCourses() {
        return this.courses;
    }

    // METHOD TO GENERATE A UNIQUE ID
    public String generateId() {
        return "C"+String.format("%d", this.courses.length() + 1);
    }

    //Convert JSONArray to ArrayList<Lesson>
    public ArrayList<Lesson> JSONToLesson(JSONArray jsonArray) {
        ArrayList<Lesson> lessonsArray = new ArrayList<Lesson>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String lessonId = obj.getString("lessonId");
            String title = obj.getString("title");
            String content = obj.getString("content");
            String courseId = obj.getString("courseId");
            Lesson lesson = new Lesson(lessonId, title, courseId, content);
            lesson.setOptionalResources(JsonDatabaseManager.toStringList(obj.getJSONArray("optionalResources")));
            lessonsArray.add(lesson);
        }
        return lessonsArray;
    }

    //Convert ArrayList<Lesson> to JSONArray
    public JSONArray LessonToJSON( ArrayList<Lesson> lessonsArray) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < lessonsArray.size(); i++) {
            Lesson obj = lessonsArray.get(i);
            JSONObject lesson = new JSONObject();
            lesson.put("lessonId",  obj.getLessonId());
            lesson.put("title",  obj.getTitle());
            lesson.put("content",  obj.getContent());
            lesson.put("courseId",  obj.getCourseId());
            lesson.put("optionalResources", JsonDatabaseManager.toJSONArray(obj.getOptionalResources()));
            //add object to json array
            jsonArray.put(lesson);
        }
        return jsonArray;
    }

    //For testing purposes only
    public static void main(String[] args) {
        //test for all methods here
        CourseDatabaseManager coursesDB = new CourseDatabaseManager();
        Course course = coursesDB.getCourse("C1");
        ArrayList<Lesson> lessons = new ArrayList<Lesson>();
        lessons.add(new Lesson("L1", "Lesson 1", "C11", "Content 1"));
        lessons.add(new Lesson("L2", "Lesson 2", "C11", "Content 2"));
        course.setLessons(lessons);
        coursesDB.update(course);
        Course newCourse = new Course("Course 11", "I1", "Description 11");
        coursesDB.addCourse(newCourse);
        coursesDB.SaveCoursesToFile();

    }
    
}