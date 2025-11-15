
package Backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class JsonDatabaseManager {

    private final String USERS_FILE = "users.json";

    public ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();

        try {
            File file = new File(USERS_FILE);
            if (!file.exists()) return users;

            // Read the file into a String
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();

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
