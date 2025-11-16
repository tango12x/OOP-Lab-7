package Backend;

public class Instructor extends User {
    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, "instructor", username, email, passwordHash);
    }
}
