import java.util.List;
import java.util.Objects;

public class Question {
    //CLASS ATTRIBUTES
    private String questionId;
    private String questionText;
    private List<String> options;          
    private int correctOptionIndex;        
    private String explanation;            
    private int points;               

    // Constructor
    public Question(String questionId, String questionText, ArrayList<String> options, 
                   int correctOptionIndex, String explanation, int points) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.options = options != null ? options : new ArrayList<>();
        this.correctOptionIndex = correctOptionIndex;
        this.explanation = explanation != null ? explanation : "";
        this.points = points > 0 ? points : 1;
    }

    // Constructor without explanation and points (default values)
    public Question(String questionId, String questionText, ArrayList<String> options, 
                   int correctOptionIndex) {
        this(questionId, questionText, options, correctOptionIndex, "", 1);
    }

    // Getters and Setters
    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public ArrayList<String> getOptions() { return options; }
    public void setOptions(ArrayList<String> options) { 
        this.options = options != null ? options : new ArrayList<>(); 
    }

    public int getCorrectOptionIndex() { return correctOptionIndex; }
    public void setCorrectOptionIndex(int correctOptionIndex) { 
        if (correctOptionIndex >= 0 && correctOptionIndex < options.size()) {
            this.correctOptionIndex = correctOptionIndex;
        }
    }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { 
        this.explanation = explanation != null ? explanation : ""; 
    }

    public int getPoints() { return points; }
    public void setPoints(int points) { 
        this.points = points > 0 ? points : 1; 
    }

    // Utility methods
    public boolean isCorrectAnswer(int selectedIndex) {
        return selectedIndex == correctOptionIndex;
    }

    public boolean isCorrectAnswer(String selectedOption) {
        return selectedOption != null && 
               selectedOption.equals(options.get(correctOptionIndex));
    }

    public String getCorrectAnswerText() {
        if (correctOptionIndex >= 0 && correctOptionIndex < options.size()) {
            return options.get(correctOptionIndex);
        }
        return "";
    }

    public void shuffleOptions() {
        if (options != null && options.size() > 1) {
            String correctAnswer = options.get(correctOptionIndex);
            Collections.shuffle(options);
            // Update correct option index after shuffling
            correctOptionIndex = options.indexOf(correctAnswer);
        }
    }

    public void addOption(String option) {
        if (option != null && !option.trim().isEmpty()) {
            if (options == null) {
                options = new ArrayList<>();
            }
            if (!options.contains(option)) {
                options.add(option);
            }
        }
    }

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