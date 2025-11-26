package frontend.Instructor;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.util.ArrayList;

import backend.databaseManager.*;
import backend.models.*;

/**
 *
 * @author pc
 */
public class QuestionEditorDialog extends javax.swing.JDialog {
        String mode;
        String courseId;
        String lessonId;
        Lesson lesson;
        Course course;
        String quizId;
        Quiz quiz;
        Question question;
        String questionId;
        CourseDatabaseManager Cdb;
        ArrayList<Question> questions;
        private LessonEditor parentView;

        /**
         * Creates new form QuestionEditorDialog
         */
        public QuestionEditorDialog(LessonEditor parent, int num) {
                super(parent, "Dialog", true);
                mode = "create";
                this.questionId = "Question-" + Integer.toString(num);
                this.question = new Question();
                this.parentView = parent;
                initComponents();
                advancedIntialize();
        }

        public QuestionEditorDialog(LessonEditor parent, String CourseId, String LessonId) {
                super(parent, "Dialog", true);
                mode = "create";
                this.Cdb = new CourseDatabaseManager();
                this.courseId = CourseId;
                this.lessonId = LessonId;
                this.course = Cdb.getCourse(courseId);
                for (int i = 0; i < course.getLessons().size(); i++) {
                        if (course.getLessons().get(i).getLessonId().equals(lessonId)) {
                                this.lesson = course.getLessons().get(i);
                                break;
                        }
                }
                this.quiz = lesson.getQuiz();
                this.quizId = quiz.getQuizId();
                this.questions = quiz.getQuestions();
                this.questionId = quiz.generateQuestionId();
                this.question = new Question();
                this.parentView = parent;

                // Debug: Check if parent is properly set
                System.out.println("Dialog created with parent: " + parent);
                System.out.println("Parent Quiz: " + quizId);

                initComponents();
                advancedIntialize();
        }

        public QuestionEditorDialog(LessonEditor parent, String CourseId, String LessonId, String questionId) {
                super(parent, "Dialog", true);
                mode = "edit";
                this.Cdb = new CourseDatabaseManager();
                this.courseId = CourseId;
                this.lessonId = LessonId;
                this.course = Cdb.getCourse(courseId);
                for (int i = 0; i < course.getLessons().size(); i++) {
                        if (course.getLessons().get(i).getLessonId().equals(lessonId)) {
                                this.lesson = course.getLessons().get(i);
                                break;
                        }
                }
                this.quiz = lesson.getQuiz();
                this.quizId = quiz.getQuizId();
                this.questions = quiz.getQuestions();
                this.questionId = questionId;
                this.question = quiz.getQuestion(questionId);
                this.parentView = parent;

                initComponents();
                advancedIntialize();
        }

        private void advancedIntialize() {
                try {
                        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        this.setLocationRelativeTo(parentView);
                        String questionText;
                        String explanation;
                        if (mode.equalsIgnoreCase("create")) {
                                questionText = "";
                                explanation = "";
                                LblQuestionInfo.setText("");
                                LblMain.setText("Create Question");
                                jComboBoxCorrectOption.removeAllItems();
                                jSpinnerPoints.setValue(0);

                        } else {
                                questionText = question.getQuestionText();
                                explanation = question.getExplanation();
                                LblQuestionInfo.setText("Question (ID: " + questionId + ")");
                                jComboBoxCorrectOption.removeAllItems();
                                for (int i = 0; i < question.getOptions().size(); i++) {
                                        jComboBoxCorrectOption.addItem(question.getOptions().get(i));
                                }
                                jComboBoxCorrectOption.setSelectedItem(question.getCorrectOption());
                                jSpinnerPoints.setValue(question.getPoints());
                        }
                        addOptionsField.setText("");
                        jTextAreaQuestionText.setText(questionText);
                        jTextAreaExplanation.setText(explanation);
                } catch (Exception e) {
                        e.getMessage();
                        e.printStackTrace();
                }
        }

