package frontend.Instructor;

import backend.models.Quiz;
import backend.models.Question;
import backend.models.Course;
import backend.models.Lesson;
import backend.services.InstructorQuizService;
import backend.services.InstructorService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class QuizEditorFrame extends JDialog {
    private String courseId;
    private String lessonId;
    private String instructorId;
    private Quiz currentQuiz;
    private InstructorQuizService quizService;
    private InstructorService instructorService;
    
    private JPanel mainPanel;
    private JTextField txtQuizTitle;
    private JTextArea txtQuizDescription;
    private JSpinner spnPassingScore;
    private JSpinner spnMaxAttempts;
    private JTable questionsTable;
    private DefaultTableModel questionsModel;
    private JButton btnAddQuestion;
    private JButton btnEditQuestion;
    private JButton btnDeleteQuestion;
    private JButton btnSaveQuiz;
    private JButton btnCancel;
    private JLabel lblLessonInfo;

    public QuizEditorFrame(JFrame parent, String courseId, String lessonId, String instructorId) {
        super(parent, "Quiz Editor", true);
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.instructorId = instructorId;
        this.quizService = new InstructorQuizService(instructorId);
        this.instructorService = new InstructorService(instructorId);
        
        initializeUI();
        loadQuizData();
    }

    private void initializeUI() {
        setSize(900, 700);
        setLocationRelativeTo(getParent());
        setResizable(false);

        // Main panel
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(mainPanel);

        // Lesson info panel
        JPanel infoPanel = createInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Quiz details panel
        JPanel detailsPanel = createDetailsPanel();
        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        // Questions panel
        JPanel questionsPanel = createQuestionsPanel();
        mainPanel.add(questionsPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = createButtonsPanel();
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lesson Information"));
        
        // Get lesson info
        String lessonTitle = "Unknown Lesson";
        String courseTitle = "Unknown Course";
        try {
            ArrayList<Course> courses = instructorService.getCreatedCourses();
            for (Course course : courses) {
                if (course.getCourseId().equals(courseId)) {
                    courseTitle = course.getTitle();
                    for (Lesson lesson : course.getLessons()) {
                        if (lesson.getLessonId().equals(lessonId)) {
                            lessonTitle = lesson.getTitle();
                            break;
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        lblLessonInfo = new JLabel("<html><b>Course:</b> " + courseTitle + 
                                 " | <b>Lesson:</b> " + lessonTitle + "</html>");
        lblLessonInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        panel.add(lblLessonInfo, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Quiz Details"));
        panel.setPreferredSize(new Dimension(800, 150));

        // Quiz Title
        JLabel lblTitle = new JLabel("Quiz Title:*");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtQuizTitle = new JTextField();
        
        // Quiz Description
        JLabel lblDescription = new JLabel("Description:");
        lblDescription.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtQuizDescription = new JTextArea(3, 20);
        txtQuizDescription.setLineWrap(true);
        txtQuizDescription.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(txtQuizDescription);

        // Passing Score
        JLabel lblPassingScore = new JLabel("Passing Score (%):*");
        lblPassingScore.setFont(new Font("Segoe UI", Font.BOLD, 12));
        spnPassingScore = new JSpinner(new SpinnerNumberModel(50, 0, 100, 1));

        // Max Attempts (0 = unlimited)
        JLabel lblMaxAttempts = new JLabel("Max Attempts (0=unlimited):");
        lblMaxAttempts.setFont(new Font("Segoe UI", Font.BOLD, 12));
        spnMaxAttempts = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));

        panel.add(lblTitle);
        panel.add(txtQuizTitle);
        panel.add(lblDescription);
        panel.add(descScrollPane);
        panel.add(lblPassingScore);
        panel.add(spnPassingScore);
        panel.add(lblMaxAttempts);
        panel.add(spnMaxAttempts);

        return panel;
    }

    private JPanel createQuestionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Questions"));
        panel.setPreferredSize(new Dimension(800, 300));

        // Questions table
        questionsModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {"Question ID", "Question Text", "Options", "Correct Answer", "Points"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        questionsTable = new JTable(questionsModel);
        questionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        questionsTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        questionsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        JScrollPane tableScrollPane = new JScrollPane(questionsTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        // Questions buttons panel
        JPanel questionsButtonsPanel = new JPanel(new FlowLayout());
        
        btnAddQuestion = new JButton("Add Question");
        btnAddQuestion.addActionListener(e -> addQuestion());
        
        btnEditQuestion = new JButton("Edit Question");
        btnEditQuestion.addActionListener(e -> editQuestion());
        
        btnDeleteQuestion = new JButton("Delete Question");
        btnDeleteQuestion.addActionListener(e -> deleteQuestion());
        
        questionsButtonsPanel.add(btnAddQuestion);
        questionsButtonsPanel.add(btnEditQuestion);
        questionsButtonsPanel.add(btnDeleteQuestion);
        
        panel.add(questionsButtonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        btnSaveQuiz = new JButton("Save Quiz");
        btnSaveQuiz.setBackground(new Color(0, 153, 0));
        btnSaveQuiz.setForeground(Color.WHITE);
        btnSaveQuiz.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSaveQuiz.addActionListener(e -> saveQuiz());
        
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> cancel());
        
        panel.add(btnSaveQuiz);
        panel.add(btnCancel);
        
        return panel;
    }

    private void loadQuizData() {
        // Load existing quiz or create new one
        currentQuiz = quizService.getQuizForLesson(courseId, lessonId);
        
        if (currentQuiz != null) {
            // Populate fields with existing quiz data
            txtQuizTitle.setText(currentQuiz.getTitle());
            txtQuizDescription.setText(currentQuiz.getDescription());
            spnPassingScore.setValue(currentQuiz.getPassingScore());
            spnMaxAttempts.setValue(currentQuiz.getMaxAttempts());
            
            // Load questions
            loadQuestions();
        } else {
            // Initialize with default values for new quiz
            txtQuizTitle.setText("");
            txtQuizDescription.setText("");
            spnPassingScore.setValue(50);
            spnMaxAttempts.setValue(0);
        }
    }

    private void loadQuestions() {
        questionsModel.setRowCount(0);
        if (currentQuiz != null) {
            for (Question question : currentQuiz.getQuestions()) {
                String options = String.join("; ", question.getOptions());
                questionsModel.addRow(new Object[]{
                    question.getQuestionId(),
                    question.getQuestionText(),
                    options,
                    question.getCorrectOption(),
                    question.getPoints()
                });
            }
        }
    }

    private void addQuestion() {
        QuestionEditorDialog dialog = new QuestionEditorDialog(this, null);
        dialog.setVisible(true);
        
        Question newQuestion = dialog.getQuestion();
        if (newQuestion != null) {
            if (currentQuiz == null) {
                // Create new quiz if it doesn't exist
                currentQuiz = new Quiz(generateQuizId(), lessonId, txtQuizTitle.getText());
            }
            
            // Add question to the quiz (in memory)
            currentQuiz.addQuestion(newQuestion);
            
            // Update table
            String options = String.join("; ", newQuestion.getOptions());
            questionsModel.addRow(new Object[]{
                newQuestion.getQuestionId(),
                newQuestion.getQuestionText(),
                options,
                newQuestion.getCorrectOption(),
                newQuestion.getPoints()
            });
        }
    }

    private void editQuestion() {
        int selectedRow = questionsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a question to edit.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String questionId = (String) questionsModel.getValueAt(selectedRow, 0);
        Question questionToEdit = findQuestionById(questionId);
        
        if (questionToEdit != null) {
            QuestionEditorDialog dialog = new QuestionEditorDialog(this, questionToEdit);
            dialog.setVisible(true);
            
            Question updatedQuestion = dialog.getQuestion();
            if (updatedQuestion != null) {
                // Update question in the quiz (in memory)
                currentQuiz.removeQuestion(questionId);
                currentQuiz.addQuestion(updatedQuestion);
                
                // Update table
                String options = String.join("; ", updatedQuestion.getOptions());
                questionsModel.setValueAt(updatedQuestion.getQuestionText(), selectedRow, 1);
                questionsModel.setValueAt(options, selectedRow, 2);
                questionsModel.setValueAt(updatedQuestion.getCorrectOption(), selectedRow, 3);
                questionsModel.setValueAt(updatedQuestion.getPoints(), selectedRow, 4);
            }
        }
    }

    private void deleteQuestion() {
        int selectedRow = questionsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a question to delete.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String questionId = (String) questionsModel.getValueAt(selectedRow, 0);
        String questionText = (String) questionsModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this question?\n\n" + questionText,
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (currentQuiz != null) {
                currentQuiz.removeQuestion(questionId);
                questionsModel.removeRow(selectedRow);
            }
        }
    }

    private Question findQuestionById(String questionId) {
        if (currentQuiz != null) {
            for (Question question : currentQuiz.getQuestions()) {
                if (question.getQuestionId().equals(questionId)) {
                    return question;
                }
            }
        }
        return null;
    }

    private void saveQuiz() {
        // Validate input
        if (txtQuizTitle.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a quiz title.",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (questionsModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "Please add at least one question to the quiz.",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create or update quiz
        if (currentQuiz == null) {
            currentQuiz = new Quiz(generateQuizId(), lessonId, txtQuizTitle.getText());
        }

        // Update quiz properties
        currentQuiz.setTitle(txtQuizTitle.getText());
        currentQuiz.setDescription(txtQuizDescription.getText());
        currentQuiz.setPassingScore((Integer) spnPassingScore.getValue());
        currentQuiz.setMaxAttempts((Integer) spnMaxAttempts.getValue());

        // Validate quiz
        String validationResult = quizService.validateQuiz(currentQuiz);
        if (!validationResult.equals("VALID")) {
            JOptionPane.showMessageDialog(this,
                "Quiz validation failed:\n" + validationResult,
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Save to database
        boolean success;
        if (quizService.getQuizForLesson(courseId, lessonId) == null) {
            success = quizService.createQuizForLesson(courseId, lessonId, currentQuiz);
        } else {
            success = quizService.updateQuizForLesson(courseId, lessonId, currentQuiz);
        }

        if (success) {
            JOptionPane.showMessageDialog(this,
                "Quiz saved successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to save quiz. Please try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancel() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel? Any unsaved changes will be lost.",
            "Confirm Cancel", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private String generateQuizId() {
        return "QZ" + System.currentTimeMillis();
    }
}