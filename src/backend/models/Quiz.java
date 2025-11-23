package backend.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;
import java.util.Objects;

public class Quiz {

    //CLASS ATTRIBUTES 
    private String quizId;
    private String lessonId;
    private String title;
    private String description;
    private ArrayList<Question> questions;
    private int passingScore;      
    private int maxAttempts;

    //CLASS CONSTRUCTOR FOR DEALING WITH JSON FILES
    public Quiz() {
        this.questions = new ArrayList<>();
        this.description = "";
        this.passingScore = 70;
        this.maxAttempts = 0; }

    //CLASS CONSTRUCTOR 
    public Quiz(String quizId, String lessonId, String title) {
        this();
        this.quizId = quizId;
        this.lessonId = lessonId;
        this.title = title != null ? title.trim() : "Untitled Quiz";}

    // Full CLASS CONSTRUCTOR
    public Quiz(String quizId, String lessonId, String title, String description,
                int passingScore, int maxAttempts) {
        this(quizId, lessonId, title);
        this.description = description != null ? description : "";
        this.passingScore = Math.max(0, Math.min(100, passingScore));
        this.maxAttempts = Math.max(0, maxAttempts);} //0 ATTEMPS MEAN UNLIMITED ATTEMPS 

    // GETTRES AND SETTERS 
    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }
    public String getLessonId() { return lessonId; }
    public void setLessonId(String lessonId) { this.lessonId = lessonId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description != null ? description : "";}
    public ArrayList<Question> getQuestions() { return questions; }
    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions != null ? questions : new ArrayList<>();}
    public int getPassingScore() { return passingScore; }
    public void setPassingScore(int passingScore) {
        this.passingScore = Math.max(0, Math.min(100, passingScore));}
    public int getMaxAttempts() { return maxAttempts; }
    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = Math.max(0, maxAttempts);}

    //CLASS METHODS 

    //CALCULATE THE TOTAL QUESTION POINTS OF THE QUIZ 
    public int getTotalPoints() {
        int total = 0;
        for (Question q : questions) {
            total += q.getPoints();}
        return total;}
    
    //RETURN THE NUMBER OF THE QUESTIONS IN THE QUIZ
    public int getQuestionCount() {
        return questions.size();}

    


    
}