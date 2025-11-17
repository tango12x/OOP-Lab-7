
package backend.JsonDatabaseManager;


import backend.ProgramFunctions.CourseManagement.Course;
import backend.ProgramFunctions.InstructorManagement.Instructor;
import backend.ProgramFunctions.LessonAndLearningFeatures.Lesson;
import backend.ProgramFunctions.StudentManagement.Student;
import backend.ProgramFunctions.UserAccountManagement.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class JsonDatabaseManager {

    private final String USERS_FILE = "users.json";
    private final String COURSES_FILE = "data/courses.json";

    public JsonDatabaseManager() {
        // Ensure the data directory exists
        new File("data").mkdirs();
    }


    public ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        String content = readFile(USERS_FILE);
        if (content.isEmpty()) return users;

        // Parse JSON array
        JSONArray arr = new JSONArray(content);

        // Convert JSON → User objects
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            String userId = obj.getString("userId");
            String role = obj.getString("role");
            String username = obj.getString("username");
            String email = obj.getString("email");
            String passwordHash = obj.getString("passwordHash");

            if (role.equals("student")) {
                Student s = new Student(userId, username, email, passwordHash);

                JSONArray enrolledArr = obj.optJSONArray("enrolledCourses");
                if (enrolledArr != null) {
                    ArrayList<String> courseIds = new ArrayList<>();
                    for (int j = 0; j < enrolledArr.length(); j++) {
                        courseIds.add(enrolledArr.getString(j));
                    }
                    s.setEnrolledCourses(courseIds);
                }
                JSONObject progressObj = obj.optJSONObject("progress");
                if (progressObj != null) {
                    HashMap<String, ArrayList<String>> progressMap = new HashMap<>();
                    Iterator<String> keys = progressObj.keys();
                    while (keys.hasNext()) {
                        String courseId = keys.next();
                        JSONArray completedLessonsArr = progressObj.getJSONArray(courseId);
                        ArrayList<String> completedIds = new ArrayList<>();
                        for (int k = 0; k < completedLessonsArr.length(); k++) {
                            completedIds.add(completedLessonsArr.getString(k));
                        }
                        progressMap.put(courseId, completedIds);
                    }
                    s.setProgress(progressMap);
                }
                users.add(s);
            } else { //instructor
                // Instructor
                    Instructor instructor = new Instructor(userId, username, email, passwordHash);

                    // Load created courses (JSONArray of Strings)
                    JSONArray createdArr = obj.optJSONArray("createdCourses");
                    if (createdArr != null) {
                        ArrayList<String> courseIds = new ArrayList<>();
                        for (int j = 0; j < createdArr.length(); j++) {
                            courseIds.add(createdArr.getString(j));
                        }
                        instructor.setCreatedCourses(courseIds);
                    }
                    users.add(instructor);
                }
        }
        return users;
    }

        public void saveUsers(ArrayList<User> users) {
            JSONArray arr = new JSONArray();
            for (User u : users) {
                JSONObject obj = new JSONObject();
                obj.put("userId", u.getUserId());
                obj.put("role", u.getRole());
                obj.put("username", u.getUsername());
                obj.put("email", u.getEmail());
                obj.put("passwordHash", u.getPasswordHash());

                if (u instanceof Student) {
                    Student s = (Student) u;
                    // Save enrolled courses
                    obj.put("enrolledCourses", new JSONArray(s.getEnrolledCourses()));
                    // Save progress
                    obj.put("progress", new JSONObject(s.getProgress()));

                } else if (u instanceof Instructor) {
                    Instructor i = (Instructor) u;
                    // Save created courses
                    obj.put("createdCourses", new JSONArray(i.getCreatedCourses()));
                }
                arr.put(obj);
            }
            writeFile(USERS_FILE, arr.toString(4));
        }
        public ArrayList<Course> loadCourses() {
            ArrayList<Course> courses = new ArrayList<>();
            String content = readFile(COURSES_FILE);
            if (content.isEmpty()) return courses;

            JSONArray arr = new JSONArray(content);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Course c = new Course(
                        obj.getString("courseId"),
                        obj.getString("title"),
                        obj.getString("description"),
                        obj.getString("instructorId")
                );



            JSONArray lessonsArr = obj.optJSONArray("lessons");
            if (lessonsArr != null) {
                for (int j = 0; j < lessonsArr.length(); j++) {
                    JSONObject lessonObj = lessonsArr.getJSONObject(j);
                    Lesson l = new Lesson(
                            lessonObj.getString("lessonId"),
                            lessonObj.getString("title"),
                            lessonObj.getString("content")
                    );
                    // Load resources for the lesson
                    JSONArray resourcesArr = lessonObj.optJSONArray("resources");
                    if (resourcesArr != null) {
                        for (int k = 0; k < resourcesArr.length(); k++) {
                            l.addResource(resourcesArr.getString(k));
                        }
                    }
                    c.addLesson(l);
                }
            }

            JSONArray studentsArr = obj.optJSONArray("students");
            if (studentsArr != null) {
                for (int j = 0; j < studentsArr.length(); j++) {
                    c.addStudent(studentsArr.getString(j));
                }
            }
            courses.add(c);

            }
        return courses;
    }
    public void saveCourses(ArrayList<Course> courses) {
        JSONArray arr = new JSONArray();
        for (Course c : courses) {
            JSONObject obj = new JSONObject();
            obj.put("courseId", c.getCourseId());
            obj.put("title", c.getTitle());
            obj.put("description", c.getDescription());
            obj.put("instructorId", c.getInstructorId());

            // Save lessons
            JSONArray lessonsArr = new JSONArray();
            for (Lesson l : c.getLessons()) {
                JSONObject lessonObj = new JSONObject();
                lessonObj.put("lessonId", l.getLessonId());
                lessonObj.put("title", l.getTitle());
                lessonObj.put("content", l.getContent());
                lessonObj.put("resources", new JSONArray(l.getResources()));
                lessonsArr.put(lessonObj);
            }
            obj.put("lessons", lessonsArr);

            // Save students
            obj.put("students", new JSONArray(c.getStudents()));

            arr.put(obj);
        }
        writeFile(COURSES_FILE, arr.toString(4));
    }
    private String readFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return ""; // Return empty string, don't create file here
            }
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void writeFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





