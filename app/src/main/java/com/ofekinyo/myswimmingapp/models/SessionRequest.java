package com.ofekinyo.myswimmingapp.models;

public class SessionRequest {
    private String date;
    private String time;
    private String goals;
    private String trainerName;
    private String traineeName; // Add this field

    // Constructor to initialize all fields
    public SessionRequest(String date, String time, String goals, String trainerName, String traineeName) {
        this.date = date;
        this.time = time;
        this.goals = goals;
        this.trainerName = trainerName;
        this.traineeName = traineeName; // Assign the trainee name
    }

    // Getters and setters
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getGoals() { return goals; }
    public String getTrainerName() { return trainerName; }
    public String getTraineeName() { return traineeName; }

    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setGoals(String goals) { this.goals = goals; }
    public void setTrainerName(String trainerName) { this.trainerName = trainerName; }
    public void setTraineeName(String traineeName) { this.traineeName = traineeName; }
}
