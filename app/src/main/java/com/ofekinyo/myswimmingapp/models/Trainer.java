package com.ofekinyo.myswimmingapp.models;

import java.io.Serializable;

public class Trainer extends User implements Serializable {
    private String domain;  // Area of expertise (e.g., fitness, sports)
    private double price;   // Coaching price
    private int experience; // Number of years of experience

    // Constructor with all parameters
    public Trainer(String id, String Fname, String Lname, int phone, String email, int age, String password, String gender, String city, String role, String domain, int experience, double price) {
        super(id, Fname, Lname, phone, email, age, gender, city, password, role);
        this.domain = domain;
        this.experience = experience;
        this.price = price;
    }

    // Getters and Setters
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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

    @Override
    public String toString() {
        return "Trainer{" +
                "domain='" + domain + '\'' +
                ", price=" + price +
                ", experience=" + experience +
                ", id='" + id + '\'' +
                ", Fname='" + Fname + '\'' +
                ", Lname='" + Lname + '\'' +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
