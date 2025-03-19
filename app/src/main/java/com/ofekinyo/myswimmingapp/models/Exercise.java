package com.ofekinyo.myswimmingapp.models;

public class Exercise {
    private String title;
    private String description;
    private String videoUrl;

    public Exercise(String title, String description, String videoUrl) {
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
