package backend.ProgramFunctions.LessonAndLearningFeatures;

import java.util.ArrayList;

public class Lesson {
    private String lessonId;
    private String title;
    private String content;
    private String courseId;
    private ArrayList<String> optionalResources;


    //CLASS CONSTRUCTOR
    public Lesson(String lessonId, String title, String courseId, String content) {
        this.lessonId = lessonId;
        this.title = title;
        this.courseId = courseId;
        this.content = content;
        this.optionalResources = new ArrayList<String>();
    }
    public Lesson(String lessonId, String title, String courseId) {
        this.lessonId = lessonId;
        this.title = title;
        this.courseId = courseId;
        this.optionalResources = new ArrayList<String>();
    }


    //standard getters and setters
    public String getLessonId() {
        return lessonId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public ArrayList<String> getOptionalResources() {
        return optionalResources;
    }
    public void setOptionalResources(ArrayList<String> optionalResources) {
        this.optionalResources = optionalResources;
    }
    public String getCourseId() {
        return courseId;
    }
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
}
