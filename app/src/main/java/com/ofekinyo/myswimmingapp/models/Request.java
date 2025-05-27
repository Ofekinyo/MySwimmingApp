package com.ofekinyo.myswimmingapp.models;

import java.util.List;

public class Request {
    private String requestId;
    private String trainerId;    // מדריך
    private String traineeId;    // שחיין
    private List<String> goals;
    private String otherGoal;
    private String date;
    private String time;
    private String location;
    private String notes;
    private String status;

    public Request(String requestId, String trainerId, String traineeId, String date, String time, String location, String goals, String notes, String pending) {
    }

    public Request(String id, String trainerId, String traineeId, List<String> goals, String otherGoal,
                          String date, String time, String location, String notes, String status) {
        this.requestId = id;
        this.trainerId = trainerId;
        this.traineeId = traineeId;
        this.goals = goals;
        this.otherGoal = otherGoal;
        this.date = date;
        this.time = time;
        this.location = location;
        this.notes = notes;
        this.status = status;
    }

    // Getters and setters

    public String getId() {
        return requestId;
    }

    public void setId(String id) {
        this.requestId = id;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
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

    @Override
    public String toString() {
        return "SessionRequest{" +
                "id='" + requestId + '\'' +
                ", trainerId='" + trainerId + '\'' +
                ", traineeId='" + traineeId + '\'' +
                ", goals=" + goals +
                ", otherGoal='" + otherGoal + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", location='" + location + '\'' +
                ", notes='" + notes + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
