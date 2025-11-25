package backend.analytics;
import java.util.Date;

/**
 * Data container for individual student performance analytics
 * Used for personalized feedback and progress tracking
 */
public class StudentAnalytics {
    private String studentId;
    private String courseId;
    private int lessonsCompleted;
    private int totalLessons;
    private double completionPercentage;
    private double averageQuizScore;
    private Date lastActivity;
    
    public StudentAnalytics(String studentId, String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.lastActivity = new Date();
    }
    
    // Getters and Setters
    
    public String getStudentId() { return studentId; }
    
    public String getCourseId() { return courseId; }
    
    /** Number of lessons the student has completed */
    public int getLessonsCompleted() { return lessonsCompleted; }
    public void setLessonsCompleted(int lessonsCompleted) { 
        this.lessonsCompleted = lessonsCompleted; 
    }
    
    /** Total number of lessons in the course */
    public int getTotalLessons() { return totalLessons; }
    public void setTotalLessons(int totalLessons) { 
        this.totalLessons = totalLessons; 
    }
    
    /** Percentage of course completion */
    public double getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(double completionPercentage) { 
        this.completionPercentage = completionPercentage; 
    }
    
    /** Student's average quiz score across all attempted quizzes */
    public double getAverageQuizScore() { return averageQuizScore; }
    public void setAverageQuizScore(double averageQuizScore) { 
        this.averageQuizScore = averageQuizScore; 
    }
    
    /** Date of student's last activity in the course */
    public Date getLastActivity() { return lastActivity; }
    public void setLastActivity(Date lastActivity) { 
        this.lastActivity = lastActivity; 
    }
    
    /**
     * Gets performance category based on quiz scores and completion rate
     */
    public String getPerformanceCategory() {
        if (completionPercentage < 50) return "Beginner";
        if (averageQuizScore >= 80) return "Advanced";
        if (averageQuizScore >= 60) return "Intermediate";
        return "Developing";
    }
    
    /**
     * Calculates estimated time to complete based on current pace
     */
    public double getEstimatedTimeToComplete() {
        if (completionPercentage == 0) return 0;
        double lessonsPerDay = (double) lessonsCompleted / 30; // Assuming 30 days of data
        return lessonsPerDay > 0 ? (totalLessons - lessonsCompleted) / lessonsPerDay : 0;
    }
}