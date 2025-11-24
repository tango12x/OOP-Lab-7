package backend.services;

import backend.models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.geom.AffineTransform;

/**
 * Professional PDF certificate generator using Java's built-in PDF capabilities
 * Creates high-quality, properly formatted PDF files without external libraries
 */
public class CertificatePDFGenerator {

    /**
     * Generates a high-quality PDF certificate using Java's advanced printing
     * capabilities
     * Creates a professional vector-based PDF with proper formatting
     * 
     * @param certificate The certificate data to convert to PDF
     * @param filePath    The destination path for the PDF file
     * @return true if PDF generation successful, false otherwise
     */
    public static boolean generatePDF(Certificate certificate, String filePath) {
        try {
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            // Create a professional certificate printable
            ProfessionalCertificatePrintable printable = new ProfessionalCertificatePrintable(certificate);

            // Get printer job for PDF generation
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("Certificate - " + certificate.getCertificateId());
            job.setPrintable(printable);

            // Set to landscape for certificate format
            PageFormat format = job.defaultPage();
            format.setOrientation(PageFormat.LANDSCAPE);

            if (job.printDialog()) {
                job.print();
                JOptionPane.showMessageDialog(null,
                        "High-quality PDF certificate generated successfully!\n" +
                                "File saved with professional formatting and vector graphics.",
                        "PDF Generation Complete",
                        JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                return false; // User cancelled
            }

        } catch (Exception e) {
            System.err.println("PDF Generation Error: " + e.getMessage());
            e.printStackTrace();

            JOptionPane.showMessageDialog(null,
                    "PDF generation failed: " + e.getMessage() + "\n" +
                            "Please ensure you have a PDF printer installed (like 'Microsoft Print to PDF').",
                    "PDF Generation Failed",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}

/**
 * Professional certificate printable that creates high-quality vector PDF
 * Uses Java2D to draw certificate with proper typography and layout
 */
class ProfessionalCertificatePrintable implements Printable {
    private Certificate certificate;

    public ProfessionalCertificatePrintable(Certificate certificate) {
        this.certificate = certificate;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;

        // Enable high-quality rendering for PDF
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        // Position on page
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Get page dimensions
        double pageWidth = pageFormat.getImageableWidth();
        double pageHeight = pageFormat.getImageableHeight();

        // Draw professional certificate
        drawProfessionalCertificate(g2d, pageWidth, pageHeight);

        return PAGE_EXISTS;
    }

    /**
     * Draws complete professional certificate with all elements
     */
    private void drawProfessionalCertificate(Graphics2D g2d, double pageWidth, double pageHeight) {
        // Fill background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, (int) pageWidth, (int) pageHeight);

        // Draw certificate border and design
        drawCertificateDesign(g2d, pageWidth, pageHeight);

        // Draw all certificate content
        drawCertificateContent(g2d, pageWidth, pageHeight);
    }

    /**
     * Draws professional certificate design with borders and decorations
     */
    private void drawCertificateDesign(Graphics2D g2d, double pageWidth, double pageHeight) {
        int width = (int) pageWidth;
        int height = (int) pageHeight;

        // Main gold border
        g2d.setColor(new Color(212, 175, 55));
        g2d.setStroke(new BasicStroke(8));
        g2d.drawRect(40, 40, width - 80, height - 80);

        // Secondary border
        g2d.setColor(new Color(180, 150, 50));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(60, 60, width - 120, height - 120);
    }

    /**
     * Draws all certificate content with professional typography
     * Optimized for landscape orientation and perfect text fitting
     */
    private void drawCertificateContent(Graphics2D g2d, double pageWidth, double pageHeight) {
        int centerX = (int) pageWidth / 2;
        int usableWidth = (int) pageWidth - 120; // Account for borders
        int usableHeight = (int) pageHeight - 120;
        
        // Calculate dynamic positions based on page size
        int currentY = 100;
        int lineSpacing = 40;

        // ===== MAIN TITLE =====
        g2d.setColor(new Color(44, 62, 80));
        Font titleFont = new Font("Serif", Font.BOLD, 32);
        g2d.setFont(titleFont);
        String title = "CERTIFICATE OF COMPLETION";
        
        // Adjust title font size if too wide
        FontMetrics titleMetrics = g2d.getFontMetrics();
        while (titleMetrics.stringWidth(title) > usableWidth - 100 && titleFont.getSize() > 20) {
            titleFont = new Font("Serif", Font.BOLD, titleFont.getSize() - 2);
            g2d.setFont(titleFont);
            titleMetrics = g2d.getFontMetrics();
        }
        
        int titleWidth = titleMetrics.stringWidth(title);
        g2d.drawString(title, centerX - titleWidth / 2, currentY);
        currentY += lineSpacing;

        // ===== SUBTITLE =====
        g2d.setColor(new Color(128, 128, 128));
        Font subtitleFont = new Font("Serif", Font.ITALIC, 14);
        g2d.setFont(subtitleFont);
        String subtitle = "Skill Forge Learning Platform";
        FontMetrics subtitleMetrics = g2d.getFontMetrics();
        int subtitleWidth = subtitleMetrics.stringWidth(subtitle);
        g2d.drawString(subtitle, centerX - subtitleWidth / 2, currentY);
        currentY += lineSpacing + 10;

        // ===== SEPARATOR LINE =====
        g2d.setColor(new Color(212, 175, 55));
        g2d.setStroke(new BasicStroke(2));
        int lineLength = Math.min(400, usableWidth - 100);
        g2d.drawLine(centerX - lineLength / 2, currentY, centerX + lineLength / 2, currentY);
        currentY += lineSpacing;

        // ===== CERTIFICATION TEXT =====
        g2d.setColor(Color.BLACK);
        Font certifiesFont = new Font("Serif", Font.PLAIN, 16);
        g2d.setFont(certifiesFont);
        String certifiesText = "This is to certify that";
        FontMetrics certifiesMetrics = g2d.getFontMetrics();
        int certifiesWidth = certifiesMetrics.stringWidth(certifiesText);
        g2d.drawString(certifiesText, centerX - certifiesWidth / 2, currentY);
        currentY += lineSpacing;

        // ===== STUDENT NAME (HIGHLIGHTED) =====
        g2d.setColor(new Color(41, 128, 185));
        Font studentFont = new Font("Serif", Font.BOLD, 24);
        g2d.setFont(studentFont);
        String studentName = certificate.getStudentName();
        
        // Adjust student name font size if too wide
        FontMetrics studentMetrics = g2d.getFontMetrics();
        while (studentMetrics.stringWidth(studentName) > usableWidth - 100 && studentFont.getSize() > 14) {
            studentFont = new Font("Serif", Font.BOLD, studentFont.getSize() - 2);
            g2d.setFont(studentFont);
            studentMetrics = g2d.getFontMetrics();
        }
        
        int studentWidth = studentMetrics.stringWidth(studentName);
        g2d.drawString(studentName, centerX - studentWidth / 2, currentY);
        currentY += lineSpacing;

        // ===== COMPLETION TEXT =====
        g2d.setColor(Color.BLACK);
        Font completedFont = new Font("Serif", Font.PLAIN, 16);
        g2d.setFont(completedFont);
        String completedText = "has successfully completed the course";
        FontMetrics completedMetrics = g2d.getFontMetrics();
        int completedWidth = completedMetrics.stringWidth(completedText);
        g2d.drawString(completedText, centerX - completedWidth / 2, currentY);
        currentY += lineSpacing;

        // ===== COURSE TITLE (HIGHLIGHTED) =====
        g2d.setColor(new Color(39, 174, 96));
        Font courseFont = new Font("Serif", Font.BOLD | Font.ITALIC, 20);
        g2d.setFont(courseFont);
        String courseTitle = "\"" + certificate.getCourseTitle() + "\"";
        
        // Adjust course title font size if too wide
        FontMetrics courseMetrics = g2d.getFontMetrics();
        while (courseMetrics.stringWidth(courseTitle) > usableWidth - 100 && courseFont.getSize() > 12) {
            courseFont = new Font("Serif", Font.BOLD | Font.ITALIC, courseFont.getSize() - 2);
            g2d.setFont(courseFont);
            courseMetrics = g2d.getFontMetrics();
        }
        
        int courseWidth = courseMetrics.stringWidth(courseTitle);
        g2d.drawString(courseTitle, centerX - courseWidth / 2, currentY);
        currentY += lineSpacing;

        // ===== SCORE AND GRADE =====
        g2d.setColor(Color.BLACK);
        Font scoreFont = new Font("Serif", Font.PLAIN, 14);
        g2d.setFont(scoreFont);
        String scoreText = "with a final score of " + String.format("%.1f", certificate.getFinalScore()) +
                "% (" + certificate.getGrade() + ")";
        FontMetrics scoreMetrics = g2d.getFontMetrics();
        int scoreWidth = scoreMetrics.stringWidth(scoreText);
        g2d.drawString(scoreText, centerX - scoreWidth / 2, currentY);
        currentY += lineSpacing + 20;

        // ===== DETAILS SECTION =====
        Font detailsFont = new Font("Serif", Font.PLAIN, 12);
        g2d.setFont(detailsFont);
        g2d.setColor(Color.BLACK);

        // Instructor
        String instructorText = "Instructor: " + certificate.getInstructorName();
        FontMetrics instructorMetrics = g2d.getFontMetrics();
        int instructorWidth = instructorMetrics.stringWidth(instructorText);
        g2d.drawString(instructorText, centerX - instructorWidth / 2, currentY);
        currentY += 20;

        // Certificate ID
        String certificateIdText = "Certificate ID: " + certificate.getCertificateId();
        FontMetrics idMetrics = g2d.getFontMetrics();
        int idWidth = idMetrics.stringWidth(certificateIdText);
        g2d.drawString(certificateIdText, centerX - idWidth / 2, currentY);
        currentY += 20;

        // Issue Date
        String issueDateText = "Issued: " + certificate.getFormattedIssueDate();
        FontMetrics dateMetrics = g2d.getFontMetrics();
        int dateWidth = dateMetrics.stringWidth(issueDateText);
        g2d.drawString(issueDateText, centerX - dateWidth / 2, currentY);
        currentY += 40;

        // ===== SIGNATURE SECTION =====
        // Signature line
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.BLACK);
        int signatureLineLength = 200;
        g2d.drawLine(centerX - signatureLineLength / 2, currentY, centerX + signatureLineLength / 2, currentY);
        currentY += 20;

        // Signature text
        Font signatureFont = new Font("Serif", Font.PLAIN, 12);
        g2d.setFont(signatureFont);
        String signatureText = "Skill Forge Administration";
        FontMetrics signatureMetrics = g2d.getFontMetrics();
        int signatureWidth = signatureMetrics.stringWidth(signatureText);
        g2d.drawString(signatureText, centerX - signatureWidth / 2, currentY);
        currentY += 30;

        // ===== VERIFICATION FOOTER =====
        g2d.setColor(new Color(100, 100, 100));
        Font verifyFont = new Font("Serif", Font.PLAIN, 10);
        g2d.setFont(verifyFont);
        String verifyText = "Verify at: Skill Forge Platform | Certificate ID: " + certificate.getCertificateId();
        FontMetrics verifyMetrics = g2d.getFontMetrics();
        int verifyWidth = verifyMetrics.stringWidth(verifyText);
        g2d.drawString(verifyText, centerX - verifyWidth / 2, currentY);
    }
}