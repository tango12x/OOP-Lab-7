package frontend.student;

import backend.models.Quiz;
import backend.models.Question;
import backend.services.StudentQuizService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class QuizFrame extends JFrame {
    private Quiz quiz;
    private String courseId;
    private String lessonId;
    private String studentId;
    private StudentQuizService quizService;
    
    private ArrayList<String> studentAnswers;
    private int currentQuestionIndex;
    private JPanel mainPanel;
    private JLabel questionLabel;
    private ButtonGroup optionsGroup;
    private JPanel optionsPanel;
    private JButton prevButton;
    private JButton nextButton;
    private JButton submitButton;
    private JLabel progressLabel;
    private JPanel navigationPanel;

    public QuizFrame(String studentId, String courseId, String lessonId, Quiz quiz) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.quiz = quiz;
        this.quizService = new StudentQuizService(studentId);
        this.studentAnswers = new ArrayList<>();
        this.currentQuestionIndex = 0;
        
        initializeStudentAnswers();
        initializeUI();
        displayQuestion(0);
    }

    private void initializeStudentAnswers() {
        // Initialize with empty answers
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            studentAnswers.add("");
        }
    }

    private void initializeUI() {
        setTitle("Quiz: " + quiz.getTitle());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(mainPanel);

        // Quiz info panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Quiz Information"));
        
        JLabel titleLabel = new JLabel("<html><b>" + quiz.getTitle() + "</b></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        JLabel descLabel = new JLabel(quiz.getDescription());
        JLabel passingLabel = new JLabel("Passing Score: " + quiz.getPassingScore() + "%");
        
        JPanel infoDetails = new JPanel(new GridLayout(3, 1, 5, 5));
        infoDetails.add(titleLabel);
        infoDetails.add(descLabel);
        infoDetails.add(passingLabel);
        
        infoPanel.add(infoDetails, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Question panel
        JPanel questionPanel = new JPanel(new BorderLayout(10, 10));
        questionPanel.setBorder(BorderFactory.createTitledBorder("Question"));
        
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        questionLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        
        JScrollPane optionsScrollPane = new JScrollPane(optionsPanel);
        optionsScrollPane.setPreferredSize(new Dimension(700, 300));
        
        questionPanel.add(questionLabel, BorderLayout.NORTH);
        questionPanel.add(optionsScrollPane, BorderLayout.CENTER);
        mainPanel.add(questionPanel, BorderLayout.CENTER);

        // Navigation panel
        navigationPanel = new JPanel(new BorderLayout());
        
        // Progress label
        progressLabel = new JLabel();
        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        progressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        prevButton = new JButton("Previous");
        prevButton.addActionListener(e -> showPreviousQuestion());
        
        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> showNextQuestion());
        
        submitButton = new JButton("Submit Quiz");
        submitButton.addActionListener(e -> submitQuiz());
        submitButton.setBackground(new Color(0, 153, 0));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(submitButton);
        
        navigationPanel.add(progressLabel, BorderLayout.NORTH);
        navigationPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(navigationPanel, BorderLayout.SOUTH);

        updateNavigationButtons();
    }

    private void displayQuestion(int index) {
        if (index < 0 || index >= quiz.getQuestions().size()) return;
        
        currentQuestionIndex = index;
        Question question = quiz.getQuestions().get(index);
        
        // Update question text
        questionLabel.setText("<html><b>Question " + (index + 1) + ":</b> " + 
                             question.getQuestionText() + " (" + question.getPoints() + " points)</html>");
        
        // Clear existing options
        optionsPanel.removeAll();
        optionsGroup = new ButtonGroup();
        
        // Create radio buttons for each option
        for (int i = 0; i < question.getOptions().size(); i++) {
            String option = question.getOptions().get(i);
            JRadioButton radioButton = new JRadioButton(option);
            radioButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            radioButton.setActionCommand(option);
            
            // Pre-select if already answered
            if (option.equals(studentAnswers.get(index))) {
                radioButton.setSelected(true);
            }
            
            radioButton.addActionListener(e -> {
                studentAnswers.set(index, option);
                updateNavigationButtons();
            });
            
            optionsGroup.add(radioButton);
            optionsPanel.add(radioButton);
            optionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        // Update progress
        progressLabel.setText("Question " + (index + 1) + " of " + quiz.getQuestions().size());
        
        // Refresh the panel
        optionsPanel.revalidate();
        optionsPanel.repaint();
        updateNavigationButtons();
    }

    private void showPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            displayQuestion(currentQuestionIndex - 1);
        }
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < quiz.getQuestions().size() - 1) {
            displayQuestion(currentQuestionIndex + 1);
        }
    }

    private void updateNavigationButtons() {
        prevButton.setEnabled(currentQuestionIndex > 0);
        nextButton.setEnabled(currentQuestionIndex < quiz.getQuestions().size() - 1);
        
        // Check if all questions are answered
        boolean allAnswered = true;
        for (String answer : studentAnswers) {
            if (answer == null || answer.trim().isEmpty()) {
                allAnswered = false;
                break;
            }
        }
        
        submitButton.setEnabled(allAnswered);
        
        // Update submit button color
        if (allAnswered) {
            submitButton.setBackground(new Color(0, 153, 0));
        } else {
            submitButton.setBackground(Color.GRAY);
        }
    }

    private void submitQuiz() {
        // Confirm submission
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to submit the quiz? You cannot change your answers after submission.",
                "Confirm Submission", JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Check if student can retake quiz
        if (!quizService.canRetakeQuiz(courseId, quiz)) {
            JOptionPane.showMessageDialog(this,
                    "You have reached the maximum number of attempts for this quiz.",
                    "Maximum Attempts Reached", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Submit quiz
        StudentQuizService.QuizAttempt attempt = quizService.submitQuiz(
                courseId, lessonId, quiz, studentAnswers);

        if (attempt != null) {
            showQuizResults(attempt);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error submitting quiz. Please try again.",
                    "Submission Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showQuizResults(StudentQuizService.QuizAttempt attempt) {
        // Create results dialog
        JDialog resultsDialog = new JDialog(this, "Quiz Results", true);
        resultsDialog.setSize(500, 400);
        resultsDialog.setLocationRelativeTo(this);
        resultsDialog.setLayout(new BorderLayout(10, 10));
        resultsDialog.getContentPane().setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(attempt.isPassed() ? new Color(220, 255, 220) : new Color(255, 220, 220));
        
        JLabel resultLabel = new JLabel(attempt.isPassed() ? "🎉 Quiz Passed! 🎉" : "❌ Quiz Failed");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(resultLabel, BorderLayout.CENTER);
        resultsDialog.add(headerPanel, BorderLayout.NORTH);

        // Results details
        JPanel detailsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        detailsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        detailsPanel.add(createResultRow("Score:", attempt.getScore() + " / " + quiz.getTotalPoints() + " points"));
        detailsPanel.add(createResultRow("Percentage:", String.format("%.1f%%", attempt.getPercentage())));
        detailsPanel.add(createResultRow("Passing Score:", quiz.getPassingScore() + "%"));
        detailsPanel.add(createResultRow("Attempt:", "#" + attempt.getAttemptNumber()));
        detailsPanel.add(createResultRow("Status:", attempt.isPassed() ? "PASSED" : "FAILED"));
        
        resultsDialog.add(detailsPanel, BorderLayout.CENTER);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton reviewButton = new JButton("Review Answers");
        reviewButton.addActionListener(e -> {
            resultsDialog.dispose();
            showAnswerReview(attempt);
        });
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            resultsDialog.dispose();
            this.dispose();
        });
        
        buttonPanel.add(reviewButton);
        buttonPanel.add(closeButton);
        resultsDialog.add(buttonPanel, BorderLayout.SOUTH);

        resultsDialog.setVisible(true);
    }

    private JPanel createResultRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueComp.setHorizontalAlignment(SwingConstants.RIGHT);
        
        row.add(labelComp, BorderLayout.WEST);
        row.add(valueComp, BorderLayout.EAST);
        
        return row;
    }

    private void showAnswerReview(StudentQuizService.QuizAttempt attempt) {
        JDialog reviewDialog = new JDialog(this, "Answer Review", true);
        reviewDialog.setSize(700, 500);
        reviewDialog.setLocationRelativeTo(this);
        reviewDialog.setLayout(new BorderLayout());

        JTextArea reviewArea = new JTextArea();
        reviewArea.setEditable(false);
        reviewArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        reviewArea.setMargin(new Insets(10, 10, 10, 10));

        StringBuilder reviewText = new StringBuilder();
        reviewText.append("QUIZ REVIEW - ").append(quiz.getTitle()).append("\n");
        reviewText.append("=".repeat(50)).append("\n\n");

        ArrayList<Question> questions = quiz.getQuestions();
        ArrayList<String> studentAnswers = attempt.getStudentAnswers();

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String studentAnswer = studentAnswers.get(i);
            String correctAnswer = q.getCorrectOption();
            boolean isCorrect = q.isCorrectAnswer(studentAnswer);

            reviewText.append("Question ").append(i + 1).append(": ").append(q.getQuestionText()).append("\n");
            reviewText.append("Your Answer: ").append(studentAnswer.isEmpty() ? "Not answered" : studentAnswer);
            reviewText.append(isCorrect ? " ✓ CORRECT\n" : " ✗ INCORRECT\n");
            reviewText.append("Correct Answer: ").append(correctAnswer).append("\n");
            
            if (!isCorrect && !studentAnswer.isEmpty()) {
                reviewText.append("Explanation: ").append(q.getExplanation()).append("\n");
            }
            
            reviewText.append("-".repeat(50)).append("\n\n");
        }

        reviewArea.setText(reviewText.toString());

        JScrollPane scrollPane = new JScrollPane(reviewArea);
        reviewDialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> reviewDialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        reviewDialog.add(buttonPanel, BorderLayout.SOUTH);

        reviewDialog.setVisible(true);
    }

    public static void main(String[] args) {
        // Test method
        SwingUtilities.invokeLater(() -> {
            // This would be called from StudentDashboard with actual data
            QuizFrame frame = new QuizFrame("testStudent", "testCourse", "testLesson", null);
            frame.setVisible(true);
        });
    }
}