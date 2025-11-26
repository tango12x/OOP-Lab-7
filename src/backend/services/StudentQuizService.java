package backend.services;

import backend.models.*;
import backend.databaseManager.*;
import backend.models.Course;
import backend.models.Lesson;
import backend.models.Student.studentCourseInfo;

import java.util.ArrayList;
import java.util.HashMap;

//!NTST
public class StudentQuizService {
    private String studentId;
    private Student student;
    private UsersDatabaseManager Udb;
    private CourseDatabaseManager Cdb;

    public StudentQuizService(String studentId) {
        try {
            this.Cdb = new CourseDatabaseManager();
            this.Udb = new UsersDatabaseManager();
            this.studentId = studentId;
            this.student = (Student) Udb.getUser(studentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get quiz for a specific lesson
     */
    public Quiz getQuizForLesson(String courseId, String lessonId) {
        try {
            Course course = Cdb.getCourse(courseId);
            if (course != null) {
                for (Lesson lesson : course.getLessons()) {
                    if (lesson.getLessonId().equals(lessonId)) {
                        if (lesson.getQuiz() == null) {
                            System.out.println("Lesson " + lessonId + " has no quiz");
                            return null;
                        }
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
     * Submit quiz answers and record attempt score and mark the lesson as completed
     * if passed
     */
    public double submitQuiz(String courseId, String lessonId, Quiz quiz, ArrayList<String> answers) {
        if (quiz == null || answers == null) {
            return -1;
        }

        // Calculate score
        int score = quiz.calculateScore(answers);
        boolean passed = quiz.isPassed(answers);

        student.addQuizAttempt(courseId, lessonId, score);
        Udb.update(student);
        Udb.SaveUsersToFile();

        // If passed, mark lesson as completed
        if (passed) {
            markLessonAsCompleted(courseId, lessonId);
        }

        return score;
    }

    /**
     * Check if student can retake quiz (based on max attempts)
     */
    public boolean canRetakeQuiz(String courseId, Quiz quiz) {
        try {
            if (quiz.getMaxAttempts() == 0)
                return true; // Unlimited attempts
            studentCourseInfo info = student.getEnrolledCourses().get(courseId);
            ArrayList<Integer> attempts = info.getQuizAttempts().get(quiz.getLessonId());
            if (attempts == null)
                return true;
            int numAttempts = attempts.size();

            return numAttempts < quiz.getMaxAttempts();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public int nextAttemptNumber(String courseId, Quiz quiz) {
        try {
            studentCourseInfo info = student.getEnrolledCourses().get(courseId);
            ArrayList<Integer> attempts = info.getQuizAttempts().get(quiz.getLessonId());
            if (attempts == null)
                return -1;
            int numAttempts = attempts.size();

            return numAttempts + 1;
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            return -1;
        }
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

}