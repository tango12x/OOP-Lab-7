package backend.databaseManager;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
//lib imports
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import backend.models.*;
import backend.models.parents.User;

/**
 * Main database management class that handles all JSON file operations
 * Uses Singleton pattern to ensure single point of database access
 */
public class ReadWrite {

    private final Gson gson;

    /**
     * Private constructor for Singleton pattern
     * Initializes GSON and ensures data directory exists
     */
    public ReadWrite() {
        // Create custom deserializer for User
        JsonDeserializer<User> userDeserializer = new JsonDeserializer<User>() {
            @Override
            public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();
                String role = jsonObject.get("role").getAsString();
                switch (role) {
                    case "student":
                        return context.deserialize(jsonObject, Student.class);
                    case "instructor":
                        return context.deserialize(jsonObject, Instructor.class);
                    default:
                        throw new JsonParseException("Unknown user role: " + role);
                }
            }
        };
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .disableHtmlEscaping()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(User.class, userDeserializer) // Register custom deserializer
                .create();
    }

    // READ ANY TYPE OF LIST FROM JSON , TAKES FILENAME AND TYPE (e.g.,
    // User.class),returns list
    public synchronized <T> ArrayList<T> readFromFile(String filename, Class<T> type) {

        File file = new File(filename);
        // If file doesn't exist, return empty list instead of throwing error
        if (!file.exists()) {
            System.out.println("File not found: " + filename + ", returning empty list");
            return new ArrayList<T>();
        }

        try (Reader reader = new FileReader(file)) {
            /**
             * TypeToken trick: GSON needs to know the generic type at runtime
             * so we use TypeToken to preserve them This allows GSON to properly deserialize
             * List<User>, List<Course>, etc.
             */
            Type listType = TypeToken.getParameterized(List.class, type).getType();
            ArrayList<T> items = (ArrayList<T>) gson.fromJson(reader, listType);
            // Handle case where file is empty or contains null
            return items != null ? items : new ArrayList<T>();

        } catch (IOException e) {
            /**
             * IOException handling: Log error and return empty list
             * This prevents the entire application from crashing due to file issues
             */
            System.err.println("Error reading from " + filename + ": " + e.getMessage());
            return new ArrayList<T>();
        }
    }

    // WRITES TO FILE ANY TYPE OF LIST , TAKES FILENAME AND LIST
    public synchronized <T> void writeToFile(String filename, List<T> items) {
        // Create parent directories if they don't exist
        File file = new File(filename);
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

        try (Writer writer = new FileWriter(file)) {

            // toJson method: Converts Java objects to JSON string
            gson.toJson(items, writer);
            System.out.println("Successfully wrote " + items.size() + " items to " + filename);

        } catch (IOException e) {

            // Critical to inform user when save fails
            System.err.println("Error writing to " + filename + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // !FOR TESTING ONLY
    public static void main(String[] args) {

        // File paths for our JSON databases
        // final String USERS_FILE = "data/users.json";
        final String COURSES_FILE = "data/courses.json";
        // final String CERTIFICATES_FILE = "data/certificates.json";

        ReadWrite db = new ReadWrite();
        ArrayList<Course> courses = (ArrayList<Course>) db.readFromFile(COURSES_FILE, Course.class);// new
                                                                                                    // ArrayList<Course>();
        for (int i = 0; i < courses.size(); i++) {
            System.err.println("Course " + i + ": " + courses.get(i).getCourseId()
                    + "-" + courses.get(i).getTitle()
                    + "-" + courses.get(i).getDescription()
                    + "-" + courses.get(i).getLessons().getFirst().getLessonId());
        }
        // courses.add(course);
        // courses.add(course1);

    }

}
