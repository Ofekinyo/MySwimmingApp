package com.ofekinyo.myswimmingapp.models;

public class SwimStudent {

    private String uid;        // Unique ID for the swim student
    private String name;       // Swim student's name
    private String email;      // Swim student's email
    private String phone;      // Swim student's phone number
    private String profileImageUrl; // URL to the student's profile image
    private String goals;      // Swimming goals (e.g., learning strokes, improving endurance)

    // Default constructor required for Firebase
    public SwimStudent() {}

    // Constructor with fields
    public SwimStudent(String uid, String name, String email, String phone, String profileImageUrl, String goals) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profileImageUrl = profileImageUrl;
        this.goals = goals;
    }

    // Getters and Setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }
}
