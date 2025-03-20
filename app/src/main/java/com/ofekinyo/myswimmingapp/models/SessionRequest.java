package com.ofekinyo.myswimmingapp.models;

public class SessionRequest {
    private String sessionType;
    private String duration;
    private String date;
    private String trainerName;

    public SessionRequest(String sessionType, String duration, String date, String trainerName) {
        this.sessionType = sessionType;
        this.duration = duration;
        this.date = date;
        this.trainerName = trainerName;
    }

    public String getSessionType() {
        return sessionType;
    }

    public String getDuration() {
        return duration;
    }

    public String getDate() {
        return date;
    }

    public String getTrainerName() {
        return trainerName;
    }
}
