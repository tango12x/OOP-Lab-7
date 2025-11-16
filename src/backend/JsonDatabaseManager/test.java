package backend.JsonDatabaseManager;

import org.json.JSONArray;
import org.json.JSONObject;

import backend.ProgramFunctions.UserAccountManagement.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import backend.ProgramFunctions.InstructorManagement.Instructor;
import backend.ProgramFunctions.StudentManagement.Student;
import backend.ProgramFunctions.UserAccountManagement.User;


//! student and instructor reading and writing issue stil needs to be fixed

public class test {

    private final String USERS_FILE = "data/DatabaseJSONFiles/users.json";

    public ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();

        try {
            File file = new File(USERS_FILE);
            if (!file.exists()) return users;

            // Read the file into a String
            /* 
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();
            */
            String content = new String(Files.readAllBytes(Paths.get("USERS_FILE")));
            JSONArray jsonArray = new JSONArray(content);


            // Parse JSON array
            JSONArray arr = new JSONArray(sb.toString());

            // Convert JSON → User objects
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                String userId = obj.getString("userId");
                String role = obj.getString("role");
                String username = obj.getString("username");
                String email = obj.getString("email");
                String passwordHash = obj.getString("passwordHash");

                if (role.equals("student")) {
                    users.add(new Student(userId, username, email, passwordHash));
                } else {
                    users.add(new Instructor(userId, username, email, passwordHash));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public void saveUsers(ArrayList<User> users) {
        try {
            JSONArray arr = new JSONArray();

            for (User u : users) {
                JSONObject obj = new JSONObject();
                obj.put("userId", u.getUserId());
                obj.put("role", u.getRole());
                obj.put("username", u.getUsername());
                obj.put("email", u.getEmail());
                obj.put("passwordHash", u.getPasswordHash());
                arr.put(obj);
            }

            FileWriter writer = new FileWriter(USERS_FILE);
            writer.write(arr.toString(4));  // pretty print
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
