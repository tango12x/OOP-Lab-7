package backend.analytics;

import backend.models.*;
import backend.databaseManager.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main analytics engine that processes and aggregates student performance data
 * Provides insights for instructors and administrators
 * 
 * Key Features:
 * - Student performance tracking per course/lesson
 * - Completion rate calculations
 * - Quiz performance analytics
 * - Progress visualization data
 */
public class AnalyticsManager {
    private CourseDatabaseManager courseDB;
    private UsersDatabaseManager userDB;
    
    public AnalyticsManager() {
        this.courseDB = new CourseDatabaseManager();
        this.userDB = new UsersDatabaseManager();
    }
    
    /**
     * Calculates comprehensive course analytics for instructor insights
     * @param courseId The course to analyze
     * @return CourseAnalytics object with all metrics
     */
    public CourseAnalytics getCourseAnalytics(String courseId) {
        Course course = courseDB.getCourse(courseId);
        if (course == null) {
            return new CourseAnalytics(courseId, "Course Not Found");
        }
        
        List<Student> enrolledStudents = getEnrolledStudents(courseId);
        CourseAnalytics analytics = new CourseAnalytics(courseId, course.getTitle());
        
        // Calculate basic metrics
        analytics.setTotalStudents(enrolledStudents.size());
        analytics.setTotalLessons(course.getLessons().size());
        
        // Calculate completion rates
        calculateCompletionRates(analytics, enrolledStudents, course);
        
        // Calculate quiz performance
        calculateQuizPerformance(analytics, enrolledStudents, course);
        
        // Calculate engagement metrics
        calculateEngagementMetrics(analytics, enrolledStudents, course);
        
        // Generate student performance distribution
        generatePerformanceDistribution(analytics, enrolledStudents, course);
        
        return analytics;
    }
    
    /**
     * Retrieves all students enrolled in a specific course
     */
    private List<Student> getEnrolledStudents(String courseId) {
        return userDB.getAllUsers().stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .filter(student -> student.getEnrolledCourses().contains(courseId))
                .collect(Collectors.toList());
    }
    
    /**
     * Calculates course and lesson completion rates
     */
    private void calculateCompletionRates(CourseAnalytics analytics, 
                                         List<Student> students, 
                                         Course course) {
        if (students.isEmpty()) {
            analytics.setCourseCompletionRate(0.0);
            analytics.setAverageLessonsCompleted(0.0);
            return;
        }
        
        int totalCourseCompletions = 0;
        double totalLessonsCompleted = 0;
        
        // Lesson completion tracking
        Map<String, Integer> lessonCompletionCount = new HashMap<>();
        course.getLessons().forEach(lesson -> 
            lessonCompletionCount.put(lesson.getLessonId(), 0));
        
        for (Student student : students) {
            // Check if student completed all lessons (course completion)
            if (hasCompletedCourse(student, course)) {
                totalCourseCompletions++;
            }
            
            // Count lessons completed by this student
            int studentLessonsCompleted = countCompletedLessons(student, course);
            totalLessonsCompleted += studentLessonsCompleted;
            
            // Update lesson completion counts
            updateLessonCompletionCounts(student, course, lessonCompletionCount);
        }
        
        // Set completion rates
        analytics.setCourseCompletionRate((double) totalCourseCompletions / students.size() * 100);
        analytics.setAverageLessonsCompleted(totalLessonsCompleted / students.size());
        
        // Calculate lesson completion rates
        Map<String, Double> lessonCompletionRates = new HashMap<>();
        for (Map.Entry<String, Integer> entry : lessonCompletionCount.entrySet()) {
            double rate = (double) entry.getValue() / students.size() * 100;
            lessonCompletionRates.put(entry.getKey(), rate);
        }
        analytics.setLessonCompletionRates(lessonCompletionRates);
    }
    
