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
    
}
