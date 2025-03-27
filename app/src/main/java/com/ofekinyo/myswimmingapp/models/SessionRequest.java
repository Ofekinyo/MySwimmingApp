package com.ofekinyo.myswimmingapp.models;

public class SessionRequest {
    private String date;
    private String time;
    private String goals;
    private String trainerName; // Add this field

    public SessionRequest(String date, String time, String goals, String trainerName) {
        this.date = date;
        this.time = time;
        this.goals = goals;
        this.trainerName = trainerName; // Assign it
    }

    // Getters and setters
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getGoals() { return goals; }
    public String getTrainerName() { return trainerName; }
}