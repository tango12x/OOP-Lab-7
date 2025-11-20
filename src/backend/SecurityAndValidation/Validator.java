package backend.SecurityAndValidation;

public class Validator {

    // Required fields must NOT be empty
    public static boolean isFilled(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // Email validation
    public static boolean isValidEmail(String email) {
        if (!isFilled(email)) return false;
        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex == email.length() - 1) return false;
        String localPart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex + 1);
        if (localPart.isEmpty() || domainPart.isEmpty()) return false;
        int dotIndex = domainPart.indexOf('.');
        if (dotIndex <= 0 || dotIndex == domainPart.length() - 1) return false;
        return domainPart.substring(dotIndex + 1).length() >= 2;}

    // course description validation
    public static boolean isValidCourseDescription(String desc) {
        if (!isFilled(desc)) return false;
        if (desc.length() < 10 || desc.length() > 1000) return false;
        String forbidden = "<>&;";
        for (char c : desc.toCharArray()) {
            if (forbidden.indexOf(c) != -1) return false;}
        return true;}

    //lesson title validation
    public static boolean isValidLessonTitle(String title) {
        return isFilled(title) && title.length() >= 3 && title.length() <= 150;}
    //course title validation
    public static boolean isValidCourseTitle(String title) {
        if (!isFilled(title)) return false;
        if (title.length() < 5 || title.length() > 100) return false;
        String forbidden = "<>\"&;";
        for (char c : title.toCharArray()) {
            if (forbidden.indexOf(c) != -1) return false;}
        return true;}

    //username validation
    public static boolean isValidUsername(String username) {
        if (!isFilled(username)) return false;
        if (username.length() < 3 || username.length() > 20) return false;
        for (char c : username.toCharArray()) {
            if (!((c >= 'a' && c <= 'z') ||
                    (c >= 'A' && c <= 'Z') ||
                    (c >= '0' && c <= '9') ||
                    c == '_' || c == '-')) {
                return false;}}
        return true;}

    //Matching password validation when signing up
    public static boolean passwordsMatch(String pass1, String pass2) {
        return pass1 != null && pass1.equals(pass2);
    }

    //role validation
    public static boolean isValidRole(String role) {
        if (!isFilled(role)) return false;
        String lower = role.toLowerCase().trim();
        return "student".equals(lower) || "instructor".equals(lower);
    }

    //RESOURCE VALIDATION
    public static boolean isValidUrl(String url) {
        if (!isFilled(url)) return true; // optional
        String lower = url.toLowerCase().trim();
        return lower.startsWith("http://") ||
                lower.startsWith("https://") ||
                lower.startsWith("www.");}
}
