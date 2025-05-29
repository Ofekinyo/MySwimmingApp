package com.ofekinyo.myswimmingapp.models;

import java.util.List;

public class SessionRequest {
    private String requestId;
    private String tutorId;
    private String swimmerId;
    private List<String> goals;
    private String otherGoal;
    private String date;
    private String time;
    private String location;
    private String notes;
    private String status; // e.g., "pending", "approved", "rejected"

    // Required no-arg constructor for Firebase
    public SessionRequest() {}

    public SessionRequest(String requestId, String tutorId, String swimmerId,
                          List<String> goals, String otherGoal, String date,
                          String time, String location, String notes, String status) {
        this.requestId = requestId;
        this.tutorId = tutorId;
        this.swimmerId = swimmerId;
        this.goals = goals;
        this.otherGoal = otherGoal;
        this.date = date;
        this.time = time;
        this.location = location;
        this.notes = notes;
        this.status = status;
    }

    // Getters and setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getSwimmerId() {
        return swimmerId;
    }

    public void setSwimmerId(String swimmerId) {
        this.swimmerId = swimmerId;
    }

    public List<String> getGoals() {
        return goals;
    }

    public void setGoals(List<String> goals) {
        this.goals = goals;
    }

    public String getOtherGoal() {
        return otherGoal;
    }

    public void setOtherGoal(String otherGoal) {
        this.otherGoal = otherGoal;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
