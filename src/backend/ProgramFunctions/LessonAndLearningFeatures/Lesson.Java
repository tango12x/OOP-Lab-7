package backend.ProgramFunctions.LessonAndLearningFeatures;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//! Ai generated code below just to fill the file, not checked for correctness yet 


public class Lesson {
    private String lessonId;
    private String title;
    private String description;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;
    private List<String> tags;
    private int durationMinutes;
    private boolean published;

    public Lesson(String lessonId, String title, String description) {
        this.lessonId = lessonId;
        this.title = title;
        this.description = description;
        this.content = "";
        this.createdDate = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.tags = new ArrayList<>();
        this.durationMinutes = 0;
        this.published = false;
    }

    public String getLessonId() {
        return lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.lastModified = LocalDateTime.now();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.lastModified = LocalDateTime.now();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.lastModified = LocalDateTime.now();
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public List<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
        this.lastModified = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "lessonId='" + lessonId + '\'' +
                ", title='" + title + '\'' +
                ", published=" + published +
                ", durationMinutes=" + durationMinutes +
                '}';
    }
}