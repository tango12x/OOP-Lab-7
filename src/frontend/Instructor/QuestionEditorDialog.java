package frontend.Instructor;

import backend.models.Question;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class QuestionEditorDialog extends JDialog {
    private Question question;
    private boolean saved = false;
    
    private JTextField txtQuestionId;
    private JTextArea txtQuestionText;
    private JTextField txtOption1, txtOption2, txtOption3, txtOption4;
    private JComboBox<String> cmbCorrectOption;
    private JSpinner spnPoints;
    private JTextArea txtExplanation;
    private JButton btnSave;
    private JButton btnCancel;

    public QuestionEditorDialog(JDialog parent, Question existingQuestion) {
        super(parent, "Question Editor", true);
        this.question = existingQuestion;
        initializeUI();
    }

    private void initializeUI() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(mainPanel);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Question ID
        JLabel lblQuestionId = new JLabel("Question ID:*");
        lblQuestionId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtQuestionId = new JTextField();
        
        // Question Text
        JLabel lblQuestionText = new JLabel("Question Text:*");
        lblQuestionText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtQuestionText = new JTextArea(3, 20);
        txtQuestionText.setLineWrap(true);
        txtQuestionText.setWrapStyleWord(true);
        JScrollPane questionScrollPane = new JScrollPane(txtQuestionText);

        // Options
        JLabel lblOptions = new JLabel("Options:*");
        lblOptions.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        txtOption1 = new JTextField();
        txtOption2 = new JTextField();
        txtOption3 = new JTextField();
        txtOption4 = new JTextField();
        optionsPanel.add(txtOption1);
        optionsPanel.add(txtOption2);
        optionsPanel.add(txtOption3);
        optionsPanel.add(txtOption4);

        // Correct Option
        JLabel lblCorrectOption = new JLabel("Correct Option:*");
        lblCorrectOption.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cmbCorrectOption = new JComboBox<>(new String[]{"", "Option 1", "Option 2", "Option 3", "Option 4"});

        // Points
        JLabel lblPoints = new JLabel("Points:*");
        lblPoints.setFont(new Font("Segoe UI", Font.BOLD, 12));
        spnPoints = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        // Explanation
        JLabel lblExplanation = new JLabel("Explanation:");
        lblExplanation.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtExplanation = new JTextArea(3, 20);
        txtExplanation.setLineWrap(true);
        txtExplanation.setWrapStyleWord(true);
        JScrollPane explanationScrollPane = new JScrollPane(txtExplanation);

        // Add components to form
        formPanel.add(lblQuestionId);
        formPanel.add(txtQuestionId);
        formPanel.add(lblQuestionText);
        formPanel.add(questionScrollPane);
        formPanel.add(lblOptions);
        formPanel.add(optionsPanel);
        formPanel.add(lblCorrectOption);
        formPanel.add(cmbCorrectOption);
        formPanel.add(lblPoints);
        formPanel.add(spnPoints);
        formPanel.add(lblExplanation);
        formPanel.add(explanationScrollPane);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        
        btnSave = new JButton("Save Question");
        btnSave.setBackground(new Color(0, 153, 0));
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> saveQuestion());
        
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> cancel());
        
        buttonsPanel.add(btnSave);
        buttonsPanel.add(btnCancel);
        
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Load existing question data if editing
        if (question != null) {
            loadQuestionData();
        } else {
            // Generate new question ID for new question
            txtQuestionId.setText("Q" + System.currentTimeMillis());
        }
    }

    private void loadQuestionData() {
        txtQuestionId.setText(question.getQuestionId());
        txtQuestionText.setText(question.getQuestionText());
        
        // Load options
        ArrayList<String> options = question.getOptions();
        if (options.size() > 0) txtOption1.setText(options.get(0));
        if (options.size() > 1) txtOption2.setText(options.get(1));
        if (options.size() > 2) txtOption3.setText(options.get(2));
        if (options.size() > 3) txtOption4.setText(options.get(3));
        
        // Set correct option
        String correctOption = question.getCorrectOption();
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).equals(correctOption)) {
                cmbCorrectOption.setSelectedIndex(i + 1);
                break;
            }
        }
        
        spnPoints.setValue(question.getPoints());
        txtExplanation.setText(question.getExplanation());
    }

    private void saveQuestion() {
        // Validate input
        if (txtQuestionId.getText().trim().isEmpty()) {
            showError("Please enter a question ID.");
            return;
        }

        if (txtQuestionText.getText().trim().isEmpty()) {
            showError("Please enter question text.");
            return;
        }

        // Validate options
        ArrayList<String> options = new ArrayList<>();
        if (!txtOption1.getText().trim().isEmpty()) options.add(txtOption1.getText().trim());
        if (!txtOption2.getText().trim().isEmpty()) options.add(txtOption2.getText().trim());
        if (!txtOption3.getText().trim().isEmpty()) options.add(txtOption3.getText().trim());
        if (!txtOption4.getText().trim().isEmpty()) options.add(txtOption4.getText().trim());

        if (options.size() < 2) {
            showError("Please provide at least 2 options.");
            return;
        }

        if (cmbCorrectOption.getSelectedIndex() == 0) {
            showError("Please select the correct option.");
            return;
        }

        // Get correct option
        String correctOption = options.get(cmbCorrectOption.getSelectedIndex() - 1);

        // Create or update question
        if (question == null) {
            question = new Question(
                txtQuestionId.getText().trim(),
                txtQuestionText.getText().trim(),
                options,
                correctOption,
                txtExplanation.getText().trim(),
                (Integer) spnPoints.getValue()
            );
        } else {
            question.setQuestionText(txtQuestionText.getText().trim());
            question.setOptions(options);
            question.setCorrectOption(correctOption);
            question.setExplanation(txtExplanation.getText().trim());
            question.setPoints((Integer) spnPoints.getValue());
        }

        // Validate question
        if (!question.isValid()) {
            showError("Question is not valid. Please check all fields.");
            return;
        }

        saved = true;
        dispose();
    }

    private void cancel() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel? Any unsaved changes will be lost.",
            "Confirm Cancel", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    public Question getQuestion() {
        return saved ? question : null;
    }
}
