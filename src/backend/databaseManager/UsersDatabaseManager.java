package backend.databaseManager;

import backend.models.*;
import backend.models.parents.User;

import java.util.ArrayList;

public class UsersDatabaseManager {
    private ArrayList<User> users;
    private ReadWrite db;
    private final String USERS_FILE = "data/DatabaseJSONFiles/users.json";

    // CLASS CONSTRUCTOR and INITIALIZER (READS THE FILE OR CREATES A NEW ONE IF NOT
    // EXIST)
    public UsersDatabaseManager() {

        db = new ReadWrite();
        users = db.readFromFile(USERS_FILE, User.class);
    }

    // METHOD TO SEARCH AND RETURN THE USER IF EXIST IN THE DB
    public User getUser(String userId) {
        try {
            if (userId == null) {
                return null;
            }
            if (this.users.size() == 0) {
                return null; // No users in the database
            }
            for (int i = 0; i < this.users.size(); i++) {
                User tempUser = this.users.get(i);
                if (tempUser.getUserId().equals(userId)) {
                    // the line written under here is to make an new instance of users array
                    // as editing the object passed as a reference may alter the data in the array
                    // so to prevent that we make a new instance of the array
                    this.users = db.readFromFile(USERS_FILE, User.class);
                    if (tempUser.getRole().equals("student")) {
                        return ((Student) tempUser);
                    } else if (tempUser.getRole().equals("instructor")) {
                        return ((Instructor) tempUser);
                    }
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // METHOD TO ADD A USER (USED AT SIGNUP FUNCTIONALITY ONLY) AND RETURN THE
    // GENERATED ID (Changes saved permanently)
    public String addUser(User newUser) {
        try {
            if (newUser == null) {
                return "";
            }
            String id = generateId();
            newUser.setUserId(id);
            User user = newUser;
            this.users.add(user);
            SaveUsersToFile();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // !NTST
    // USED TO UPDATE THE USER DETAILS;
    public void update(User updatedUser) {
        try {
            if (updatedUser != null) {
                boolean found = false;
                String id = updatedUser.getUserId();
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUserId().equals(id)) {
                        found = true;
                        users.remove(i);
                        users.add(updatedUser);
                        SaveUsersToFile();
                    }
                }
                if (!found) {
                    users.add(updatedUser);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // METHOD TO SAVE USERS TO FILE
    public void SaveUsersToFile() {
        try {
            db.writeToFile(USERS_FILE, users);
            // updates the users array
            this.users = db.readFromFile(USERS_FILE, User.class);
            System.out.println("Users saved successfully to file.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Users NOT saved successfully to file.");
        }
    }

    // METHOD TO GENERATE A UNIQUE ID
    public String generateId() {
        return "U" + String.format("%d", this.users.size() + 1);
    }

    // METHOD TO RETURN ALL USERS JSON ARRAY (FOR VALIDATION PURPOSES)
    public ArrayList<User> getAllUsers() {
        ArrayList<User> tempUsers = this.users;
        this.users = db.readFromFile(USERS_FILE, User.class);
        return tempUsers;
    }

    // For testing purposes only
    public static void main(String[] args) {
        UsersDatabaseManager usersDB = new UsersDatabaseManager();
        User user1 = new Student("AYmen", "email", "passwordHash");
        User user2 = new Student("Gamal", "email2", "passwordHash2");
        User user3 = new Instructor("3aa", "email2", "passwordHash2");
        User user4 = new Instructor("bebe", "email2", "passwordHash2");
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
