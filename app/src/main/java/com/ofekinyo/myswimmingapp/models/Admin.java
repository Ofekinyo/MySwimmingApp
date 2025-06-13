package com.ofekinyo.myswimmingapp.models;

import androidx.annotation.NonNull;

public class Admin extends User {
    private boolean isOnDuty;          // Track if admin is currently on duty
    private String workingHours;        // Admin's working hours

    // Constructor with all parameters
    public Admin(String id, String fname, String lname, String phone, String email, 
                int age, String gender, String city, String password, String role) {
        super(id, fname, lname, phone, email, age, gender, city, password, role);
        this.isOnDuty = false;
        this.workingHours = "";
    }

    // Default constructor
    public Admin() {
        super();
        this.isOnDuty = false;
        this.workingHours = "";
    }

    public Admin(Admin admin) {
        super(admin);
        this.isOnDuty = admin.isOnDuty;
        this.workingHours = admin.workingHours;
    }

    @NonNull
    @Override
    public Admin clone() {
        return new Admin(this);
    }

    // Getters and Setters
    public boolean isOnDuty() {
        return isOnDuty;
    }

    public void setOnDuty(boolean onDuty) {
        isOnDuty = onDuty;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    @Override
    public String toString() {
        return super.toString() + 
               ", isOnDuty=" + isOnDuty +
               ", workingHours=" + workingHours;
    }
}

