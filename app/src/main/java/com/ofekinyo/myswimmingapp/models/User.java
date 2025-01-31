package com.ofekinyo.myswimmingapp.models;

public class User {
    private String id;
    private String fname;
    private String lname;
    private int phone;
    private String email;
    private int age;
    private String city;
    private String gender;
    private String password;
    private String role;  // Add role to the user

    // Constructor with the role
    public User(String id, String fname, String lname, int phone, String email, int age, String city, String gender, String password, String role) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;
        this.age = age;
        this.city = city;
        this.gender = gender;
        this.password = password;
        this.role = role;  // Initialize the role
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public int getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;  // Getter for the role
    }

    public void setRole(String role) {
        this.role = role;  // Setter for the role
    }

    // Other setters can be added if needed
}
