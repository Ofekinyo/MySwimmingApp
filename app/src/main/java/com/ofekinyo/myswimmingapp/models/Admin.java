package com.ofekinyo.myswimmingapp.models;

public class Admin extends User {
    private boolean isAdmin;

    // Constructor with all parameters
    public Admin(String id, String Fname, String Lname, String phone, String email, int age, String gender, String city, String password, String role, boolean isAdmin) {
        super(id, Fname, Lname, phone, email, age, gender, city, password, role);
        this.isAdmin = isAdmin; // true if it's an admin
    }

    // Default constructor
    public Admin() {
        super(); // Call the parent constructor
        this.isAdmin = true; // By default, an Admin is always true
    }

    // Getter for isAdmin
    public boolean isAdmin() {
        return isAdmin;
    }

    // Setter for isAdmin
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    // Optionally, override toString method to reflect the Admin status
    @Override
    public String toString() {
        return super.toString() + ", isAdmin=" + isAdmin;
    }
}

