package com.ofekinyo.myswimmingapp.models;

import java.io.Serializable;

public class Trainee extends User implements Serializable {
    private double height, weight;
    private int age;
    private User myCoach;  // The trainer assigned to the trainee

    // Constructor with all parameters
    public Trainee(String id, String Fname, String Lname, String phone, String email, int age, String gender, String city, String password, String role, double height, double weight, User myCoach) {
        super(id, Fname, Lname, phone, email, age, gender, city, password, role);
        this.height = height;
        this.weight = weight;
        this.myCoach = myCoach;
    }

    // Getters and Setters
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public User getMyCoach() {
        return myCoach;
    }

    public void setMyCoach(User myCoach) {
        this.myCoach = myCoach;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "height=" + height +
                ", weight=" + weight +
                ", age=" + age +
                ", myCoach=" + myCoach +
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
