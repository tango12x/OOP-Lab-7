package Backend;

public class Validator {

    // Required fields must NOT be empty
    public static boolean isFilled(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // Email must contain @ and .
    public static boolean isValidEmail(String email) {
        if (!isFilled(email)) return false;
        return email.contains("@") && email.contains(".");
    }
}
