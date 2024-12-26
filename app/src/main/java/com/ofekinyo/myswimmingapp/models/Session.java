package com.ofekinyo.myswimmingapp.models;

public class Session {

    private String sessionId;   // Unique ID for the session
    private String trainerId;   // ID of the trainer conducting the session
    private String swimStudentId;   // ID of the SwimStudent attending the session
    private String date;        // Date and time of the session
    private String location;    // Location of the session
    private String status;      // Status of the session (e.g., scheduled, completed)

    // Default constructor required for Firebase
    public Session() {}

    // Constructor with fields
    public Session(String sessionId, String trainerId, String swimStudentId, String date, String location, String status) {
        this.sessionId = sessionId;
        this.trainerId = trainerId;
        this.swimStudentId = swimStudentId;
        this.date = date;
        this.location = location;
        this.status = status;
    }

    // Getters and Setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getSwimStudentId() {
        return swimStudentId;
    }

    public void setSwimStudentId(String swimStudentId) {
        this.swimStudentId = swimStudentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
