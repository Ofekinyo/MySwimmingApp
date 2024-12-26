package com.ofekinyo.myswimmingapp.models;

public class Feedback {

    private String feedbackId;  // Unique ID for the feedback
    private String sessionId;   // The session this feedback is for
    private String swimStudentId;   // ID of the SwimStudent giving feedback
    private String trainerId;   // ID of the trainer receiving feedback
    private String comments;    // Feedback comments
    private int rating;         // Rating out of 5

    // Default constructor required for Firebase
    public Feedback() {}

    // Constructor with fields
    public Feedback(String feedbackId, String sessionId, String swimStudentId, String trainerId, String comments, int rating) {
        this.feedbackId = feedbackId;
        this.sessionId = sessionId;
        this.swimStudentId = swimStudentId;
        this.trainerId = trainerId;
        this.comments = comments;
        this.rating = rating;
    }

    // Getters and Setters
    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSwimStudentId() {
        return swimStudentId;
    }

    public void setSwimStudentId(String swimStudentId) {
        this.swimStudentId = swimStudentId;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
