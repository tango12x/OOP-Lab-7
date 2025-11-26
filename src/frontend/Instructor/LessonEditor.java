package frontend.Instructor;

import java.util.ArrayList;

import javax.swing.JDialog;

import backend.databaseManager.*;
import backend.models.*;

/**
 *
 * @author pc
 */
public class LessonEditor extends JDialog {
        String mode;
        String courseId;
        String lessonId;
        Lesson lesson;
        Course course;
        String quizId;
        Quiz quiz;
        ArrayList<Question> questions;
        CourseDatabaseManager Cdb;
        private JDialog parentView;
        private javax.swing.table.DefaultTableModel questionsModel;

        /**
         * Creates new form LessonEditor
         */
        public LessonEditor(String courseId, JDialog parent) {
                super(parent, "Dialog", true);
                this.parentView = parent; // Store reference to parent
                this.mode = "create";
                this.courseId = courseId;
                Cdb = new CourseDatabaseManager();
                course = Cdb.getCourse(courseId);
                initComponents();
                advancedIntialize();
                lesson = new Lesson(course.generateLessonId(), "", courseId, "");
                this.lessonId = lesson.getLessonId();
                quiz = new Quiz();
                questions = new ArrayList<Question>();
        }

        public LessonEditor(String courseId, String lessonId, JDialog parent) {
                super(parent, "Dialog", true);
                this.parentView = parent; // Store reference to parent
                this.mode = "edit";
                this.courseId = courseId;
                this.lessonId = lessonId;
                Cdb = new CourseDatabaseManager();
                course = Cdb.getCourse(courseId);
                for (int i = 0; i < course.getLessons().size(); i++) {
                        if (lessonId.equals(course.getLessons().get(i).getLessonId())) {
                                lesson = course.getLessons().get(i);
                                break;
                        }
                }
                initComponents();
                advancedIntialize();
        }

        // Loading data into fields
        private void advancedIntialize() {
                try {
                        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        this.setLocationRelativeTo(parentView);
                        String title;
                        String content;
                        if (mode.equalsIgnoreCase("create")) {
                                title = "";
                                content = "";
                                LblLessonInfo.setText("");
                                jLabelLessonMain.setText("Create Lesson");
                        } else {
                                title = lesson.getTitle();
                                content = lesson.getContent();
                                LblLessonInfo.setText("Lesson: " + title + " (ID: " + lessonId + ")");
                        }
                        titlefield.setText(title);
                        jTextAreaContent.setText(content);
                        jTextAreaOptionalResources.setText("");
                        initializeQuizTableAndLoadData();
                } catch (Exception e) {
                        e.getMessage();
                        e.printStackTrace();
                }
        }

        public void initializeQuizTableAndLoadData() {
                try {
                        // Set Quiz Info Label and Title
                        if (mode.equalsIgnoreCase("create")) {
                                LblQuizInfo.setText("");
                                jLabelQuizMain.setText("Create Quiz");
                        } else {
                                this.quiz = lesson.getQuiz();
                                // temporary fix for null quiz
                                LblQuizInfo.setText("");
                                if (quiz != null) {
                                        this.quizId = quiz.getQuizId();
                                        this.questions = quiz.getQuestions();
                                        LblQuizInfo.setText("Quiz: " + quiz.getTitle() + " (ID: " + quizId + ")");
                                }
                        }

                        // Set Question Table
                        if (this.questions == null) {
                                this.questions = new ArrayList<Question>();
                        }
                        questionsModel = new javax.swing.table.DefaultTableModel(
                                        new Object[][] {}, // Empty data initially
                                        new String[] { "Question ID", "Question Text", "Correct Answer", "Points" } // Column
                                                                                                                    // names
                        ) {
                                @Override
                                public boolean isCellEditable(int row, int column) {
                                        // Make all cells non-editable
                                        return false;
                                }
                        };
                        jTableQuestions.setModel(questionsModel);
                        for (int i = 0; i < questions.size(); i++) {
                                Question q = questions.get(i);
                                questionsModel.addRow(new Object[] {
                                                q.getQuestionId(), q.getQuestionText(),
                                                q.getCorrectOption(), Integer.toString(q.getPoints())
                                });
                        }
                        System.out.println("question table loaded , size: " + questions.size());
                } catch (Exception e) {
                        e.getMessage();
                        e.printStackTrace();
                }
        }

