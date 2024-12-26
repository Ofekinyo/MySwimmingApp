package com.ofekinyo.myswimmingapp.models;

public class User {
    String id;
    String fname, lname, phone,email, password;

    public User() {
    }

    public User(String id, String fname, String lname, String phone, String email) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;
    }

    public User(String id, String fname, String lname, String phone, String email, String password) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }
}
