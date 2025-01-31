package com.ofekinyo.myswimmingapp.models;

import java.io.Serializable;

public class Trainer extends User implements Serializable {
    protected String domain;   // Area of expertise (e.g., fitness, sports)
    protected double price;    // Coaching price
    protected int experience;  // Number of years of experience

    // Constructor with all parameters
    public Trainer(String id, String Fname, String Lname, int phone, String email, int age, String password,
                 String gender, String city, String domain, int experience, double price) {
        super(id, Fname, Lname, phone, email, age, gender, city ,password); // Passing to the superclass constructor
        this.domain = domain;
        this.experience = experience;
        this.price = price;
    }




    // Copy constructor
    public Trainer(Trainer trainer) {
        super(trainer.getId(), trainer.getFname(), trainer.getLname(), trainer.getPhone(), trainer.getEmail(),trainer.getAge()
                ,trainer.getGender(), trainer.getCity(), trainer.getPassword());  // Copying User data
        this.domain = trainer.getDomain();
        this.experience = trainer.getExperience();
        this.price = trainer.getPrice();
    }

    // Constructor to initialize only domain, experience, and price
    public Trainer(String domain, int experience, double price) {
        super();
        this.domain = domain;
        this.experience = experience;
        this.price = price;
    }

    // Default constructor
    public Trainer() {
        super(); // Call the superclass constructor (if needed, could be modified)
    }

    // Getters and Setters for all fields:

    // Getters and Setters for domain
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    // Getters and Setters for experience
    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    // Getters and Setters for price
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Getters and Setters for inherited fields (User class)
    public String getId() {
        return super.getId();
    }

    public void setId(String id) {
        super.setId(id);
    }

    public String getFname() {
        return super.getFname();
    }

    public void setFname(String fname) {
        super.setFname(fname);
    }

    public String getLname() {
        return super.getLname();
    }

    public void setLname(String lname) {
        super.setLname(lname);
    }

    public int getPhone() {
        return super.getPhone();
    }

    public void setPhone(int phone) {
        super.setPhone(phone);
    }

    public String getEmail() {
        return super.getEmail();
    }

    public void setEmail(String email) {
        super.setEmail(email);
    }

    public String getPass() {
        return super.getPassword();  // Accessing the User class's password via getter
    }

    public void setPass(String pass) {
        super.setPassword(pass);  // Accessing the User class's password via setter
    }

    public String getGender() {
        return super.getGender();
    }

    public void setGender(String gender) {
        super.setGender(gender);
    }

    public String getCity() {
        return super.getCity();
    }

    public void setCity(String city) {
        super.setCity(city);
    }

    @Override
    public String toString() {
        return "Coach{" +
                "domain='" + domain + '\'' +
                ", price=" + price +
                ", experience=" + experience +
                ", id='" + id + '\'' +
                ", fname='" + Fname + '\'' +
                ", lname='" + Lname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", city='" + city + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}