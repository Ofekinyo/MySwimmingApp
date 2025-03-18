package com.ofekinyo.myswimmingapp.models;

import java.io.Serializable;

public class User implements Serializable {
    protected String id, Fname, Lname, email, gender, city, role, phone;
    protected int age;
    protected String password;

    // Constructor with all parameters
    public User(String id, String Fname, String Lname, String phone, String email, int age, String gender, String city, String password, String role) {
        this.id = id;
        this.Fname = Fname;
        this.Lname = Lname;
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
        this.Fname = user.Fname;
        this.Lname = user.Lname;
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
        return Fname;
    }

    public void setFname(String fname) {
        this.Fname = fname;
    }

    public String getLname() {
        return Lname;
    }

    public void setLname(String lname) {
        this.Lname = lname;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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
                ", Fname='" + Fname + '\'' +
                ", Lname='" + Lname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
