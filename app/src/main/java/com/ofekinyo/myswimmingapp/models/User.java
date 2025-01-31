package com.ofekinyo.myswimmingapp.models;

public class User {
    protected String id;
    protected String Lname, Fname, email, password, city, gender;
    protected int age, phone;

    protected String profileImageUrl;

    // Default constructor
    public User() {
    }

    // Constructor with password
    public User(String id, String Fname, String Lname, int phone, String email, int age, String city, String gender, String password) {
        this.id = id;
        this.Fname = Fname;
        this.Lname = Lname;
        this.phone = phone;
        this.email = email;
        this.age = age;
        this.city = city;
        this.gender = gender;
        this.password = password;
    }
    public User(User user){

    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFname() {
        return Fname;
    }
    public void setFname(String Fname) {
        this.Fname = Fname;
    }
    public String getLname() {
        return Lname;
    }
    public void setLname(String Lname) {
        this.Lname = Lname;
    }
    public int getPhone() {
        return phone;
    }
    public void setPhone(int phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getCity() { return city; }
    public void setCity(String city){ this.city = city; }
    public String getGender() { return gender; }
    public void setGender(String gender){ this.gender = gender; }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
