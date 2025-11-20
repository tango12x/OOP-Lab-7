package backend.JsonDatabaseManager;

import org.json.JSONArray;
import org.json.JSONObject;

import backend.ProgramFunctions.UserAccountManagement.User;
import backend.ProgramFunctions.InstructorManagement.Instructor;
import backend.ProgramFunctions.StudentManagement.Student;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



public class UsersDatabaseManager {
    private JSONArray users;
    private final String USERS_FILE = "data/DatabaseJSONFiles/users.json";

    // CLASS CONSTRUCTOR and INITIALIZER (READS THE FILE OR CREATES A NEW ONE IF NOT EXIST)
    public UsersDatabaseManager() {
        try {
            File file = new File(USERS_FILE);
            String content = Files.readString(Paths.get(USERS_FILE));
            this.users = new JSONArray(content);
        } catch (Exception e) {
            e.printStackTrace();
            // If file not found, create a new one

            File file = new File(USERS_FILE);
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
            this.users = new JSONArray();
            System.out.println("No existing users file found. Starting with an empty user database.");
        }

    }
    
    // METHOD TO SEARCH AND RETURN THE INDEX OF THE USER IF EXIST IN THE DB , -1 IF NOT
    public int SearchUserIndex(String userId) {
        for (int i = 0; i < this.users.length(); i++) {
            JSONObject obj = this.users.getJSONObject(i);
            if (obj.getString("userId").equals(userId)) {
                // System.out.println("Found user at index: " + i);
                return i; // Return the index if found
            }
        }
        System.out.println("User not found: " + userId);
        return -1; // Return -1 if not found
    }

    // METHOD TO SEARCH AND RETURN THE USER IF EXIST IN THE DB
    public User getUser(String userId) {
        User user = null;
        try {
            if(this.users.length() == 0) {
                return null; // No users in the database
            }
            // Search for the userId
            int userIndex = SearchUserIndex(userId);
            if (userIndex == -1) {
                return null; // User not found
            }
            JSONObject obj = this.users.getJSONObject(userIndex);
            String role = obj.getString("role");
            String username = obj.getString("username");
            String email = obj.getString("email");
            String passwordHash = obj.getString("passwordHash");
            if (role.equals("student")) {
                user = new Student(userId, username, email, passwordHash);
                // convert JSON arrays to ArrayLists and set them
                JSONArray enrolledCourses = obj.getJSONArray("enrolledCourses");
                ((Student) user).setEnrolledCourses(JsonDatabaseManager.toStringList(enrolledCourses));
                JSONArray progress = obj.getJSONArray("progress");
                ((Student) user).setProgress(JsonDatabaseManager.toListOfStringLists(progress));

            } else if (role.equals("instructor")) {
                user = new Instructor(userId, username, email, passwordHash);
                // convert JSON arrays to ArrayLists and set them
                JSONArray createdCourses = obj.getJSONArray("createdCourses");
                ((Instructor) user).setCreatedCourses(JsonDatabaseManager.toStringList(createdCourses));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    // METHOD TO ADD A USER (USED AT SIGNUP FUNCTIONALITY ONLY) AND RETURN THE GENERATED ID
    public String addUser(User newUser) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", generateId());
            obj.put("role", newUser.getRole());
            obj.put("username", newUser.getUsername());
            obj.put("email", newUser.getEmail());
            obj.put("passwordHash", newUser.getPasswordHash());

            // Add role-specific fields
            if (newUser.getRole().equals("student")) {
                obj.put("enrolledCourses", new ArrayList<String>());
                obj.put("progress", new ArrayList<ArrayList<String>>());
            } else if (newUser.getRole().equals("instructor")) {
                obj.put("createdCourses", new ArrayList<String>());
            }

            this.users.put(obj);
            return obj.getString("userId");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //USED TO UPDATE THE USER DETAILS (LIKE ENROLLED COURSES, PROGRESS, ETC.) NOT FOR PASSWORD OR USERNAME
    public void update(User updatedUser) {
        int userIndex = SearchUserIndex(updatedUser.getUserId());
        if (userIndex == -1) {
            addUser(updatedUser);
            System.out.println("User not found, adding new user.");
            return;
        }
        //! conversion to Jsonarray must be tested
        try {
            JSONObject obj = this.users.getJSONObject(userIndex);
            // Add role-specific fields
            if (updatedUser.getRole().equals("student")) {
                obj.put("enrolledCourses", ((Student) updatedUser).getEnrolledCourses() == null ?
                 new ArrayList<String>() : JsonDatabaseManager.toJSONArray(((Student) updatedUser).getEnrolledCourses()));
                obj.put("progress", ((Student) updatedUser).getProgress() == null ? 
                 new ArrayList<ArrayList<String>>() : JsonDatabaseManager.toJSONArrayOfLists(((Student) updatedUser).getProgress()));
            } else if (updatedUser.getRole().equals("instructor")) {
                System.out.println("Updating instructor created courses");
                obj.put("createdCourses", ((Instructor) updatedUser).getCreatedCourses() == null ? 
                 new ArrayList<String>() : JsonDatabaseManager.toJSONArray(((Instructor) updatedUser).getCreatedCourses()));
            }

            //this.users.put(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // METHOD TO SAVE USERS TO FILE
    public void SaveUsersToFile() {
        try {
            FileWriter writer = new FileWriter(USERS_FILE);
            writer.write(this.users.toString(4)); // pretty print
            writer.close();
            System.out.println("Users saved successfully to file.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // METHOD TO GENERATE A UNIQUE ID
    public String generateId() {
        return "U" + String.format("%d", this.users.length() + 1);
    }

    // METHOD TO RETURN ALL USERS JSON ARRAY (FOR VALIDATION PURPOSES)
    public JSONArray getAllUsers() {
        return this.users;
    }

    //For testing purposes only
    public static void main(String[] args) {
        UsersDatabaseManager usersDB = new UsersDatabaseManager();
        User user1 = new Student("AYmen", "email","passwordHash");
        User user2 = new Student("Gamal", "email2","passwordHash2");
        User user3 = new Instructor("3aa", "email2","passwordHash2");
        User user4 = new Instructor("bebe", "email2","passwordHash2");
        ArrayList<String> courses = new ArrayList<String>();
        courses.add("C1");
        courses.add("C13");
        courses.add("C3");
        usersDB.addUser(user1);
        usersDB.addUser(user2);
        usersDB.addUser(user3);
        usersDB.addUser(user4);
        usersDB.SaveUsersToFile();

        usersDB = new UsersDatabaseManager();
        ((Instructor) user3).setCreatedCourses(courses);
        usersDB.update(user3);
        usersDB.SaveUsersToFile();
    }
}
