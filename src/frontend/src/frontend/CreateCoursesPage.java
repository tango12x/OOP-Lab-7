/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frontend;
import backend.ProgramFunctions.CourseManagement.Course;
import backend.ProgramFunctions.InstructorManagement.Instructor;
import backend.ProgramService.ProgramService;
import javax.swing.JOptionPane;

/**
 *
 * @author ahmme
 */
public class CreateCoursesPage extends javax.swing.JFrame {


    private ProgramService service;
    private Instructor currentUser;

    /**
     * Creates new form CreateCoursespage
     */
    public CreateCoursesPage(ProgramService service, Instructor user) {

        initComponents();
        this.service = service;
        this.currentUser = user;
        this.setLocationRelativeTo(null); //center
        setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);//meaning
    }

 private void initComponents() {
     jLabel1 = new javax.swing.JLabel();
     jLabel2 = new javax.swing.JLabel();
     titleTextField = new javax.swing.JTextField();
     jLabel3 = new javax.swing.JLabel();
     jScrollPane1 = new javax.swing.JScrollPane();
     descriptionTextArea = new javax.swing.JTextArea();
     createCourseButton = new javax.swing.JButton();

     setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
     setTitle("Create New Course");

     jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
     jLabel1.setText("Create a New Course");

     jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
     jLabel2.setText("Course Title:");

     titleTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

     jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
     jLabel3.setText("Course Description:");

     descriptionTextArea.setColumns(20);
     descriptionTextArea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
     descriptionTextArea.setLineWrap(true);
     descriptionTextArea.setRows(5);
     descriptionTextArea.setWrapStyleWord(true);
     jScrollPane1.setViewportView(descriptionTextArea);

     createCourseButton.setBackground(new java.awt.Color(0, 153, 51));
     createCourseButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
     createCourseButton.setForeground(new java.awt.Color(0, 0, 0));
     createCourseButton.setText("Create Course");
     createCourseButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
             createCourseButtonActionPerformed(evt);
         }
     });
     javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
     getContentPane().setLayout(layout);
     layout.setHorizontalGroup(
             layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(layout.createSequentialGroup()
                             .addGap(25, 25, 25)
                             .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                     .addComponent(createCourseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                     .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                             .addComponent(jLabel2)
                                             .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                                             .addComponent(titleTextField)
                                             .addComponent(jLabel3)
                                             .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)))
                             .addContainerGap(25, Short.MAX_VALUE))
     );
     layout.setVerticalGroup(
             layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(layout.createSequentialGroup()
                             .addGap(23, 23, 23)
                             .addComponent(jLabel1)
                             .addGap(28, 28, 28)
                             .addComponent(jLabel2)
                             .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                             .addComponent(titleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                             .addGap(28, 28, 28)
                             .addComponent(jLabel3)
                             .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                             .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                             .addGap(18, 18, 18)
                             .addComponent(createCourseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                             .addContainerGap(24, Short.MAX_VALUE))
     );

     pack();

 }

    private void createCourseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String title = titleTextField.getText().trim();
        String description = descriptionTextArea.getText().trim();

        // Use the service to create the course
        Course newCourse = service.createCourse(title, description, currentUser);

        if (newCourse != null) {
            JOptionPane.showMessageDialog(this, "Course '" + newCourse.getTitle() + "' created successfully!");
            this.dispose(); // Close the window
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create course. Title and Description must not be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private javax.swing.JButton createCourseButton;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField titleTextField;
    // End of variables declaration
}