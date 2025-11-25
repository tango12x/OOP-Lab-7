package backend.services;

import backend.models.Quiz;
import backend.models.Question;
import backend.models.Course;
import backend.models.Lesson;
import backend.databaseManager.CourseDatabaseManager;
import java.util.ArrayList;

public class InstructorQuizService {
    private String instructorId;
    private CourseDatabaseManager courseDB;

    public InstructorQuizService(String instructorId) {
        this.instructorId = instructorId;
        this.courseDB = new CourseDatabaseManager();
    }

    /**
     * Create a new quiz for a lesson
     */
    public boolean createQuizForLesson(String courseId, String lessonId, Quiz quiz) {
        try {
            Course course = courseDB.getCourse(courseId);
            if (course == null) {
                System.out.println("Course not found");
                return false;
            }

            // Verify instructor owns the course
            if (!course.getInstructorId().equals(instructorId)) {
                System.out.println("Instructor does not own this course");
                return false;
            }

            // Find the lesson and set the quiz
            for (Lesson lesson : course.getLessons()) {
                if (lesson.getLessonId().equals(lessonId)) {
                    lesson.setQuiz(quiz);
                    courseDB.update(course);
                    courseDB.SaveCoursesToFile();
                    System.out.println("Quiz created successfully for lesson: " + lessonId);
                    return true;
                }
            }
            System.out.println("Lesson not found");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update an existing quiz for a lesson
     */
    public boolean updateQuizForLesson(String courseId, String lessonId, Quiz updatedQuiz) {
        try {
            Course course = courseDB.getCourse(courseId);
            if (course == null) {
                System.out.println("Course not found");
                return false;
            }

            // Verify instructor owns the course
            if (!course.getInstructorId().equals(instructorId)) {
                System.out.println("Instructor does not own this course");
                return false;
            }

            // Find the lesson and update the quiz
            for (Lesson lesson : course.getLessons()) {
                if (lesson.getLessonId().equals(lessonId)) {
                    lesson.setQuiz(updatedQuiz);
                    courseDB.update(course);
                    courseDB.SaveCoursesToFile();
                    System.out.println("Quiz updated successfully for lesson: " + lessonId);
                    return true;
                }
            }
            System.out.println("Lesson not found");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete quiz from a lesson
     */
    public boolean deleteQuizFromLesson(String courseId, String lessonId) {
        try {
            Course course = courseDB.getCourse(courseId);
            if (course == null) {
                System.out.println("Course not found");
                return false;
            }

            // Verify instructor owns the course
            if (!course.getInstructorId().equals(instructorId)) {
                System.out.println("Instructor does not own this course");
                return false;
            }

            // Find the lesson and remove the quiz
            for (Lesson lesson : course.getLessons()) {
                if (lesson.getLessonId().equals(lessonId)) {
                    lesson.setQuiz(null);
                    courseDB.update(course);
                    courseDB.SaveCoursesToFile();
                    System.out.println("Quiz deleted successfully from lesson: " + lessonId);
                    return true;
                }
            }
            System.out.println("Lesson not found");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get quiz for a specific lesson
     */
    public Quiz getQuizForLesson(String courseId, String lessonId) {
        try {
            Course course = courseDB.getCourse(courseId);
            if (course == null) {
                return null;
            }

            // Verify instructor owns the course
            if (!course.getInstructorId().equals(instructorId)) {
                return null;
            }

            // Find the lesson and return the quiz
            for (Lesson lesson : course.getLessons()) {
                if (lesson.getLessonId().equals(lessonId)) {
                    return lesson.getQuiz();
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Add question to a quiz
     */
    public boolean addQuestionToQuiz(String courseId, String lessonId, Question question) {
        try {
            Quiz quiz = getQuizForLesson(courseId, lessonId);
            if (quiz == null) {
                System.out.println("Quiz not found for this lesson");
                return false;
            }

            if (question != null && question.isValid()) {
                quiz.addQuestion(question);
                return updateQuizForLesson(courseId, lessonId, quiz);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Remove question from a quiz
     */
    public boolean removeQuestionFromQuiz(String courseId, String lessonId, String questionId) {
        try {
            Quiz quiz = getQuizForLesson(courseId, lessonId);
            if (quiz == null) {
                System.out.println("Quiz not found for this lesson");
                return false;
            }

            boolean removed = quiz.removeQuestion(questionId);
            if (removed) {
                return updateQuizForLesson(courseId, lessonId, quiz);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update question in a quiz
     */
    public boolean updateQuestionInQuiz(String courseId, String lessonId, Question updatedQuestion) {
        try {
            Quiz quiz = getQuizForLesson(courseId, lessonId);
            if (quiz == null) {
                System.out.println("Quiz not found for this lesson");
                return false;
            }

            // Remove the old question and add the updated one
            boolean removed = quiz.removeQuestion(updatedQuestion.getQuestionId());
            if (removed) {
                quiz.addQuestion(updatedQuestion);
                return updateQuizForLesson(courseId, lessonId, quiz);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all questions for a quiz
     */
    public ArrayList<Question> getQuestionsForQuiz(String courseId, String lessonId) {
        try {
            Quiz quiz = getQuizForLesson(courseId, lessonId);
            if (quiz != null) {
                return quiz.getQuestions();
            }
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Validate quiz configuration
     */
    public String validateQuiz(Quiz quiz) {
        if (quiz == null) {
            return "Quiz cannot be null";
        }

        if (!quiz.isValid()) {
            return "Quiz is not valid. Please check quiz ID, lesson ID, title, and questions.";
        }

        if (quiz.getPassingScore() < 0 || quiz.getPassingScore() > 100) {
            return "Passing score must be between 0 and 100";
        }

        if (quiz.getMaxAttempts() < 0) {
            return "Max attempts cannot be negative";
        }

        if (quiz.getQuestions().isEmpty()) {
            return "Quiz must have at least one question";
        }

        // Validate all questions
        for (Question question : quiz.getQuestions()) {
            if (!question.isValid()) {
                return "Question '" + question.getQuestionText() + "' is not valid";
            }
        }

        return "VALID";
    }
}