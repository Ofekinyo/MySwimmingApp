package com.ofekinyo.myswimmingapp.models;

public class Session {
    private String sessionId;
    private String trainerId;
    private String traineeId;
    private SessionRequest request;

    // No-argument constructor required for Firebase
    public Session() {}

    public Session(String sessionId, String trainerId, String traineeId, SessionRequest request) {
        this.sessionId = sessionId;
        this.trainerId = trainerId;
        this.traineeId = traineeId;
        this.request = request;
    }

    // Getters and setters
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

    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId) {
            this.traineeId = traineeId;
    }

    public SessionRequest getRequest() {
        return request;
    }

    public void setRequest(SessionRequest request) {
        this.request = request;
    }
}
