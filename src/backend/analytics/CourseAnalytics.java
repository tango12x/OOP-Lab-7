package backend.analytics;

import java.util.*;

/**
 * Data container for course-level analytics
 * Stores all calculated metrics for a specific course
 */
public class CourseAnalytics {
    private String courseId;
    private String courseTitle;
    private int totalStudents;
    private int totalLessons;
    
    // Completion metrics
    private double courseCompletionRate; // Percentage
    private double averageLessonsCompleted;
    private Map<String, Double> lessonCompletionRates; // Lesson ID -> Completion %
    
    // Performance metrics
    private double averageQuizScore; // Percentage
    private double quizPassRate; // Percentage
    private Map<String, Double> averageLessonScores; // Lesson ID -> Average score %
    
    // Engagement metrics
    private double averageTimeToComplete; // Days
    private double dropoutRate; // Percentage
    
    // Distribution data for charts
    private int[] performanceDistribution; // Histogram data
    
    // Timestamp
    private Date generatedAt;
    
    public CourseAnalytics(String courseId, String courseTitle) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.generatedAt = new Date();
        this.lessonCompletionRates = new HashMap<>();
        this.averageLessonScores = new HashMap<>();
        this.performanceDistribution = new int[5]; // 5 buckets
    }
    
    // Getters and Setters with detailed comments
    
    public String getCourseId() { return courseId; }
    
    public String getCourseTitle() { return courseTitle; }
    
    /** Total number of students enrolled in the course */
    public int getTotalStudents() { return totalStudents; }
    public void setTotalStudents(int totalStudents) { this.totalStudents = totalStudents; }
    
    /** Total number of lessons in the course */
    public int getTotalLessons() { return totalLessons; }
    public void setTotalLessons(int totalLessons) { this.totalLessons = totalLessons; }
    
    /** Percentage of students who completed all lessons in the course */
    public double getCourseCompletionRate() { return courseCompletionRate; }
    public void setCourseCompletionRate(double courseCompletionRate) { 
        this.courseCompletionRate = courseCompletionRate; 
    }
    
    /** Average number of lessons completed per student */
    public double getAverageLessonsCompleted() { return averageLessonsCompleted; }
    public void setAverageLessonsCompleted(double averageLessonsCompleted) { 
        this.averageLessonsCompleted = averageLessonsCompleted; 
    }
    
    /** Completion rates for individual lessons */
    public Map<String, Double> getLessonCompletionRates() { return lessonCompletionRates; }
    public void setLessonCompletionRates(Map<String, Double> lessonCompletionRates) { 
        this.lessonCompletionRates = lessonCompletionRates; 
    }
    
    /** Average quiz score across all students and all quizzes */
    public double getAverageQuizScore() { return averageQuizScore; }
    public void setAverageQuizScore(double averageQuizScore) { 
        this.averageQuizScore = averageQuizScore; 
    }
    
    /** Percentage of quizzes that were passed (score >= 70%) */
    public double getQuizPassRate() { return quizPassRate; }
    public void setQuizPassRate(double quizPassRate) { 
        this.quizPassRate = quizPassRate; 
    }
    
    /** Average scores for individual lessons */
    public Map<String, Double> getAverageLessonScores() { return averageLessonScores; }
    public void setAverageLessonScores(Map<String, Double> averageLessonScores) { 
        this.averageLessonScores = averageLessonScores; 
    }
    
    /** Average time (in days) for students to complete the course */
    public double getAverageTimeToComplete() { return averageTimeToComplete; }
    public void setAverageTimeToComplete(double averageTimeToComplete) { 
        this.averageTimeToComplete = averageTimeToComplete; 
    }
    
    /** Percentage of students who started but didn't complete the course */
    public double getDropoutRate() { return dropoutRate; }
    public void setDropoutRate(double dropoutRate) { 
        this.dropoutRate = dropoutRate; 
    }
    
    /** Performance distribution histogram data (5 buckets: 0-20%, 21-40%, etc.) */
    public int[] getPerformanceDistribution() { return performanceDistribution; }
    public void setPerformanceDistribution(int[] performanceDistribution) { 
        this.performanceDistribution = performanceDistribution; 
    }
    
    /** When these analytics were generated */
    public Date getGeneratedAt() { return generatedAt; }
    
    /**
     * Returns overall course health score (0-100)
     * Composite metric based on completion rate, quiz performance, and engagement
     */
    public double getCourseHealthScore() {
        double completionWeight = 0.4;
        double performanceWeight = 0.4;
        double engagementWeight = 0.2;
        
        return (courseCompletionRate * completionWeight) +
               (averageQuizScore * performanceWeight) +
               ((100 - dropoutRate) * engagementWeight);
    }
    
    /**
     * Gets health status based on composite score
     */
    public String getHealthStatus() {
        double healthScore = getCourseHealthScore();
        if (healthScore >= 80) return "Excellent";
        if (healthScore >= 60) return "Good";
        if (healthScore >= 40) return "Fair";
        return "Needs Attention";
    }
    
    @Override
    public String toString() {
        return String.format(
            "Course Analytics for '%s' (%s):\n" +
            "Students: %d | Completion: %.1f%% | Avg Score: %.1f%%\n" +
            "Health: %s (%.1f/100)",
            courseTitle, courseId, totalStudents, courseCompletionRate, 
            averageQuizScore, getHealthStatus(), getCourseHealthScore()
        );
    }
}