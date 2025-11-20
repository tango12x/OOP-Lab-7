package backend.SecurityAndValidation;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import backend.JsonDatabaseManager.UsersDatabaseManager;
import backend.ProgramFunctions.InstructorManagement.*;
import backend.ProgramFunctions.StudentManagement.*;
import backend.ProgramFunctions.UserAccountManagement.*;
import backend.SecurityAndValidation.*;

public class AuthManager {

    private UsersDatabaseManager db;

    public AuthManager() {
        db = new UsersDatabaseManager();
    }

    // LOGIN METHOD
    public User login(String email, String password) {
        // 1. Validate fields
        if (!Validator.isFilled(email) || !Validator.isFilled(password))
            return null;

        if (!Validator.isValidEmail(email))
            return null;

        // 2. Load users
        JSONArray users = db.getAllUsers();

        // 3. Hash password
        String hashed = PasswordHasher.hashPassword(password);

        // 4. Compare
        for (int i = 0; i < users.length(); i++) {
            JSONObject u = users.getJSONObject(i);
            if (u.getString("email").equalsIgnoreCase(email) && u.getString("passwordHash").equals(hashed)) 
            {
                return db.getUser(u.getString("userId"));
            }
        }

        return null; // not found
    }


    // REGISTER METHOD TO CREATE A NEW USER
    public User register(String username, String email, String password, String role) {

        // Load users
        JSONArray users = db.getAllUsers();


        // Check existing email
        for (int i = 0; i < users.length(); i++) {
            JSONObject u = users.getJSONObject(i);
            if (u.getString("email").equalsIgnoreCase(email)) {
                return null;
            }
        }

        // Hash password
        String hashed = PasswordHasher.hashPassword(password);


        User newUser;
        // create correct role
        if (role.equals("student")) {
            newUser = new Student(username, email, hashed);
        } else {
            newUser = new Instructor(username, email, hashed);
        }
        String id = db.addUser(newUser);
        db.SaveUsersToFile();


        return db.getUser(id);
    }



}