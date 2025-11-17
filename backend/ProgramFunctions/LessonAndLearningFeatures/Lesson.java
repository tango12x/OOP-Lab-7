package backend.ProgramFunctions.LessonAndLearningFeatures;

import java.util.ArrayList;

/**
 * Requirement 5: Implement the Lesson class (Corrected Version).
 * This is a simple data object.
 * UPDATED: Includes toString() for better JList display.
 */
public class Lesson {
    private String lessonId;
    private String title;
    private String content;
    private ArrayList<String> resources; // List of links or file names

    public Lesson(String lessonId, String title, String content) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.resources = new ArrayList<>();
    }

    // --- Getters ---
    public String getLessonId() { return lessonId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public ArrayList<String> getResources() { return resources; }

    // --- Methods ---
    public void addResource(String resource) {
        if (resource != null && !resource.isEmpty()) {
            this.resources.add(resource);
        }
    }

    // Setters (for editing)
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * This controls how the object appears in a JList.
     * @return The title of the lesson.
     */
    @Override
    public String toString() {
        return this.title; // Display the title in lists
    }
}