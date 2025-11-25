package backend.services;

import backend.models.*;
import backend.databaseManager.*;
import backend.models.Course;
import backend.models.Lesson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentQuizService {
    private String studentId;
    private Map<String, Map<String, QuizAttempt>> studentAttempts; // courseId -> (quizId -> attempt)
    private CourseDatabaseManager Cdb;

    public StudentQuizService(String studentId) {
        this.Cdb = new CourseDatabaseManager();
        this.studentId = studentId;
        this.studentAttempts = new HashMap<>();
        loadStudentAttempts();
    }

    public static class QuizAttempt {
        private String quizId;
        private String lessonId;
        private ArrayList<String> studentAnswers;
        private int score;
        private double percentage;
        private boolean passed;
        private String timestamp;
        private int attemptNumber;

        public QuizAttempt(String quizId, String lessonId, ArrayList<String> studentAnswers, 
                          int score, double percentage, boolean passed, int attemptNumber) {
            this.quizId = quizId;
            this.lessonId = lessonId;
            this.studentAnswers = studentAnswers;
            this.score = score;
            this.percentage = percentage;
            this.passed = passed;
            this.attemptNumber = attemptNumber;
            this.timestamp = java.time.LocalDateTime.now().toString();
        }

        // Getters
        public String getQuizId() { return quizId; }
        public String getLessonId() { return lessonId; }
        public ArrayList<String> getStudentAnswers() { return studentAnswers; }
        public int getScore() { return score; }
        public double getPercentage() { return percentage; }
        public boolean isPassed() { return passed; }
        public String getTimestamp() { return timestamp; }
        public int getAttemptNumber() { return attemptNumber; }
    }

    /**
     * Get quiz for a specific lesson
     */
    public Quiz getQuizForLesson(String courseId, String lessonId) {
        try {
            Course course = Cdb.getCourse(courseId);
            if (course != null) {
                for (Lesson lesson : course.getLessons()) {
                    if (lesson.getLessonId().equals(lessonId) && lesson.getQuiz() != null) {
                        return lesson.getQuiz();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Submit quiz answers and record attempt
     */
    public QuizAttempt submitQuiz(String courseId, String lessonId, Quiz quiz, ArrayList<String> answers) {
        if (quiz == null || answers == null) {
            return null;
        }

        // Calculate score
        int score = quiz.calculateScore(answers);
        double percentage = quiz.calculatePercentage(answers);
        boolean passed = quiz.isPassed(answers);

        // Get attempt number
        int attemptNumber = getNextAttemptNumber(courseId, quiz.getQuizId());

        // Create attempt record
        QuizAttempt attempt = new QuizAttempt(
            quiz.getQuizId(), lessonId, new ArrayList<>(answers),
            score, percentage, passed, attemptNumber
        );

        // Store attempt
        storeAttempt(courseId, quiz.getQuizId(), attempt);

        // If passed, mark lesson as completed
        if (passed) {
            markLessonAsCompleted(courseId, lessonId);
        }

        return attempt;
    }

    /**
     * Get student's attempts for a specific quiz
     */
    public ArrayList<QuizAttempt> getQuizAttempts(String courseId, String quizId) {
        Map<String, QuizAttempt> courseAttempts = studentAttempts.get(courseId);
        if (courseAttempts != null) {
            ArrayList<QuizAttempt> attempts = new ArrayList<>(courseAttempts.values());
            attempts.sort((a1, a2) -> Integer.compare(a2.getAttemptNumber(), a1.getAttemptNumber()));
            return attempts;
        }
        return new ArrayList<>();
    }

    /**
     * Get latest attempt for a quiz
     */
    public QuizAttempt getLatestAttempt(String courseId, String quizId) {
        ArrayList<QuizAttempt> attempts = getQuizAttempts(courseId, quizId);
        return attempts.isEmpty() ? null : attempts.get(0);
    }

    /**
     * Check if student can retake quiz (based on max attempts)
     */
    public boolean canRetakeQuiz(String courseId, Quiz quiz) {
        if (quiz.getMaxAttempts() == 0) return true; // Unlimited attempts
        
        QuizAttempt latest = getLatestAttempt(courseId, quiz.getQuizId());
        if (latest == null) return true;
        
        return latest.getAttemptNumber() < quiz.getMaxAttempts();
    }

    /**
     * Check if student has passed the quiz
     */
    public boolean hasPassedQuiz(String courseId, String quizId) {
        QuizAttempt latest = getLatestAttempt(courseId, quizId);
        return latest != null && latest.isPassed();
    }

    /**
     * Get next attempt number for a quiz
     */
    private int getNextAttemptNumber(String courseId, String quizId) {
        ArrayList<QuizAttempt> attempts = getQuizAttempts(courseId, quizId);
        return attempts.size() + 1;
    }

    /**
     * Store quiz attempt
     */
    private void storeAttempt(String courseId, String quizId, QuizAttempt attempt) {
        studentAttempts.putIfAbsent(courseId, new HashMap<>());
        studentAttempts.get(courseId).put(quizId + "_attempt_" + attempt.getAttemptNumber(), attempt);
        saveStudentAttempts();
    }

    /**
     * Mark lesson as completed when quiz is passed
     */
    private void markLessonAsCompleted(String courseId, String lessonId) {
        try {
            StudentService studentService = new StudentService(studentId);
            studentService.markLessonCompleted(courseId, lessonId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load student attempts from persistence
     */
    private void loadStudentAttempts() {
        // This would typically load from JSON file
        // For now, we'll initialize empty
        studentAttempts = new HashMap<>();
    }

    /**
     * Save student attempts to persistence
     */
    private void saveStudentAttempts() {
        // This would typically save to JSON file
        // Implementation depends on your JSON structure
    }
}