    /**
     * Calculates quiz performance metrics including averages and pass rates
     */
    private void calculateQuizPerformance(CourseAnalytics analytics, 
                                        List<Student> students, 
                                        Course course) {
        if (students.isEmpty()) {
            analytics.setAverageQuizScore(0.0);
            analytics.setQuizPassRate(0.0);
            return;
        }
        
        double totalQuizScores = 0;
        int totalQuizzesTaken = 0;
        int passedQuizzes = 0;
        
        // Track quiz performance by lesson
        Map<String, List<Double>> lessonQuizScores = new HashMap<>();
        course.getLessons().forEach(lesson -> 
            lessonQuizScores.put(lesson.getLessonId(), new ArrayList<>()));
        

        //!CAUSES ERRORS SO SHUT FOR NOW
        // for (Student student : students) {
        //     for (Lesson lesson : course.getLessons()) {
        //         QuizAttempt bestAttempt = getBestQuizAttempt(student, lesson.getLessonId());
        //         if (bestAttempt != null) {
        //             double scorePercentage = bestAttempt.getPercentage();
        //             totalQuizScores += scorePercentage;
        //             totalQuizzesTaken++;
                    
        //             if (scorePercentage >= 70) { // Passing threshold
        //                 passedQuizzes++;
        //             }
                    
        //             // Add to lesson-specific scores
        //             lessonQuizScores.get(lesson.getLessonId()).add(scorePercentage);
        //         }
        //     }
        // }
        
        // Set overall quiz metrics
        analytics.setAverageQuizScore(totalQuizzesTaken > 0 ? totalQuizScores / totalQuizzesTaken : 0);
        analytics.setQuizPassRate(totalQuizzesTaken > 0 ? (double) passedQuizzes / totalQuizzesTaken * 100 : 0);
        
        // Calculate average scores per lesson
        Map<String, Double> averageLessonScores = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : lessonQuizScores.entrySet()) {
            double average = entry.getValue().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            averageLessonScores.put(entry.getKey(), average);
        }
        analytics.setAverageLessonScores(averageLessonScores);
    }
    
    /**
     * Calculates student engagement metrics
     */
    private void calculateEngagementMetrics(CourseAnalytics analytics, 
                                          List<Student> students, 
                                          Course course) {
        if (students.isEmpty()) {
            analytics.setAverageTimeToComplete(0.0);
            analytics.setDropoutRate(0.0);
            return;
        }
        
        // Calculate average time to complete course (for completed students)
        List<Student> completedStudents = students.stream()
                .filter(student -> hasCompletedCourse(student, course))
                .collect(Collectors.toList());
        
        double totalCompletionTime = completedStudents.stream()
                .mapToDouble(student -> getCourseCompletionTime(student, course))
                .sum();
        
        analytics.setAverageTimeToComplete(completedStudents.isEmpty() ? 0 : 
            totalCompletionTime / completedStudents.size());
        
        // Calculate dropout rate (students who started but didn't complete)
        long studentsStarted = students.stream()
                .filter(student -> hasStartedCourse(student, course))
                .count();
        
        long studentsDropped = students.stream()
                .filter(student -> hasStartedCourse(student, course) && !hasCompletedCourse(student, course))
                .count();
        
        analytics.setDropoutRate(studentsStarted > 0 ? (double) studentsDropped / studentsStarted * 100 : 0);
    }
    
    /**
     * Generates performance distribution for charting (histogram data)
     */
    private void generatePerformanceDistribution(CourseAnalytics analytics, 
                                               List<Student> students, 
                                               Course course) {
        // Create performance buckets: 0-20%, 21-40%, 41-60%, 61-80%, 81-100%
        int[] distribution = new int[5];
        
        for (Student student : students) {
            double performance = calculateStudentPerformance(student, course);
            int bucket = (int) (performance / 20);
            if (bucket >= 5) bucket = 4; // Handle 100% case
            distribution[bucket]++;
        }
        
        analytics.setPerformanceDistribution(distribution);
    }
    
    // Helper methods for student progress analysis
    private boolean hasCompletedCourse(Student student, Course course) {
        return countCompletedLessons(student, course) == course.getLessons().size();
    }
    
    private boolean hasStartedCourse(Student student, Course course) {
        return countCompletedLessons(student, course) > 0;
    }
    
    private int countCompletedLessons(Student student, Course course) {
        // This would check student's progress tracking for completed lessons
        // Implementation depends on how progress is stored
        return (int) course.getLessons().stream()
                .filter(lesson -> isLessonCompleted(student, lesson.getLessonId()))
                .count();
    }
    
    private void updateLessonCompletionCounts(Student student, Course course, 
                                            Map<String, Integer> completionCount) {
        for (Lesson lesson : course.getLessons()) {
            if (isLessonCompleted(student, lesson.getLessonId())) {
                completionCount.merge(lesson.getLessonId(), 1, Integer::sum);
            }
        }
    }
    
    private boolean isLessonCompleted(Student student, String lessonId) {
        // Check if student has completed this lesson (passed quiz or marked complete)
        // This is a simplified implementation
        return getBestQuizAttempt(student, lessonId) != null && 
               getBestQuizAttempt(student, lessonId).getPercentage() >= 50;
    }
    
    private QuizAttempt getBestQuizAttempt(Student student, String lessonId) {
        // Get the best quiz attempt for a student in a lesson
        // This would typically query a quiz attempts database
        // Simplified implementation - returns null if no attempts
        return null; // Would be implemented based on your data structure
    }
    
    private double getCourseCompletionTime(Student student, Course course) {
        // Calculate time taken to complete the course (in days)
        // Simplified implementation
        return 30.0; // Default value
    }
    
    private double calculateStudentPerformance(Student student, Course course) {
        int completedLessons = countCompletedLessons(student, course);
        return course.getLessons().isEmpty() ? 0 : 
               (double) completedLessons / course.getLessons().size() * 100;
    }
    
    /**
     * Gets analytics for multiple courses (for admin dashboard)
     */
    public List<CourseAnalytics> getAllCoursesAnalytics() {
        return courseDB.getAllCourses().stream()
                .map(course -> getCourseAnalytics(course.getCourseId()))
                .collect(Collectors.toList());
    }
    
    /**
     * Gets student-specific analytics for personalized feedback
     */
    public StudentAnalytics getStudentAnalytics(String studentId, String courseId) {
        Student student = (Student) userDB.getUser(studentId);
        Course course = courseDB.getCourse(courseId);
        
        if (student == null || course == null) {
            return null;
        }
        
        StudentAnalytics analytics = new StudentAnalytics(studentId, courseId);
        
        // Calculate student-specific metrics
        int completedLessons = countCompletedLessons(student, course);
        analytics.setLessonsCompleted(completedLessons);
        analytics.setTotalLessons(course.getLessons().size());
        analytics.setCompletionPercentage(course.getLessons().isEmpty() ? 0 : 
            (double) completedLessons / course.getLessons().size() * 100);
        
        // Calculate average quiz score
        double totalScore = 0;
        int quizzesTaken = 0;
        
        for (Lesson lesson : course.getLessons()) {
            QuizAttempt attempt = getBestQuizAttempt(student, lesson.getLessonId());
            if (attempt != null) {
                totalScore += attempt.getPercentage();
                quizzesTaken++;
            }
        }
        
        analytics.setAverageQuizScore(quizzesTaken > 0 ? totalScore / quizzesTaken : 0);
        
        return analytics;
    }
}