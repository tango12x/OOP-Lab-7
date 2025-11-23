import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Question {
    //CLASS ATTRIBUTES
    private String questionId;
    private String questionText;
    private ArrayList<String> options;          
    private int correctOptionIndex;        
    private String explanation;            
    private int points;               

    //CLASS CONSTRUCTOR
    public Question(String questionId, String questionText, ArrayList<String> options,
                    int correctOptionIndex, String explanation, int points) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.options = (options != null) ? new ArrayList<>(options) : new ArrayList<>();
        this.correctOptionIndex = correctOptionIndex;
        this.explanation = explanation;
        this.points = points;}

    // CLASS CONSTRUCTOR WITH NO EXPLANATION AND DEFAULT POINTS VALUE
    public Question(String questionId, String questionText, ArrayList<String> options, 
                   int correctOptionIndex) {
        this(questionId, questionText, options, correctOptionIndex, "", 1);}

    // GETTERS AND SETTERS
    public String getQuestionId() {
        return questionId;}
    public void setQuestionId(String questionId) {
        this.questionId = questionId;}
    public String getQuestionText() {
        return questionText;}
    public void setQuestionText(String questionText) {
        this.questionText = questionText;}
    public ArrayList<String> getOptions() {
        return options;}
    public void setOptions(ArrayList<String> options) {
        this.options = (options != null) ? options : new ArrayList<>();}
    public int getCorrectOptionIndex() {
        return correctOptionIndex;}
    public void setCorrectOptionIndex(int correctOptionIndex) {
        this.correctOptionIndex = correctOptionIndex;}
    public String getExplanation() {
        return explanation;}
    public void setExplanation(String explanation) {
        this.explanation = explanation;}
    public int getPoints() {
        return points;}
    public void setPoints(int points) {
        this.points = points > 0 ? points : 1;}

    //CLASS METHODS 

    //CHECK IF THE CHOSEN OPTION MAPS TO THE CORRECT ANSWER 
    public boolean isCorrectAnswer(int selectedIndex) {
        return selectedIndex == correctOptionIndex;}
    //RETURNS THE CORRECT ANSWER USING CORRECT OPTION INDEX
    public String getCorrectAnswerText() {
        if (correctOptionIndex >= 0 && correctOptionIndex < options.size()) {
            return options.get(correctOptionIndex);}
        return "";}
    //SHUFFLING THE OPTIONS 
    public void shuffleOptions() {
        if (options != null && options.size() > 1) {
            String correctAnswer = options.get(correctOptionIndex);
            Collections.shuffle(options);
            correctOptionIndex = options.indexOf(correctAnswer);}}
    //ADD OPTION TO THE QUESTION OPTIONS
    public void addOption(String option) {
        if (option != null && !option.trim().isEmpty()) {
            if (options == null) {
                options = new ArrayList<>();}
            if (!options.contains(option)) {
                options.add(option);}}}

    public void removeOption(int index) {
        if (options != null && index >= 0 && index < options.size()) {
            // If removing the correct option, reset correct option index
            if (index == correctOptionIndex) {
                correctOptionIndex = -1;
            } else if (index < correctOptionIndex) {
                correctOptionIndex--;
            }
            options.remove(index);
        }
    }

    // Validation methods
    public boolean isValid() {
        return questionId != null && !questionId.trim().isEmpty() &&
               questionText != null && !questionText.trim().isEmpty() &&
               options != null && options.size() >= 2 &&
               correctOptionIndex >= 0 && correctOptionIndex < options.size();
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionId='" + questionId + '\'' +
                ", questionText='" + questionText + '\'' +
                ", options=" + options +
                ", correctOptionIndex=" + correctOptionIndex +
                ", points=" + points +
                '}';
    }
}