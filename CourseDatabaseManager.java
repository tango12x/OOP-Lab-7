package Backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class CourseDatabaseManager {

    private final String COURSES_FILE = "courses.json";

    // Load all courses from JSON file
    public ArrayList<Course> loadCourses() {
        ArrayList<Course> courses = new ArrayList<>();

        try {
            File file = new File(COURSES_FILE);
            if (!file.exists()) return courses;

            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null)
                sb.append(line);

            br.close();

            JSONArray arr = new JSONArray(sb.toString());

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                String courseId = obj.getString("courseId");
                String courseName = obj.getString("courseName");
                String instructor = obj.getString("instructor");
                String description = obj.getString("description");

                courses.add(new Course(courseId, courseName, instructor, description));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return courses;
    }

    // Save all courses back to JSON file
    public void saveCourses(ArrayList<Course> courses) {

        try {
            JSONArray arr = new JSONArray();

            for (Course c : courses) {
                JSONObject obj = new JSONObject();
                obj.put("courseId", c.getCourseId());
                obj.put("courseName", c.getCourseName());
                obj.put("instructor", c.getInstructorName()); // FIXED
                obj.put("description", c.getDescription());

                arr.put(obj);
            }

            FileWriter writer = new FileWriter(COURSES_FILE);
            writer.write(arr.toString(4));  // pretty print
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}