        public Question getQuestion() {
                return this.question;
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jPanel1 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                jComboBoxCorrectOption = new javax.swing.JComboBox<>();
                jSpinnerPoints = new javax.swing.JSpinner();
                jScrollPane2 = new javax.swing.JScrollPane();
                jTextAreaExplanation = new javax.swing.JTextArea();
                jLabel7 = new javax.swing.JLabel();
                jLabel8 = new javax.swing.JLabel();
                jLabel9 = new javax.swing.JLabel();
                jLabel10 = new javax.swing.JLabel();
                LblMain = new javax.swing.JLabel();
                LblQuestionInfo = new javax.swing.JLabel();
                addOptionsField = new javax.swing.JTextField();
                btnAddResource = new javax.swing.JButton();
                jScrollPane3 = new javax.swing.JScrollPane();
                jTextAreaQuestionText = new javax.swing.JTextArea();
                btnSave = new javax.swing.JButton();
                btnReturn = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                setResizable(false);

                jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
                jLabel1.setText("Question Text");

                jComboBoxCorrectOption.setModel(
                                new javax.swing.DefaultComboBoxModel<>(
                                                new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

                jSpinnerPoints.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 1));

                jTextAreaExplanation.setColumns(20);
                jTextAreaExplanation.setRows(5);
                jScrollPane2.setViewportView(jTextAreaExplanation);

                jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
                jLabel7.setText("Options");

                jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
                jLabel8.setText("Correct Option");

                jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
                jLabel9.setText("Points");

                jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
                jLabel10.setText("Explanation");

                LblMain.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
                LblMain.setText("Question Edit");
                LblMain.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
                LblMain.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

                LblQuestionInfo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                LblQuestionInfo.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                LblQuestionInfo.setText("jLabel2");
                LblQuestionInfo.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
                LblQuestionInfo.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

                addOptionsField.setText("jTextField1");

                btnAddResource.setBackground(new java.awt.Color(0, 51, 255));
                btnAddResource.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
                btnAddResource.setForeground(new java.awt.Color(255, 255, 255));
                btnAddResource.setText("add Option");
                btnAddResource.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnAddResourceActionPerformed(evt);
                        }
                });

                jTextAreaQuestionText.setColumns(20);
                jTextAreaQuestionText.setRows(5);
                jScrollPane3.setViewportView(jTextAreaQuestionText);

                btnSave.setBackground(new java.awt.Color(0, 204, 0));
                btnSave.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
                btnSave.setForeground(new java.awt.Color(255, 255, 255));
                btnSave.setText("Save");
                btnSave.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnSaveActionPerformed(evt);
                        }
                });

                btnReturn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
                btnReturn.setText("Return");
                btnReturn.setToolTipText("");
                btnReturn.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                btnReturn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
                btnReturn.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
                btnReturn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                btnReturn.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnReturnActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addContainerGap()
                                                                                                .addComponent(LblMain,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(LblQuestionInfo,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                380,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(14, 14, 14)
                                                                                                .addGroup(jPanel1Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(jLabel1)
                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addGap(6, 6, 6)
                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                .createParallelGroup(
                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                                .createSequentialGroup()
                                                                                                                                                                .addComponent(jLabel10)
                                                                                                                                                                .addPreferredGap(
                                                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                                                                                39,
                                                                                                                                                                                Short.MAX_VALUE)
                                                                                                                                                                .addComponent(jScrollPane2,
                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                395,
                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                                                .addComponent(jLabel9))))
                                                                                                .addGap(27, 27, 27)))
                                                                .addContainerGap())
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(16, 16, 16)
                                                                                                .addGroup(jPanel1Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(jLabel8)
                                                                                                                .addComponent(jLabel7))
                                                                                                .addGap(18, 18, 18)
                                                                                                .addGroup(jPanel1Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(jSpinnerPoints,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                .createParallelGroup(
                                                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                                                                false)
                                                                                                                                                .addComponent(jComboBoxCorrectOption,
                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                                                0,
                                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                Short.MAX_VALUE)
                                                                                                                                                .addComponent(addOptionsField,
                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                311,
                                                                                                                                                                Short.MAX_VALUE))
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                                .addComponent(btnAddResource))))
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(201, 201, 201)
                                                                                                .addComponent(btnSave)
                                                                                                .addGap(44, 44, 44)
                                                                                                .addComponent(btnReturn)))
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                jPanel1Layout.createSequentialGroup()
                                                                                                .addContainerGap(158,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(jScrollPane3,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                388,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(43, 43, 43))));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                jPanel1Layout.createSequentialGroup()
                                                                                                                .addContainerGap()
                                                                                                                .addComponent(LblQuestionInfo,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                71,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(LblMain,
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                77,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(24, 24, 24)
                                                                .addComponent(jLabel1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                56,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(15, 15, 15)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel7,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                56,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(addOptionsField,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                28,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btnAddResource))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel8,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                56,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jComboBoxCorrectOption,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel9,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                56,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jSpinnerPoints,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabel10,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                56,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addContainerGap(
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE))
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jScrollPane2,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                30,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addGroup(jPanel1Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(btnSave)
                                                                                                                .addComponent(btnReturn))
                                                                                                .addGap(32, 32, 32))))
                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                .addGap(111, 111, 111)
                                                                                .addComponent(jScrollPane3,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                62,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addContainerGap(368,
                                                                                                Short.MAX_VALUE))));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE));

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void btnAddResourceActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddResourceActionPerformed
                String option = addOptionsField.getText();
                // input is present
                if (option.length() > 0 && jTextAreaQuestionText.getText().length() > 0) {
                        question.addOption(option);
                        addOptionsField.setText("");
                        jComboBoxCorrectOption.addItem(option);
                } else if (option.length() <= 0) {
                        javax.swing.JOptionPane.showMessageDialog(this,
                                        "cannot add empty options to Question.",
                                        "No data", javax.swing.JOptionPane.WARNING_MESSAGE);
                        return;
                } else {
                        javax.swing.JOptionPane.showMessageDialog(this,
                                        "cannot add options to empty Question.",
                                        "No data", javax.swing.JOptionPane.WARNING_MESSAGE);
                        return;
                }
        }// GEN-LAST:event_btnAddResourceActionPerformed

        // private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//
        // GEN-FIRST:event_btnSaveActionPerformed
        // int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
        // "save changes?",
        // "Confirm save", javax.swing.JOptionPane.YES_NO_OPTION);

        // if (confirm == javax.swing.JOptionPane.YES_OPTION) {
        // if (jTextAreaExplanation.getText().isEmpty() ||
        // jTextAreaQuestionText.getText().isEmpty()) {
        // javax.swing.JOptionPane.showMessageDialog(this,
        // "cannot save empty questions.",
        // "No data", javax.swing.JOptionPane.WARNING_MESSAGE);
        // return;
        // }
        // if ((int) jSpinnerPoints.getValue() == 0) {
        // javax.swing.JOptionPane.showMessageDialog(this,
        // "cannot add Questions with 0 points",
        // "No data", javax.swing.JOptionPane.WARNING_MESSAGE);
        // return;
        // }
        // if (question.getOptions().isEmpty()) {
        // javax.swing.JOptionPane.showMessageDialog(this,
        // "cannot add Questions without options",
        // "No data", javax.swing.JOptionPane.WARNING_MESSAGE);
        // return;
        // }
        // if (mode.equalsIgnoreCase("create")) {
        // questionId = quiz.generateQuestionId();
        // question.setQuestionId(questionId);
        // question.setQuestionText(jTextAreaQuestionText.getText());
        // question.setExplanation(jTextAreaExplanation.getText());
        // question.setCorrectOption((String) jComboBoxCorrectOption.getSelectedItem());
        // question.setPoints((int) jSpinnerPoints.getValue());
        // parentView.quiz.addQuestion(question);
        // System.out.println("added Question ID: " + questionId + " in Quiz ID: "
        // + parentView.quizId);
        // this.setVisible(false);
        // } else {
        // question.setQuestionText(jTextAreaQuestionText.getText());
        // question.setExplanation(jTextAreaExplanation.getText());
        // question.setCorrectOption((String) jComboBoxCorrectOption.getSelectedItem());
        // question.setPoints((int) jSpinnerPoints.getValue());
        // for (int i = 0; i < parentView.quiz.getQuestions().size(); i++) {
        // if (questionId.equals(parentView.quiz.getQuestions().get(i).getQuestionId()))
        // {
        // parentView.quiz.getQuestions().set(i, question);
        // break;
        // }
        // }
        // System.out.println("Updated Question ID: " + questionId + " in Quiz ID: "
        // + parentView.quizId);
        // this.dispose();
        // }
        // }
        // }// GEN-LAST:event_btnSaveActionPerformed

        private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSaveActionPerformed
                int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                                "save changes?",
                                "Confirm save", javax.swing.JOptionPane.YES_NO_OPTION);

                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                        if (jTextAreaExplanation.getText().isEmpty() || jTextAreaQuestionText.getText().isEmpty()) {
                                javax.swing.JOptionPane.showMessageDialog(this,
                                                "cannot save empty questions.",
                                                "No data", javax.swing.JOptionPane.WARNING_MESSAGE);
                                return;
                        }
                        if ((int) jSpinnerPoints.getValue() == 0) {
                                javax.swing.JOptionPane.showMessageDialog(this,
                                                "cannot add Questions with 0 points",
                                                "No data", javax.swing.JOptionPane.WARNING_MESSAGE);
                                return;
                        }
                        if (question.getOptions().isEmpty()) {
                                javax.swing.JOptionPane.showMessageDialog(this,
                                                "cannot add Questions without options",
                                                "No data", javax.swing.JOptionPane.WARNING_MESSAGE);
                                return;
                        }

                        // Update the question object with current form data
                        question.setQuestionText(jTextAreaQuestionText.getText());
                        question.setExplanation(jTextAreaExplanation.getText());
                        question.setCorrectOption((String) jComboBoxCorrectOption.getSelectedItem());
                        question.setPoints((int) jSpinnerPoints.getValue());

                        if (mode.equalsIgnoreCase("create")) {
                                question.setQuestionId(questionId);

                                // Debug output
                                System.out.println("Created question with ID: " + questionId);
                                System.out.println("Question text: " + question.getQuestionText());
                                System.out.println("Options: " + question.getOptions());
                                System.out.println("Correct option: " + question.getCorrectOption());
                                System.out.println("Points: " + question.getPoints());

                                this.dispose(); // Close dialog
                        } else {
                                // for (int i = 0; i < parentView.quiz.getQuestions().size(); i++) {
                                // if (questionId.equals(parentView.quiz.getQuestions().get(i).getQuestionId()))
                                // {
                                // parentView.quiz.getQuestions().set(i, question);
                                // break;
                                // }
                                // }
                                System.out.println("Updated Question ID: " + questionId + " in Quiz ID: "
                                                + parentView.quizId);
                                this.dispose();
                        }
                }
        }// GEN-LAST:event_btnSaveActionPerformed

        private void btnReturnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnReturnActionPerformed
                int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                                "Return without saving changes?",
                                "Confirm return", javax.swing.JOptionPane.YES_NO_OPTION);

                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                        this.dispose();
                }
        }// GEN-LAST:event_btnReturnActionPerformed

        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
                /* Set the Nimbus look and feel */
                // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
                // (optional) ">
                /*
                 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
                 * look and feel.
                 * For details see
                 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
                 */
                try {
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                                        .getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                        break;
                                }
                        }
                } catch (ClassNotFoundException ex) {
                        java.util.logging.Logger.getLogger(QuestionEditorDialog.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                } catch (InstantiationException ex) {
                        java.util.logging.Logger.getLogger(QuestionEditorDialog.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                } catch (IllegalAccessException ex) {
                        java.util.logging.Logger.getLogger(QuestionEditorDialog.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                        java.util.logging.Logger.getLogger(QuestionEditorDialog.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                }
                // </editor-fold>

                /* Create and display the dialog */
                java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                                // QuestionEditorDialog dialog = new QuestionEditorDialog(new
                                // javax.swing.JDialog());
                                // dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                                // @Override
                                // public void windowClosing(java.awt.event.WindowEvent e) {
                                // System.exit(0);
                                // }
                                // });
                                // dialog.setVisible(true);
                        }
                });
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JLabel LblMain;
        private javax.swing.JLabel LblQuestionInfo;
        private javax.swing.JTextField addOptionsField;
        private javax.swing.JButton btnAddResource;
        private javax.swing.JButton btnReturn;
        private javax.swing.JButton btnSave;
        private javax.swing.JComboBox<String> jComboBoxCorrectOption;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel10;
        private javax.swing.JLabel jLabel7;
        private javax.swing.JLabel jLabel8;
        private javax.swing.JLabel jLabel9;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JSpinner jSpinnerPoints;
        private javax.swing.JTextArea jTextAreaExplanation;
        private javax.swing.JTextArea jTextAreaQuestionText;
        // End of variables declaration//GEN-END:variables
}
