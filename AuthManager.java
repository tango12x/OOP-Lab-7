package Backend;

import java.util.ArrayList;

public class AuthManager {

    private JsonDatabaseManager db;

    public AuthManager() {
        db = new JsonDatabaseManager();
    }

    public User login(String email, String password) {
        // 1. Validate fields
        if (!Validator.isFilled(email) || !Validator.isFilled(password))
            return null;

        if (!Validator.isValidEmail(email))
            return null;

        // 2. Load users
        ArrayList<User> users = db.loadUsers();

        // 3. Hash password
        String hashed = PasswordHasher.hashPassword(password);

        // 4. Compare
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) &&
                u.getPasswordHash().equals(hashed)) {
                return u; // found
            }
        }

        return null; // not found
    }
   public User register(String username, String email, String password, String role) {

    if (!Validator.isFilled(username) ||
        !Validator.isFilled(email) ||
        !Validator.isFilled(password)) {
        return null;
    }

    if (!Validator.isValidEmail(email)) return null;

    ArrayList<User> users = db.loadUsers();

    // Check existing email
    for (User u : users) {
        if (u.getEmail().equalsIgnoreCase(email)) {
            return null;
        }
    }

    String hashed = PasswordHasher.hashPassword(password);

    User newUser;

    // create correct role
    if (role.equals("student")) {
        newUser = new Student("U" + (users.size() + 1), username, email, hashed);
    } else {
        newUser = new Instructor("U" + (users.size() + 1), username, email, hashed);
    }

    users.add(newUser);
    db.saveUsers(users);

    return newUser;
}

}