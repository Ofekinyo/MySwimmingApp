package com.ofekinyo.myswimmingapp.models;

import java.io.Serializable;
import java.util.List;

public class Turor extends User implements Serializable {
    private List<String> trainingTypes;
    private Double price;                // Coaching price - now nullable
    private Integer experience;          // Years of experience - now nullable

    // No-argument constructor required by Firebase
    public Turor() {
        // Firebase requires a no-argument constructor
    }

    // Constructor with all parameters
    public Turor(String id, String fname, String lname, String phone, String email, Integer age, String password, String gender, String city, String role, List<String> trainingTypes, Integer experience, Double price) {
        super(id, fname, lname, phone, email, age, gender, city, password, role);
        this.trainingTypes = trainingTypes;
        this.experience = experience;
        this.price = price;
    }

    public Turor(Turor turor) {
        super(turor.id, turor.fname, turor.lname, turor.phone, turor.email, turor.age, turor.gender, turor.city, turor.password, turor.role);
        this.trainingTypes = trainingTypes;
        this.experience = experience;
        this.price = price;


    }

    // Getters and Setters
    public List<String> getTrainingTypes() {
        return trainingTypes;
    }

    public void setTrainingTypes(List<String> trainingTypes) {
        this.trainingTypes = trainingTypes;
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

    // Method to get the full name of the Trainer
    public String getName() {
        return fname + " " + lname;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "trainingTypes=" + trainingTypes +
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
