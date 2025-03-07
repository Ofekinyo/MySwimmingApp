package com.ofekinyo.myswimmingapp.models;

import java.io.Serializable;
import java.util.List;

public class Trainer extends User implements Serializable {
    private List<String> trainingTypes;  // List of training types (e.g., swimming, fitness)
    private double price;   // Coaching price
    private int experience; // Number of years of experience

    // No-argument constructor required by Firebase
    public Trainer() {
        // Firebase requires a no-argument constructor
    }

    // Constructor with all parameters
    public Trainer(String id, String Fname, String Lname, String phone, String email, int age, String password, String gender, String city, String role, List<String> trainingTypes, int experience, double price) {
        super(id, Fname, Lname, phone, email, age, gender, city, password, role);
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    // Method to get the full name of the Trainer
    public String getName() {
        return Fname + " " + Lname;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "trainingTypes=" + trainingTypes +
                ", price=" + price +
                ", experience=" + experience +
                ", id='" + id + '\'' +
                ", Fname='" + Fname + '\'' +
                ", Lname='" + Lname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
