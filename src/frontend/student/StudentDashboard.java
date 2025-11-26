package frontend.student;

import java.security.cert.Certificate;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.UIManager;

import backend.databaseManager.CourseDatabaseManager;
import backend.databaseManager.UsersDatabaseManager;
import backend.models.Course;
import backend.models.Instructor;
import backend.models.Lesson;
import backend.models.Student;
import backend.services.StudentQuizService;
import backend.services.StudentService;
import frontend.Login;
import frontend.util.*;

//! possible error sources are labelled (PES) for easy search
//!for need testing NTST
// TODO <-- search for it to find todo

/**
 * Student Dashboard - Skill Forge System
 * Enhanced version with complete functionality for:
 * - Viewing available and enrolled courses
 * - Enrolling in courses
 * - Accessing and managing lessons
 * - Tracking learning progress
 * 
 * @author pc
 */
public class StudentDashboard extends javax.swing.JFrame {

        // Current student information
        private String currentStudentId;
        private String currentStudentName;
        private CourseDatabaseManager Cdb;
        private UsersDatabaseManager Udb;

        // Table models for dynamic data
        private javax.swing.table.DefaultTableModel availableCoursesModel;
        private javax.swing.table.DefaultTableModel enrolledCoursesModel;
        private javax.swing.table.DefaultTableModel lessonsModel;

        StudentService SS;
        String courseIdForCurrentLesson = null;

        /**
         * Creates new form StudentDashboard
         * Enhanced constructor to accept student information
         */
        public StudentDashboard(String studentId, String studentName) {
                this.currentStudentId = studentId;
                this.currentStudentName = studentName;
                SS = new StudentService(currentStudentId);
                Cdb = new CourseDatabaseManager();
                Udb = new UsersDatabaseManager();
                initComponents();
                initializeEnhancedFeatures();
        }

        /**
         * ========================================================================
         * ENHANCED INITIALIZATION METHOD
         * Initializes additional features beyond basic GUI setup
         * ========================================================================
         */
        private void initializeEnhancedFeatures() {
                // Set student information in the label
                LblStudentInfo.setText("Welcome, " + currentStudentName + " (ID: " + currentStudentId + ")");

                // Initialize table models with proper column definitions
                initializeTableModels();

                // Load initial student data
                loadStudentData();
        }

