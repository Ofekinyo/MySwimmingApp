package com.ofekinyo.myswimmingapp.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tutor extends User implements Serializable {
    private List<String> sessionTypes;
    private double price;       // Coaching price - now nullable
    private int experience;      // Years of experience - now nullable
    private List<Schedule> schedules;

    // No-argument constructor required by Firebase
    public Tutor() {
        sessionTypes = new ArrayList<>();
        schedules = new ArrayList<>();
    }

    // Constructor with all parameters
    public Tutor(String id, String fname, String lname, String email, String phone, String city, String gender,
                 Integer age, String password, Boolean isAdmin,
                 String role, List<String> sessionTypes, int experience, double price,
                 List<Schedule> schedules) {
        super(id, fname, lname,  email,phone, city,  gender, age,password, isAdmin, role);
        this.sessionTypes = sessionTypes;
        this.experience = experience;
        this.price = price;
        this.schedules = schedules;
    }

    public Tutor(Tutor tutor) {
        super(tutor.id, tutor.fname, tutor.lname,
                tutor.email, tutor.phone, tutor.city,
                tutor.gender, tutor.age, tutor.password,
                tutor.isAdmin, tutor.role);
        this.sessionTypes = new ArrayList<>(tutor.sessionTypes);
        this.experience = tutor.experience;
        this.price = tutor.price;
        this.schedules = new ArrayList<>(tutor.schedules);
    }

    // Getters and Setters
    public List<String> getSessionTypes() {
        return sessionTypes;
    }

    public void setSessionTypes(List<String> sessionTypes) {
        this.sessionTypes = sessionTypes;
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

    // Method to get the full name of the Tutor
    public String getName() {
        return fname + " " + lname;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    @NonNull
    @Override
    public Tutor clone() {
        return new Tutor(this);
    }

    @NonNull
    @Override
    public String toString() {
        return "Tutor{" +
                "sessionTypes=" + sessionTypes +
                ", price=" + price +
                ", experience=" + experience +
                ", schedules=" + schedules +
                ", id='" + id + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", role='" + role + '\'' +
                ", phone='" + phone + '\'' +
                ", age=" + age +
                ", password='" + password + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                '}';
    }
}
