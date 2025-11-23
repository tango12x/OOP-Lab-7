package backend.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;

public class Quiz {
    private String quizId;
    private String lessonId;
    private String title;
    private String description;
    private ArrayList<Question> questions;
    private int timeLimit; // in minutes, 0 means no time limit
    private int passingScore; // percentage required to pass
    private int maxAttempts; // 0 means unlimited attempts
    private boolean shuffleQuestions;
    private boolean shuffleOptions;
    private Date createdAt;
    private Date updatedAt;

    // Constructors
    public Quiz(String quizId, String lessonId, String title) {
        this.quizId = quizId;
        this.lessonId = lessonId;
        this.title = title;
        this.description = "";
        this.questions = new ArrayList<>();
        this.timeLimit = 0;
        this.passingScore = 70; // default 70%
        this.maxAttempts = 3; // default 3 attempts
        this.shuffleQuestions = false;
        this.shuffleOptions = false;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Quiz(String quizId, String lessonId, String title, String description, 
                int timeLimit, int passingScore, int maxAttempts) {
        this(quizId, lessonId, title);
        this.description = description != null ? description : "";
        this.timeLimit = Math.max(0, timeLimit);
        this.passingScore = Math.max(0, Math.min(100, passingScore));
        this.maxAttempts = Math.max(0, maxAttempts);
    }

    // Getters and Setters
    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { 
        this.quizId = quizId; 
        this.updatedAt = new Date();
    }

    public String getLessonId() { return lessonId; }
    public void setLessonId(String lessonId) { 
        this.lessonId = lessonId; 
        this.updatedAt = new Date();
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { 
        this.title = title; 
        this.updatedAt = new Date();
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description != null ? description : ""; 
        this.updatedAt = new Date();
    }

    public ArrayList<Question> getQuestions() { return questions; }
    public void setQuestions(ArrayList<Question> questions) { 
        this.questions = questions != null ? questions : new ArrayList<>(); 
        this.updatedAt = new Date();
    }

    public int getTimeLimit() { return timeLimit; }
    public void setTimeLimit(int timeLimit) { 
        this.timeLimit = Math.max(0, timeLimit); 
        this.updatedAt = new Date();
    }

    public int getPassingScore() { return passingScore; }
    public void setPassingScore(int passingScore) { 
        this.passingScore = Math.max(0, Math.min(100, passingScore)); 
        this.updatedAt = new Date();
    }

    public int getMaxAttempts() { return maxAttempts; }
    public void setMaxAttempts(int maxAttempts) { 
        this.maxAttempts = Math.max(0, maxAttempts); 
        this.updatedAt = new Date();
    }

    public boolean isShuffleQuestions() { return shuffleQuestions; }
    public void setShuffleQuestions(boolean shuffleQuestions) { 
        this.shuffleQuestions = shuffleQuestions; 
        this.updatedAt = new Date();
    }

    public boolean isShuffleOptions() { return shuffleOptions; }
    public void setShuffleOptions(boolean shuffleOptions) { 
        this.shuffleOptions = shuffleOptions; 
        this.updatedAt = new Date();
    }

    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }

    // Business logic methods
    public int getTotalPoints() {
        int total = 0;
        for (Question question : questions) {
            total += question.getPoints();
        }
        return total;
    }

    public int getQuestionCount() {
        return questions != null ? questions.size() : 0;
    }

    public boolean isPassingScore(int score) {
        if (questions.isEmpty()) return false;
        double percentage = (double) score / getTotalPoints() * 100;
        return percentage >= passingScore;
    }

    public int calculateScore(ArrayList<Integer> userAnswers) {
        if (userAnswers == null || questions == null) return 0;
        
        int score = 0;
        int minSize = Math.min(userAnswers.size(), questions.size());
        
        for (int i = 0; i < minSize; i++) {
            if (questions.get(i).isCorrectAnswer(userAnswers.get(i))) {
                score += questions.get(i).getPoints();
            }
        }
        return score;
    }

    public int calculateScoreFromStrings(ArrayList<String> userAnswers) {
        if (userAnswers == null || questions == null) return 0;
        
        int score = 0;
        int minSize = Math.min(userAnswers.size(), questions.size());
        
        for (int i = 0; i < minSize; i++) {
            if (questions.get(i).isCorrectAnswer(userAnswers.get(i))) {
                score += questions.get(i).getPoints();
            }
        }
        return score;
    }

    // Question management methods
    public void addQuestion(Question question) {
        if (question != null && question.isValid()) {
            if (questions == null) {
                questions = new ArrayList<>();
            }
            questions.add(question);
            this.updatedAt = new Date();
        }
    }

    public void removeQuestion(String questionId) {
        if (questions != null && questionId != null) {
            questions.removeIf(q -> questionId.equals(q.getQuestionId()));
            this.updatedAt = new Date();
        }
    }

    public Question getQuestion(String questionId) {
        if (questions != null && questionId != null) {
            for (Question q : questions) {
                if (questionId.equals(q.getQuestionId())) {
                    return q;
                }
            }
        }
        return null;
    }

    public void shuffleQuiz() {
        if (shuffleQuestions && questions != null) {
            Collections.shuffle(questions);
        }
        
        if (shuffleOptions && questions != null) {
            for (Question question : questions) {
                question.shuffleOptions();
            }
        }
    }

    // Validation methods
    public boolean isValid() {
        return quizId != null && !quizId.trim().isEmpty() &&
               lessonId != null && !lessonId.trim().isEmpty() &&
               title != null && !title.trim().isEmpty() &&
               questions != null && !questions.isEmpty() &&
               passingScore >= 0 && passingScore <= 100 &&
               maxAttempts >= 0;
    }

    public boolean hasQuestions() {
        return questions != null && !questions.isEmpty();
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "quizId='" + quizId + '\'' +
                ", lessonId='" + lessonId + '\'' +
                ", title='" + title + '\'' +
                ", questionCount=" + getQuestionCount() +
                ", passingScore=" + passingScore +
                ", maxAttempts=" + maxAttempts +
                '}';
    }
}