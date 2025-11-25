package backend.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import backend.databaseManager.UsersDatabaseManager;

/**
 * Represents a digital certificate awarded to students upon course completion
 * Stores certificate metadata and provides formatting for display and printing
 * 
 * Features:
 * - Unique certificate ID generation
 * - Student and course information tracking
 * - Score and grade calculation
 * - Multiple output formats (text, HTML, PDF)
 * - Date formatting and validation
 */
public class Certificate {
    private String certificateId;
    private String studentId;
    private String studentName;
    private String courseId;
    private String courseTitle;
    private String instructorName;
    private Date issueDate;
    private double finalScore;
    private String certificateUrl; // Optional: path to stored PDF file


    /**
     * Primary constructor for creating new certificates
     * Automatically generates certificate ID and sets current date
     */
    public Certificate(Student student, Course course,String instructorName , double finalScore) {
        try {
            this.certificateId = generateCertificateId(); // Auto-generate unique ID
            this.studentId = student.getUserId();
            this.studentName = student.getUserId();
            this.courseId = course.getCourseId();
            this.courseTitle = course.getTitle();
            this.instructorName = instructorName;
            this.finalScore = finalScore;
            this.issueDate = new Date(); // Set to current date/time
        } catch (Exception e) {
            if(student == null) System.out.println("Certificate: Student not found.");
            if(course == null) System.out.println("Certificate: Course not found.");
            if(finalScore < 0) System.out.println("Certificate: Final score is negative.");
            e.printStackTrace();
        }
    }

    public Certificate(String studentId, String studentName, String courseId,
            String courseTitle, String instructorName, double finalScore) {
        this.certificateId = generateCertificateId(); // Auto-generate unique ID
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.instructorName = instructorName;
        this.finalScore = finalScore;
        this.issueDate = new Date(); // Set to current date/time
    }

    /**
     * Full constructor for loading existing certificates from database
     * Used when reconstructing certificates from JSON storage
     */
    public Certificate(String certificateId, String studentId, String studentName,
            String courseId, String courseTitle, String instructorName,
            Date issueDate, double finalScore) {
        this.certificateId = certificateId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.instructorName = instructorName;
        this.issueDate = issueDate;
        this.finalScore = finalScore;
    }

    // ==================== GETTERS AND SETTERS ====================

    /**
     * @return Unique certificate identifier (e.g., "CERT-A1B2C3D4")
     */
    public String getCertificateId() {
        return certificateId;
    }

    /**
     * @param certificateId Unique identifier for the certificate
     */
    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    /**
     * @return Student's unique user ID
     */
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * @return Student's full name for display on certificate
     */
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /**
     * @return Course unique identifier
     */
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    /**
     * @return Course title for display on certificate
     */
    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    /**
     * @return Instructor's name for display on certificate
     */
    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    /**
     * @return Date when certificate was issued
     */
    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    /**
     * @return Final score achieved (0-100 scale)
     */
    public double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }

    /**
     * @return File path to stored PDF version (optional)
     */
    public String getCertificateUrl() {
        return certificateUrl;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }


    // ==================== UTILITY METHODS ====================

    /**
     * Generates a unique certificate ID using UUID
     * Format: "CERT-" + 8-character uppercase random string
     * 
     * @return Unique certificate identifier
     */
    private String generateCertificateId() {
        return "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Calculates letter grade based on final score
     * Uses standard academic grading scale
     * 
     * @return Letter grade (A+, A, A-, B+, etc.)
     */
    public String getGrade() {
        if (finalScore >= 90)
            return "A+";
        else if (finalScore >= 85)
            return "A";
        else if (finalScore >= 80)
            return "A-";
        else if (finalScore >= 75)
            return "B+";
        else if (finalScore >= 70)
            return "B";
        else if (finalScore >= 65)
            return "C+";
        else if (finalScore >= 60)
            return "C";
        else
            return "Pass"; // For scores below 60%
    }

    /**
     * Formats issue date for display
     * 
     * @return Formatted date string (e.g., "January 15, 2024")
     */
    public String getFormattedIssueDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        return sdf.format(issueDate);
    }

    // ==================== VALIDATION METHODS ====================

    /**
     * Validates that all required certificate fields are populated
     * Ensures data integrity before saving to database
     * 
     * @return true if certificate contains all required valid data
     */
    public boolean isValid() {
        return certificateId != null && !certificateId.trim().isEmpty() &&
                studentId != null && !studentId.trim().isEmpty() &&
                courseId != null && !courseId.trim().isEmpty() &&
                studentName != null && !studentName.trim().isEmpty() &&
                courseTitle != null && !courseTitle.trim().isEmpty() &&
                issueDate != null && finalScore >= 0;
    }

    // ==================== OUTPUT FORMATS ====================

    /**
     * Creates formatted text version of certificate for console output
     * Useful for debugging and simple display
     * 
     * @return Formatted text certificate with borders and alignment
     */
    public String toTextFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append("================================================================================\n");
        sb.append("                      CERTIFICATE OF COMPLETION\n");
        sb.append("================================================================================\n\n");
        sb.append("This is to certify that\n\n");
        sb.append("                  ").append(studentName).append("\n\n");
        sb.append("has successfully completed the course\n\n");
        sb.append("                  \"").append(courseTitle).append("\"\n\n");
        sb.append("with a final score of ").append(String.format("%.1f", finalScore)).append("% (").append(getGrade())
                .append(")\n\n");
        sb.append("Instructor: ").append(instructorName).append("\n");
        sb.append("Certificate ID: ").append(certificateId).append("\n\n");
        sb.append("================================================================================\n");
        return sb.toString();
    }

    /**
     * Creates HTML version of certificate for Swing display
     * Uses inline CSS for consistent styling across systems
     * 
     * @return HTML string with certificate styling
     */
    public String toHTMLFormat() {
        return "<html>" +
                "<div style='text-align: center; border: 3px solid #D4AF37; padding: 30px; background: linear-gradient(to bottom, #fefefe, #f5f5f5); font-family: Arial, sans-serif;'>"
                +
                "<h1 style='color: #2c3e50; margin-bottom: 10px; font-size: 28px;'>CERTIFICATE OF COMPLETION</h1>" +
                "<hr style='border: 1px solid #D4AF37; width: 80%;'>" +
                "<p style='font-size: 16px; margin-top: 20px;'>This is to certify that</p>" +
                "<h2 style='color: #2980b9; font-size: 24px; margin: 15px 0;'>" + studentName + "</h2>" +
                "<p style='font-size: 16px;'>has successfully completed the course</p>" +
                "<h3 style='color: #27ae60; font-size: 20px; margin: 15px 0; font-style: italic;'>\"" + courseTitle
                + "\"</h3>" +
                "<p style='font-size: 16px;'>with a final score of <strong style='color: #e74c3c;'>"
                + String.format("%.1f", finalScore) + "% (" + getGrade() + ")</strong></p>" +
                "<div style='margin-top: 25px;'>" +
                "<p style='font-size: 14px;'><strong>Instructor:</strong> " + instructorName + "</p>" +
                "<p style='font-size: 14px;'><strong>Certificate ID:</strong> " + certificateId + "</p>" +
                "</div>" +
                "</div>" +
                "</html>";
    }

    /**
     * Default string representation for debugging
     * 
     * @return Compact string with key certificate information
     */
    @Override
    public String toString() {
        return "Certificate{" +
                "certificateId='" + certificateId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", courseTitle='" + courseTitle + '\'' +
                ", finalScore=" + finalScore +
                ", issueDate=" + getFormattedIssueDate() +
                '}';
    }
}