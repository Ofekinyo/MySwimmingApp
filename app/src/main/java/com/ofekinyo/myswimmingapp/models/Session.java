package com.ofekinyo.myswimmingapp.models;

public class Session {
    private String sessionId;
    private String title;
    private String date;
    private String time;
    private String location;

    // No-argument constructor required for Firebase
    public Session() {}

    public Session(String sessionId, String title, String date, String time, String location) {
        this.sessionId = sessionId;
        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    // Getters and setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
