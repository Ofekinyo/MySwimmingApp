package com.ofekinyo.myswimmingapp.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {
    protected String id, fname, lname, email, gender, city, role, phone;
    protected Integer age; // Changed from int to Integer
    protected String password;

    // Constructor with all parameters
    public User(String id, String fname, String lname, String phone, String email, Integer age, String gender, String city, String password, String role) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.city = city;
        this.password = password;
        this.role = role;
    }

    // Default constructor
    public User() {}

    // Copy constructor
    public User(User user) {
        this.id = user.id;
        this.fname = user.fname;
        this.lname = user.lname;
        this.phone = user.phone;
        this.email = user.email;
        this.age = user.age;
        this.gender = user.gender;
        this.city = user.city;
        this.role = user.role;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getAge() { // Changed from int to Integer
        return age;
    }

    public void setAge(Integer age) { // Changed from int to Integer
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    @NonNull
    @Override
    public User clone() {
        return new User(this);
    }
}
