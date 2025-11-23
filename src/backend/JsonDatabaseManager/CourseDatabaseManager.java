package backend.JsonDatabaseManager;

import org.json.JSONArray;
import org.json.JSONObject;

import backend.ProgramFunctions.CourseManagement.Course;
import backend.ProgramFunctions.LessonAndLearningFeatures.Lesson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CourseDatabaseManager {

    private JSONArray courses;
    private final String COURSES_FILE = "data/DatabaseJSONFiles/courses.json";

    // ===========================================
    // CONSTRUCTOR: loads JSON file
    // ===========================================
    public CourseDatabaseManager() {
        try {
            File file = new File(COURSES_FILE);
            String content = Files.readString(Paths.get(COURSES_FILE));
            this.courses = new JSONArray(content);
        } catch (Exception e) {
            System.out.println("No existing courses file. Creating a new one.");
            this.courses = new JSONArray();
            SaveCoursesToFile();
        }
    }

    // ===========================================
    // SEARCH COURSE INDEX
    // ===========================================
    public int SearchCourseIndex(String courseId) {
        for (int i = 0; i < this.courses.length(); i++) {
            if (this.courses.getJSONObject(i).getString("courseId").equals(courseId)) {
                return i;
            }
        }
        return -1;
    }

    // ===========================================
    // GET COURSE BY ID
    // ===========================================
    public Course getCourse(String courseId) {
        int index = SearchCourseIndex(courseId);
        if (index == -1) return null;

        JSONObject obj = this.courses.getJSONObject(index);

        String title = obj.getString("title");
        String instructorId = obj.getString("instructorId");
        String description = obj.getString("description");

        Course course = new Course(courseId, title, instructorId, description);

        // students list
        JSONArray stdArr = obj.getJSONArray("students");
        course.setStudents(JsonDatabaseManager.toStringList(stdArr));

        // lessons list
        JSONArray lessonsArr = obj.getJSONArray("lessons");
        course.setLessons(JSONToLesson(lessonsArr));

        // approvalStatus support (added)
        if (obj.has("approvalStatus"))
            course.setApprovalStatus(obj.getString("approvalStatus"));
        else
            course.setApprovalStatus("PENDING");

        return course;
    }

    // ===========================================
    // ADD COURSE
    // ===========================================
    public String addCourse(Course newCourse) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("courseId", generateId());
            obj.put("title", newCourse.getTitle());
            obj.put("instructorId", newCourse.getInstructorId());
            obj.put("description", newCourse.getDescription());

            // Always start new courses as PENDING
            obj.put("approvalStatus", "PENDING");

            obj.put("lessons", LessonToJSON(newCourse.getLessons()));
            obj.put("students", JsonDatabaseManager.toJSONArray(newCourse.getStudents()));

            this.courses.put(obj);
            SaveCoursesToFile();
            return obj.getString("courseId");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ===========================================
    // UPDATE COURSE
    // ===========================================
    public void update(Course updatedCourse) {
        int index = SearchCourseIndex(updatedCourse.getCourseId());
        if (index == -1) {
            addCourse(updatedCourse);
            return;
        }

        try {
            JSONObject obj = this.courses.getJSONObject(index);

            obj.put("title", updatedCourse.getTitle());
            obj.put("description", updatedCourse.getDescription());
            obj.put("instructorId", updatedCourse.getInstructorId());

            obj.put("lessons", LessonToJSON(updatedCourse.getLessons()));
            obj.put("students", JsonDatabaseManager.toJSONArray(updatedCourse.getStudents()));

            // Save approvalStatus (already existed)
            obj.put("approvalStatus", updatedCourse.getApprovalStatus());

            SaveCoursesToFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===========================================
    // DELETE COURSE
    // ===========================================
    public void deleteCourse(String courseId) {
        int index = SearchCourseIndex(courseId);
        if (index != -1) {
            this.courses.remove(index);
            SaveCoursesToFile();
        }
    }

    // ===========================================
    // SAVE DATABASE
    // ===========================================
    public void SaveCoursesToFile() {
        try (FileWriter writer = new FileWriter(COURSES_FILE)) {
            writer.write(this.courses.toString(4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getAllCourses() {
        return this.courses;
    }

    // ===========================================
    // ID GENERATOR
    // ===========================================
    public String generateId() {
        return "C" + (this.courses.length() + 1);
    }

    // ===========================================
    // JSON <-> LESSON Conversion
    // ===========================================
    public ArrayList<Lesson> JSONToLesson(JSONArray jsonArray) {
        ArrayList<Lesson> lessonsArray = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            Lesson lesson = new Lesson(
                obj.getString("lessonId"),
                obj.getString("title"),
                obj.getString("courseId"),
                obj.getString("content")
            );

            lesson.setOptionalResources(
                JsonDatabaseManager.toStringList(obj.getJSONArray("optionalResources"))
            );

            lessonsArray.add(lesson);
        }
        return lessonsArray;
    }

    public JSONArray LessonToJSON(ArrayList<Lesson> lessons) {
        JSONArray arr = new JSONArray();
        for (Lesson obj : lessons) {
            JSONObject json = new JSONObject();
            json.put("lessonId", obj.getLessonId());
            json.put("title", obj.getTitle());
            json.put("content", obj.getContent());
            json.put("courseId", obj.getCourseId());
            json.put("optionalResources", JsonDatabaseManager.toJSONArray(obj.getOptionalResources()));
            arr.put(json);
        }
        return arr;
    }

    // =======================================================
    // *** NEW METHODS ADDED ONLY (NO REMOVAL OF ANY CODE) ***
    // =======================================================

    public void approveCourse(String courseId) {
        int index = SearchCourseIndex(courseId);
        if (index != -1) {
            JSONObject obj = courses.getJSONObject(index);
            obj.put("approvalStatus", "APPROVED");
            SaveCoursesToFile();
        }
    }

    public void rejectCourse(String courseId) {
        int index = SearchCourseIndex(courseId);
        if (index != -1) {
            JSONObject obj = courses.getJSONObject(index);
            obj.put("approvalStatus", "REJECTED");
            SaveCoursesToFile();
        }
    }

    public ArrayList<Course> getPendingCourses() {
        ArrayList<Course> list = new ArrayList<>();
        for (int i = 0; i < courses.length(); i++) {
            JSONObject obj = courses.getJSONObject(i);
            if (obj.optString("approvalStatus", "PENDING").equals("PENDING")) {
                list.add(getCourse(obj.getString("courseId")));
            }
        }
        return list;
    }

    public ArrayList<Course> getApprovedCourses() {
        ArrayList<Course> list = new ArrayList<>();
        for (int i = 0; i < courses.length(); i++) {
            JSONObject obj = courses.getJSONObject(i);
            if (obj.optString("approvalStatus", "PENDING").equals("APPROVED")) {
                list.add(getCourse(obj.getString("courseId")));
            }
        }
        return list;
    }

    public ArrayList<Course> getRejectedCourses() {
        ArrayList<Course> list = new ArrayList<>();
        for (int i = 0; i < courses.length(); i++) {
            JSONObject obj = courses.getJSONObject(i);
            if (obj.optString("approvalStatus", "PENDING").equals("REJECTED")) {
                list.add(getCourse(obj.getString("courseId")));
            }
        }
        return list;
    }
    public Course getCourseWithoutLessons(String courseId) {
    int index = SearchCourseIndex(courseId);
    if (index == -1) return null;

    JSONObject obj = this.courses.getJSONObject(index);

    String title = obj.getString("title");
    String instructorId = obj.getString("instructorId");
    String description = obj.getString("description");

    // Create simple course object
    Course c = new Course(courseId, title, instructorId, description);

    // Set students list
    JSONArray stdArr = obj.getJSONArray("students");
    c.setStudents(JsonDatabaseManager.toStringList(stdArr));

    // Approval status
    if (obj.has("approvalStatus"))
        c.setApprovalStatus(obj.getString("approvalStatus"));
    else
        c.setApprovalStatus("PENDING");

    // DO NOT READ or PARSE lessons
    c.setLessons(new ArrayList<>());

    return c;
}
    public ArrayList<Course> getApprovedCoursesWithLessons() {
    ArrayList<Course> list = new ArrayList<>();

    for (int i = 0; i < courses.length(); i++) {
        JSONObject obj = courses.getJSONObject(i);

        if (obj.optString("approvalStatus", "PENDING").equals("APPROVED")) {
            // get course WITH lessons
            list.add(getCourse(obj.getString("courseId")));
        }
    }
    return list;
}

}

