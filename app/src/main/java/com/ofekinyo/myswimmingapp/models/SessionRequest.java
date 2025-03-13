package com.ofekinyo.myswimmingapp.models;

public class SessionRequest {
    private String sessionType;
    private String date;
    private String goals;
    private String status; // New property to store the request status

    public SessionRequest(String sessionType, String date, String goals) {
        this.sessionType = sessionType;
        this.date = date;
        this.goals = goals;
        this.status = "Pending"; // Default status is "Pending"
    }

    public String getSessionType() {
        return sessionType;
    }

    public String getDate() {
        return date;
    }

    public String getGoals() {
        return goals;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

