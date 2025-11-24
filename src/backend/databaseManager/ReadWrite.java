package backend.databaseManager;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
//lib imports
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import backend.models.*;
import backend.models.parents.User;

/**
 * Main database management class that handles all JSON file operations
 * Uses Singleton pattern to ensure single point of database access
 * 
 * Enhanced Features:
 * 1. Handles abstract class User deserialization with polymorphism
 * 2. Returns empty collections instead of null for missing/empty JSON arrays
 * 3. Comprehensive error handling and logging
 * 4. Automatic directory creation
 * 5. Type-safe generic operations
 * 6. Thread-safe operations with synchronized methods
 */
public class ReadWrite {

    private final Gson gson;

    /**
     * Constructor initializes GSON with custom configuration
     * - Handles User polymorphism via custom deserializer
     * - Ensures empty collections instead of null
     * - Pretty prints JSON for readability
     * - Handles dates consistently
     */
    public ReadWrite() {
        //! VERY ERROR PRONE!!!!!!!!!!!!!!!!!!!!!!
        // Custom type adapter for collections that returns empty lists instead of null
        TypeAdapter<Collection> collectionAdapter = new TypeAdapter<Collection>() {
            @Override
            public void write(JsonWriter out, Collection value) throws IOException {
                if (value == null || value.isEmpty()) {
                    out.nullValue(); // Write null for empty collections
                } else {
                    out.beginArray();
                    for (Object item : value) {
                        gson.toJson(item, item.getClass(), out);
                    }
                    out.endArray();
                }
            }

            @Override
            public Collection read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return new ArrayList(); // Return empty ArrayList instead of null
                }
                
                // Handle case where JSON array is empty
                if (in.peek() == JsonToken.BEGIN_ARRAY) {
                    in.beginArray();
                    if (in.peek() == JsonToken.END_ARRAY) {
                        in.endArray();
                        return new ArrayList(); // Empty array returns empty list
                    }
                    in.endArray();
                    // Reset and read properly
                    in = new JsonReader(new StringReader(in.toString()));
                }

                ArrayList<Object> list = new ArrayList<>();
                in.beginArray();
                while (in.hasNext()) {
                    Object item = gson.fromJson(in, Object.class);
                    list.add(item);
                }
                in.endArray();
                return list;
            }
        };

        // Custom deserializer for User class to handle polymorphism
        // This resolves "Abstract classes can't be instantiated" error
        JsonDeserializer<User> userDeserializer = new JsonDeserializer<User>() {
            @Override
            public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                try {
                    JsonObject jsonObject = json.getAsJsonObject();
                    
                    // Check if role field exists to determine concrete type
                    if (!jsonObject.has("role")) {
                        throw new JsonParseException("User JSON missing 'role' field - cannot determine concrete type");
                    }
                    
                    String role = jsonObject.get("role").getAsString().toLowerCase();
                    
                    // Route to appropriate concrete class based on role
                    switch (role) {
                        case "student":
                            Student student = context.deserialize(jsonObject, Student.class);
                            // Ensure collections are never null
                            if (student.getEnrolledCourses() == null) {
                                student.setEnrolledCourses(new ArrayList<String>());
                            }
                            if (student.getProgress() == null) {
                                student.setProgress(new ArrayList<ArrayList<String>>());
                            }
                            if (student.getCertificates() == null) {
                                student.setCertificates(new ArrayList<Certificate>());
                            }
                            return student;
                            
                        case "instructor":
                            Instructor instructor = context.deserialize(jsonObject, Instructor.class);
                            // Ensure collections are never null
                            if (instructor.getCreatedCourses() == null) {
                                instructor.setCreatedCourses(new ArrayList<>());
                            }
                            return instructor;
                         case "admin":
                            Admin admin = context.deserialize(jsonObject, Admin.class);
                            return admin;
                                
                        default:
                            throw new JsonParseException("Unknown user role: " + role + 
                                ". Supported roles: 'student', 'instructor'");
                    }
                } catch (Exception e) {
                    System.err.println("Error deserializing User: " + e.getMessage());
                    throw new JsonParseException("Failed to deserialize User object", e);
                }
            }
        };

        // Initialize GSON with all custom configurations
        this.gson = new GsonBuilder()
                .setPrettyPrinting() // Format JSON for readability
                .serializeNulls() // Include null values in serialization
                .disableHtmlEscaping() // Preserve HTML in content
                .setDateFormat("yyyy-MM-dd HH:mm:ss") // Consistent date format
                .registerTypeAdapter(User.class, userDeserializer) // Handle User polymorphism
                .registerTypeAdapter(Collection.class, collectionAdapter)
                .registerTypeAdapter(List.class, collectionAdapter)
                .registerTypeAdapter(ArrayList.class, collectionAdapter)
                .create();
    }

    /**
     * Reads data from JSON file and returns as ArrayList
     * - Returns empty list if file doesn't exist or is empty
     * - Handles all exceptions gracefully
     * - Type-safe generic method
     * 
     * @param <T> The type of objects in the list
     * @param filename Path to the JSON file
     * @param type Class type of the objects (e.g., User.class, Course.class)
     * @return ArrayList of objects, never null (returns empty list on error)
     */
    public synchronized <T> ArrayList<T> readFromFile(String filename, Class<T> type) {
        // Validate input parameters
        if (filename == null || filename.trim().isEmpty()) {
            System.err.println("Filename cannot be null or empty");
            return new ArrayList<T>();
        }
        
        if (type == null) {
            System.err.println("Type parameter cannot be null");
            return new ArrayList<T>();
        }

        File file = new File(filename);
        
        // If file doesn't exist, return empty list instead of throwing error
        if (!file.exists()) {
            System.out.println("File not found: " + filename + ", returning empty list");
            return new ArrayList<T>();
        }

        // Check if file is empty
        if (file.length() == 0) {
            System.out.println("File is empty: " + filename + ", returning empty list");
            return new ArrayList<T>();
        }

        try (Reader reader = new FileReader(file)) {
            /**
             * TypeToken trick: GSON needs to know the generic type at runtime due to type erasure
             * TypeToken preserves generic type information so GSON can properly deserialize
             * List<User>, List<Course>, etc.
             */
            Type listType = TypeToken.getParameterized(List.class, type).getType();
            ArrayList<T> items = gson.fromJson(reader, listType);
            
            // Always return non-null list - convert null to empty list
            return items != null ? items : new ArrayList<T>();

        } catch (FileNotFoundException e) {
            System.err.println("File not found (should not happen after existence check): " + filename);
            return new ArrayList<T>();
        } catch (IOException e) {
            /**
             * IOException handling: Log error and return empty list
             * This prevents the entire application from crashing due to file issues
             */
            System.err.println("IO Error reading from " + filename + ": " + e.getMessage());
            return new ArrayList<T>();
        } catch (JsonSyntaxException e) {
            /**
             * JSON syntax error: File exists but contains invalid JSON
             * Log detailed error and return empty list
             */
            System.err.println("Invalid JSON syntax in " + filename + ": " + e.getMessage());
            return new ArrayList<T>();
        } catch (JsonIOException e) {
            /**
             * JSON IO error: Issues with JSON parsing
             */
            System.err.println("JSON IO Error in " + filename + ": " + e.getMessage());
            return new ArrayList<T>();
        } catch (Exception e) {
            /**
             * Catch-all for any other unexpected exceptions
             */
            System.err.println("Unexpected error reading from " + filename + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<T>();
        }
    }

    /**
     * Writes data to JSON file
     * - Creates parent directories if they don't exist
     * - Handles all IO exceptions gracefully
     * - Thread-safe operation
     * 
     * @param <T> The type of objects in the list
     * @param filename Path to the JSON file
     * @param items List of objects to serialize
     */
    public synchronized <T> void writeToFile(String filename, List<T> items) {
        // Validate input parameters
        if (filename == null || filename.trim().isEmpty()) {
            System.err.println("Filename cannot be null or empty for write operation");
            return;
        }

        // Handle null items list
        if (items == null) {
            System.out.println("Warning: Writing null items list to " + filename + ", will write empty array");
            items = new ArrayList<T>();
        }

        File file = new File(filename);
        
        try {
            // Create parent directories if they don't exist
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (parentDir.mkdirs()) {
                    System.out.println("Created directories: " + parentDir.getAbsolutePath());
                } else {
                    System.err.println("Failed to create directories: " + parentDir.getAbsolutePath());
                }
            }

            // Create file if it doesn't exist
            if (!file.exists()) {
                if (file.createNewFile()) {
                    System.out.println("File created: " + file.getName());
                } else {
                    throw new IOException("Failed to create file: " + filename);
                }
            }

            // Write data to file
            try (Writer writer = new FileWriter(file)) {
                gson.toJson(items, writer);
                System.out.println("Successfully wrote " + items.size() + " items to " + filename);
            }

        } catch (IOException e) {
            // Critical to inform user when save fails
            System.err.println("Error writing to " + filename + ": " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error writing to " + filename + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Utility method to check if a file exists and is readable
     * 
     * @param filename Path to check
     * @return true if file exists and is readable
     */
    public boolean fileExists(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return false;
        }
        File file = new File(filename);
        return file.exists() && file.canRead();
    }

    /**
     * Utility method to get file size
     * 
     * @param filename Path to check
     * @return File size in bytes, or -1 if error
     */
    public long getFileSize(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return -1;
        }
        File file = new File(filename);
        return file.exists() ? file.length() : -1;
    }

    /**
     * FOR TESTING ONLY - Demonstrates usage and tests functionality
     */
    public static void main(String[] args) {
        final String COURSES_FILE = "data/courses.json";
        final String USERS_FILE = "data/users.json";

        ReadWrite db = new ReadWrite();
        
        System.out.println("=== Testing ReadWrite Class ===");
        
        // Test reading courses
        System.out.println("\n1. Reading courses from: " + COURSES_FILE);
        ArrayList<Course> courses = db.readFromFile(COURSES_FILE, Course.class);
        System.out.println("Found " + courses.size() + " courses");
        
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            System.out.println("Course " + i + ": " + course.getCourseId()
                    + " - " + course.getTitle()
                    + " - Lessons: " + course.getLessons().size()
                    + " - Students: " + course.getStudents().size());
        }

        // Test reading users
        System.out.println("\n2. Reading users from: " + USERS_FILE);
        ArrayList<User> users = db.readFromFile(USERS_FILE, User.class);
        System.out.println("Found " + users.size() + " users");
        
        for (User user : users) {
            System.out.println("User: " + user.getUserId() 
                    + " - " + user.getUsername() 
                    + " - " + user.getRole() 
                    + " - " + user.getClass().getSimpleName());
        }

        // Test file operations
        System.out.println("\n3. File operations:");
        System.out.println("Courses file exists: " + db.fileExists(COURSES_FILE));
        System.out.println("Courses file size: " + db.getFileSize(COURSES_FILE) + " bytes");
        
        System.out.println("\n=== ReadWrite Test Complete ===");
    }


    
}