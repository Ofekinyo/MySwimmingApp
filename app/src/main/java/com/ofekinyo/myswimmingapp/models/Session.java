package com.ofekinyo.myswimmingapp.models;

import java.util.List;

public class Session {

    private String id;
    private String swimmerId;
    private String tutorId;
    private String date;
    private String time;
    private List<String> goals;
    private String location;
    private String notes;
    private Boolean isAccepted;

    public Session() {
    }

    public Session(String id, String swimmerId, String tutorId, String date, String time, List<String> goals, String location, String notes, Boolean isAccepted) {
        this.id = id;
        this.swimmerId = swimmerId;
        this.tutorId = tutorId;
        this.date = date;
        this.time = time;
        this.goals = goals;
        this.location = location;
        this.notes = notes;
        this.isAccepted = isAccepted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSwimmerId() {
        return swimmerId;
    }

    public void setSwimmerId(String swimmerId) {
        this.swimmerId = swimmerId;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
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

    public List<String> getGoals() {
        return goals;
    }

    public void setGoals(List<String> goals) {
        this.goals = goals;
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

    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id='" + id + '\'' +
                ", swimmerId='" + swimmerId + '\'' +
                ", tutorId='" + tutorId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", goals=" + goals +
                ", location='" + location + '\'' +
                ", notes='" + notes + '\'' +
                ", isAccepted=" + isAccepted +
                '}';
    }
}
