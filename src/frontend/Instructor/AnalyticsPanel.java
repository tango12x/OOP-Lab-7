package frontend.Instructor;

import backend.analytics.*;
import backend.models.Course;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Main analytics panel for instructor dashboard
 * Displays comprehensive course performance metrics and visualizations
 * 
 * Features:
 * - Course selection dropdown
 * - Key metrics display
 * - Interactive charts
 * - Student performance tables
 * - Export functionality
 */
public class AnalyticsPanel extends JPanel {
    private AnalyticsManager analyticsManager;
    private JComboBox<String> courseSelector;
    private JLabel studentsLabel, completionLabel, avgScoreLabel, healthLabel;
    private JPanel chartsPanel;
    private JTable performanceTable;
    private Course currentCourse;
    
    // Color scheme for consistent UI
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    
    public AnalyticsPanel() {
        this.analyticsManager = new AnalyticsManager();
        initializeComponents();
        setupLayout();
        loadCourses();
    }
    
    /**
     * Initializes all UI components with proper styling
     */
    private void initializeComponents() {
        // Course selector with label
        JLabel selectorLabel = new JLabel("Select Course:");
        selectorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        courseSelector = new JComboBox<>();
        courseSelector.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        courseSelector.setPreferredSize(new Dimension(250, 30));
        courseSelector.addActionListener(e -> onCourseSelected());
        
        // Metrics panel components
        studentsLabel = createMetricLabel("Total Students: 0", PRIMARY_COLOR);
        completionLabel = createMetricLabel("Completion Rate: 0%", SUCCESS_COLOR);
        avgScoreLabel = createMetricLabel("Avg Quiz Score: 0%", WARNING_COLOR);
        healthLabel = createMetricLabel("Course Health: Unknown", PRIMARY_COLOR);
        
        // Charts panel
        chartsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        chartsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            "Performance Analytics",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            PRIMARY_COLOR
        ));
        
        // Performance table
        performanceTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(performanceTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Student Performance Details"));
        tableScrollPane.setPreferredSize(new Dimension(800, 200));
    }
    
    /**
     * Creates styled metric labels with consistent formatting
     */
    private JLabel createMetricLabel(String text, Color color) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(color);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setPreferredSize(new Dimension(180, 60));
        return label;
    }
    
    /**
     * Sets up the main layout with sections for metrics, charts, and details
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel - Course selection and basic info
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.add(new JLabel("Select Course:"));
        topPanel.add(courseSelector);
        
        // Metrics panel - Key performance indicators
        JPanel metricsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        metricsPanel.add(studentsLabel);
        metricsPanel.add(completionLabel);
        metricsPanel.add(avgScoreLabel);
        metricsPanel.add(healthLabel);
        
        // Combine top panels
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(metricsPanel, BorderLayout.CENTER);
        
        // Center panel - Charts and visualizations
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(chartsPanel, BorderLayout.CENTER);
        
        // Main layout assembly
        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);
    }
    
    /**
     * Creates the bottom panel with detailed table and export options
     */
    private JPanel createSouthPanel() {
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        
        // Table for detailed student performance
        JScrollPane tableScrollPane = new JScrollPane(performanceTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(
            "Student Performance Details"
        ));
        tableScrollPane.setPreferredSize(new Dimension(800, 250));
        
        // Export button panel
        JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exportButton = new JButton("Export Analytics Report");
        exportButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        exportButton.setBackground(PRIMARY_COLOR);
        exportButton.setForeground(Color.WHITE);
        exportButton.addActionListener(e -> exportAnalyticsReport());
        exportPanel.add(exportButton);
        
        southPanel.add(tableScrollPane, BorderLayout.CENTER);
        southPanel.add(exportPanel, BorderLayout.SOUTH);
        
        return southPanel;
    }
    
    /**
     * Loads available courses into the dropdown selector
     */
    private void loadCourses() {
        // This would typically fetch from CourseDatabaseManager
        // For demo, using sample data
        String[] sampleCourses = {
            "C1 - Java Programming Basics",
            "C2 - Advanced Data Structures", 
            "C3 - Web Development Fundamentals",
            "C4 - Machine Learning Introduction"
        };
        
        for (String course : sampleCourses) {
            courseSelector.addItem(course);
        }
        
        // Load initial data for first course
        if (courseSelector.getItemCount() > 0) {
            loadAnalyticsData("C1");
        }
    }
    
    /**
     * Handles course selection changes and updates all displays
     */
    private void onCourseSelected() {
        String selected = (String) courseSelector.getSelectedItem();
        if (selected != null) {
            String courseId = selected.split(" - ")[0]; // Extract course ID
            loadAnalyticsData(courseId);
        }
    }
    
    /**
     * Loads and displays analytics data for the selected course
     */
    private void loadAnalyticsData(String courseId) {
        // In real implementation, this would fetch from AnalyticsManager
        // For now, using simulated data
        
        // Simulate analytics data
        CourseAnalytics analytics = analyticsManager.getCourseAnalytics(courseId);
        
        // Update metrics labels
        updateMetricsDisplay(analytics);
        
        // Update charts
        updateCharts(analytics);
        
        // Update performance table
        updatePerformanceTable(analytics);
    }
    
    /**
     * Updates the key metrics display with current analytics data
     */
    private void updateMetricsDisplay(CourseAnalytics analytics) {
        studentsLabel.setText(String.format("<html><center>Total Students<br><b>%d</b></center></html>", 
            analytics.getTotalStudents()));
        
        completionLabel.setText(String.format("<html><center>Completion Rate<br><b>%.1f%%</b></center></html>", 
            analytics.getCourseCompletionRate()));
        
        avgScoreLabel.setText(String.format("<html><center>Avg Quiz Score<br><b>%.1f%%</b></center></html>", 
            analytics.getAverageQuizScore()));
        
        // Color code health status
        String healthStatus = analytics.getHealthStatus();
        Color healthColor = getHealthColor(healthStatus);
        healthLabel.setForeground(healthColor);
        healthLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(healthColor, 2),
            BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        healthLabel.setText(String.format("<html><center>Course Health<br><b>%s</b></center></html>", 
            healthStatus));
    }
    
    /**
     * Returns color based on course health status for visual indicators
     */
    private Color getHealthColor(String healthStatus) {
        switch (healthStatus.toLowerCase()) {
            case "excellent": return SUCCESS_COLOR;
            case "good": return new Color(52, 152, 219);
            case "fair": return WARNING_COLOR;
            case "needs attention": return DANGER_COLOR;
            default: return Color.GRAY;
        }
    }
    
    /**
     * Updates all chart visualizations with current analytics data
     */
    private void updateCharts(CourseAnalytics analytics) {
        chartsPanel.removeAll();
        
        // Add chart panels
        chartsPanel.add(createCompletionChart(analytics));
        chartsPanel.add(createScoreDistributionChart(analytics));
        chartsPanel.add(createLessonPerformanceChart(analytics));
        chartsPanel.add(createEngagementChart(analytics));
        
        chartsPanel.revalidate();
        chartsPanel.repaint();
    }
    
    /**
     * Creates completion rate visualization chart
     */
    private JPanel createCompletionChart(CourseAnalytics analytics) {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("Completion Progress"));
        
        // Simulated chart - in real implementation, use JFreeChart or similar
        JLabel chartLabel = new JLabel(
            String.format("<html><center>Course Completion: %.1f%%<br>" +
                         "Avg Lessons Completed: %.1f/%d</center></html>",
                         analytics.getCourseCompletionRate(),
                         analytics.getAverageLessonsCompleted(),
                         analytics.getTotalLessons()),
            JLabel.CENTER
        );
        chartLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Progress bar visualization
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue((int) analytics.getCourseCompletionRate());
        progressBar.setStringPainted(true);
        progressBar.setForeground(SUCCESS_COLOR);
        
        chartPanel.add(chartLabel, BorderLayout.CENTER);
        chartPanel.add(progressBar, BorderLayout.SOUTH);
        
        return chartPanel;
    }
    
    /**
     * Creates quiz score distribution visualization
     */
    private JPanel createScoreDistributionChart(CourseAnalytics analytics) {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("Quiz Score Distribution"));
        
        // Score distribution visualization
        int[] distribution = analytics.getPerformanceDistribution();
        JPanel distributionPanel = new JPanel(new GridLayout(1, 5, 2, 0));
        
        String[] ranges = {"0-20%", "21-40%", "41-60%", "61-80%", "81-100%"};
        Color[] colors = {DANGER_COLOR, WARNING_COLOR, WARNING_COLOR, 
                         new Color(52, 152, 219), SUCCESS_COLOR};
        
        int maxStudents = Arrays.stream(distribution).max().orElse(1);
        
        for (int i = 0; i < distribution.length; i++) {
            JPanel barPanel = new JPanel(new BorderLayout());
            
            // Bar
            JPanel bar = new JPanel();
            bar.setBackground(colors[i]);
            int height = maxStudents > 0 ? (distribution[i] * 100 / maxStudents) : 0;
            bar.setPreferredSize(new Dimension(30, height));
            
            // Label
            JLabel label = new JLabel(
                String.format("<html><center>%s<br>%d</center></html>", 
                            ranges[i], distribution[i]),
                JLabel.CENTER
            );
            label.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            
            barPanel.add(bar, BorderLayout.CENTER);
            barPanel.add(label, BorderLayout.SOUTH);
            distributionPanel.add(barPanel);
        }
        
        chartPanel.add(distributionPanel, BorderLayout.CENTER);
        return chartPanel;
    }
    
    /**
     * Creates lesson-by-lesson performance visualization
     */
    private JPanel createLessonPerformanceChart(CourseAnalytics analytics) {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("Lesson Performance"));
        
        JTextArea lessonData = new JTextArea();
        lessonData.setEditable(false);
        lessonData.setFont(new Font("Monospaced", Font.PLAIN, 11));
        
        StringBuilder sb = new StringBuilder();
        sb.append("Lesson    Completion  Avg Score\n");
        sb.append("------------------------------\n");
        
        // Sample lesson data - in real app, use actual lesson analytics
        String[] sampleLessons = {"L1 - Basics", "L2 - OOP", "L3 - Advanced", "L4 - Projects"};
        for (int i = 0; i < sampleLessons.length; i++) {
            double completion = 80 - (i * 10); // Sample data
            double score = 75 - (i * 5); // Sample data
            sb.append(String.format("%-10s %6.1f%%    %6.1f%%\n", 
                                  sampleLessons[i], completion, score));
        }
        
        lessonData.setText(sb.toString());
        chartPanel.add(new JScrollPane(lessonData), BorderLayout.CENTER);
        
        return chartPanel;
    }
    
    /**
     * Creates engagement and dropout rate visualization
     */
    private JPanel createEngagementChart(CourseAnalytics analytics) {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("Engagement Metrics"));
        
        JPanel metricsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        
        // Engagement metrics
        addMetric(metricsPanel, "Dropout Rate", 
                 String.format("%.1f%%", analytics.getDropoutRate()), 
                 analytics.getDropoutRate() > 30 ? DANGER_COLOR : SUCCESS_COLOR);
        
        addMetric(metricsPanel, "Avg Completion Time", 
                 String.format("%.1f days", analytics.getAverageTimeToComplete()), 
                 PRIMARY_COLOR);
        
        addMetric(metricsPanel, "Quiz Pass Rate", 
                 String.format("%.1f%%", analytics.getQuizPassRate()), 
                 analytics.getQuizPassRate() > 70 ? SUCCESS_COLOR : WARNING_COLOR);
        
        addMetric(metricsPanel, "Health Score", 
                 String.format("%.1f/100", analytics.getCourseHealthScore()), 
                 getHealthColor(analytics.getHealthStatus()));
        
        chartPanel.add(metricsPanel, BorderLayout.CENTER);
        return chartPanel;
    }
    
    /**
     * Helper method to add consistent metric displays
     */
    private void addMetric(JPanel panel, String title, String value, Color color) {
        JPanel metricPanel = new JPanel(new BorderLayout());
        metricPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        titleLabel.setForeground(Color.DARK_GRAY);
        
        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        valueLabel.setForeground(color);
        
        metricPanel.add(titleLabel, BorderLayout.NORTH);
        metricPanel.add(valueLabel, BorderLayout.CENTER);
        
        panel.add(metricPanel);
    }
    
    /**
     * Updates the student performance table with detailed data
     */
    private void updatePerformanceTable(CourseAnalytics analytics) {
        // Sample student data - in real app, fetch from analytics
        String[] columnNames = {"Student ID", "Name", "Lessons Completed", 
                               "Avg Score", "Status", "Last Activity"};
        
        Object[][] data = {
            {"S001", "John Doe", "8/10", "85%", "Active", "2024-01-15"},
            {"S002", "Jane Smith", "10/10", "92%", "Completed", "2024-01-14"},
            {"S003", "Bob Johnson", "3/10", "45%", "Struggling", "2024-01-10"},
            {"S004", "Alice Brown", "6/10", "78%", "Active", "2024-01-13"},
            {"S005", "Charlie Wilson", "2/10", "35%", "At Risk", "2024-01-08"}
        };
        
        performanceTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        });
        
        // Style the table
        performanceTable.setRowHeight(25);
        performanceTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        performanceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
    }
    
    /**
     * Handles exporting analytics report (PDF/CSV functionality)
     */
    private void exportAnalyticsReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Analytics Report");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "PDF Files (*.pdf)", "pdf"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            // In real implementation, generate PDF/CSV report
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }
            
            // Simulate export process
            JOptionPane.showMessageDialog(this,
                "Analytics report exported successfully to:\n" + filePath,
                "Export Successful",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Refreshes all analytics data (called from parent frame)
     */
    public void refreshData() {
        if (courseSelector.getSelectedItem() != null) {
            onCourseSelected();
        }
    }
}