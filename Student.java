package Backend;

public class Student extends User {
    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, "student", username, email, passwordHash);
    }
}
