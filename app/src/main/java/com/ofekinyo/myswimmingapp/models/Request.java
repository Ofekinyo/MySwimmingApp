package com.ofekinyo.myswimmingapp.models;

import java.util.List;

public class Request {
    private String requestId;
    private String tutorId;    // מדריך
    private String swimmerId;    // שחיין
    private List<String> goals;
    private String otherGoal;
    private String date;
    private String time;
    private String location;
    private String notes;
    private String status;

    public Request(String requestId, String tutorId, String swimmerId, String date, String time, String location, String goals, String notes, String status) {
    }

    public Request(String id, String tutorId, String swimmerId, List<String> goals, String otherGoal,
                          String date, String time, String location, String notes, String status) {
        this.requestId = id;
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

    public String getId() {
        return requestId;
    }

    public void setId(String id) {
        this.requestId = id;
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

    public void setSwimmerId(String traineeId) {
        this.swimmerId = traineeId;
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
                ", tutorId='" + tutorId + '\'' +
                ", swimmerId='" + swimmerId + '\'' +
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
