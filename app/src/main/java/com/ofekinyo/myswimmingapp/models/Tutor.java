package com.ofekinyo.myswimmingapp.models;

import java.io.Serializable;
import java.util.List;

public class Tutor extends User implements Serializable {
    private List<String> sessionTypes;
    private Double price;                // Coaching price - now nullable
    private Integer experience;          // Years of experience - now nullable

    // No-argument constructor required by Firebase
    public Tutor() {
        // Firebase requires a no-argument constructor
    }

    // Constructor with all parameters
    public Tutor(String id, String fname, String lname, String phone, String email, Integer age, String password, String gender, String city, String role, List<String> sessionTypes, Integer experience, Double price) {
        super(id, fname, lname, phone, email, age, gender, city, password, role);
        this.sessionTypes = sessionTypes;
        this.experience = experience;
        this.price = price;
    }

    public Tutor(Tutor tutor) {
        super(tutor.id, tutor.fname, tutor.lname, tutor.phone, tutor.email, tutor.age, tutor.gender, tutor.city, tutor.password, tutor.role);
        this.sessionTypes = tutor.sessionTypes;
        this.experience = tutor.experience;
        this.price = tutor.price;
    }

    // Getters and Setters
    public List<String> getSessionTypes() {
        return sessionTypes;
    }

    public void setSessionTypes(List<String> sessionTypes) {
        this.sessionTypes = sessionTypes;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    // Method to get the full name of the Tutor
    public String getName() {
        return fname + " " + lname;
    }

    @Override
    public String toString() {
        return "Tutor{" +
                "sessionTypes=" + sessionTypes +
                ", price=" + price +
                ", experience=" + experience +
                ", id='" + id + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