        public void refreshQuestionsTable() {
                questionsModel.setRowCount(0); // Clear existing rows
                for (int i = 0; i < quiz.getQuestions().size(); i++) {
                        System.out.println("Refreshing question " + i);
                        Question q = quiz.getQuestions().get(i);
                        questionsModel.addRow(new Object[] {
                                        q.getQuestionId(), q.getQuestionText(),
                                        q.getCorrectOption(), Integer.toString(q.getPoints())
                        });
                }
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

                jTabbedPane1 = new javax.swing.JTabbedPane();
                jPanel1 = new javax.swing.JPanel();
                jLabelLessonMain = new javax.swing.JLabel();
                LblLessonInfo = new javax.swing.JLabel();
                btnToQuiz = new javax.swing.JButton();
                titlefield = new javax.swing.JTextField();
                btnAddResource = new javax.swing.JButton();
                LblInstructorInfo1 = new javax.swing.JLabel();
                LblInstructorInfo2 = new javax.swing.JLabel();
                LblInstructorInfo3 = new javax.swing.JLabel();
                jScrollPane2 = new javax.swing.JScrollPane();
                jTextAreaContent = new javax.swing.JTextArea();
                jScrollPane1 = new javax.swing.JScrollPane();
                jTextAreaOptionalResources = new javax.swing.JTextArea();
                jPanel2 = new javax.swing.JPanel();
                jScrollPane3 = new javax.swing.JScrollPane();
                jTableQuestions = new javax.swing.JTable();
                jLabelQuizMain = new javax.swing.JLabel();
                LblQuizInfo = new javax.swing.JLabel();
                jButtonAddQuestion = new javax.swing.JButton();
                btnReturn1 = new javax.swing.JButton();
                btnSave1 = new javax.swing.JButton();
                jButtonEditQuestion1 = new javax.swing.JButton();
                jButtonDeleteQuestion2 = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                setResizable(false);

                jLabelLessonMain.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
                jLabelLessonMain.setText("Lesson Edit");
                jLabelLessonMain.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
                jLabelLessonMain.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

                LblLessonInfo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                LblLessonInfo.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                LblLessonInfo.setText("jLabel2");
                LblLessonInfo.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
                LblLessonInfo.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

                btnToQuiz.setBackground(new java.awt.Color(0, 204, 0));
                btnToQuiz.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                btnToQuiz.setForeground(new java.awt.Color(255, 255, 255));
                btnToQuiz.setText("To Quiz Section");
                btnToQuiz.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnToQuizActionPerformed(evt);
                        }
                });

                titlefield.setText("jTextField1");

                btnAddResource.setBackground(new java.awt.Color(0, 51, 255));
                btnAddResource.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
                btnAddResource.setForeground(new java.awt.Color(255, 255, 255));
                btnAddResource.setText("add Resource");
                btnAddResource.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnAddResourceActionPerformed(evt);
                        }
                });

                LblInstructorInfo1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                LblInstructorInfo1.setText("Title");
                LblInstructorInfo1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
                LblInstructorInfo1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

                LblInstructorInfo2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                LblInstructorInfo2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                LblInstructorInfo2.setText("Optional Resources");
                LblInstructorInfo2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
                LblInstructorInfo2.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

                LblInstructorInfo3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                LblInstructorInfo3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                LblInstructorInfo3.setText("Content");
                LblInstructorInfo3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
                LblInstructorInfo3.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

                jTextAreaContent.setColumns(20);
                jTextAreaContent.setRows(5);
                jScrollPane2.setViewportView(jTextAreaContent);

                jTextAreaOptionalResources.setColumns(20);
                jTextAreaOptionalResources.setRows(5);
                jScrollPane1.setViewportView(jTextAreaOptionalResources);

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                                                .addComponent(btnAddResource))
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                jPanel1Layout.createSequentialGroup()
                                                                                                                .addComponent(jLabelLessonMain)
                                                                                                                .addGap(11, 11, 11)
                                                                                                                .addComponent(LblLessonInfo,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE))
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(jPanel1Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addGap(6, 6, 6)
                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                .createParallelGroup(
                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                .addComponent(LblInstructorInfo3)
                                                                                                                                                .addComponent(LblInstructorInfo1,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                46,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                                                .addComponent(LblInstructorInfo2))
                                                                                                .addGap(19, 19, 19)
                                                                                                .addGroup(jPanel1Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(titlefield)
                                                                                                                .addComponent(jScrollPane1))))
                                                                .addContainerGap())
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(286, 286, 286)
                                                                .addComponent(btnToQuiz,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                171,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(288, Short.MAX_VALUE))
                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                jPanel1Layout.createSequentialGroup()
                                                                                                .addGap(185, 185, 185)
                                                                                                .addComponent(jScrollPane2,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                554,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addContainerGap())));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout
                                                                .createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabelLessonMain,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                98,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(LblLessonInfo,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel1Layout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                .addComponent(LblInstructorInfo1,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(titlefield,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                28,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addComponent(LblInstructorInfo3)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                92,
                                                                                Short.MAX_VALUE)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(LblInstructorInfo2,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                34,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jScrollPane1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                98,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 30, Short.MAX_VALUE)
                                                                .addComponent(btnAddResource)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnToQuiz)
                                                                .addGap(10, 10, 10))
                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                .addGap(167, 167, 167)
                                                                                .addComponent(jScrollPane2,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addContainerGap(232,
                                                                                                Short.MAX_VALUE))));

                jTabbedPane1.addTab("Lesson", jPanel1);

                jTableQuestions.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null }
                                },
                                new String[] {
                                                "Question ID", "Question Text", "Correct Answer", "Points"
                                }));
                jScrollPane3.setViewportView(jTableQuestions);

                jLabelQuizMain.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
                jLabelQuizMain.setText("Quiz Edit");
                jLabelQuizMain.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
                jLabelQuizMain.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

                LblQuizInfo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                LblQuizInfo.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                LblQuizInfo.setText("jLabel2");
                LblQuizInfo.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
                LblQuizInfo.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

                jButtonAddQuestion.setBackground(new java.awt.Color(51, 204, 0));
                jButtonAddQuestion.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
                jButtonAddQuestion.setForeground(new java.awt.Color(255, 255, 255));
                jButtonAddQuestion.setText("Add Question");
                jButtonAddQuestion.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButtonAddQuestionActionPerformed(evt);
                        }
                });

                btnReturn1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
                btnReturn1.setText("Return");
                btnReturn1.setToolTipText("");
                btnReturn1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                btnReturn1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
                btnReturn1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
                btnReturn1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                btnReturn1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnReturn1ActionPerformed(evt);
                        }
                });

                btnSave1.setBackground(new java.awt.Color(0, 204, 0));
                btnSave1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
                btnSave1.setForeground(new java.awt.Color(255, 255, 255));
                btnSave1.setText("Save All");
                btnSave1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnSave1ActionPerformed(evt);
                        }
                });

                jButtonEditQuestion1.setBackground(new java.awt.Color(255, 204, 0));
                jButtonEditQuestion1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
                jButtonEditQuestion1.setForeground(new java.awt.Color(255, 255, 255));
                jButtonEditQuestion1.setText("Edit Question");
                jButtonEditQuestion1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButtonEditQuestion1ActionPerformed(evt);
                        }
                });

                jButtonDeleteQuestion2.setBackground(new java.awt.Color(255, 0, 51));
                jButtonDeleteQuestion2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
                jButtonDeleteQuestion2.setForeground(new java.awt.Color(255, 255, 255));
                jButtonDeleteQuestion2.setText("Delete Question");
                jButtonDeleteQuestion2.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButtonDeleteQuestion2ActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGap(201, 201, 201)
                                                                .addComponent(jButtonAddQuestion)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jButtonEditQuestion1)
                                                                .addGap(12, 12, 12)
                                                                .addComponent(jButtonDeleteQuestion2)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE))
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel2Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jScrollPane3,
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                jPanel2Layout.createSequentialGroup()
                                                                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                                                                .addComponent(btnSave1)
                                                                                                                .addPreferredGap(
                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                .addComponent(btnReturn1))
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                jPanel2Layout
                                                                                                                .createSequentialGroup()
                                                                                                                .addComponent(jLabelQuizMain,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                183,
                                                                                                                                Short.MAX_VALUE)
                                                                                                                .addPreferredGap(
                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                .addComponent(LblQuizInfo,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                538,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap()));
                jPanel2Layout.setVerticalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel2Layout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                .addComponent(jLabelQuizMain,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                49,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(LblQuizInfo,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jScrollPane3,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                346, Short.MAX_VALUE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(jPanel2Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jButtonAddQuestion)
                                                                                .addComponent(jButtonEditQuestion1)
                                                                                .addComponent(jButtonDeleteQuestion2))
                                                                .addGap(14, 14, 14)
                                                                .addGroup(jPanel2Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(btnSave1)
                                                                                .addComponent(btnReturn1))
                                                                .addContainerGap()));

                jTabbedPane1.addTab("Quiz", jPanel2);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                                .createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(jTabbedPane1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                745,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, 0)));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGap(0, 0, 0)
                                                                .addComponent(jTabbedPane1)
                                                                .addContainerGap()));

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void btnToQuizActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnToQuizActionPerformed
                jTabbedPane1.setSelectedIndex(1);
        }// GEN-LAST:event_btnToQuizActionPerformed

        private void btnAddResourceActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddResourceActionPerformed
                String resource = jTextAreaOptionalResources.getText();
                // input is present
                if (resource.length() > 0 && titlefield.getText().length() > 0
                                && jTextAreaContent.getText().length() > 0) {
                        lesson.addResource(resource);
                        jTextAreaOptionalResources.setText("");
                } else if (resource.length() <= 0) {
                        javax.swing.JOptionPane.showMessageDialog(this,
                                        "cannot empty resources to lesson.",
                                        "No data", javax.swing.JOptionPane.WARNING_MESSAGE);
                        return;
                } else {
                        javax.swing.JOptionPane.showMessageDialog(this,
                                        "cannot add resources to empty lesson.",
                                        "No data", javax.swing.JOptionPane.WARNING_MESSAGE);
                        return;
                }
        }// GEN-LAST:event_btnAddResourceActionPerformed

        private void btnReturn1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnReturn1ActionPerformed
                int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                                "Return without saving changes?",
                                "Confirm return", javax.swing.JOptionPane.YES_NO_OPTION);

                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                        this.dispose();
                }
        }// GEN-LAST:event_btnReturn1ActionPerformed

        private void btnSave1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSave1ActionPerformed
                int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                                "save changes?",
                                "Confirm save", javax.swing.JOptionPane.YES_NO_OPTION);

                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                        if (titlefield.getText().isEmpty() || jTextAreaContent.getText().isEmpty()) {
                                javax.swing.JOptionPane.showMessageDialog(this,
                                                "cannot save empty lesson.",
                                                "No data", javax.swing.JOptionPane.WARNING_MESSAGE);
                                return;
                        }
                        if (mode.equalsIgnoreCase("create")) {
                                quiz.setQuizId("QZ" + lessonId);
                                quiz.setLessonId(lessonId);
                                quiz.setTitle(titlefield.getText());
                                lesson.setTitle(titlefield.getText());
                                lesson.setContent(jTextAreaContent.getText());
                                lesson.setQuiz(quiz);
                                course.addLesson(lesson);
                                Cdb.update(course);
                                Cdb.SaveCoursesToFile();
                                // Refresh parent table
                                // !NTST
                                if (parentView != null && parentView instanceof ViewLessons) {
                                        ViewLessons viewLessons = (ViewLessons) parentView;
                                        viewLessons.initializeTableAndLoadData();
                                }
                                this.dispose();
                        } else {
                                quiz.setQuizId("QZ" + lessonId);
                                quiz.setLessonId(lessonId);
                                quiz.setTitle(titlefield.getText());
                                lesson.setTitle(titlefield.getText());
                                lesson.setContent(jTextAreaContent.getText());
                                course.editLesson(lessonId, lesson);
                                Cdb.update(course);
                                Cdb.SaveCoursesToFile();
                                // Refresh parent table
                                // !NTST
                                if (parentView != null && parentView instanceof ViewLessons) {
                                        ViewLessons viewLessons = (ViewLessons) parentView;
                                        viewLessons.initializeTableAndLoadData();
                                }
                                this.dispose();
                        }
                }
        }// GEN-LAST:event_btnSave1ActionPerformed

        // !NTST
        private void jButtonAddQuestionActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonAddQuestionActionPerformed
                int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                                "Add Question ?",
                                "Confirm addition", javax.swing.JOptionPane.YES_NO_OPTION);

                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                        QuestionEditorDialog frame = new QuestionEditorDialog(this, quiz.getQuestions().size() + 1);
                        frame.setVisible(true);
                        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        frame.setLocationRelativeTo(null);
                        this.quiz.addQuestion(frame.getQuestion());
                        frame.dispose();
                }
                // wait for child to finish
                try {
                        refreshQuestionsTable();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }// GEN-LAST:event_jButtonAddQuestionActionPerformed

        private void jButtonEditQuestion1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonEditQuestion1ActionPerformed
                int selectedRow = jTableQuestions.getSelectedRow();
                if (selectedRow == -1) {
                        javax.swing.JOptionPane.showMessageDialog(this,
                                        "Please select a question to edit.",
                                        "No Selection", javax.swing.JOptionPane.WARNING_MESSAGE);
                        return;
                }
                String id = (String) jTableQuestions.getValueAt(selectedRow, 0);

                int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                                "Edit Question: " + jTableQuestions.getValueAt(selectedRow, 1) + "?",
                                "Confirm Edit", javax.swing.JOptionPane.YES_NO_OPTION);

                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                        QuestionEditorDialog frame = new QuestionEditorDialog(this, courseId, lessonId, id);
                        frame.setVisible(true);
                        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        frame.setLocationRelativeTo(null);
                        for (int i = 0; i < quiz.getQuestions().size(); i++) {
                                if (id.equals(quiz.getQuestions().get(i).getQuestionId())) {
                                        quiz.getQuestions().set(i, frame.getQuestion());
                                        break;
                                }
                        }
                        frame.dispose();
                }
                try {
                        refreshQuestionsTable();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }// GEN-LAST:event_jButtonEditQuestion1ActionPerformed

        private void jButtonDeleteQuestion2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonDeleteQuestion2ActionPerformed
                int selectedRow = jTableQuestions.getSelectedRow();
                if (selectedRow == -1) {
                        javax.swing.JOptionPane.showMessageDialog(this,
                                        "Please select a question to delete.",
                                        "No Selection", javax.swing.JOptionPane.WARNING_MESSAGE);
                        return;
                }
                String id = (String) jTableQuestions.getValueAt(selectedRow, 0);
                int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                                "Delete Question: " + jTableQuestions.getValueAt(selectedRow, 1) + "?",
                                "Confirm Deletion", javax.swing.JOptionPane.YES_NO_OPTION);

                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                        quiz.removeQuestion(id);
                        Cdb.update(course);
                        Cdb.SaveCoursesToFile();
                }
                try {
                        refreshQuestionsTable();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }// GEN-LAST:event_jButtonDeleteQuestion2ActionPerformed

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
                        java.util.logging.Logger.getLogger(LessonEditor.class.getName()).log(
                                        java.util.logging.Level.SEVERE, null,
                                        ex);
                } catch (InstantiationException ex) {
                        java.util.logging.Logger.getLogger(LessonEditor.class.getName()).log(
                                        java.util.logging.Level.SEVERE, null,
                                        ex);
                } catch (IllegalAccessException ex) {
                        java.util.logging.Logger.getLogger(LessonEditor.class.getName()).log(
                                        java.util.logging.Level.SEVERE, null,
                                        ex);
                } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                        java.util.logging.Logger.getLogger(LessonEditor.class.getName()).log(
                                        java.util.logging.Level.SEVERE, null,
                                        ex);
                }
                // </editor-fold>

                /* Create and display the form */
                java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                                // new LessonEditor().setVisible(true);
                        }
                });
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JLabel LblInstructorInfo1;
        private javax.swing.JLabel LblInstructorInfo2;
        private javax.swing.JLabel LblInstructorInfo3;
        private javax.swing.JLabel LblLessonInfo;
        private javax.swing.JLabel LblQuizInfo;
        private javax.swing.JButton btnAddResource;
        private javax.swing.JButton btnReturn1;
        private javax.swing.JButton btnSave1;
        private javax.swing.JButton btnToQuiz;
        private javax.swing.JButton jButtonAddQuestion;
        private javax.swing.JButton jButtonDeleteQuestion2;
        private javax.swing.JButton jButtonEditQuestion1;
        private javax.swing.JLabel jLabelLessonMain;
        private javax.swing.JLabel jLabelQuizMain;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JTabbedPane jTabbedPane1;
        private javax.swing.JTable jTableQuestions;
        private javax.swing.JTextArea jTextAreaContent;
        private javax.swing.JTextArea jTextAreaOptionalResources;
        private javax.swing.JTextField titlefield;
        // End of variables declaration//GEN-END:variables
}
