package com.ofekinyo.myswimmingapp.models;

public class Trainer {

    private String uid;       // Unique ID for the trainer
    private String name;      // Trainer's name
    private String email;     // Trainer's email
    private String phone;     // Trainer's phone number
    private String bio;       // A brief bio about the trainer
    private String profileImageUrl;  // URL to the trainer's profile image

    // Default constructor required for Firebase
    public Trainer() {}

    // Constructor with fields
    public Trainer(String uid, String name, String email, String phone, String bio, String profileImageUrl) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
