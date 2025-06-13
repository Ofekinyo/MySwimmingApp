package com.ofekinyo.myswimmingapp.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Swimmer extends User implements Serializable {
    private double height, weight;
    private String goal;
   // The tutor assigned to the swimmer

    // Constructor with all parameters


    public Swimmer(String id, String Fname, String Lname, String phone, String email, Integer age, String gender, String city, String password, String role, double height, double weight) {
        super(id, Fname, Lname, phone, email, age, gender, city, password, role);
        this.height = height;
        this.weight = weight;
    }

    public Swimmer(Swimmer swimmer) {
        super(swimmer);
        this.height = swimmer.height;
        this.weight = swimmer.weight;
    }

    public Swimmer() {
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

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    @Override
    public String toString() {
        return "Swimmer{" +
                "height=" + height +
                ", weight=" + weight +
                ", goal='" + goal + '\'' +
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

    @NonNull
    @Override
    public Swimmer clone() {
        return new Swimmer(this);
    }
}
