package frontend.student;

import backend.models.Certificate;
import backend.services.CertificatePDFGenerator;
import frontend.Instructor.InstructorDashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Swing frame for displaying certificates with PDF export functionality
 * Provides a professional certificate display with option to save as PDF
 * 
 * Features:
 * - Elegant certificate display with professional styling
 * - PDF export button with file chooser
 * - Responsive layout for different certificate sizes
 * - Status feedback for PDF generation
 */
public class CertificateViewerFrame extends javax.swing.JFrame {
    private Certificate certificate;

    /**
     * Constructor for creating certificate viewer window
     * 
     * @param certificate The certificate to display and export
     */
    public CertificateViewerFrame(Certificate certificate) {
        this.certificate = certificate;
        initComponents();
        initializeUI();
        setupEventHandlers();
    }

    /**
     * Initializes all UI components and layouts
     * Creates the main certificate display area and control buttons
     */
    private void initializeUI() {
        // Set window properties
        setTitle("Certificate - " + certificate.getCertificateId());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setResizable(true);

        // Set certificate display content
        certificateDisplay.setContentType("text/html");
        certificateDisplay.setText(certificate.toHTMLFormat());
        certificateDisplay.setEditable(false);
        certificateDisplay.setBackground(Color.WHITE);
    }

    /**
     * Sets up event handlers for buttons
     * Handles PDF export and window closing
     */
    private void setupEventHandlers() {
        // PDF export button handler
        btnExportPDF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportCertificateAsPDF();
            }
        });

        // Close button handler
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the window
            }
        });
    }

    /**
     * Handles PDF export process with file chooser and status feedback
     * Guides user through saving certificate as PDF file
     */
    private void exportCertificateAsPDF() {
        try {
            // Show PDF export confirmation
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Generate high-quality PDF certificate?\n\n" +
                            "This will create a professional PDF document with:\n" +
                            "• Vector graphics for sharp printing\n" +
                            "• Professional certificate design\n" +
                            "• All certificate details and security features\n" +
                            "• Landscape orientation optimized for certificates",
                    "Generate PDF Certificate",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return; // User cancelled
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Certificate as PDF");

            // Suggest professional filename
            String suggestedFilename = "Certificate_" +
                    certificate.getStudentName().replace(" ", "_") + "_" +
                    certificate.getCourseTitle().replace(" ", "_") + ".pdf";
            fileChooser.setSelectedFile(new File(suggestedFilename));

            // Set PDF file filter
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "PDF Documents (*.pdf)", "pdf"));

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();

                // Ensure PDF extension and make final for lambda usage
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                }

                // Create final variable for use in lambda
                final String finalFilePath = filePath;

                // Show progress dialog
                JDialog progressDialog = new JDialog(this, "Generating PDF...", true);
                progressDialog.setSize(300, 100);
                progressDialog.setLocationRelativeTo(this);
                progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

                JPanel progressPanel = new JPanel(new BorderLayout());
                progressPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                progressPanel.add(new JLabel("Generating high-quality PDF certificate...", JLabel.CENTER),
                        BorderLayout.CENTER);
                progressDialog.add(progressPanel);

                // Use SwingWorker to avoid blocking the UI
                SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        return CertificatePDFGenerator.generatePDF(certificate, finalFilePath);
                    }

                    @Override
                    protected void done() {
                        progressDialog.dispose();
                        try {
                            boolean success = get();

                            if (success) {
                                JOptionPane.showMessageDialog(CertificateViewerFrame.this,
                                        "✓ High-quality PDF certificate generated successfully!\n\n" +
                                                "File: " + new File(finalFilePath).getName() + "\n" +
                                                "Location: " + new File(finalFilePath).getParent() + "\n\n" +
                                                "The PDF contains:\n" +
                                                "• Professional certificate design\n" +
                                                "• Vector graphics for perfect printing\n" +
                                                "• All student and course details\n" +
                                                "• Security features and verification\n\n" +
                                                "You can now print or share this professional certificate.",
                                        "PDF Generation Complete",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(CertificateViewerFrame.this,
                                        "PDF generation was cancelled or failed.\n" +
                                                "Please ensure you have a PDF printer available\n" +
                                                "(like 'Microsoft Print to PDF' on Windows).",
                                        "PDF Generation",
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(CertificateViewerFrame.this,
                                    "Error generating PDF: " + ex.getMessage() + "\n\n" +
                                            "Please ensure:\n" +
                                            "1. You have a PDF printer installed\n" +
                                            "2. You have write permissions to the save location\n" +
                                            "3. The file is not open in another program",
                                    "PDF Generation Error",
                                    JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    }
                };

                worker.execute();
                progressDialog.setVisible(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Unexpected error during PDF export: " + ex.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Displays the certificate viewer window
     * Makes the frame visible and brings it to front
     */
    public void showCertificate() {
        setVisible(true);
        toFront(); // Bring window to front
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
        jScrollPane1 = new javax.swing.JScrollPane();
        certificateDisplay = new javax.swing.JEditorPane();
        jPanel2 = new javax.swing.JPanel();
        btnExportPDF = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Certificate Viewer");
        setPreferredSize(new java.awt.Dimension(800, 600));

        jPanel1.setLayout(new java.awt.BorderLayout());

        certificateDisplay.setBackground(new java.awt.Color(255, 255, 255));
        certificateDisplay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jScrollPane1.setViewportView(certificateDisplay);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        btnExportPDF.setBackground(new java.awt.Color(0, 0, 255));
        btnExportPDF.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnExportPDF.setForeground(new java.awt.Color(0, 0, 0));
        btnExportPDF.setText("Export as PDF");
        btnExportPDF.setFocusPainted(false);
        btnExportPDF.setToolTipText("Save certificate as PDF file");

        btnClose.setBackground(new java.awt.Color(0, 0, 0));
        btnClose.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnClose.setForeground(new java.awt.Color(0, 0, 0));
        btnClose.setText("Close");
        btnClose.setFocusPainted(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap(263, Short.MAX_VALUE)
                                .addComponent(btnExportPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 140,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 140,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(239, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnExportPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap()));

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Main method for testing the certificate viewer
     * Creates a sample certificate and displays it
     */
    public static void main(String args[]) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        // Create and display the form
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Create sample certificate for testing
                    Certificate testCertificate = new Certificate(
                            "STU123",
                            "John Smith",
                            "CS101",
                            "Introduction to Programming",
                            "Dr. Jane Anderson",
                            92.5);

                    // Create and display certificate viewer
                    CertificateViewerFrame viewer = new CertificateViewerFrame(testCertificate);
                    viewer.setVisible(true);
                    viewer.setLocationRelativeTo(null);

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Error creating certificate viewer: " + e.getMessage(),
                            "Initialization Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnExportPDF;
    private javax.swing.JEditorPane certificateDisplay;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}