        /**
         * ========================================================================
         * INITIALIZE CUSTOM TABLE MODELS
         * Creates proper table models with correct column structure and editability
         * ========================================================================
         */
        private void initializeTableModels() {
                // Available courses table model
                availableCoursesModel = new javax.swing.table.DefaultTableModel(
                                new Object[][] {}, // Empty data initially
                                new String[] { "Course ID", "Title", "Description", "Instructor" } // Column names
                ) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                // Make all cells non-editable
                                return false;
                        }
                };
                jTable1.setModel(availableCoursesModel);

                // Enrolled courses table model
                enrolledCoursesModel = new javax.swing.table.DefaultTableModel(
                                new Object[][] {},
                                new String[] { "Course ID", "Title", "Progress", "Next Lesson" }) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false;
                        }
                };
                tblMyCourses.setModel(enrolledCoursesModel);
                tblMyCourses.getColumnModel().getColumn(2).setCellRenderer(new ProgressBarRenderer());
                // Lessons table model with status indicator
                lessonsModel = new javax.swing.table.DefaultTableModel(
                                new Object[][] {},
                                new String[] { "Status", "Lesson ID", "Title", "Content" }) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false;
                        }
                };
                tblLessons.setModel(lessonsModel);
        }

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        // [EXISTING INITCOMPONENTS CODE REMAINS EXACTLY THE SAME]
        private void initComponents() {

                jPanel1 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                LblStudentInfo = new javax.swing.JLabel();
                btnLogout = new javax.swing.JButton();
                jTabbedPane2 = new javax.swing.JTabbedPane();
                jPanel2 = new javax.swing.JPanel();
                jScrollPane1 = new javax.swing.JScrollPane();
                jTable1 = new javax.swing.JTable();
                btnEnroll = new javax.swing.JButton();
                jPanel3 = new javax.swing.JPanel();
                jScrollPane2 = new javax.swing.JScrollPane();
                tblMyCourses = new javax.swing.JTable();
                jScrollPane3 = new javax.swing.JScrollPane();
                tblLessons = new javax.swing.JTable();
                btnViewLessons = new javax.swing.JButton();
                btnTakeLesson = new javax.swing.JButton();
                jLabel2 = new javax.swing.JLabel();
                btnRefresh = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
                jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel1.setText("Student Dashboard");
                jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
                jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

                LblStudentInfo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                LblStudentInfo.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                LblStudentInfo.setText("jLabel2");
                LblStudentInfo.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
                LblStudentInfo.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

                btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
                btnLogout.setText("Logout");
                btnLogout.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                btnLogout.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
                btnLogout.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
                btnLogout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                btnLogout.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnLogoutActionPerformed(evt);
                        }
                });

                jTable1.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null }
                                },
                                new String[] {
                                                "Course ID", "Title", "Description", "Instructor"
                                }) {
                        boolean[] canEdit = new boolean[] {
                                        false, false, false, false
                        };

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return canEdit[columnIndex];
                        }
                });
                jScrollPane1.setViewportView(jTable1);

                btnEnroll.setBackground(new java.awt.Color(0, 102, 204));
                btnEnroll.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
                btnEnroll.setForeground(new java.awt.Color(255, 255, 255));
                btnEnroll.setText("Enroll in Course");
                btnEnroll.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
                btnEnroll.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
                btnEnroll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                btnEnroll.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnEnrollActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 812,
                                                                Short.MAX_VALUE)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(btnEnroll,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                153,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE)));
                jPanel2Layout.setVerticalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(jScrollPane1,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                392, Short.MAX_VALUE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(btnEnroll)
                                                                .addGap(12, 12, 12)));

                jTabbedPane2.addTab("Available Courses", jPanel2);

                tblMyCourses.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null }
                                },
                                new String[] {
                                                "Course ID", "Title", "Progress", "next Lesson"
                                }));
                jScrollPane2.setViewportView(tblMyCourses);

                tblLessons.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null }
                                },
                                new String[] {
                                                "Status", "Lesson ID", "Title", "Content"
                                }));
                jScrollPane3.setViewportView(tblLessons);

                btnViewLessons.setBackground(new java.awt.Color(0, 51, 204));
                btnViewLessons.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
                btnViewLessons.setForeground(new java.awt.Color(255, 255, 255));
                btnViewLessons.setText("View Lessons");
                btnViewLessons.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnViewLessonsActionPerformed(evt);
                        }
                });

                btnTakeLesson.setBackground(new java.awt.Color(0, 204, 0));
                btnTakeLesson.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
                btnTakeLesson.setForeground(new java.awt.Color(255, 255, 255));
                btnTakeLesson.setText("Take Lesson");
                btnTakeLesson.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnTakeLessonActionPerformed(evt);
                        }
                });

                jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
                jLabel2.setText("Lessons");
                jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
                jLabel2.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(jScrollPane2,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                800,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jScrollPane3,
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                jPanel3Layout
                                                                                                                .createSequentialGroup()
                                                                                                                .addGroup(jPanel3Layout
                                                                                                                                .createParallelGroup(
                                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                                jPanel3Layout.createSequentialGroup()
                                                                                                                                                                .addComponent(btnViewLessons)
                                                                                                                                                                .addPreferredGap(
                                                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                                                                .addComponent(btnTakeLesson))
                                                                                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                                jPanel3Layout.createSequentialGroup()
                                                                                                                                                                .addGap(6, 6, 6)
                                                                                                                                                                .addComponent(jLabel2)))
                                                                                                                .addGap(0, 0, Short.MAX_VALUE)))
                                                                .addContainerGap()));
                jPanel3Layout.setVerticalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addComponent(jScrollPane2,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                197, Short.MAX_VALUE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel2,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                26,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jScrollPane3,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                169,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(btnViewLessons)
                                                                                .addComponent(btnTakeLesson))
                                                                .addContainerGap()));

                jTabbedPane2.addTab("My Courses", jPanel3);

                btnRefresh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
                btnRefresh.setText("Refresh Data");
                btnRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
                btnRefresh.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
                btnRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                btnRefresh.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnRefreshActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jTabbedPane2)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(btnRefresh,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                131,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(btnLogout))
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabel1,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                232,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(LblStudentInfo,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                484,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap()));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel1Layout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                .addComponent(jLabel1,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                39,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(LblStudentInfo,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jTabbedPane2)
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(btnLogout)
                                                                                .addComponent(btnRefresh))
                                                                .addContainerGap()));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE));

                pack();
        }// </editor-fold>//GEN-END:initComponents

        // ========================================================================
        // ENHANCED EVENT HANDLERS WITH API INTEGRATION POINTS
        // ========================================================================

        /**
         * ========================================================================
         * REFRESH BUTTON ACTION
         * Reloads all student data from the backend
         * ========================================================================
         */
        private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnRefreshActionPerformed
                // Clear existing data from tables
                availableCoursesModel.setRowCount(0);
                enrolledCoursesModel.setRowCount(0);
                lessonsModel.setRowCount(0);
                courseIdForCurrentLesson = null;
                SS.refresh();
                loadStudentData();
                javax.swing.JOptionPane.showMessageDialog(this,
                                "Data refreshed successfully!",
                                "Refresh Complete", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }// GEN-LAST:event_btnRefreshActionPerformed

        /**
         * ========================================================================
         * LOGOUT BUTTON ACTION
         * Logs out the student and returns to login screen
         * API INTEGRATION POINT: AuthService.logout(studentId)
         * ========================================================================
         */
        private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLogoutActionPerformed
                int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                                "Are you sure you want to logout?",
                                "Confirm Logout", javax.swing.JOptionPane.YES_NO_OPTION);

                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                        // Suggested function: AuthService.logout(currentStudentId)
                        Login frame = new Login();
                        frame.setVisible(true);
                        frame.setLocationRelativeTo(null);

                        // Close the dashboard
                        this.dispose();

                        // Show success message
                        javax.swing.JOptionPane.showMessageDialog(this,
                                        "Logged out successfully!",
                                        "Logout", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                }
        }// GEN-LAST:event_btnLogoutActionPerformed

        /**
         * ========================================================================
         * ENROLL BUTTON ACTION
         * Enrolls student in the selected available course
         * ========================================================================
         */
        private void btnEnrollActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnEnrollActionPerformed
                int selectedRow = jTable1.getSelectedRow();
                if (selectedRow == -1) {
                        javax.swing.JOptionPane.showMessageDialog(this,
                                        "Please select a course to enroll in.",
                                        "No Selection", javax.swing.JOptionPane.WARNING_MESSAGE);
                        return;
                }

                String courseId = (String) availableCoursesModel.getValueAt(selectedRow, 0);
                String courseTitle = (String) availableCoursesModel.getValueAt(selectedRow, 1);

                int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                                "Enroll in course: " + courseTitle + "?",
                                "Confirm Enrollment", javax.swing.JOptionPane.YES_NO_OPTION);

                if (confirm == javax.swing.JOptionPane.YES_OPTION) {

                        SS.enrollInCourse(courseId);
                        javax.swing.JOptionPane.showMessageDialog(this,
                                        "Enrolled in: " + courseTitle,
                                        "Enrollment Successful", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
                try {
                        availableCoursesModel.setRowCount(0);
                        enrolledCoursesModel.setRowCount(0);
                        lessonsModel.setRowCount(0);
                        courseIdForCurrentLesson = null;
                        SS.refresh();
                        loadStudentData();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }// GEN-LAST:event_btnEnrollActionPerformed

        /**
         * ========================================================================
         * VIEW LESSONS BUTTON ACTION
         * Loads and displays lessons for the selected enrolled course
         * ========================================================================
         */
        private void btnViewLessonsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnViewLessonsActionPerformed
                int selectedRow = tblMyCourses.getSelectedRow();
                if (selectedRow == -1) {
                        javax.swing.JOptionPane.showMessageDialog(this,
                                        "Please select an enrolled course to view lessons.",
                                        "No Selection", javax.swing.JOptionPane.WARNING_MESSAGE);
                        return;
                }

                String courseId = (String) enrolledCoursesModel.getValueAt(selectedRow, 0);
                loadLessons(courseId);
        }// GEN-LAST:event_btnViewLessonsActionPerformed

        /**
         * ========================================================================
         * MARK COMPLETED BUTTON ACTION
         * Marks the selected lesson as completed
         * ========================================================================
         */
        private void btnTakeLessonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTakeLessonActionPerformed
                int selectedRow = tblLessons.getSelectedRow();
                if (selectedRow == -1) {
                        javax.swing.JOptionPane.showMessageDialog(this,
                                        "Please select a lesson to mark as completed.",
                                        "No Selection", javax.swing.JOptionPane.WARNING_MESSAGE);
                        return;
                }
                String lessonId = (String) lessonsModel.getValueAt(selectedRow, 1);
                String lessonTitle = (String) lessonsModel.getValueAt(selectedRow, 2);

                if (SS.getCompletedLesson(courseIdForCurrentLesson).size() == Cdb.getCourse(courseIdForCurrentLesson)
                                .getLessons().size()) {
                        if (((Student) Udb.getUser(currentStudentId)).getEnrolledCourses()
                                        .get(courseIdForCurrentLesson).getCertificate() == null) {
                                backend.models.Certificate cert = SS.generateCertificate(courseIdForCurrentLesson);
                                JFrame frame = new CertificateViewerFrame(cert);
                                frame.setVisible(true);
                                frame.setLocationRelativeTo(null);
                                frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                        }
                        JFrame frame = new CertificateViewerFrame(
                                        ((Student) Udb.getUser(currentStudentId)).getEnrolledCourses()
                                                        .get(courseIdForCurrentLesson).getCertificate());
                        frame.setVisible(true);
                        frame.setLocationRelativeTo(null);
                        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                } else {
                        // Check if already completed
                        String isCompleted = (String) lessonsModel.getValueAt(selectedRow, 0);
                        if (isCompleted != null && isCompleted == "Completed") {
                                javax.swing.JOptionPane.showMessageDialog(this,
                                                "This lesson is already marked as completed.",
                                                "Already Completed", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                return;
                        }

                        StudentQuizService SQ = new StudentQuizService(currentStudentId);
                        QuizFrame frame = new QuizFrame(currentStudentId, courseIdForCurrentLesson,
                                        lessonId, SQ.getQuizForLesson(courseIdForCurrentLesson, lessonId));
                        frame.setVisible(true);
                        frame.setLocationRelativeTo(null);
                }

                // try {
                // SS.markLessonCompleted(courseIdForCurrentLesson, lessonId);
                // } catch (Exception e) {
                // e.printStackTrace();
                // }
                // lessonsModel.setValueAt("Completed", selectedRow, 0);
                // javax.swing.JOptionPane.showMessageDialog(this,
                // "Lesson marked as completed!",
                // "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        }// GEN-LAST:event_btnTakeLessonActionPerformed

        /**
         * ========================================================================
         * LOAD STUDENT DATA
         * Loads initial student data including available and enrolled courses
         * ========================================================================
         */
        private void loadStudentData() {
                // !NTST
                ArrayList<Course> availableCourses = SS.getAvailableCourses();
                ArrayList<Instructor> availableInstructors = SS.getAvailableInstructors();
                ArrayList<Course> enrolledCourses = SS.getEnrolledCourses();
                ArrayList<Instructor> enrolledInstructors = SS.getEnrolledInstructors();
                for (int i = 0; i < availableCourses.size(); i++) {
                        Course avCrs = availableCourses.get(i);
                        availableCoursesModel.addRow(new Object[] {
                                        avCrs.getCourseId(), avCrs.getTitle(),
                                        avCrs.getDescription(), availableInstructors.get(i).getUsername()
                        });
                }
                for (int i = 0; i < enrolledCourses.size(); i++) {
                        Course enCrs = enrolledCourses.get(i);
                        // for progress
                        int numLessons = enCrs.getLessons().size();
                        int numCompleted = SS.getCompletedLesson(enCrs.getCourseId()).size();
                        int percentage = numLessons == 0 ? 0 : (numCompleted * 100) / numLessons;
                        // String shown_progress = Integer.toString(numCompleted) +
                        // " completed out of " +
                        // Integer.toString(numLessons);
                        enrolledCoursesModel.addRow(new Object[] {
                                        enCrs.getCourseId(), enCrs.getTitle(),
                                        percentage, enrolledInstructors.get(i).getUsername()
                        });
                }
        }

        /**
         * ========================================================================
         * LOAD LESSONS
         * ========================================================================
         */
        private void loadLessons(String courseId) {
                lessonsModel.setRowCount(0); // Clear existing lessons
                ArrayList<Course> enrolledCourses = SS.getEnrolledCourses();
                for (int i = 0; i < enrolledCourses.size(); i++) {
                        if ((enrolledCourses.get(i).getCourseId()).equals(courseId)) {
                                for (int j = 0; j < enrolledCourses.get(i).getLessons().size(); j++) {
                                        courseIdForCurrentLesson = courseId;
                                        Lesson l = enrolledCourses.get(i).getLessons().get(j);
                                        System.out.println("lesson got");
                                        String finished = SS.getCompletedLesson(courseId).contains(l.getLessonId())
                                                        ? "Completed"
                                                        : "Uncompleted";
                                        lessonsModel.addRow(new Object[] {
                                                        finished, l.getLessonId(),
                                                        l.getTitle(), l.getContent()
                                        });
                                }
                        }
                }

        }

        /**
         * ========================================================================
         * MAIN METHOD FOR TESTING
         * ========================================================================
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
                        java.util.logging.Logger.getLogger(StudentDashboard.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                } catch (InstantiationException ex) {
                        java.util.logging.Logger.getLogger(StudentDashboard.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                } catch (IllegalAccessException ex) {
                        java.util.logging.Logger.getLogger(StudentDashboard.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                        java.util.logging.Logger.getLogger(StudentDashboard.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                }
                // </editor-fold>

                /* Create and display the form */
                java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                                // Use parameterized constructor with student data
                                JFrame farme = new StudentDashboard("U2", "Gamal");
                                farme.setVisible(true);
                                farme.setLocationRelativeTo(null);
                                farme.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        }
                });
        }

        // ========================================================================
        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JLabel LblStudentInfo;
        private javax.swing.JButton btnEnroll;
        private javax.swing.JButton btnLogout;
        private javax.swing.JButton btnTakeLesson;
        private javax.swing.JButton btnRefresh;
        private javax.swing.JButton btnViewLessons;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JTabbedPane jTabbedPane2;
        private javax.swing.JTable jTable1;
        private javax.swing.JTable tblLessons;
        private javax.swing.JTable tblMyCourses;
        // End of variables declaration//GEN-END:variables
}