package com.ofekinyo.myswimmingapp.models;

import java.io.Serializable;

public class Trainee extends User implements Serializable {
    private double height, weight;
   // The trainer assigned to the trainee

    // Constructor with all parameters


    public Trainee(String id, String Fname, String Lname, String phone, String email, Integer age, String gender, String city, String password, String role, double height, double weight) {
        super(id, Fname, Lname, phone, email, age, gender, city, password, role);
        this.height = height;
        this.weight = weight;
    }

    public Trainee(double height, double weight) {
        this.height = height;
        this.weight = weight;
    }

    public Trainee(User user, double height, double weight) {
        super(user);
        this.height = height;
        this.weight = weight;
    }

    public Trainee() {
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "height=" + height +
                ", weight=" + weight +
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
                '}';
    }
}
